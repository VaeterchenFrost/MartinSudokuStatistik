package sudoku.schwer.daten;

import java.util.ArrayList;

import sudoku.logik.Gruppe;
import sudoku.logik.Logik_ID;
import sudoku.logik.SudokuLogik;
import sudoku.logik.bericht.BE_Logik;
import sudoku.schwer.AnzeigeInfo;
import sudoku.schwer.daten.LogikAnzahlen.LogikAnzahlFormatierer;

/**
 * @author heroe Die Infos zu einem Lauf von SudokuLogik.setzeMoegliche(). Oder
 *         in dem komprimierten Fall, dass mehrere hintereinander laufende
 *         SudokuLogik.setzeMoegliche() mit derselben Logik einen Soll-Eintrag
 *         gefunden haben die Infos zu diesen L�ufen. Dieser Fall ist erkennbar
 *         an anzahlGesetzteEintraege und durchLauf > 1.
 */
public class InfoKlareDetail implements AnzeigeInfo {
	private int durchLauf;
	private ArrayList<BE_Logik> logikEintraege;
	private int nFreieFelder;
	private int nFreieZeilen;
	private int nFreieSpalten;
	private int nFreieKaesten;

	public InfoKlareDetail(int durchLauf, ArrayList<BE_Logik> logikEintraege, int nFreieFelder, int nFreieZeilen,
			int nFreieSpalten, int nFreieKaesten) {
		this.durchLauf = durchLauf;
		this.logikEintraege = logikEintraege;
		this.nFreieFelder = nFreieFelder;
		this.nFreieZeilen = nFreieZeilen;
		this.nFreieSpalten = nFreieSpalten;
		this.nFreieKaesten = nFreieKaesten;
	}

	public void addLogiken(InfoKlareDetail info) {
		this.logikEintraege.addAll(info.logikEintraege);
	}

	public int gibAnzahlGesetzteEintraege() {
		int anzahlEintraege = 0;
		for (BE_Logik logikEintrag : logikEintraege) {
			if (logikEintrag.eintrag != null) {
				anzahlEintraege++;
			}
		}

		return anzahlEintraege;
	}

	/**
	 * @return Die Anzahl erfolgreicher Logiken (je Logik) in diesem
	 *         InfoKlareDetail
	 */
	public LogikAnzahlen gibErfolgreicheLogiken() {
		LogikAnzahlen logikAnzahlen = new LogikAnzahlen();

		for (BE_Logik logikEintrag : logikEintraege) {
			if ((logikEintrag.eintrag != null) | (logikEintrag.loeschZahlen != null)) {
				logikAnzahlen.add(logikEintrag.logik, 1);
			}
		}

		return logikAnzahlen;
	}

	/**
	 * @return Die Anzahl gelaufener Logiken (je Logik) in diesem
	 *         InfoKlareDetail
	 */
	LogikAnzahlen gibGelaufeneLogiken() {
		LogikAnzahlen logikAnzahlen = new LogikAnzahlen();

		for (BE_Logik logikEintrag : logikEintraege) {
			int n = logikEintrag.gruppenlaeufeListe.gibLaeufeAnzahlSumme();
			logikAnzahlen.add(logikEintrag.logik, n);
		}

		return logikAnzahlen;
	}

	public int gibDurchLauf() {
		return durchLauf;
	}

	// public LogikAnzahlen gibGenutzteLogiken() {
	// return genutzteLogiken;
	// }

	/**
	 * @return true wenn nur die Logik OrtFest1 vertreten ist
	 */
	public boolean istNurLogikOrtFest1() {
		for (BE_Logik logikEintrag : logikEintraege) {
			if (logikEintrag.logik != Logik_ID.ORTFEST1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return Anzahl freier Felder
	 */
	public int gibAnzahlFreieFelder() {
		return nFreieFelder;
	}

	/**
	 * @return Anzahl freier Zeilen
	 */
	public int gibAnzahlFreieZeilen() {
		return nFreieZeilen;
	}

	/**
	 * @return Anzahl freier Spalten
	 */
	public int gibAnzahlFreieSpalten() {
		return nFreieSpalten;
	}

	/**
	 * @return Anzahl freier K�sten
	 */
	public int gibAnzahlFreieKaesten() {
		return nFreieKaesten;
	}

	/**
	 * @return Anzahl freier K�sten
	 */
	public int gibAnzahlFreieGruppen() {
		return nFreieKaesten + nFreieZeilen + nFreieSpalten;
	}

	public double gibZeit() {
		ArrayList<InfoKlareDetail> list = new ArrayList<>();
		list.add(this);
		return AnalysatorKlare.gibZeit(list);
	}

	private Gruppe.Typ gibOrtFest1GruppenTyp() {
		Gruppe.Typ gruppenTyp = null;

		if (istNurLogikOrtFest1()) {
			BE_Logik logikEintrag = logikEintraege.get(0);
			gruppenTyp = logikEintrag.gruppenlaeufeListe.gibLetztenTyp();
		}
		return gruppenTyp;
	}

	private String gibOrtFest1GruppenTypZeichen() {
		String soloTyp = new String();
		Gruppe.Typ gruppenTyp = gibOrtFest1GruppenTyp();
		if (gruppenTyp != null) {
			soloTyp = "(" + gruppenTyp.toString().substring(0, 1) + ")";
		}
		return soloTyp;
	}

	String gibZahlTextPerUnterstrich(int zahl, int stringLaenge) {
		String s = zahl == 0 ? "" : String.valueOf(zahl);

		while (s.length() < stringLaenge) {
			s = '_' + s;
		}
		return s;
	}

	private class AnzeigeTextFormatierer implements LogikAnzahlFormatierer {
		@Override
		public String gibString(Logik_ID logikID, int anzahl) {
			String sAnzahl = gibZahlTextPerUnterstrich(anzahl, 2);
			String sLogik = SudokuLogik.gibNameKurz(logikID);
			String s = String.format("%sx%s", sAnzahl, sLogik);
			return s;
		}
	}

	@Override
	public String gibAnzeigeText() {
		// double zeit = AnalysatorKlareZeit.gibDetailZeit(this);
		int anzahlGesetzteEintraege = gibAnzahlGesetzteEintraege();
		LogikAnzahlen logikAnzahlen = gibErfolgreicheLogiken();
		String sLogikAnzahlen = logikAnzahlen.gibString(new AnzeigeTextFormatierer());

		// String gruppenTypZeichen = "";
		// if (anzahlGesetzteEintraege == 1){
		// gruppenTypZeichen = gibOrtFest1GruppenTypZeichen();
		// }

		String s = String.format("[%s]    %s: %s", gibZahlTextPerUnterstrich(nFreieFelder, 2),
				gibZahlTextPerUnterstrich(anzahlGesetzteEintraege, 2), sLogikAnzahlen);
		return s;
	}

	// ---------------------------------------------------------------------------------------
	/**
	 * @return Beschreibung des Aufbaus des Anzeigetextes
	 */
	public static String[] gibAnzeigeTextBeschreibung() {
		String[] texte = { "Anzeige der Logik-Details:", "[fF]   X: nxO1", "fF   ---> Anzahl freier Felder",
				"X    ---> Anzahl gesetze Zahlen (Eintr�ge)", "n   ---> Anzahl der Nutzung der Logik",
				"O1 ---> Eine der Logiken (siehe rechts oben)" };
		return texte;
	}

	// ---------------------------------------------------------------------------------------
	@Override
	public String gibToolTip() {
		String s1 = String.format("%2d neue eingetragene Zahlen im Logiklauf %d", gibAnzahlGesetzteEintraege(),
				this.durchLauf);
		String s2 = String.format("mit folgenden wirksamen Logiken: %s", this.gibErfolgreicheLogiken());
		String s = String.format("<html>%s<br>%s</html>", s1, s2);
		return s;
	}

	@Override
	public String toString() {
		return "InfoKlareDetail [anzahlGesetzteEintraege=" + gibAnzahlGesetzteEintraege() + ", durchLauf=" + durchLauf
				+ ", erfolgreicheLogiken=" + gibErfolgreicheLogiken() + ", gruppenTypen="
				+ gibOrtFest1GruppenTypZeichen() + ", nFreieFelder=" + nFreieFelder + ", nFreieZeilen=" + nFreieZeilen
				+ ", nFreieSpalten=" + nFreieSpalten + ", nFreieKaesten=" + nFreieKaesten + "]";
	}

	public void systemOut(int index) {
		System.out.println("InfoKlareDetail [" + index + "]: Lauf=" + durchLauf + " nF=" + nFreieFelder + ", nZ="
				+ nFreieZeilen + ", nS=" + nFreieSpalten + ", nK=" + nFreieKaesten);
		for (int iLogikEintrag = 0; iLogikEintrag < logikEintraege.size(); iLogikEintrag++) {
			BE_Logik logikEintrag = logikEintraege.get(iLogikEintrag);
			System.out.println(logikEintrag.gibKurzText());
		}
	}
}
