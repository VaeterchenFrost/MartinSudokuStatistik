package sudoku.kern.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.tools.TextDatei;

/**
 * @author heroe Beinhaltet die FeldInfos zu bekannten Feldern des Sudokus. Wenn
 *         f�r einen FeldNummer keine FeldInfo existiert handelt es sich sicher
 *         um ein leeres Feld. Das Sudoku kann einen Titel besitzen.
 */
@SuppressWarnings("serial")
public class InfoSudoku extends HashMap<FeldNummer, FeldInfo> {
	/**
	 * @param dateiName
	 * @return dateiName sicher mit der Standard-Datei-Erweiterung f�r ein
	 *         gespeichertes Sudoku
	 */
	static public String dateiEndungSichern(String dateiName) {
		String s = dateiName;
		String fnLower = dateiName.toLowerCase();
		if (!fnLower.endsWith(dateiErweiterung)) {
			s += dateiErweiterung;
		}
		return s;
	}

	// Datei-Erweiterung einer InfoSudoku-Datei
	public static final String dateiErweiterung = ".sdk";

	/**
	 * @param dateiName
	 * @return InfoSudoku aus der Datei geladen
	 * @throws IOException
	 * @throws Exc
	 */
	public static InfoSudoku lade(String dateiName) throws IOException, Exc {
		String sudokuText = new String();
		sudokuText = TextDatei.lese1String(dateiName);
		InfoSudoku vorgaben = TextKonverter.gibVorgaben(sudokuText);
		return vorgaben;
	}

	public static ArrayList<InfoSudoku> sortiereNachTitel2Zeit(ArrayList<InfoSudoku> liste) {
		ArrayList<InfoSudoku> listeSortiert = new ArrayList<>();
		listeSortiert.addAll(liste);
		Collections.sort(listeSortiert, new ComparatorTitel2Zeit());
		return listeSortiert;
	}

	// ==========================================================================
	/**
	 * @author heroe Sortiert InfoSudokus nach ihrem Titel2, der die
	 *         L�sungs-Zeit ist
	 */
	static private class ComparatorTitel2Zeit implements Comparator<InfoSudoku> {

		public ComparatorTitel2Zeit() {
		}

		private static int gibCompareResult(int me, int other) {
			if (me > other) {
				return 1;
			}
			if (me < other) {
				return -1;
			}
			return 0;
		}

		@Override
		public int compare(InfoSudoku o1, InfoSudoku o2) {
			String titel1 = o1.gibTitel2();
			String titel2 = o2.gibTitel2();
			int zeit1 = 0;
			int zeit2 = 0;
			try {
				if (titel1 != null) {
					zeit1 = Integer.parseUnsignedInt(titel1);
				}
				if (titel2 != null) {
					zeit2 = Integer.parseUnsignedInt(titel2);
				}
			} catch (NumberFormatException e) {
			}
			int cr = gibCompareResult(zeit1, zeit2);
			return cr;
		}
	}

	// ==========================================================================
	// Linker Titelteil bei titel2 != null, ansonsten zentrierter Titel
	private String titel1;
	// Rechter Titelteil
	private String titel2;

	/**
	 * Erstellt das leere Info-Sudoku
	 */
	public InfoSudoku() {
		super();
		titel1 = null;
		titel2 = null;
	}

	/**
	 * Erstellt das Info-Sudoku mit allen Infos zum aktuellen Stand der Felder
	 * 
	 * @param felder
	 */
	public InfoSudoku(FeldListe felder) {
		super();
		for (int i = 0; i < felder.size(); i++) {
			Feld feld = felder.get(i);
			this.put(feld.gibFeldNummer(), new FeldInfo(feld));
		}
		titel1 = null;
		titel2 = null;
	}

	/**
	 * Info-Sudoku mit den Vorgaben der infos: Felder ohne Vorgabe kommen mit
	 * Vorgabe=0 zur�ck.
	 * 
	 * @param infos
	 */
	public InfoSudoku(FeldInfoListe infos) {
		super();
		for (FeldInfo feldInfo : infos) {
			FeldNummer feldNummer = feldInfo.gibFeldNummer();
			this.put(feldNummer, feldInfo);
		}
		titel1 = null;
		titel2 = null;
	}

	public void setzeTitel(String titel) {
		this.titel1 = titel;
		this.titel2 = null;
	}

	public void setzeTitel(String titel1, String titel2) {
		this.titel1 = titel1;
		this.titel2 = titel2;
	}

	public String gibTitel1() {
		return titel1;
	}

	public String gibTitel2() {
		return titel2;
	}

	public int gibAnzahlVorgaben() {
		int nVorgaben = 0;
		for (Map.Entry<FeldNummer, FeldInfo> entry : this.entrySet()) {
			FeldInfo feldInfo = entry.getValue();
			if (feldInfo.istVorgabe()) {
				nVorgaben++;
			}
		}
		return nVorgaben;
	}

	public boolean istLeer() {
		return gibAnzahlVorgaben() == 0;
	}

	public boolean existiertMarkierungMoeglicherZahl() {
		for (Map.Entry<FeldNummer, FeldInfo> entry : this.entrySet()) {
			FeldInfo feldInfo = entry.getValue();
			if (feldInfo.existiertMarkierungMoeglicherZahl()) {
				return true;
			}
		}
		return false;
	}

	public String gibSpeicherText() {
		String sudokuText = TextKonverter.gibText(this);
		return sudokuText;
	}

	public void speichern(String dateiName) throws IOException {
		// Datei-Erweiterung erzwingen
		String sichererDateiName = dateiEndungSichern(dateiName);

		String sudokuText = TextKonverter.gibText(this);
		TextDatei.erstelle(sichererDateiName, sudokuText);
	}

	/**
	 * @param aktiveFelder
	 *            aktiven Feldern wird eine ev. vorhandene Markierung gel�scht,
	 *            den anderen eine passive Markierung gesetzt.
	 */
	public void markiereAllePassivAusser(FeldNummerListe aktiveFelder) {
		for (Map.Entry<FeldNummer, FeldInfo> entry : this.entrySet()) {
			FeldNummer feldNummer = entry.getKey();
			FeldInfo feldInfo = entry.getValue();
			if (!aktiveFelder.contains(feldNummer)) {
				feldInfo.setzeMarkierung(false);
			} else {
				feldInfo.loescheMarkierung();
			}
		}
	}

	public void markiereAktiv(FeldNummerListe aktiveFelder) {
		for (FeldNummer feldNummer : aktiveFelder) {
			FeldInfo feldInfo = this.get(feldNummer);
			feldInfo.setzeMarkierung(true);
		}
	}

	public void markiereMoeglicheZahlen(ZahlenListe moeglicheZahlen) {
		for (FeldNummerMitZahl feldNummerMitZahl : moeglicheZahlen) {
			FeldInfo feldInfo = this.get(feldNummerMitZahl.gibFeldNummer());
			feldInfo.markiereMoeglicheZahl(feldNummerMitZahl.gibZahl());
		}
	}

	/**
	 * @return Gibt die freien Felder des Sudokus an
	 */
	public FeldNummerListe gibFreieFelder() {
		FeldNummerListe freieFelder = new FeldNummerListe();

		for (int zeile = 1; zeile < 10; zeile++) {
			for (int spalte = 1; spalte < 10; spalte++) {
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				FeldInfo feldInfo = this.get(feldNummer);
				if (feldInfo != null) {
					if (feldInfo.istFrei()) {
						freieFelder.add(feldNummer);
					}
				} else {
					freieFelder.add(feldNummer);
				}
			}
		}

		return freieFelder;
	}

	@Override
	public String toString() {
		// So jedenfalls ben�tigt es die Benutzung von InfoSudokuIcon
		return "";
	}

}
