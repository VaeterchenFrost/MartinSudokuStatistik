package sudoku.kern.info;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldNummer;

/**
 * Wandelt das Sudoku (dessen Vorgaben) in Text- (Datei-)Form. Setzt das Sudoku
 * aus diesem vereinbarten Text.
 * 
 * @author Hendrick
 */
public class TextKonverter {

	private static int anzahlFelder = 81; // 9x9
	private static String sCR = String.format("%n");
	private static String linieAussen = "$$$$$$$$$$$$$$$$$$$";
	private static String linieInnenDuenn = "$-----$-----$-----$";
	private static String linieInnenDick = "$=================$";
	private static char cSeparatorAussen = linieAussen.charAt(0);
	private static char cSeparatorInnen = '|';

	/**
	 * L�scht aus text alle Zeichen au�er den Feldinhalten.
	 * 
	 * @param text
	 * @return String der L�nge 81, der f�r jedes Feld dessen Zahl enth�lt.
	 *         Leere Feldinhalte (" ") werden als "0" gesetzt. Die
	 *         Felder-Reihenfolge ist von links oben zeilenweise nach rechts
	 *         unten.
	 */
	private static String gibGepackt(String text) {
		// Alle Zeilenvorsch�be l�schen
		text = text.replace(sCR, "");

		// Alle Zeichen vor der einleitenden LinieAussen l�schen
		int indexLinieAussen = text.indexOf(linieAussen);
		if (indexLinieAussen >= 0) {
			text = text.substring(indexLinieAussen + linieAussen.length());
		}

		// Alle Zeichen nach der abschlie�enden LinieAussen l�schen
		indexLinieAussen = text.indexOf(linieAussen);
		if (indexLinieAussen > 0) {
			text = text.substring(0, indexLinieAussen);
		}

		// Alle Leerzeichen durch '0' ersetzen
		text = text.replace(' ', '0');
		// Alle Linien ersetzen
		text = text.replace(linieInnenDuenn, "");
		text = text.replace(linieInnenDick, "");
		text = text.replace(linieAussen, "");
		// Alle Separatoren ersetzen
		String sSeparator = String.valueOf(cSeparatorInnen);
		text = text.replace(sSeparator, "");
		sSeparator = String.valueOf(cSeparatorAussen);
		text = text.replace(sSeparator, "");

		// Alle Leerzeichen l�schen
		sSeparator = String.valueOf(" ");
		text = text.replace(sSeparator, "");

		return text;
	}

	/**
	 * @param felder
	 * @return Text-Darstellung der Felder, der zum Speichern in einer Textdatei
	 *         geeignet ist. Er enth�lt auch Zeilenvorsch�be, sodass der Text
	 *         gleich als Sudoku lesbar ist.
	 */
	public static String gibText(InfoSudoku vorgaben) {
		String text = new String(linieAussen);

		text += sCR;

		// Zeilenweise Ablage
		for (int zeile = 1; zeile <= 9; zeile++) {
			for (int spalte = 1; spalte <= 9; spalte++) {
				FeldInfo feldInfo = vorgaben.get(new FeldNummer(spalte, zeile));
				int vorgabe = 0;
				if (feldInfo != null) {
					vorgabe = feldInfo.gibVorgabe();
				}

				if (1 == spalte) { // Rahmen
					text += cSeparatorAussen;
				}
				if (vorgabe > 0) {
					text += vorgabe;
				} else {
					text += ' ';
				}
				switch (spalte) {
				case 1:
				case 2:
				case 4:
				case 5:
				case 7:
				case 8:
					text += cSeparatorInnen;
					break;
				case 3:
				case 6:
				case 9:
					text += cSeparatorAussen;
					break;
				}
				if (9 == spalte) {// Neue Zeile
					text += sCR;
					switch (zeile) {// linie
					case 1:
					case 2:
					case 4:
					case 5:
					case 7:
					case 8:
						text += linieInnenDuenn;
						text += sCR;
						break;
					case 3:
					case 6:
						text += linieInnenDick;
						text += sCR;
						break;
					case 9:
						text += linieAussen;
						text += sCR;
						break;
					}
				}
			} // for spalte
		} // for zeile

		return text;
	}

	public static InfoSudoku gibVorgaben(String text) throws Exc {
		// Alle Kommentare entfernen
		text = TextKonverter.gibGepackt(text);

		// Kontrollen
		String fehler = istSudokuText(text);
		if (fehler != null) {
			throw Exc.ausnahme(fehler);
		}

		InfoSudoku vorgaben = new InfoSudoku();
		// Zeilenweise Ablage
		for (int zeile = 1; zeile <= 9; zeile++) {
			for (int spalte = 1; spalte <= 9; spalte++) {
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				int iText = (zeile - 1) * 9 + (spalte - 1);
				int vorgabe = Integer.valueOf(text.substring(iText, iText + 1));
				if (vorgabe > 0) {
					vorgaben.put(feldNummer, FeldInfo.gibVorgabeInstanz(feldNummer, vorgabe));
				}
			}
		}
		return vorgaben;
	}

	/**
	 * @param text
	 *            Vorgaben in vereinbarter Textform
	 * @return null wenn es sich um einen lesbaren Sudokutext handelt, sonst der
	 *         Fehler in Textform
	 */
	private static String istSudokuText(String sudokuText) {
		if (sudokuText == null) {
			return "Keine Daten";
		}
		if (sudokuText.isEmpty()) {
			return "Leere Daten";
		}

		String text = gibGepackt(sudokuText);
		if (text.length() != anzahlFelder) {
			String sFehler = String.format("Daten falscher L�nge=%d, (erwartet=%d)", text.length(), anzahlFelder);
			return sFehler;
		}

		for (int iChar = 0; iChar < text.length(); iChar++) {
			char c = text.charAt(iChar);
			if ((c < '0') || (c > '9')) {
				String sFehler = String.format("falsches Zeichen %c", c);
				return sFehler;
			}
		}
		return null;
	}
}
