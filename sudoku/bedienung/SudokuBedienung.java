package sudoku.bedienung;

import java.io.IOException;
import java.util.ArrayList;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.animator.Animator;
import sudoku.kern.exception.EndeDurchAusnahme;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;
import sudoku.kern.info.FeldInfo;
import sudoku.kern.info.InfoSudoku;
import sudoku.kern.protokoll.Protokoll;
import sudoku.kern.protokoll.Protokoll_IO;
import sudoku.kern.protokoll.Protokoll.Schrittweite;
import sudoku.kern.protokoll.ProtokollKursorInfo;
import sudoku.knacker.Ergebnis;
import sudoku.knacker.Knacker;
import sudoku.langerprozess.FortschrittAnzeige;
import sudoku.logik.Klugheit;
import sudoku.logik.Logik_ID;
import sudoku.logik.Schwierigkeit;
import sudoku.logik.SudokuLogik;
import sudoku.schwer.Analysator;
import sudoku.schwer.SudokuSchwierigkeit;
import sudoku.schwer.daten.Schwierigkeiten;
import sudoku.tip.TipBericht;
import sudoku.tools.TextDatei;
import sudoku.neu.NeuTyp;
import sudoku.neu.SudokuPool;
import sudoku.neu.pool.NeuTypOption;
import sudoku.neu.pool.PoolInfo;
import sudoku.varianz.Loesungen;
import sudoku.varianz.Varianz;

public class SudokuBedienung {
	// F�r Fehlersuche
	private static boolean istPrintoutProtokoll = false;

	// Das aktuell in Bearbeitung befindliche Sudoku
	private SudokuLogik sudokuLogik;
	// Die Klugheit, die f�r das L�sen des aktuell in Bearbeitung befindlichen
	// Sudoku genutzt wird
	private Klugheit klugheit;
	// protokolliert den L�sungsvorgang des aktuell in Bearbeitung befindlichen
	// Sudoku
	private Protokoll protokoll;
	// Name des aktuell in Bearbeitung befindlichen Sudoku
	private String name;
	// zur Optimierung von this.gibAnzahlVorgaben() des aktuell in Bearbeitung
	// befindlichen Sudoku
	private int nVorgaben;
	// Schwierigkeit des aktuell in Bearbeitung befindlichen Sudoku
	private SudokuSchwierigkeit schwierigkeit;

	// Ist der Liererant f�r neue Sudokus: Im besten Fall sind immer welche
	// vorr�tig
	private SudokuPool sudokuPool;

	// Alle Anzeigeelemente, die sich angemeldet haben, werden hier vermerkt.
	private ArrayList<AnzeigeElement> anzeigeElemente;
	// Alle Bedienelemente, die sich angemeldet haben, werden hier vermerkt.
	private ArrayList<BedienElement> bedienElemente;
	/**
	 * ist typisch null. Das kann beim letzten this.auffrischen() genannt worden
	 * sein. Oder bei setzeEintraegeAufKlareAlle()
	 */
	private Problem problem;
	// Vermerkt den letzten Bericht der Logik bis zum Abruf per gibTipBericht()
	// private sudoku.tip.TipBericht tipBericht;

	/**
	 * @param externeAusnahmeBehandlung
	 *            falls != null wird im internen Thread diese Ausnahmebehandlung
	 *            f�r nicht gefangene Ausnahmen eingeklinkt, ansonsten die
	 *            Standardbehandlung des genannten Typs.
	 * @throws Exc
	 */
	public SudokuBedienung(sudoku.tools.AusnahmeBehandlung externeAusnahmeBehandlung) throws Exc {
		klugheit = new Klugheit(true);
		sudokuLogik = new SudokuLogik(new EintragsEbenen());
		protokoll = new Protokoll(sudokuLogik);
		sudokuPool = new SudokuPool(externeAusnahmeBehandlung);
		name = null;
		anzeigeElemente = new ArrayList<AnzeigeElement>();
		bedienElemente = new ArrayList<BedienElement>();
		initZwischenspeicher();
	}

	/**
	 * Setzt alle internen Zwischenspeicher zur�ck: In Grundstellung
	 */
	private void initZwischenspeicher() {
		problem = null;
		// tipBericht = null;
		nVorgaben = 0;
		schwierigkeit = null;
	}

	private void aktionVorbereiten() {
		sperreBedienElemente(true);
		problem = null;
		// tipBericht = null;
	}

	private void aktionNachbereiten(Exception e) {
		if (e != null) {
			throw (new EndeDurchAusnahme(e));
		}
		sperreBedienElemente(false);
		anzeige();
	}

	/**
	 * L�scht das Sudoku und stellt es aus der Datei
	 */
	public void reset(String nameVerzeichnis, String nameDatei) {
		aktionVorbereiten();
		try {
			InfoSudoku vorgaben = InfoSudoku.lade(nameVerzeichnis + nameDatei);
			Protokoll_IO protokoll_IO = new Protokoll_IO(nameVerzeichnis + nameDatei);
			resetIntern(vorgaben, nameDatei, protokoll_IO);
			auffrischen(true);
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	private void resetIntern(InfoSudoku vorgaben, String name, Protokoll_IO protokoll_IO) throws Exc {
		this.name = name;
		this.sudokuLogik.reset(vorgaben);
		initZwischenspeicher();
		protokoll.reset(protokoll_IO);
		this.schwierigkeit = Analysator.gibSchwierigkeit(vorgaben);
	}

	/**
	 * L�scht das Sudoku und stellt es aus dem InfoSudoku
	 * 
	 * @param vorgaben
	 * @param name
	 *            des Sudoku
	 */
	public void reset(InfoSudoku vorgaben, String name) {
		aktionVorbereiten();
		try {
			resetIntern(vorgaben, name, null);
			auffrischen(true);
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * Bewegt die Vorgaben des Sudoku unkritisch. Diese einfache Implementation
	 * ist nur m�glich ohne Eintr�ge, denn sie tr�gt einfach die Vorgaben hin
	 * und her! Eine Implementation mit Eintr�gen muss neben dem jeweiligen
	 * Umtragen der Vorgaben auch das Protokoll animieren und dann die Eintr�ge
	 * wieder einspielen! Plan: Ohne jedes reset() und initZwischenspeicher. 0.
	 * protokoll: Markierung setzen. 1. protokoll: Alles zur�ckgehen bedeutet
	 * alle Eintr�ge verschwinden lassen. 2. protokoll: Sperren: Ist das
	 * �berhaupt n�tig? 3. FeldMatrix: Vorgaben animieren 4. protokoll:
	 * animieren der FeldNummern 5. protokoll: Zur Markierung wieder vorgehen.
	 * Geht VOR-Gehen - bis jetzt war wohl blo� R�ckw�rts-Gehen!?
	 * 
	 * @param vorgaben
	 * @param name
	 *            des Sudoku
	 */
	public void animiere(Animator animator) {
		aktionVorbereiten();
		try {
			int protokollMarkierung = protokoll.markierungSetzen();
			// Eintr�ge verschwinden lassen, schon l�schen
			protokoll.gehe(Schrittweite.ALLES, /* vorwaerts= */ false);
			// Vorgaben animieren
			this.sudokuLogik.animiere(animator);
			protokoll.animiere(animator);
			// Eintr�ge wieder setzen
			protokoll.markierungAnsteuern(protokollMarkierung);
			// this.schwierigkeit = Analysator.gibSchwierigkeit(vorgaben);
			auffrischen(true);
			aktionNachbereiten(null);
		} catch (Exc e) {
			this.problem = new Problem(e.getMessage());
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * L�scht das Sudoku und stellt es aus dem text
	 * 
	 * @param vorgaben
	 * @param name1
	 */
	public void setzeSchwierigkeit() {
		aktionVorbereiten();
		try {
			InfoSudoku vorgaben = sudokuLogik.gibVorgaben();
			this.schwierigkeit = Analysator.gibSchwierigkeit(vorgaben);
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * @return Die Anzahl der Vorgaben
	 */
	public int gibAnzahlVorgaben() {
		if (nVorgaben == 0) {
			this.nVorgaben = sudokuLogik.gibAnzahlVorgaben();
		}
		return nVorgaben;
	}

	/**
	 * @return Die Anzahl der freien Felder
	 */
	public int gibAnzahlFreierFelder() {
		int nFreieFelder = sudokuLogik.gibFreieFelder().size();
		return nFreieFelder;
	}

	/**
	 * @param anzeigeElement
	 *            wird als solches vermerkt
	 */
	public void registriereAnzeigeElement(AnzeigeElement anzeigeElement) {
		anzeigeElemente.add(anzeigeElement);
	}

	private void anzeige() {
		for (int i = 0; i < anzeigeElemente.size(); i++) {
			AnzeigeElement anzeigeElement = anzeigeElemente.get(i);
			anzeigeElement.zeige(this);
		}
	}

	/**
	 * @param anzeigeElement
	 *            wird als solches vermerkt
	 */
	public void registriereBedienElement(BedienElement bedienElement) {
		bedienElemente.add(bedienElement);
	}

	private void sperreBedienElemente(boolean istSperren) {
		for (int i = 0; i < bedienElemente.size(); i++) {
			BedienElement bedienElement = bedienElemente.get(i);
			if (istSperren) {
				bedienElement.sperre();
			} else {
				int nVorgaben = this.sudokuLogik.gibAnzahlVorgaben();
				boolean istVorgabenMin = nVorgaben >= SudokuLogik.gibAnzahlVorgabenMin();
				ProtokollKursorInfo protokollKursorInfo = null;
				try {
					protokollKursorInfo = gibProtokollKursorInfo();
				} catch (Exc e) {
					e.printStackTrace();
				}
				bedienElement.entsperre(istVorgabenMin, protokollKursorInfo);
			}
		}
		// Thread.sleep(10);
	}

	/**
	 * Wird gerufen nach einer Ver�nderung des Sudoku
	 * 
	 * @param mitAnzeige
	 *            bei true werden die angemeldeten AnzeigeElemente angesprochen
	 */
	private void auffrischen(boolean mitAnzeige) {
		problem = null;
		try {
			sudokuLogik.setzeMoegliche(klugheit, mitAnzeige);
		} catch (Exc e) {
			e.printStackTrace();
			throw new EndeDurchAusnahme(e);
		}

		if (mitAnzeige) {
			anzeige();
		}
	}

	/**
	 * @param zeile
	 *            1 bis 9
	 * @param spalte
	 *            1 bis 9
	 * @return FeldInfo oder null
	 */
	public FeldInfo gibFeldInfo(FeldNummer feldNummer) {
		// Ohne aktionVorbereiten() + aktionNachbereiten(e) wegen Stack-Overflow
		// !!!
		try {
			FeldInfo feldInfo = sudokuLogik.gibFeldInfo(feldNummer);
			return feldInfo;
		} catch (Exc e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return Das Problem, das beim letzten Auffrischen aufgetreten ist oder
	 *         null
	 */
	public String gibProblem() {
		if (problem != null) {
			return problem.toString();
		}
		return null;
	}

	/**
	 * Gibt die Vorgaben des Sudoku zur�ck.
	 */
	public InfoSudoku gibVorgaben() {
		return sudokuLogik.gibVorgaben();
	}

	/**
	 * Gibt einen Schnappschuss des Sudoku zur�ck.
	 */
	public InfoSudoku gibSchnappschuss() {
		InfoSudoku schnappschuss = this.sudokuLogik.gibFeldInfos();
		return schnappschuss;
	}

	public String gibKlugheitTextKurz() {
		return klugheit.gibTextKurz();
	}

	/**
	 * @return true wenn die Klugheit aktuell eingeschaltet ist
	 */
	public boolean istSoKlug(Logik_ID logik) {
		return klugheit.istSoKlug(logik);
	}

	/**
	 * @return true wenn die Logik zur Erstellung von m�glichen Zahlen (schon)
	 *         l�uft
	 */
	public boolean istMoeglichLogik() {
		return gibAnzahlVorgaben() >= SudokuLogik.gibAnzahlVorgabenMin();
	}

	/**
	 * @return Den Stand des Protokolls
	 * @throws Exc
	 */
	private ProtokollKursorInfo gibProtokollKursorInfo() throws Exc {
		ProtokollKursorInfo info = protokoll.gibKursorInfo();
		return info;
	}

	/**
	 * @return true wenn es Eintr�ge gibt
	 */
	public boolean ebeneLaeuftEine() {
		return sudokuLogik.ebeneLaeuftEine();
	}

	/**
	 * @param zeile
	 *            1 bis 9
	 * @param spalte
	 *            1 bis 9
	 * @param vorgabe
	 */
	public void setzeVorgabe(FeldNummer feldNummer, int vorgabe) {
		aktionVorbereiten();
		try {
			sudokuLogik.setzeVorgabe(feldNummer, vorgabe);
			nVorgaben = 0;
			auffrischen(true);
			aktionNachbereiten(null);
		} catch (Exc e) {
			this.problem = new Problem(e.getMessage());
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * @param zeile
	 *            1 bis 9
	 * @param spalte
	 *            1 bis 9
	 * @param eintrag
	 *            1 bis 9 oder 0 l�scht den Eintrag
	 */
	public void setzeEintrag(FeldNummer feldNummer, int eintrag) {
		aktionVorbereiten();
		try {
			sudokuLogik.setzeEintrag(new FeldNummerMitZahl(feldNummer, eintrag));
			auffrischen(true);
			printoutProtokoll("Sudoku.setzeEintrag()");
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	public void setzeName(String name) {
		this.name = name;
	}

	/**
	 * Freie Felder, in denen die Logik erkennt, dass nur eine Zahl m�glich ist,
	 * erhalten diese m�gliche Zahl als Eintrag
	 * 
	 * @param alle
	 *            bei true wird ein Eintrag in alle klaren Felder gesetzt, bei
	 *            false nur in das erste (beste) logisch erkannte Feld.
	 * @return Die Anzahl der gesetzten Eintr�ge
	 * @throws Problem
	 */
	public void setzeEintraegeAufKlare(boolean alle) {
		aktionVorbereiten();
		try {
			problem = null;
			problem = sudokuLogik.setzeEintraegeAufKlare(klugheit, alle, true);
			anzeige();
			printoutProtokoll("Sudoku.setzeEintraegeAufKlareAlle()");
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	public void setzeLogik(Logik_ID logik, boolean istSoKlug) {
		sperreBedienElemente(true);
		klugheit.setzeLogik(logik, istSoKlug);
		sperreBedienElemente(false);
		auffrischen(true);
	}

	public void setzeKlugheit(Klugheit klugheit) {
		sperreBedienElemente(true);
		this.klugheit = new Klugheit(klugheit);
		sperreBedienElemente(false);
		auffrischen(true);
	}

	public Klugheit gibKlugheit() {
		Klugheit kopie = new Klugheit(this.klugheit);
		return kopie;
	}

	public void gehe(Schrittweite schrittweite, boolean vorwaerts) {
		aktionVorbereiten();
		try {
			protokoll.gehe(schrittweite, vorwaerts);
			auffrischen(true);
			printoutProtokoll("Sudoku.gehe()");
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * Versucht das Sudoku zu knacken (zu l�sen). Wenn das Sudoku fertig ist,
	 * sieht man das in den Feldern, ansonsten wird bei gibProblem() (im Rahmen
	 * der Anzeige) das Dilemma genannt.
	 */
	public void knacke() {
		aktionVorbereiten();
		try {
			Knacker knacker = new Knacker(klugheit, sudokuLogik, protokoll);
			Ergebnis ergebnis = knacker.knacke(name);
			auffrischen(false);

			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG) {
				// Zwischenspeicher bis gibSchwierigkeit();
				this.schwierigkeit = Analysator.gibSchwierigkeit(sudokuLogik.gibVorgaben());
			} else {
				// Folgende auffrischen() sollen nicht ein vom Automaten
				// genanntes Problem zerst�ren:
				// Deshalb wird es hier zwischengespeichert.
				String s = Knacker.gibText(ergebnis);
				problem = new Problem(s);
			}
			anzeige();
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * Ermittelt die Anzahl der L�sungen des Sudoku
	 */
	public Loesungen ermittleLoesungen(int maxAnzahl) {
		aktionVorbereiten();
		Loesungen loesungen = null;
		try {
			loesungen = Varianz.gibLoesungen(sudokuLogik, protokoll, maxAnzahl);

			auffrischen(false);

			anzeige();
			aktionNachbereiten(null);
			return loesungen;
		} catch (Exception e) {
			loesungen = null;
			aktionNachbereiten(e);
		}
		return loesungen;
	}

	// public TipBericht gibTipBericht(){
	// TipBericht t = this.tipBericht;
	// this.tipBericht = null;
	// return t;
	// }

	/**
	 * @return Falls das knackerErgebnis existiert dessen Schwierigkeit, sonst
	 *         null
	 */
	public SudokuSchwierigkeit gibSchwierigkeit() {
		return schwierigkeit;
	}

	/**
	 * Versucht das Sudoku zu l�sen. Wenn das gelingt, wird die TipZahl intern
	 * vermerkt. Die TipZahl kann im Rahmen der Anzeige mit gibTipZahl()
	 * abgefragt werden.
	 */
	public void setzeTipZahl() {
		aktionVorbereiten();
		try {
			Knacker knacker = new Knacker(klugheit, sudokuLogik, protokoll);

			Knacker.ZahlTipErgebnis zahlErgebnis = knacker.gibTipZahl(name);
			// Der Tip wird hier sofort realisiert.
			// Die Frage ist, ob das jedem schmeckt.
			if (zahlErgebnis.zahl != null) {
				sudokuLogik.setzeEintragAlsTip(zahlErgebnis.zahl);
				auffrischen(false);
			}
			if (zahlErgebnis.problem != null) {
				problem = zahlErgebnis.problem;
			}

			if ((zahlErgebnis.zahl == null) & (zahlErgebnis.problem == null)) {
				problem = new Problem("Das Sudoku scheint nicht l�sbar");
			}

			anzeige();
			printoutProtokoll("Sudoku.setzeEintragAlsTip()");
			aktionNachbereiten(null);
		} catch (Exception e) {
			aktionNachbereiten(e);
		}
	}

	/**
	 * L��t die Sudoku-Logik.setzeMoegliche() laufen. Wenn dabei ein Tip
	 * entsteht, wird dieser im TipBericht intern vermerkt. Der TipBericht kann
	 * im Rahmen der Anzeige mit gibTipBericht() abgefragt werden.
	 */
	public void setzeTip(TipBericht tipBericht) {
		boolean istNurLogik = false;
		if (!istNurLogik) {
			aktionVorbereiten();
		}
		try {
			// this.tipBericht = tipBericht;
			sudokuLogik.registriereTipKurier(tipBericht);
			// tipBericht vollschreiben
			auffrischen(false);
			// Die Tip-Erstellung hinterl��t leider irgendwas untypisches.
			// Deshalb hier hinterher nochmal der Standard:
			// auffrischen( true); // mit Anzeige
			if (!istNurLogik) {
				aktionNachbereiten(null);
			}
		} catch (Exception e) {
			if (!istNurLogik) {
				aktionNachbereiten(e);
			}
		}
	}

	/**
	 * Setzt in den Pool das Sudoku zur Aufbewahrung
	 * 
	 * @param neuTyp
	 * @param sudoku
	 * @param loesungsZeit
	 */
	public void setze(NeuTyp neuTyp, InfoSudoku sudoku, int loesungsZeit) {
		this.sudokuPool.setze(neuTyp, sudoku, loesungsZeit);
	}

	private void printoutProtokoll(String wo) {
		if (istPrintoutProtokoll) {
			System.out.println(wo);
			// kern.printoutEbenen();
			protokoll.printout();
		}
	}

	/**
	 * Neues Sudoku erstellen.
	 * 
	 * @param neuTyp
	 * @param option
	 * @param vorlage
	 *            != null bei NeuTyp.Typ.VORLAGE
	 * @param fortschrittAnzeige
	 *            != null bei NeuTyp.Typ.VORLAGE
	 * @return null wenn kein neues zur Verf�gung steht
	 */
	public InfoSudoku gibNeues(NeuTyp neuTyp, NeuTypOption option, InfoSudoku vorlage,
			FortschrittAnzeige fortschrittAnzeige) {
		this.sperreBedienElemente(true);

		InfoSudoku neuesSudoku = null;
		if (neuTyp.gibTyp() == NeuTyp.Typ.VORLAGE) {
			neuesSudoku = SudokuNachVorlage.gibNeues(vorlage, sudokuPool, fortschrittAnzeige);
		} else {
			neuesSudoku = sudokuPool.gibSudoku(neuTyp, option);
		}
		aktionNachbereiten(null);
		return neuesSudoku;
	}

	public void schreibeSchwierigkeiten(Schwierigkeit schwierigkeit, FortschrittAnzeige fortschrittAnzeige) {
		this.sperreBedienElemente(true);
		NeuTyp neutyp = new NeuTyp(schwierigkeit);
		String verzeichnisName = this.sudokuPool.gibTopfName(neutyp);
		Schwierigkeiten.schreibe(verzeichnisName, fortschrittAnzeige);
		aktionNachbereiten(null);
	}

	public PoolInfo gibSudokuPoolInfo() {
		this.sperreBedienElemente(true);
		PoolInfo poolInfo = this.sudokuPool.gibPoolInfo();
		aktionNachbereiten(null);
		return poolInfo;
	}

	/**
	 * Speichert das Sudoku komplett: seine Vorgaben und auch seine Eintr�ge.
	 * 
	 * @param nameVerzeichnis
	 * @param nameDatei
	 * @throws IOException
	 */
	public void speichern(String nameVerzeichnis, String nameDatei) throws IOException {

		InfoSudoku vorgaben = gibVorgaben();
		String textVorgaben = vorgaben.gibSpeicherText();
		ArrayList<String> texte = new ArrayList<>();

		texte.add(textVorgaben);
		Protokoll_IO protokoll_IO = protokoll.gibSpeicherInfo();
		ArrayList<String> texteProtokoll = protokoll_IO.gibSpeicherTexte();
		texte.addAll(texteProtokoll);

		nameDatei = InfoSudoku.dateiEndungSichern(nameDatei);
		String pfadName = nameVerzeichnis + nameDatei;
		TextDatei.erstelle(pfadName, texte);
	}

}
