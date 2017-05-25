package sudoku.kern.feldmatrix;

import java.util.HashMap;
import java.util.Map;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.animator.Animator;
import sudoku.kern.exception.Exc;
import sudoku.kern.info.FeldInfo;
import sudoku.kern.info.FeldInfoListe;
import sudoku.kern.info.InfoSudoku;
import sudoku.kern.protokoll.Protokoll;

/**
 * @author Hendrick
 *
 */
public class FeldMatrix {
	static public final int feldNummerMax = 9;

	static private boolean istFeldNummerOK(int index) {
		return ((index >= 1) && (index <= 9));
	}

	static public void kontrolliereFeldNummer(FeldNummer feldNummer) throws Exc {
		if (!istFeldNummerOK(feldNummer.spalte)) {
			throw Exc.unerlaubteZeile(feldNummer.spalte);
		}
		if (!istFeldNummerOK(feldNummer.zeile)) {
			throw Exc.unerlaubteZeile(feldNummer.zeile);
		}
	}

	// ==========================================================================
	final private EintragsEbenen ebenen;
	final protected FeldListe felder;
	final private Map<FeldNummer, Feld> feldNummern;

	public FeldMatrix(EintragsEbenen eintragsEbenen) {
		this.ebenen = eintragsEbenen;
		felder = new FeldListe();
		feldNummern = new HashMap<FeldNummer, Feld>();
		// Felder erschaffen
		for (int zeile = 1; zeile < 10; zeile++) {
			for (int spalte = 1; spalte < 10; spalte++) {
				Feld f = new Feld(gibOffset(), new FeldNummer(spalte, zeile), ebenen);
				felder.add(f);
				feldNummern.put(f.gibFeldNummer(), f);
			}
		}
		// Felder an alle bekanntgeben
		for (int i = 0; i < felder.size(); i++) {
			felder.get(i).setzeAlleFelder(felder);
		}
	}

	public void setzeProtokoll(Protokoll protokoll) {
		// Den Feldern den ProtokollSchreiber setzen
		for (int i = 0; i < felder.size(); i++) {
			felder.get(i).setzeProtokollSchreiber(protokoll);
		}

		// in Protokoll muss vor der Arbeit der ProtokollEintragSetzer gesetzt
		// sein:
		ProtokollEintragSetzer protokollEintragSetzer = new ProtokollEintragSetzer(this);
		protokoll.setzeEintragSetzer(protokollEintragSetzer);
	}

	/**
	 * Ist die minimale Vorbereitung f�r Mehrfachsudoku: Offset wird schon bei
	 * Anzeigen ber�cksichtigt.
	 */
	protected FeldNummer gibOffset() {
		return null;
	}

	// gibt true zur�ck wenn es keine freien Felder mehr gibt
	/**
	 * @return true wenn es noch freie Felder gibt
	 */
	public boolean istUnFertig() {
		return felder.existiertFreiesFeld();
	}

	// Zeile 1 bis 9, Spalte 1 bis 9
	protected Feld gibFeld(FeldNummer feldNummer) throws Exc {
		FeldMatrix.kontrolliereFeldNummer(feldNummer);
		Feld feld = feldNummern.get(feldNummer);

		return feld;
	}

	// Zeile 1 bis 9, Spalte 1 bis 9
	public FeldInfo gibFeldInfo(FeldNummer feldNummer) throws Exc {
		FeldMatrix.kontrolliereFeldNummer(feldNummer);
		Feld feld = feldNummern.get(feldNummer);
		FeldInfo feldInfo = new FeldInfo(feld);
		feld.setzeMarkierung(null);
		return feldInfo;
	}

	public FeldNummerListe gibFreieFelder() {
		FeldListe feldListe = this.felder.gibFreieFelder();
		FeldNummerListe liste = new FeldNummerListe(feldListe);
		return liste;
	}

	public FeldNummerListe gibAlleFeldNummern() {
		FeldNummerListe liste = new FeldNummerListe(felder);
		return liste;
	}

	/**
	 * @return Alle Felder, die die gro�e Anzahl M�glicher besitzen
	 */
	public FeldInfoListe gibFelderMitDenMeistenMoeglichen() {
		int nMaxMoegliche = felder.gibMoeglicheAnzahlMax();
		FeldListe returnFelder = felder.gibMoeglicheN(nMaxMoegliche);
		return new FeldInfoListe(returnFelder);
	}

	public FeldNummerListe gibFelderVersuchStart() {
		return felder.gibFelderVersuchStart();
	}

	/**
	 * @return F�r alle Felder die Vorgabe: Felder ohne Vorgabe kommen mit "0"
	 *         zur�ck.
	 */
	public InfoSudoku gibVorgaben() {
		FeldInfoListe infos = new FeldInfoListe();
		for (Feld feld : felder) {
			int vorgabe = feld.gibVorgabe();
			FeldInfo feldInfo = FeldInfo.gibVorgabeInstanz(feld.gibFeldNummer(), vorgabe);
			infos.add(feldInfo);
		}
		InfoSudoku infoSudoku = new InfoSudoku(infos);
		return infoSudoku;
	}

	/**
	 * Alle Felder als FeldInfos
	 */
	public InfoSudoku gibFeldInfos() {
		InfoSudoku f = new InfoSudoku(felder);
		return f;
	}

	/**
	 * L�scht alle Vorgaben und Eintr�ge
	 */
	public void reset() {
		for (int i = 0; i < felder.size(); i++) {
			felder.get(i).reset();
		}
		ebenen.reset();
	}

	/**
	 * L�scht das sudoku und stellt die Vorgaben aus dem InfoSudoku
	 * 
	 * @param vorgaben
	 *            InfoSudoku: Es werden nur seine Vorgaben �bernommen
	 * @throws Exc
	 *             falls der Text nicht gelesen werden kann
	 */
	public void reset(InfoSudoku vorgaben) throws Exc {
		reset();
		for (Map.Entry<FeldNummer, FeldInfo> vorgabe : vorgaben.entrySet()) {
			Feld feld = this.gibFeld(vorgabe.getKey());
			feld.setzeVorgabeReset(vorgabe.getValue().gibVorgabe());
		}
	}

	/**
	 * Ver�ndert das Sudoku durch (unkritisches) Bewegen der Vorgaben, z.B.
	 * Drehen des Sudoku. Diese einfache Implementation ist nur m�glich ohne
	 * Eintr�ge, denn sie tr�gt einfach die Vorgaben hin und her! Eine
	 * Implementation mit Eintr�gen muss jeweils den gesamten FeldInhalt
	 * tauschen...
	 * 
	 * @param animator
	 * @throws Exc
	 *             U.a. wenn Eintr�ge existieren
	 */
	public void animiere(Animator animator) throws Exc {
		if (this.ebeneLaeuftEine()) {
			throw Exc.aktionnurOhneEintrag(animator.gibName());
		}

		InfoSudoku altSudoku = new InfoSudoku(felder);
		reset();
		for (Map.Entry<FeldNummer, FeldInfo> vorgabe : altSudoku.entrySet()) {
			FeldNummer altFeldNummer = vorgabe.getKey();
			FeldNummer neuFeldNummer = animator.gibFeldNummer(altFeldNummer, feldNummerMax);
			Feld feld = this.gibFeld(neuFeldNummer);
			feld.setzeVorgabeReset(vorgabe.getValue().gibVorgabe());
		}
	}

	// Gibt die Anzahl der Vorgaben zur�ck
	public int gibAnzahlVorgaben() {
		return felder.gibAnzahlVorgaben();
	}

	public int ebeneGibNummer() {
		return ebenen.gibNummer();
	}

	public boolean ebeneLaeuftEine() {
		return ebenen.laeuftEine();
	}

	/**
	 * @param zahlen
	 *            Setzt diese unbeingt als M�gliche
	 */
	public void setzeMoeglicheUnbedingt(ZahlenListe zahlen) {
		if (zahlen == null) {
			return;
		}
		for (FeldNummerMitZahl zahl : zahlen) {
			Feld feld = feldNummern.get(zahl.gibFeldNummer());
			feld.setzeMoeglicheUnbedingt(zahl.gibZahl());
		}
	}

	public void wandleEintraegeZuVorgaben() {
		this.ebenen.reset();
		for (int i = 0; i < felder.size(); i++) {
			felder.get(i).wandleEintragZuVorgabe();
		}
	}

	/**
	 * @return Felder suchen ein Problem
	 * @see Feld.sucheProblem()
	 */
	protected Problem sucheFeldProblem() {
		for (int i = 0; i < felder.size(); i++) {
			Problem problem = felder.get(i).sucheProblem();
			if (problem != null) {
				return problem;
			}
		}
		return null;
	}

	/**
	 * @param feldNummer
	 *            Zeile 1 bis 9, Spalte 1 bis 9
	 * @param vorgabe
	 * @throws Exc
	 */
	public void setzeVorgabe(FeldNummer feldNummer, int vorgabe) throws Exc {
		FeldMatrix.kontrolliereFeldNummer(feldNummer);
		Feld feld = gibFeld(feldNummer);

		feld.setzeVorgabe(vorgabe);
	}

	/**
	 * Setzt in dem genannten Feld den Eintrag. Es ist zu beachten, dass in dem
	 * Falle, dass mehr als diese eine Zahl m�glich sind, eine neue
	 * Eintrags-Ebene er�ffnet wird! Wenn das nicht erw�nscht ist: Zuvor
	 * feld.setzeMoeglichEinzig(zahl) rufen.
	 * 
	 * @param eintrag
	 * @throws Exc
	 *             Es wird hier vorausgesetzt, dass es sich um einen
	 *             "ordentlichen" wirklich setzbaren Eintrag handelt. Falls dem
	 *             nicht so sein sollte, wird es per Exc gemeldet.
	 */
	public void setzeEintrag(FeldNummerMitZahl eintrag) throws Exc {
		FeldNummer feldNummer = eintrag.gibFeldNummer();
		FeldMatrix.kontrolliereFeldNummer(feldNummer);
		Feld feld = feldNummern.get(feldNummer);
		feld.setzeEintrag(eintrag.gibZahl());
	}

	/**
	 * Setzt den Tip als Tip-Eintrag
	 * 
	 * @param tip
	 * @throws Exc
	 */
	public void setzeEintragAlsTip(FeldNummerMitZahl tip) throws Exc {
		FeldNummer feldNummer = tip.gibFeldNummer();
		FeldMatrix.kontrolliereFeldNummer(feldNummer);

		Feld feld = gibFeld(feldNummer);
		feld.setzeEintragAlsTip(tip.gibZahl());
	}

	/**
	 * Setzt den Eintrag. Zuvor werden die M�glichen des Feldes auf die Zahl
	 * gesetzt, sodass kein Versuch entstehen kann.
	 * 
	 * @param feldNummer
	 *            Zeile 1 bis 9, Spalte 1 bis 9
	 * @param eintrag
	 * @throws Exc
	 */
	public void setzeEintragOhneVersuch(FeldNummer feldNummer, int eintrag) throws Exc {
		FeldMatrix.kontrolliereFeldNummer(feldNummer);
		Feld feld = gibFeld(feldNummer);
		feld.setzeMoeglichEinzig(eintrag);
		feld.setzeEintrag(eintrag);
	}

	public void setzeMarkierung(FeldNummerListe markFelder, boolean istAktiv) throws Exc {
		for (FeldNummer feldNummer : markFelder) {
			Feld feld = this.gibFeld(feldNummer);
			feld.setzeMarkierung(new Boolean(istAktiv));
		}
	}

	/**
	 * In allen meinen Feldern, die in vorlage frei sind, setze ich die Vorgabe
	 * auf 0
	 * 
	 * @param vorlage
	 */
	public void setzeMuster(InfoSudoku vorlage) {
		FeldNummerListe freieFelder = vorlage.gibFreieFelder();
		for (FeldNummer fi : freieFelder) {
			try {
				this.setzeVorgabe(fi, 0);
			} catch (Exc e) {
				e.printStackTrace();
			}
		}
	}

	public void systemOut(String wo) {

		System.out.println(wo);

		String s = String.format("==== === ====         Vorgaben=%d, Eintr�ge=%d, Ebene=%d mit %d Eintr�gen",
				this.gibAnzahlVorgaben(), felder.gibAnzahlEintraege(), this.ebeneGibNummer(),
				felder.gibAnzahlEbenenEintraege(this.ebeneGibNummer()));
		System.out.println(s);
		for (int zeile = 1; zeile < 10; zeile++) {
			switch (zeile) {
			case 4:
			case 7:
				System.out.println("|--- --- ---|");
			}
			String sZeile = new String("|");
			for (int spalte = 1; spalte < 10; spalte++) {
				switch (spalte) {
				case 4:
				case 7:
					sZeile += ' ';
				}
				char c = '?';
				try {
					Feld feld = this.gibFeld(new FeldNummer(spalte, zeile));
					c = feld.gibSystemOutChar();
				} catch (Exc e) {
					e.printStackTrace();
				}
				sZeile += c;
			}
			sZeile += '|';
			System.out.println(sZeile);
		}
		System.out.println("==== === ====");
		System.out.println();
	}

	public void systemOutEbenen() {
		String s = String.format("Felder: Vorgaben=%d aktuelle Ebene=%d", this.gibAnzahlVorgaben(),
				this.ebenen.gibNummer());
		System.out.println(s);

		for (int ebene = 0; ebene <= ebenen.gibNummer(); ebene++) {
			// Sammle alle Eintr�ge dieser Ebenen-Nummer
			FeldListe ebenenFelder = felder.gibEbenenEintraege(ebene);
			if (!ebenenFelder.isEmpty()) {
				String sEbene = "E " + ebene + ": ";

				for (int iFeld = 0; iFeld < ebenenFelder.size(); iFeld++) {
					Feld feld = ebenenFelder.get(iFeld);
					String sFeld = String.format(" [%d%d]%d", feld.gibZeile(), feld.gibSpalte(), feld.gibEintrag());
					sEbene += sFeld;
				} // for( int zeile

				System.out.println(sEbene);
			} // if (! ebenenFelder.isEmpty()
		}
	}

}
