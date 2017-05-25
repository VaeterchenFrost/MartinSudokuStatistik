package sudoku.kern.feldmatrix;

import java.util.ArrayList;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldListe.AnzahlEintraege;

/**
 * @author heroe
 *
 */
public class Feld implements Comparable<Feld> {

	private FeldNummer feldNummer;
	// Das Setzen eines Eintrags erfolgt auf einer Ebene: So werden Versuche
	// dokumentiert.
	private EintragsEbenen ebenen = null;
	// Der protokolliert das Setzen von Eintr�gen
	private ProtokollSchreiber protokollSchreiber;
	// In die Zukunft gedacht: Wnn mehrere 9x9-Sudokus miteinander verwoben
	// sind.
	private FeldNummer offset = null;

	// Alle Felder (auch ich selbst). F�r aktualisiereEbeneNachEintragLoeschen
	private FeldListe sudokuFelder;
	// 0 == keine
	private int vorgabe;
	// die durch reset gesetzte Vorgabe
	private Integer resetVorgabe;
	// null == keiner
	private Eintrag eintrag;
	// Die in diesem Feld "noch" m�glichen Zahlen
	private ArrayList<Integer> moegliche;
	/**
	 * Wenn in einer Zeile bzw. Spalte bzw. in einem Kasten eine meiner
	 * m�glichen Zahlen nur noch in einem einzigen anderen Feld m�glich ist, so
	 * ist dies andere Feld FeldPartner zu mir. Die ZahlenFeldNummern zeigen auf
	 * diese FeldPartner.
	 */
	private ZahlenFeldNummern feldPartner;
	// null=keine, false=passiv, true=aktiv
	private Boolean markierung;

	public Feld(FeldNummer offset, FeldNummer aFeldindex, EintragsEbenen ebenenObjekt) {
		this.offset = offset;
		this.feldNummer = aFeldindex;
		this.ebenen = ebenenObjekt;
		this.protokollSchreiber = null;
		this.sudokuFelder = null;

		this.moegliche = new ArrayList<Integer>();
		reset();
	}

	void setzeAlleFelder(FeldListe felder) {
		sudokuFelder = felder;
	}

	public void setzeProtokollSchreiber(ProtokollSchreiber protokollSchreiber) {
		this.protokollSchreiber = protokollSchreiber;
	}

	// Beginn eines neuen Spiels
	public void reset() {
		resetVorgabe = null;
		vorgabe = 0;
		eintrag = null;
		loescheMoegliche();
		markierung = null;
	}

	public void setzeMarkierung(Boolean markierung) {
		this.markierung = markierung;
	}

	public void resetFeldBerichte() {
		loescheMoegliche();
	}

	public String gibNameFeldNummer() {
		String sOffsetZeile = "";
		String sOffsetSpalte = "";

		if (offset != null) {
			sOffsetZeile = String.valueOf(offset.zeile) + ":";
			sOffsetSpalte = String.valueOf(offset.spalte) + ":";
		}
		String s = String.format("[Z%s%d,S%s%d]", sOffsetZeile, feldNummer.zeile, sOffsetSpalte, feldNummer.spalte);
		return s;
	}

	public String gibName() {
		String s = "Feld" + gibNameFeldNummer();
		return s;
	}

	public FeldNummer gibFeldNummer() {
		return feldNummer;
	}

	public int gibZeile() {
		return feldNummer.zeile;
	}

	public int gibSpalte() {
		return feldNummer.spalte;
	}

	public int gibVorgabe() {
		return vorgabe;
	}

	public Integer gibResetVorgabe() {
		return resetVorgabe;
	}

	public int gibEintrag() {
		if (eintrag == null) {
			return 0;
		}
		return eintrag.gibZahl();
	}

	public int gibEintragEbene() {
		if (eintrag == null) {
			return 0;
		}
		return eintrag.gibEbene();
	}

	public ZahlenFeldNummern gibFeldPartner() {
		return feldPartner;
	}

	public boolean istEintragEbenenStart() {
		if (eintrag == null) {
			return false;
		}
		return eintrag.istEbenenStart();
	}

	public boolean istEintragVersuchStart() {
		if (eintrag == null) {
			return false;
		}
		return eintrag.istVersuchsStart();
	}

	public ArrayList<Integer> gibMoegliche() {
		return moegliche;
	}

	public int gibMoeglicheAnzahl() {
		if (moegliche == null) {
			return 0;
		}
		return moegliche.size();
	}

	/**
	 * @return null oder die eine m�gliche Zahl
	 */
	public FeldNummerMitZahl gibKlareZahl() {
		FeldNummerMitZahl ergebnis = null;
		if (gibMoeglicheAnzahl() == 1) {
			int zahl = moegliche.get(0);
			ergebnis = new FeldNummerMitZahl(this.feldNummer, zahl);
		}
		return ergebnis;
	}

	public String gibMoeglicheAlsString() {
		String s = new String();
		for (int i = 0; i < moegliche.size(); i++) {
			s += " ";
			s += moegliche.get(i);
		}
		return s;
	}

	public Boolean gibMarkierung() {
		return markierung;
	}

	public boolean istVorgabe() {
		return vorgabe > 0;
	}

	public boolean istEintrag() {
		return eintrag != null;
	}

	public boolean istEintragAlsTip() {
		if (eintrag == null) {
			return false;
		} else {
			return eintrag.istTipZahl();
		}
	}

	public boolean istFrei() {
		return (vorgabe == 0) && (eintrag == null);
	}

	public boolean istFeldPaar() {
		return (istFrei() && (feldPartner != null));
	}

	public boolean istMoeglich(int zahl) {
		return (istFrei() && (moegliche.contains(new Integer(zahl))));
	}

	/**
	 * @return true wenn das Feld frei ist und (nur) genau eine m�gliche Zahl
	 *         besitzt
	 */
	public boolean istKlar() {
		return (istFrei() && (moegliche.size() == 1));
	}

	// Setzt "nur" den Eintrag dieses Feldes zur�ck: Ohne logischen Bezug zum
	// Versuch.
	public void resetEintrag() {
		eintrag = null;
	}

	/**
	 * Vermerkt, dass ich mit einem anderen Feld (mit feldNummer) �ber die
	 * m�gliche Zahl zahl ein FeldPaar bin.
	 * 
	 * @param zahl
	 * @param feld
	 */
	public void setzeFeldPaar(int zahl, FeldNummer feldNummer) {
		if (feldPartner == null) {
			feldPartner = new ZahlenFeldNummern();
		}
		feldPartner.addNurNeue(zahl, feldNummer);
	}

	public void loescheMoegliche() {
		moegliche = new ArrayList<Integer>();
		feldPartner = null;
	}

	/**
	 * @param neueMoegliche
	 *            Werden die neuen M�glichen (als Kopie)
	 * @throws Exc
	 *             Wenn die neuen M�glichen nicht Teilmenge der aktuellen sind
	 */
	public void setzeMoegliche(ArrayList<Integer> neueMoegliche) throws Exc {
		if (!this.moegliche.containsAll(neueMoegliche)) {
			throw Exc.setzeMoeglicheNicht(this.moegliche, neueMoegliche);
		}
		// Kopien erstellen:
		loescheMoegliche();
		moegliche.addAll(neueMoegliche);
	}

	/**
	 * @param zahl
	 *            wird als einzig m�gliche gesetzt
	 * @throws Exc
	 */
	public void setzeMoeglichEinzig(int zahl) throws Exc {
		kontrolliereZahl(zahl);
		moegliche = new ArrayList<Integer>();
		moegliche.add(new Integer(zahl));
	}

	/**
	 * @param zahl:
	 *            Wird unbedingt zu den M�glichen hinzugef�gt
	 */
	public void setzeMoeglicheUnbedingt(int zahl) {
		moegliche.add(new Integer(zahl));
	}

	/**
	 * @param unmoeglicheZahl
	 * @return true wenn die (nicht m�gliche) Zahl aus den M�glichen gel�scht
	 *         wurde
	 * @throws Exc
	 */
	public boolean loescheUnmoeglicheZahl(int unmoeglicheZahl) throws Exc {
		kontrolliereZahl(unmoeglicheZahl);
		for (int i = 0; i < moegliche.size(); i++) {
			if (moegliche.get(i) == unmoeglicheZahl) {
				moegliche.remove(i);
				return true;
			}
		}
		return false;
	};

	/**
	 * In einem freien Feld muss es bei Sudoku-Konsistenz mindestens eine
	 * m�gliche Zahl geben. Ansonsten besitzt das Sudoku eine falsche Zahl!
	 * 
	 * @return null (gut) oder Problem.freiesFeldOhneMoegliche
	 */
	public Problem sucheProblem() {
		if ((vorgabe > 0) || (eintrag != null)) {
			return null;
		}
		// Jetzt muss es M�gliche geben!
		if (moegliche.size() < 1) {
			return Problem.freiesFeldOhneMoegliche(feldNummer);
		}
		return null;
	}

	public char gibSystemOutChar() {
		if (this.istVorgabe()) {
			String sVorgabe = String.valueOf(this.gibVorgabe());
			return sVorgabe.charAt(0);
		}
		if (this.istEintrag()) {
			String sEintrag = String.valueOf(this.gibEintrag());
			return sEintrag.charAt(0);
		}
		return ' ';
	}

	/**
	 * Setzt den Eintrag falls ich (noch) frei bin und es nur eine m�gliche Zahl
	 * gibt
	 * 
	 * @return true falls ein Eintrag gesetzt wurde
	 * @throws Exc
	 */
	public boolean setzeEintragFallsNur1Moeglich() throws Exc {
		if (this.istFrei() && (1 == moegliche.size())) {
			setzeEintrag(moegliche.get(0));
			return true;
		}
		return false;
	}

	private void kontrolliereZahl(int zahl) throws Exc {
		if ((zahl < 0) || (zahl > 9)) {
			throw Exc.unerlaubteZahl(this, zahl);
		}
	}

	/**
	 * Setzt die Vorgabe und die Rest-Vorgabe
	 * 
	 * @param vorgabe
	 *            des reset(..)
	 * @throws Exc
	 */
	public void setzeVorgabeReset(int vorgabe) throws Exc {
		setzeVorgabe(vorgabe);
		if (vorgabe == 0) {
			resetVorgabe = null;
		} else {
			resetVorgabe = new Integer(vorgabe);
		}
	}

	public void setzeVorgabe(int vorgabe) throws Exc {
		kontrolliereZahl(vorgabe);
		if (ebenen.laeuftEine()) {
			int aktuelleEbene = ebenen.gibNummer();
			throw Exc.setzeVorgabeNurOhneEintraege(this, vorgabe, aktuelleEbene);
		}
		this.vorgabe = vorgabe;
		eintrag = null;
		loescheMoegliche();
	}

	/**
	 * Wenn der soeben gel�schte Eintrag der letzte der "alteFeldEbene" war,
	 * wird diese Ebene gel�scht
	 * 
	 * @param alteFeldEbene
	 */
	private void aktualisiereEbeneNachEintragLoeschen(int alteFeldEbene) {
		AnzahlEintraege info = sudokuFelder.gibAnzahlEintraege(alteFeldEbene);

		// War das der letzte Eintrag �berhaupt?
		if (0 == info.anzahlGesamt) {
			ebenen.reset();
			return;
		}
		// War ich der letzte Eintrag der aktuellen Ebene?
		if (0 == info.anzahlEbene) {
			ebenen.loesche();
		}
	}

	private void protokolliereEintrag(Eintrag eintragAlt, Eintrag eintragNeu) {
		if (eintragAlt != null) {
			eintragAlt = new Eintrag(eintragAlt);
		}
		if (eintragNeu != null) {
			eintragNeu = new Eintrag(eintragNeu);
		}
		protokollSchreiber.protokolliere(new FeldNummer(feldNummer), eintragAlt, eintragNeu);
	}

	/**
	 * Setzt bzw. l�scht den Eintrag und protokolliert dies.
	 * 
	 * @param zahl
	 *            1 bis 9: Setzt den Eintrag, 0 l�scht ihn.
	 * @throws Exc
	 *             Wenn das Eintragssetzen nicht stattfinden kann
	 */
	public void setzeEintrag(int zahl) throws Exc {
		Eintrag eintragAlt = this.eintrag;

		kontrolliereZahl(zahl);
		setzeEintragIntern(zahl);

		Eintrag eintragNeu = this.eintrag;
		protokolliereEintrag(eintragAlt, eintragNeu);
	}

	/**
	 * Setzt bzw. l�scht den Eintrag und protokolliert dies. Ein gesetzter
	 * Eintrag wird als Zahl-Tip markiert.
	 * 
	 * @param zahl
	 * @throws Exc
	 */
	public void setzeEintragAlsTip(int zahl) throws Exc {
		Eintrag eintragAlt = this.eintrag;

		kontrolliereZahl(zahl);
		setzeEintragIntern(zahl);
		if (istEintrag()) {
			eintrag.markiereAlsTip();
		}

		Eintrag eintragNeu = this.eintrag;
		protokolliereEintrag(eintragAlt, eintragNeu);
	}

	public void wandleEintragZuVorgabe() {
		if (istEintrag()) {
			int zahl = eintrag.gibZahl();
			eintrag = null;
			loescheMoegliche();
			vorgabe = zahl;
		}
	}

	/**
	 * @param zahl
	 *            1 bis9: Setzt den Eintrag, 0 l�scht ihn
	 * @throws Exc
	 *             Wenn das Eintragssetzen nicht stattfinden kann
	 */
	private void setzeEintragIntern(int zahl) throws Exc {
		// Unsinn bei gesetzter Vorgabe
		if (vorgabe > 0) {
			throw Exc.setzeEintragNichtAufVorgabe(this, zahl);
		}
		if (zahl == 0) {
			// ------------------------ Eintrag soll gel�scht werden
			if (!this.istEintrag()) {
				return; // Eintrag existiert NICHT => OK
			} else // Eintrag existiert => Eintrag l�schen
			{
				// gearbeitet wird nur auf der aktuellen Ebene
				int aktuelleEbene = ebenen.gibNummer();
				if (eintrag.gibEbene() < aktuelleEbene) {
					throw Exc.loescheEintragNichtAufEbene(this, aktuelleEbene);
				}
				// Eintrag l�schen
				reset();
				aktualisiereEbeneNachEintragLoeschen(aktuelleEbene);
				return;
			} // if (! this.istEintrag() )
		} // if (zahl == 0)

		// ---------------------------------- Eintrag setzen
		if ((this.istEintrag())) {
			throw Exc.setzeEintragNichtAufEintrag(this, zahl);
		} else {
			// Feld ohne M�gliche: Kann nicht akzeptiert werden wegen der
			// Ebenen-Steuerung
			if (0 == moegliche.size()) {
				throw Exc.setzeEintragNichtOhneMoegliche(this, zahl);
			}
			// Ist dieser Eintrag m�glich?
			if (!istMoeglich(zahl)) {
				throw Exc.setzeEintragNichtOhneDieseMoegliche(this, zahl);
			}
			// Eventuell ist Ebenen-Start
			boolean istNeueEbene = ebenen.setzeEintragsEbene(this, zahl);
			// Nun doch noch den Eintrag setzen
			eintrag = new Eintrag(zahl, ebenen.gibNummer(), istNeueEbene, false);
			// Und damit gibt es keine M�glichen mehr
			this.loescheMoegliche();
		}
	}

	/**
	 * Setzt den Eintrag unbedingt in das Feld. Das geschieht typisch durch das
	 * Protokoll. Hier werden keinerlei Konsistenzen �berpr�ft: Das Protokoll
	 * ist f�r die richtige Aufrufreihenfolge verantwortlich.
	 * 
	 * @param eintrag
	 * @throws Exc
	 */
	public void setzeEintragUnbedingt(Eintrag eintrag) throws Exc {
		if (eintrag == null) {
			// L�schen des Eintrags
			int meineEbene = this.eintrag.gibEbene();

			this.eintrag = null;
			// Und fein die Ebene mitf�hren!!!
			aktualisiereEbeneNachEintragLoeschen(meineEbene);
		} else {
			// Wirklich einen Eintrag setzen
			this.eintrag = eintrag;
			this.loescheMoegliche();
			// Und fein die Ebene mitf�hren!!!
			ebenen.setzeEintragsEbeneUnbedingt(eintrag.gibEbene());
		}
	}

	@Override
	public int compareTo(Feld other) {
		if (this == other) {
			return 0;
		}
		if (other == null) {
			return 1;
		}
		if (getClass() != other.getClass()) {
			return 1;
		}
		int feldNummerErgebnis = this.feldNummer.compareTo(other.feldNummer);
		return feldNummerErgebnis;
	}

}
