package sudoku.kern.protokoll;

import java.util.ArrayList;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.animator.Animator;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Eintrag;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ProtokollEintragSetzer;
import sudoku.logik.SudokuLogik;
import sudoku.kern.feldmatrix.ProtokollSchreiber;

/**
 * @author Hendrick - Das Protokoll schreibt alle Feld.setzeEintrag() mit. - In
 *         dem Mitgeschriebenen besteht dann die M�glichkeit sich schrittweise
 *         vor bzw. zur�ck zu bewegen. - Es besteht die M�glichkeit, auf
 *         Protokollpunkte eine Markierung zu setzen und sp�ter das Protokoll
 *         auf diesen Punkt zur�ckgehen zu lassen.
 */
public class Protokoll implements ProtokollSchreiber, ProtokollMarkierer {
	public enum Schrittweite {
		EINTRAG, VERSUCH, ALLES
	};

	private ProtokollEintragSetzer eintragSetzer;

	/**
	 * eintraege leer => kursor = -1, sonst kursor = Index in eintraege
	 */
	private int kursor;
	private boolean warVorwaerts;
	private ArrayList<ProtokollEintrag> eintraege;
	/**
	 * Ist nicht n�tig zum restaurieren eines Eintrags (spezielle Methode
	 * Feld.setzeEintragObjekt()). Aber f�r das restaurieren des Auffrischens!
	 */
	private boolean istGesperrt;

	/**
	 * Die id der letzten rausgegebenen Markierung
	 */
	private int letzteMarkierungsID;
	private ArrayList<Markierung> markierungen;

	public class Markierung {
		private final int mKursor;
		private final int mID;

		public Markierung() {
			this.mKursor = kursor;
			letzteMarkierungsID++;
			this.mID = letzteMarkierungsID;
		}
	}

	// -------------------------------------------------------------------
	public Protokoll(SudokuLogik sudoku) {
		reset();
		sudoku.setzeProtokoll(this);
	}

	public void setzeEintragSetzer(ProtokollEintragSetzer eintragSetzer) {
		this.eintragSetzer = eintragSetzer;
	}

	public void reset() {
		kursor = -1;
		eintraege = new ArrayList<ProtokollEintrag>();
		istGesperrt = false;
		warVorwaerts = false;
		letzteMarkierungsID = 0;
		markierungen = new ArrayList<Markierung>();
	}

	public void reset(Protokoll_IO protokoll_IO) throws Exc {
		reset();
		if (protokoll_IO != null) {
			eintraege = protokoll_IO.gibEintraege();
			int sollKursor = protokoll_IO.gibKursor();
			while (kursor < sollKursor) {
				geheEinenSchrittVorwaerts();
			}
		}
	}

	/**
	 * @param index
	 *            in eintraege
	 * @return Im Eintrag muss genau eine FeldInfo einen eintrag besitzen. Von
	 *         ihm wird die Ebene genommen.
	 * @throws Exc
	 */
	private int gibEintragsEbene(int index) throws Exc {
		ProtokollEintrag p = eintraege.get(index);

		int ebene = p.gibEintragsEbene();
		return ebene;
	}

	/**
	 * Im Sudoku ein Feld ver�ndern
	 * 
	 * @param feldInfo
	 * @throws Exc
	 */
	private void geheEinenSchritt(FeldNummer feldNummer, Eintrag eintrag) throws Exc {
		Eintrag kopie = eintrag;
		if (eintrag != null) {
			kopie = new Eintrag(eintrag);
		}
		this.eintragSetzer.setzeEintrag(feldNummer, kopie);
	}

	private void geheEinenSchrittVorwaerts() throws Exc {
		kursor++;
		warVorwaerts = true;
		ProtokollEintrag eintrag = eintraege.get(kursor);

		geheEinenSchritt(eintrag.gibFeldNummer(), eintrag.eintragNeu);
	}

	private boolean istVorwaertsMoeglich() {
		return ((!eintraege.isEmpty()) && (kursor < (eintraege.size() - 1)));
	}

	private void geheVorwaerts(Schrittweite schrittweite) throws Exc {
		switch (schrittweite) {
		case ALLES:
			while (kursor < (eintraege.size() - 1)) {
				geheEinenSchrittVorwaerts();
			}
			break;
		case VERSUCH:
			int vergleichsEbene = 0;
			// Der kursor kann hier auch -1 sein bei eintraege.size() >0 !!
			// Die Vergleichsebene ist die des N�chsten Eintrags, denn es soll
			// ja vorw�rts gehen!
			vergleichsEbene = gibEintragsEbene(kursor + 1);

			while (kursor < (eintraege.size() - 1)) {
				int ebenePlus1 = gibEintragsEbene(kursor + 1);
				if (vergleichsEbene == ebenePlus1) {
					geheEinenSchrittVorwaerts();
				} else {
					break;
				}
			} // while
			break;
		case EINTRAG:
			geheEinenSchrittVorwaerts();
			break;
		default:
			break;
		}
	}

	private void geheEinenSchrittRueckwaerts() throws Exc {
		ProtokollEintrag eintragSoll = eintraege.get(kursor);

		geheEinenSchritt(eintragSoll.gibFeldNummer(), eintragSoll.eintragAlt);
		kursor--;
		warVorwaerts = false;
	}

	private boolean istRueckwaertsMoeglich() {
		return ((!eintraege.isEmpty()) && (kursor >= 0));
	}

	private void geheRueckwaerts(Schrittweite schrittweite) throws Exc {
		switch (schrittweite) {
		case ALLES:
			while (kursor > -1) {
				geheEinenSchrittRueckwaerts();
			}
			break;
		case VERSUCH:
			int vergleichsEbene = gibEintragsEbene(kursor);
			while (kursor >= 0) {
				int ebeneAktuell = gibEintragsEbene(kursor);
				if ((ebeneAktuell == vergleichsEbene)) {
					geheEinenSchrittRueckwaerts();
				} else {
					break;
				}
			} // while
			break;
		case EINTRAG:
			geheEinenSchrittRueckwaerts();
			break;
		default:
			break;
		}
		// markierungenUeberKursorLoeschen();
	}

	public void gehe(Schrittweite schrittweite, boolean vorwaerts) throws Exc {
		// Die durch mich durchgef�hrten Schritte schreibe ich nicht mit
		istGesperrt = true;

		if (vorwaerts) {
			if (istVorwaertsMoeglich()) {
				geheVorwaerts(schrittweite);
			}
		} else {
			if (istRueckwaertsMoeglich()) {
				geheRueckwaerts(schrittweite);
			}
		}

		istGesperrt = false;
	}

	/**
	 * @param id
	 * @return Die Markierung oder null wenn es keine mit der id gibt.
	 */
	private Markierung markierungGib(int id) {
		Markierung markierung = null;

		for (int i = 0; i < markierungen.size(); i++) {
			Markierung aMarkierung = markierungen.get(i);
			if (id == aMarkierung.mID) {
				markierung = aMarkierung;
			}
		}
		return markierung;
	}

	/**
	 * @param id
	 *            Die id der Markierung die der Externe durch markierungSetzen
	 *            bekommen hatte.
	 * @return Vom Eintrag nach der markierung das was gesetzt wurde
	 *         (feldInfo-Neu) oder null
	 * @throws Exc
	 * 
	 */
	public FeldNummerMitZahl markierungGibZahlTip(int id) throws Exc {
		Markierung markierung = markierungGib(id);
		if (markierung == null) {
			throw Exc.protokollMarkierungExistiertNicht(id, markierungen);
		}

		int tipIndex = markierung.mKursor + 1;

		// if (! istVorwaertsMoeglich()){ ist direkt falsch: der Kursor steht
		// jetzt am Ende!
		if (tipIndex >= eintraege.size()) {
			throw Exc.protokollMarkierungIstOhneNachfolger(markierung.mID, markierung.mKursor, eintraege.size());
		}

		ProtokollEintrag protokollEintrag = eintraege.get(tipIndex);
		Eintrag eintrag = protokollEintrag.eintragNeu;
		if (eintrag == null) {
			throw Exc.protokollTipOhneEintrag(protokollEintrag.gibFeldNummer(), tipIndex);
		}

		FeldNummerMitZahl tip = new FeldNummerMitZahl(protokollEintrag.gibFeldNummer(), eintrag.gibZahl());
		return tip;
	}

	public void markierungAnsteuern(int id) throws Exc {
		Markierung markierung = markierungGib(id);

		if (markierung == null) {
			throw Exc.protokollMarkierungExistiertNicht(id, markierungen);
		}

		boolean istVorwaerts = kursor < markierung.mKursor;
		if (istVorwaerts) {
			if (istVorwaertsMoeglich()) {
				while (kursor < markierung.mKursor) {
					gehe(Schrittweite.EINTRAG, true);// vorwaerts);
				}
			}
		} else {
			if (istRueckwaertsMoeglich()) {
				while (kursor > markierung.mKursor) {
					gehe(Schrittweite.EINTRAG, false);// vorwaerts);
				}
			}
		}
		// System.out.println("Protokoll.geheZurueckZuMarkierung");
		// printout();
	}

	/**
	 * Ich protokolliere das Setzen eines Eintrags in einem Feld
	 * 
	 * @param eintragAlt
	 * @param eintragNeu
	 */
	public void protokolliere(FeldNummer feldNummer, Eintrag eintragAlt, Eintrag eintragNeu) {
		if (istGesperrt) {
			return;
		}

		// Wenn das L�schen auf Feldern, die gar keinen Eintrag besitzen
		// durchgef�hrt werden darf:
		// Ben�tigen wir hier dies if.. ,
		// denn ich ben�tige immer eine FeldInfo mit Eintrag zum Ermitteln der
		// Ebene
		if ((eintragAlt == null) && (eintragNeu == null)) {
			return;
		}

		// Wenn der Kursor nicht am Ende steht, will jemand irgendwo mittendrin
		// weitermachen.
		// Also werden alle Eintr�ge �ber dem Kursor gel�scht.
		if (istVorwaertsMoeglich()) {
			while (kursor < (eintraege.size() - 1)) {
				eintraege.remove(eintraege.size() - 1);
			}
			markierungenUeberKursorLoeschen();
		}

		ProtokollEintrag protokollEintrag = new ProtokollEintrag(feldNummer, eintragAlt, eintragNeu);
		eintraege.add(protokollEintrag);
		kursor++;
		warVorwaerts = true;
	}

	/**
	 * @return Die Informationen zu den Kursoren Schritt und Ebene
	 * @throws Exc
	 */
	public ProtokollKursorInfo gibKursorInfo() throws Exc {

		// Der kleinste Kursor wird auch als 0 gezeigt (zeigt auf keinen
		// Eintrag)
		int kursorEintrag = this.kursor + 1;
		// min=0: Das hei�t, dass keine (eben 0) Eintr�ge vorhanden sind
		MinMax eintrag = new MinMax(0, eintraege.size());
		;

		// 0 hei�t keine Ebene
		int kursorEbene = 0;
		int anzahlEbenen = 0;

		if (eintraege.size() > 0) {
			// Es gibt Ebenen

			// Falls mit einem Versuch gestartet wurde ist die Ebene zu
			// korrigieren um eine Anzahl zu erhalten
			int ebeneKorrektur = 0;
			{
				ProtokollEintrag eintrag0 = eintraege.get(0);
				ebeneKorrektur = EintragsEbenen.gibStandardEbene1() - eintrag0.eintragNeu.gibEbene();
			}

			if (kursor >= 0) {
				int ebeneAktuellerEintrag = gibEintragsEbene(kursor);
				kursorEbene = ebeneAktuellerEintrag + ebeneKorrektur;
			}

			ProtokollEintrag eintragMax = eintraege.get(eintraege.size() - 1);
			int ebene = 0;
			if (eintragMax.eintragNeu != null) {
				ebene = eintragMax.eintragNeu.gibEbene();
			} else {
				ebene = eintragMax.eintragAlt.gibEbene();
			}
			anzahlEbenen = ebene + ebeneKorrektur;
		}
		MinMax ebenen = new MinMax(0, anzahlEbenen);

		boolean vorwaertsMoeglich = istVorwaertsMoeglich();
		boolean rueckwaertsMoeglich = istRueckwaertsMoeglich();

		ProtokollKursorInfo protokollInfo = new ProtokollKursorInfo(eintrag, ebenen, kursorEintrag, kursorEbene,
				vorwaertsMoeglich, rueckwaertsMoeglich);
		return protokollInfo;
	}

	public void printout() {
		String sWoIntern = "Protokoll :" + " maxIndex=" + (eintraege.size() - 1) + " " + kursor + "=Kursor";
		System.out.println(sWoIntern);

		if (eintraege.isEmpty()) {
			return;
		}

		String sLeer = "   ";
		String sKursorLeer = "---";
		String sKursorVor = ">0$";
		String sKursorZurueck = "$0<";

		String sEintragIndex = String.format("Eintrag :%s", sLeer);
		String sKursor = "Kursor  :";
		String sSetzen = String.format("gesetzt :%s", sLeer);
		String sFeldNummer = String.format("FIndex  :%s", sLeer);
		String sLoeschen = String.format("gel�scht:%s", sLeer);

		if (kursor == -1) {
			sKursor += sKursorZurueck;
		} else {
			sKursor += sKursorLeer;
		}
		// Mit allen Eintr�gen
		for (int iEintrag = 0; iEintrag < eintraege.size(); iEintrag++) {
			// sEintragIndex
			{
				String s = String.format("%3d", iEintrag);
				sEintragIndex += s;
			}

			ProtokollEintrag protokollEintrag = eintraege.get(iEintrag);
			Eintrag eintragAlt = protokollEintrag.eintragAlt;
			Eintrag eintragNeu = protokollEintrag.eintragNeu;

			// sFeldNummer
			{
				String s = String.format(" %d%d", protokollEintrag.gibFeldNummer().gibZeile(),
						protokollEintrag.gibFeldNummer().gibSpalte());
				sFeldNummer += s;
			}

			int ebene = 0;

			// sSetzen
			if (eintragNeu != null) {
				ebene = eintragNeu.gibEbene();
				String s = String.format(" %d%d", ebene, eintragNeu.gibZahl());
				sSetzen += s;
			} else {
				sSetzen += sLeer;
			}

			// sLoeschen
			if (eintragNeu == null) {
				ebene = eintragAlt.gibEbene();
				String s = String.format(" %d%d", ebene, eintragAlt.gibZahl());
				sLoeschen += s;
			} else {
				sLoeschen += sLeer;
			}

			// sKursor
			String sKursorEintrag = sKursorLeer;
			if (kursor == iEintrag) {
				if (this.warVorwaerts) {
					sKursorEintrag = sKursorVor;
				} else {
					sKursorEintrag = sKursorZurueck;
				}
				String sEbene = String.valueOf(ebene);
				sKursorEintrag = sKursorEintrag.replace('0', sEbene.charAt(0));
			}
			sKursor += sKursorEintrag;

		} // for (iEintrag
		System.out.println(sEintragIndex);
		System.out.println(sKursor);
		System.out.println(sSetzen);
		System.out.println(sFeldNummer);
		System.out.println(sLoeschen);
	}

	/**
	 * Markierungen, die auf Eintr�ge �ber dem Kursor zeigen werden gel�scht.
	 */
	private void markierungenUeberKursorLoeschen() {
		ArrayList<Markierung> zuLoeschen = new ArrayList<Markierung>();

		for (int i = 0; i < markierungen.size(); i++) {
			Markierung markierung = markierungen.get(i);
			if (markierung.mKursor > this.kursor) {
				zuLoeschen.add(markierung);
			}
		}
		markierungen.removeAll(zuLoeschen);
	}

	/**
	 * Setzt eine Markierung auf die aktuelle Kursor-Position
	 * 
	 * @return Den Identifikator der Markierung
	 */
	public int markierungSetzen() {
		Markierung markierung = new Markierung();
		markierungen.add(markierung);
		// System.out.println("Protokoll.setzeMarkierung");
		// printout();
		return markierung.mID;
	}

	public Protokoll_IO gibSpeicherInfo() {
		return new Protokoll_IO(kursor, eintraege);
	}

	public void animiere(Animator animator) {
		// Mit allen Eintr�gen
		for (int iEintrag = 0; iEintrag < eintraege.size(); iEintrag++) {
			ProtokollEintrag protokollEintrag = eintraege.get(iEintrag);
			protokollEintrag.animiere(animator);
		}
	}
}
