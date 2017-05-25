package sudoku.logik;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author Hendrick Die Gruppe von 9 Feldern, in denen alle Zahlen von 1 bis 9
 *         einmal vorkommen m�ssen
 */
@SuppressWarnings("serial")
public class Gruppe extends FeldListe {
	// =========================================================
	public enum Typ {
		KASTEN, ZEILE, SPALTE, MIX
	}

	/**
	 * @param feldNummer
	 * @param linienTyp0
	 *            bei "ZEILE" wird die Zeile als 0 gesetzt, bei "SPALTEN" die
	 *            Spalte.
	 * @return Eine neue Instanz f�r feldNummer mit Zeile bzw. Spalte gleich 0.
	 *         Der Sinn dessen besteht darin, dass jetzt FeldNummer.equals an
	 *         der Stelle Gleichheit feststellt!
	 */
	static FeldNummer gibFeldNummerMit0(FeldNummer feldNummer, Gruppe.Typ linienTyp0) {
		int spalte = 0;
		int zeile = 0;
		switch (linienTyp0) {
		case ZEILE:
			spalte = feldNummer.spalte;
			zeile = 0;
			break;
		case SPALTE:
			spalte = 0;
			zeile = feldNummer.zeile;
			break;
		default:
			spalte = feldNummer.spalte;
			zeile = feldNummer.zeile;
			break;
		}
		FeldNummer neueFeldnummer = new FeldNummer(spalte, zeile);
		return neueFeldnummer;
	}

	/**
	 * @param gruppen
	 * @param freieFelderMin
	 *            Anzahl Felder, die mindestens je Gruppe frei sein sollen
	 * @return
	 */
	static public ArrayList<Gruppe> gibFreieGruppen(ArrayList<Gruppe> gruppen, int freieFelderMin) {
		ArrayList<Gruppe> freieGruppen = new ArrayList<>();

		for (int i = 0; i < gruppen.size(); i++) {
			Gruppe gruppe = gruppen.get(i);
			int anzahlFreieFelder = gruppe.gibAnzahlFreieFelder();
			if (anzahlFreieFelder >= freieFelderMin) {
				freieGruppen.add(gruppe);
			}
		}
		return freieGruppen;
	}

	/**
	 * @param gruppen
	 * @param istSatzStart
	 * @return Sowas wie: In den Zeilen 1 + 2 + 3
	 */
	static public String gibInText(Gruppe[] gruppen, boolean istSatzStart) {
		char textStart = istSatzStart ? 'I' : 'i';
		String s = null;

		switch (gruppen[0].typ) {
		case ZEILE:
			s = String.format("%cn den Zeilen %d", textStart, gruppen[0].get(0).gibZeile());
			for (int i = 1; i < gruppen.length; i++) {
				String s1 = String.format(" + %d", gruppen[i].get(0).gibZeile());
				s += s1;
			}
			return s;
		case SPALTE:
			s = String.format("%cn den Spalten %d", textStart, gruppen[0].get(0).gibSpalte());
			for (int i = 1; i < gruppen.length; i++) {
				String s1 = String.format(" + %d", gruppen[i].get(0).gibSpalte());
				s += s1;
			}
			return s;
		case KASTEN:
			KastenIndex kastenIndex = Kasten.gibKastenIndex(gruppen[0].get(0).gibFeldNummer());
			s = String.format("%cn den K�sten %s", textStart, Kasten.gibNameVomKastenIndex(kastenIndex));
			for (int i = 1; i < gruppen.length; i++) {
				KastenIndex kastenIndex1 = Kasten.gibKastenIndex(gruppen[i].get(0).gibFeldNummer());
				String sKasten = Kasten.gibNameVomKastenIndex(kastenIndex1);
				String s1 = String.format(" + %s", sKasten);
				s += s1;
			}
			return s;
		default:
			return String.format("Fehler: Gruppe mit falschem Typ %d", gruppen[0].typ);
		}
	}

	static public char gibTypZeichen(Typ typ) {
		if (typ == null) {
			return ' ';
		}
		String s = typ.toString();
		char c = s.charAt(0);
		return c;
	}

	/**
	 * @param basisFeldNummer
	 * @param feldNummer
	 * @return true wenn das Feld[feldNummer] im Kasten des
	 *         Basisfeldes[basisFeldNummer] liegt
	 */
	private static boolean istKastenFeld(FeldNummer basisFeldNummer, FeldNummer feldNummer) {
		int zeileMin = (basisFeldNummer.zeile - 1) / 3;
		zeileMin *= 3;
		zeileMin++;
		int spalteMin = (basisFeldNummer.spalte - 1) / 3;
		spalteMin *= 3;
		spalteMin++;

		int zeileMax = (basisFeldNummer.zeile - 1) / 3;
		zeileMax++;
		zeileMax *= 3;
		int spalteMax = (basisFeldNummer.spalte - 1) / 3;
		spalteMax++;
		spalteMax *= 3;

		return ((feldNummer.zeile >= zeileMin) && (feldNummer.spalte >= spalteMin) && (feldNummer.zeile <= zeileMax)
				&& (feldNummer.spalte <= spalteMax));
	}

	/**
	 * @param basisFeldNummer
	 *            Ein Feld der Gruppe
	 * @param feld
	 *            Das angefragte Feld
	 * @param typ
	 *            Typ der Gruppe
	 * @return true wenn feld in der Gruppe des typs Mitglied ist
	 */
	public static boolean istMitglied(FeldNummer basisFeldNummer, FeldNummer feldNummer, Gruppe.Typ typ) {
		boolean mitglied = false;
		switch (typ) {
		case ZEILE:
			mitglied = (basisFeldNummer.zeile == feldNummer.zeile);
			break;
		case SPALTE:
			mitglied = (basisFeldNummer.spalte == feldNummer.spalte);
			break;
		case KASTEN:
			mitglied = (istKastenFeld(basisFeldNummer, feldNummer));
			break;
		case MIX:
			mitglied = ((basisFeldNummer.zeile == feldNummer.zeile) || (basisFeldNummer.spalte == feldNummer.spalte)
					|| istKastenFeld(basisFeldNummer, feldNummer));
			break;
		}
		return mitglied;
	};

	private Typ typ;

	/**
	 * Sammelt f�r aTyp und basisFeldNummer alle Felder der Gruppe zusammen
	 * 
	 * @param aTyp
	 * @param basisFeldNummer
	 * @param mitBasisFeld
	 * @param alleFelder
	 */
	public Gruppe(Typ aTyp, FeldNummer basisFeldNummer, boolean mitBasisFeld, ArrayList<Feld> alleFelder) {
		typ = aTyp;

		for (int i = 0; i < alleFelder.size(); i++) {
			Feld feld = alleFelder.get(i);
			boolean toAdd = false;
			if (feld.gibFeldNummer() == basisFeldNummer) {
				if (mitBasisFeld) {
					toAdd = true;
				}
			} else {
				// nicht dieser feldindex
				toAdd = istMitglied(basisFeldNummer, feld.gibFeldNummer(), typ);
			}
			if (toAdd) {
				this.add(feld);
			}
		}
	}

	/**
	 * @return Text "In ..." je Gruppen-Typ den Text
	 */
	public String gibInText(boolean istSatzStart) {
		char textStart = istSatzStart ? 'I' : 'i';
		switch (typ) {
		case ZEILE:
			return String.format("%cn Zeile %d", textStart, this.get(0).gibZeile());
		case SPALTE:
			return String.format("%cn Spalte %d", textStart, this.get(0).gibSpalte());
		case KASTEN:
			KastenIndex kastenIndex = Kasten.gibKastenIndex(this.get(0).gibFeldNummer());
			return String.format("%cm %s", textStart, Kasten.gibNameVomKastenIndex(kastenIndex));
		default:
			return String.format("Fehler: Gruppe mit falschem Typ %d", this.typ);
		}
	}

	/**
	 * @return the typ
	 */
	public Typ gibTyp() {
		return typ;
	}

	public Typ gibTypDerSenkrechten() {
		switch (typ) {
		case ZEILE:
			return Typ.SPALTE;
		case SPALTE:
			return Typ.ZEILE;
		default:
			throw new IllegalArgumentException("Gruppe besitzt weder Typ ZEILE noch SPALTE");
		}
	}

}
