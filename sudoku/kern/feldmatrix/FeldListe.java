package sudoku.kern.feldmatrix;

import java.util.ArrayList;

import sudoku.kern.exception.Exc;

@SuppressWarnings("serial")
public class FeldListe extends ArrayList<Feld> {
	public FeldListe() {
	}

	public FeldListe(FeldListe aList) {
		this.addAll(aList);
	}

	/**
	 * Eine schnellere Variante, von der FeldNummer zum Feld zu kommen, sind
	 * FeldMatrix.gibFeld() oder SudokuLogik.gibLogikFeld.
	 * 
	 * @param feldNummer
	 * @return Das entsprechende Feld oder null falls eines mit dem FeldNummer
	 *         nicht existiert
	 */
	public Feld gibFeld_SehrLahm(FeldNummer feldNummer) {
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			FeldNummer iFeld = feld.gibFeldNummer();
			if (feldNummer.equals(iFeld)) {
				return feld;
			}
		}
		return null;
	}

	// /**
	// * @return Eine Zeilen-Nummer zur�ck wenn alle Felder in derselben Zeile
	// liegen, sonst 0
	// */
	// public int gibZeilenNummer() {
	// if (this.size() == 0) {
	// return 0;
	// }
	// int zeile = this.get(0).gibZeile();
	// for (int i = 1; i < this.size(); i++) {
	// if (this.get(i).gibZeile() != zeile) {
	// return 0;
	// }
	// }
	// return zeile;
	// }
	//
	// /**
	// * @return Eine Spalten-Nummer zur�ck wenn alle Felder in derselben Spalte
	// liegen, sonst 0
	// */
	// public int gibSpaltenNummer() {
	// if (this.size() == 0) {
	// return 0;
	// }
	// int spalte = this.get(0).gibSpalte();
	// for (int i = 1; i < this.size(); i++) {
	// if (this.get(i).gibSpalte() != spalte) {
	// return 0;
	// }
	// }
	// return spalte;
	// }

	/**
	 * @param zahl
	 *            1 bis 9
	 * @return wenn in meinen Feldern zahl als Eintrag (noch) m�glich ist
	 */
	public boolean istMoeglich(int zahl) {
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if ((zahl == feld.gibVorgabe()) || (zahl == feld.gibEintrag())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return Die Anzahl der gesetzten Eintr�ge
	 * @throws Exc
	 */
	public int setzeEintragAufKlare() throws Exc {
		int n = 0;
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).setzeEintragFallsNur1Moeglich()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * @return Die Anzahl der Vorgaben
	 */
	public int gibAnzahlVorgaben() {
		int n = 0;
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istVorgabe()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * @return Die Anzahl der freien Felder
	 */
	public int gibAnzahlFreieFelder() {
		int n = 0;
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istFrei()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * @return true wenn (mindestens) ein freies Feld existiert
	 */
	public boolean existiertFreiesFeld() {
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istFrei()) {
				return true;
			}
		}
		return false;
	}

	// /**
	// * Dies sollte niemals relevant sein !!!
	// * @return Die Anzahl freier Felder mit (nur) einer m�glichen Zahl
	// */
	// public int gibAnzahlKlare() {
	// int n = 0;
	// for (int i = 0; i < this.size(); i++) {
	// Feld feld = this.get(i);
	// if (feld.istKlar()) {
	// n++;
	// }
	// }
	// return n;
	//
	// }
	//
	// /**
	// * @return null oder das erste (beste) freie Feld mit (nur) 1 m�glichen
	// Zahl
	// */
	// public FeldInfo gibErstesFreiesKlaresFeld(){
	// for (int i = 0; i < this.size(); i++) {
	// Feld feld = this.get(i);
	// if (feld.istKlar()) {
	// return new FeldInfo(feld);
	// }
	// }
	//
	// return null;
	// }

	/**
	 * @author Hendrick Ergebnis von @see gibAnzahlEintraege
	 */
	public class AnzahlEintraege {
		public int anzahlGesamt;
		public int nummerEbene;
		public int anzahlEbene;

		public AnzahlEintraege(int anzahlGesamt, int nummerEbene, int anzahlEbene) {
			super();
			this.anzahlGesamt = anzahlGesamt;
			this.nummerEbene = nummerEbene;
			this.anzahlEbene = anzahlEbene;
		}
	}

	/**
	 * @param ebene
	 *            Nummer der angefragten Ebene
	 * @return Die Anzahl der Eintr�ge gesamt und die der angefragten Ebene
	 */
	public AnzahlEintraege gibAnzahlEintraege(int ebene) {
		int nG = 0; // alle Eintr�ge
		int nE = 0; // Eintr�ge der Ebene
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istEintrag()) {
				nG++;
				if (ebene == feld.gibEintragEbene()) {
					nE++;
				}
			}
		}
		AnzahlEintraege ergebnis = new AnzahlEintraege(nG, ebene, nE);
		return ergebnis;
	}

	/**
	 * @return Die Anzahl der Eintr�ge
	 */
	public int gibAnzahlEintraege() {
		AnzahlEintraege anzahlEintraege = gibAnzahlEintraege(1);
		return anzahlEintraege.anzahlGesamt;
	}

	/**
	 * @param ebene
	 * @return Die Anzahl der Eintr�ge der Ebene
	 */
	public int gibAnzahlEbenenEintraege(int ebene) {
		AnzahlEintraege anzahlEintraege = gibAnzahlEintraege(ebene);
		return anzahlEintraege.anzahlEbene;
	}

	/**
	 * @param ebene
	 * @return Die Eintr�ge der Ebene. Die Liste kann leer sein.
	 */
	public FeldListe gibEbenenEintraege(int ebene) {
		FeldListe feldList = new FeldListe();

		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istEintrag()) {
				if (ebene == feld.gibEintragEbene()) {
					feldList.add(feld);
				}
			}
		}
		return feldList;
	}

	/**
	 * @param moegliche
	 *            Die neuen m�glichen Zahlen
	 * @return Anzahl der gel�schten M�glichen
	 * @throws Exc
	 */
	public int setzeMoegliche(ArrayList<Integer> moegliche) throws Exc {
		int nGeloescht = 0;
		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			int zuvor = feld.gibMoeglicheAnzahl();
			feld.setzeMoegliche(moegliche);
			nGeloescht += zuvor - moegliche.size();
		}
		return nGeloescht;
	}

	/**
	 * @return Die in meinen Feldern (noch) m�glichen Zahlen
	 */
	public ZahlenFeldNummern gibMoeglicheZahlen() {
		ZahlenFeldNummern vorhandeneZahlen = new ZahlenFeldNummern();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			FeldNummer feldNummer = this.get(iFeld).gibFeldNummer();
			ArrayList<Integer> moegliche = this.get(iFeld).gibMoegliche();
			for (int i = 0; i < moegliche.size(); i++) {
				vorhandeneZahlen.add(moegliche.get(i), feldNummer);
			}
		}
		return vorhandeneZahlen;
	}

	/**
	 * @return Gibt die Felder mit genau 2 m�glichen Zahlen zur�ck.
	 */
	public FeldListe gibFreieFelderMit2Moeglichen() {
		FeldListe zweier = new FeldListe();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			if (feld.istFrei()) {
				ArrayList<Integer> moegliche = feld.gibMoegliche();
				if (2 == moegliche.size()) {
					zweier.add(feld);
				}
			}
		}
		return zweier;
	}

	/**
	 * @param zahl
	 * @return Die Felder, die diese Zahl als m�gliche enthalten. Achtung: Die
	 *         R�ckgabeliste kann leer sein.
	 */
	public FeldListe gibFelderDerMoeglichenZahl(int zahl) {
		FeldListe feldListe = gibFelderDerMoeglichenZahl(zahl, 1);
		return feldListe;
	}

	/**
	 * @param zahl
	 * @param minAnzahlMoeglicheImFeld
	 *            soviel m�gliche Zahlen m�ssen sich mindestens in einem
	 *            genannten Feld befinden.
	 * @return Die Felder, die diese Zahl als m�gliche enthalten. Achtung: Die
	 *         R�ckgabeliste kann leer sein.
	 */
	private FeldListe gibFelderDerMoeglichenZahl(int zahl, int minAnzahlMoeglicheImFeld) {
		FeldListe zahlenFelder = new FeldListe();

		// F�r diese Zahl die m�glichen Felder auflisten
		for (int i = 0; i < this.size(); i++) {
			Feld feld = this.get(i);
			if (feld.istMoeglich(zahl) & (feld.gibMoeglicheAnzahl() >= minAnzahlMoeglicheImFeld)) {
				zahlenFelder.add(feld);
			}
		}
		return zahlenFelder;
	}

	/**
	 * @param minAnzahlMoeglicheImFeld
	 *            soviel m�gliche Zahlen m�ssen sich mindestens in einem
	 *            genannten Feld befinden.
	 * @return Die m�glichen Felder je Zahl (1 bis 9).
	 */
	public ZahlenFeldNummern gibFelderJeMoeglicheZahl(int minAnzahlMoeglicheImFeld) {
		ZahlenFeldNummern zahlenFeldNummern = new ZahlenFeldNummern();

		// F�r jede Zahl die m�glichen Felder auflisten
		for (int zahl = 1; zahl < 10; zahl++) {
			FeldListe zahlenFelder = this.gibFelderDerMoeglichenZahl(zahl, minAnzahlMoeglicheImFeld);
			FeldNummerListe feldNummerListe = new FeldNummerListe(zahlenFelder);
			for (FeldNummer feldNummer : feldNummerListe) {
				zahlenFeldNummern.add(zahl, feldNummer);
			}
		}

		return zahlenFeldNummern;
	}

	/**
	 * @return Die gr��te Anzahl M�gliche eines der Felder
	 */
	public int gibMoeglicheAnzahlMax() {
		int n = 99;
		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			if (feld.istFrei()) {
				int n1 = feld.gibMoeglicheAnzahl();
				if (n1 < n) {
					n = n1;
				}
			}
		}
		return n;
	}

	/**
	 * @param nMaxMoegliche
	 * @return Alle Felder, die nMoegliche besitzen
	 */
	public FeldListe gibMoeglicheN(int nMoegliche) {
		FeldListe feldListe = new FeldListe();
		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			if (feld.istFrei()) {
				int n1 = feld.gibMoeglicheAnzahl();
				if (n1 == nMoegliche) {
					feldListe.add(feld);
				}
			}
		}
		return feldListe;
	}

	/**
	 * @return Alle in meinen Feldern vorhandenen Zahlen (Vorgaben, Eintr�ge,
	 *         M�gliche)
	 */
	public ZahlenFeldNummern gibVorhandeneZahlen() {
		ZahlenFeldNummern vorhandeneZahlen = gibMoeglicheZahlen();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			FeldNummer feldNummer = feld.gibFeldNummer();
			if (feld.istVorgabe()) {
				vorhandeneZahlen.add(feld.gibVorgabe(), feldNummer);
			} else if (feld.istEintrag()) {
				vorhandeneZahlen.add(feld.gibEintrag(), feldNummer);
			}
		}
		return vorhandeneZahlen;
	}

	/**
	 * @return Alle in meinen Feldern vorhandenen Vorgaben und Eintr�ge
	 */
	public ZahlenFeldNummern gibVorgabenUndEintraege() {
		ZahlenFeldNummern vorhandeneZahlen = new ZahlenFeldNummern();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			FeldNummer feldNummer = feld.gibFeldNummer();
			if (feld.istVorgabe()) {
				vorhandeneZahlen.add(feld.gibVorgabe(), feldNummer);
			} else if (feld.istEintrag()) {
				vorhandeneZahlen.add(feld.gibEintrag(), feldNummer);
			}
		}
		return vorhandeneZahlen;
	}

	/**
	 * @return Gibt die freien Felder zur�ck.
	 */
	public FeldListe gibFreieFelder() {
		FeldListe freie = new FeldListe();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			if (feld.istFrei()) {
				freie.add(feld);
			}
		}
		return freie;
	}

	public FeldNummerListe gibFelderVersuchStart() {
		FeldNummerListe versuchStarts = new FeldNummerListe();
		for (Feld feld : this) {
			if (feld.istEintragVersuchStart()) {
				versuchStarts.add(feld.gibFeldNummer());
			}
		}
		return versuchStarts;
	}

	/**
	 * @return Gibt (freie) Felder zur�ck, die ein Feld eines Feldpaares sind.
	 *         Feldpaare: In einer Gruppe gibt es genau zwei freie Felder mit
	 *         derselben Zahl.
	 */
	public FeldListe gibFreieFelderPaare() {
		FeldListe paare = new FeldListe();

		for (int iFeld = 0; iFeld < this.size(); iFeld++) {
			Feld feld = this.get(iFeld);
			if (feld.istFrei() && feld.istFeldPaar()) {
				paare.add(feld);
			}
		}
		return paare;
	}

}
