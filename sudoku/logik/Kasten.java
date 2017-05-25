package sudoku.logik;

import java.util.ArrayList;

import sudoku.ArrayListInt;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;

@SuppressWarnings("serial")
public class Kasten extends Gruppe {
	/**
	 * @param kastenIndex
	 *            Spalte/Zeile 0 bis 2
	 * @return FeldNummer des linken oberen Feldes des Kastens
	 */
	private static FeldNummer gibFeld1Nummer(KastenIndex kastenIndex) {
		int feld1Spalte = 1 + 3 * kastenIndex.iSpalte;
		int feld1Zeile = 1 + 3 * kastenIndex.iZeile;
		FeldNummer feld1Index = new FeldNummer(feld1Spalte, feld1Zeile);
		return feld1Index;
	}

	/**
	 * @param kastenIndex
	 *            Index des Kastens Spalte (0..2), Zeile (0..2)
	 * @return Die Liste der Nummern der Felder dieser Gruppe
	 * @throws Exc
	 *             falscher Parameter
	 */
	public static FeldNummerListe gibKastenFeldNummern(KastenIndex kastenIndex) throws Exc {
		if (!kastenIndex.istOK()) {
			String text = String.format("unbekannter Kasten-Index %s (min=%d, max=%d)", kastenIndex, 0, 2);
			Exc.ausnahme(text);
		}
		FeldNummer feldNr1 = gibFeld1Nummer(kastenIndex);
		FeldNummerListe feldNummern = new FeldNummerListe();
		for (int iZ = 0; iZ < 3; iZ++) {
			for (int iS = 0; iS < 3; iS++) {
				FeldNummer feldNummer = new FeldNummer(feldNr1.spalte + iS, feldNr1.zeile + iZ);
				feldNummern.add(feldNummer);
			}
		}
		return feldNummern;
	}

	/**
	 * @param kasten
	 * @param anderer
	 * @param istSpalte
	 * @return true wenn anderer ein Nachbar von kasten ist
	 */
	private static boolean istNachbar(KastenIndex kasten, KastenIndex anderer, boolean istSpalte) {
		if (istSpalte) {
			return (kasten.iSpalte == anderer.iSpalte) && (kasten.iZeile != anderer.iZeile);
		} else {
			return (kasten.iZeile == anderer.iZeile) && (kasten.iSpalte != anderer.iSpalte);
		}
	}

	/**
	 * @param feldNummer
	 *            Nummer eines beliebigen Feldes (des Kastens)
	 * @return Index des Kastens im Sudoku: Spalte/Zeile 0 bis 2
	 */
	public static KastenIndex gibKastenIndex(FeldNummer feldNummer) {
		int iZeile = (feldNummer.gibZeile() - 1) / 3;
		int iSpalte = (feldNummer.gibSpalte() - 1) / 3;
		KastenIndex kastenIndex = new KastenIndex(iSpalte, iZeile);
		return kastenIndex;
	}

	/**
	 * @param kastenIndex
	 *            Index des Kastens im Sudoku: Spalte/Zeile 0 bis 2
	 * @return Name des Kastens im Sudoku
	 */
	public static String gibNameVomKastenIndex(KastenIndex kastenIndex) {

		if ((kastenIndex.iZeile == 1) & (kastenIndex.iSpalte == 1)) {
			return "Kasten in der Mitte";
		}

		String iZeileName = "";
		switch (kastenIndex.iZeile) {
		case 0:
			iZeileName = "oben";
			break;
		case 1:
			iZeileName = "mitte";
			break;
		case 2:
			iZeileName = "unten";
			break;
		}

		String iSpalteName = "";
		switch (kastenIndex.iSpalte) {
		case 0:
			iSpalteName = "links";
			break;
		case 1:
			iSpalteName = "mitte";
			break;
		case 2:
			iSpalteName = "rechts";
			break;
		}

		String s = String.format("Kasten [%s,%s]", iZeileName, iSpalteName);
		return s;
	}

	static public Kasten gibKasten(KastenIndex kastenIndex, ArrayList<Kasten> kaesten) {
		for (int i = 0; i < kaesten.size(); i++) {
			Kasten kasten = kaesten.get(i);
			if (kastenIndex.equals(kasten.kastenIndex)) {
				return kasten;
			}
		}
		return null;
	}

	// ----------------------------------------------------------------------------
	// static

	/**
	 * @author Hendrick Ist die Liste der (3) Spalten bzw. Zeilen des Kastens
	 */
	public class LinienListe extends ArrayList<FeldListe> {
		public LinienListe() {
			for (int iLinie = 0; iLinie < 3; iLinie++) {
				this.add(new FeldListe());
			}
		}
	}
	// ----------------------------------------------------------------------------
	// private class

	// Spalte / Zeile 0, 1, 2
	public final KastenIndex kastenIndex;
	// meine Spalten
	private final LinienListe spalten;
	// meine Zeilen
	private final LinienListe zeilen;

	// Je Nachbar [0, 1] dessen Spalten
	private ArrayList<LinienListe> nachbarnLinienArraySpalten;
	// Je Nachbar [0, 1] dessen Zeilen
	private ArrayList<LinienListe> nachbarnLinienArrayZeilen;

	/**
	 * @param kastenIndex
	 *            Spalte/Zeile 0 bis 2
	 * @param alleFelder
	 */
	public Kasten(KastenIndex kastenIndex, ArrayList<Feld> alleFelder) {
		super(Gruppe.Typ.KASTEN, Kasten.gibFeld1Nummer(kastenIndex), true, alleFelder);
		this.kastenIndex = kastenIndex;

		// Index des linken oberen Feldes
		FeldNummer feld1Nummer = Kasten.gibFeld1Nummer(kastenIndex);
		nachbarnLinienArraySpalten = null;
		nachbarnLinienArrayZeilen = null;
		// nachbarnLinienSpalten = null;
		// nachbarnLinienZeilen = null;

		// Spalten erstellen
		spalten = new LinienListe();
		for (int iSpalte = 0; iSpalte < spalten.size(); iSpalte++) {
			FeldListe spaltenFelder = new FeldListe();
			int spaltenIndex = feld1Nummer.spalte + iSpalte;
			for (int iFeld = 0; iFeld < this.size(); iFeld++) {
				Feld feld = this.get(iFeld);
				if (spaltenIndex == feld.gibSpalte()) {
					spaltenFelder.add(feld);
				}
			}
			spalten.set(iSpalte, spaltenFelder);
		}

		// Zeilen erstellen
		zeilen = new LinienListe();
		for (int iZeile = 0; iZeile < zeilen.size(); iZeile++) {
			FeldListe zeilenFelder = new FeldListe();
			int zeilenIndex = feld1Nummer.zeile + iZeile;
			for (int iFeld = 0; iFeld < this.size(); iFeld++) {
				Feld feld = this.get(iFeld);
				if (zeilenIndex == feld.gibZeile()) {
					zeilenFelder.add(feld);
				}
			}
			zeilen.set(iZeile, zeilenFelder);
		}
	}

	/**
	 * @param alleKaesten
	 * @param istSpalte
	 * @return Die beiden Nachbark�sten
	 */
	private ArrayList<Kasten> gibNachbarn(ArrayList<Kasten> alleKaesten, boolean istSpalte) {
		ArrayList<Kasten> nachbarn = new ArrayList<Kasten>();
		for (int i = 0; i < alleKaesten.size(); i++) {
			Kasten kasten = alleKaesten.get(i);
			if ((kasten != this) && (istNachbar(this.kastenIndex, kasten.kastenIndex, istSpalte))) {
				nachbarn.add(kasten);
			}
		}
		return nachbarn;
	}

	/**
	 * @param istSpalte
	 * @return Die beiden Nachbark�sten als KastenIndex (Zeile/Spalte je 0..2)
	 */
	public static KastenIndex[] gibNachbarn(KastenIndex kastenIndex, boolean istSpalte) {
		KastenIndex[] nachbarn = new KastenIndex[2];

		int iNachbar = 0;
		for (int i = 0; i < 3; i++) {
			KastenIndex nachBarIndex = null;
			if (istSpalte) {
				nachBarIndex = new KastenIndex(kastenIndex.iSpalte, i);
			} else {
				nachBarIndex = new KastenIndex(i, kastenIndex.iZeile);
			}
			if (!kastenIndex.equals(nachBarIndex)) {
				nachbarn[iNachbar] = nachBarIndex;
				iNachbar++;
			}
		}
		return nachbarn;
	}

	/**
	 * @param istSpalte
	 * @return Die Spalten bzw. Zeilen der beiden Nachbarn
	 */
	private ArrayList<LinienListe> gibNachbarLinienArray(ArrayList<Kasten> nachbarnArray, boolean istSpalte) {
		ArrayList<LinienListe> nachbarLinien = new ArrayList<LinienListe>();

		for (int iNachbar = 0; iNachbar < nachbarnArray.size(); iNachbar++) {
			Kasten nachbarKasten = nachbarnArray.get(iNachbar);

			if (istSpalte) {
				nachbarLinien.add(nachbarKasten.spalten);
			} else {
				nachbarLinien.add(nachbarKasten.zeilen);
			}
		}
		return nachbarLinien;
	}

	/**
	 * @param alleKaesten
	 *            Setzt die Linien der Nachbarn
	 */
	public void setzeNachbarn(ArrayList<Kasten> alleKaesten) {
		{
			ArrayList<Kasten> nachbarnArraySpalten = gibNachbarn(alleKaesten, true);
			this.nachbarnLinienArraySpalten = gibNachbarLinienArray(nachbarnArraySpalten, true);
		}

		ArrayList<Kasten> nachbarnArrayZeilen = gibNachbarn(alleKaesten, false);
		this.nachbarnLinienArrayZeilen = gibNachbarLinienArray(nachbarnArrayZeilen, false);
	}

	public LinienListe gibMeineLinien(boolean istSpalte) {
		LinienListe meineLinien = null;
		if (istSpalte) {
			meineLinien = this.spalten;
		} else {
			meineLinien = this.zeilen;
		}
		return meineLinien;
	}

	/**
	 * @param zahl
	 * @param linien
	 * @return Die Indizees der linien, die Zahl als M�gliche enthalten
	 */
	static public ArrayListInt gibLinienDerZahl(int zahl, LinienListe linien) {
		ArrayListInt iLinienDerZahl = new ArrayListInt();
		for (int iLinie = 0; iLinie < linien.size(); iLinie++) {
			FeldListe linie = linien.get(iLinie);
			FeldListe felderMoegliche = linie.gibFelderDerMoeglichenZahl(zahl);
			if (!felderMoegliche.isEmpty()) {
				iLinienDerZahl.add(iLinie);
			}
		}
		return iLinienDerZahl;
	}

	public ArrayList<LinienListe> gibNachbarLinienArray(boolean istSpalte) {
		if (istSpalte) {
			return nachbarnLinienArraySpalten;
		} else {
			return nachbarnLinienArrayZeilen;
		}
	}

	// public boolean istKasten(KastenIndex kastenIndex){
	// return this.kastenIndex.equals(kastenIndex);
	// }

}
