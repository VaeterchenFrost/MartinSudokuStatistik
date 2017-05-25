package sudoku.varianz;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.info.FeldInfo;
import sudoku.kern.info.InfoSudoku;

/**
 * Eine L�sung des Sudoku
 */
public class Loesung {
	static public ArrayList<String> gibKopftexte() {
		ArrayList<String> kopf = new ArrayList<>();
		String zeile1 = new String();
		String zeile2 = new String();
		for (int zeile = 1; zeile < 10; zeile++) {
			for (int spalte = 1; spalte < 10; spalte++) {
				if (spalte == 1) {
					zeile1 += "|";
					zeile2 += "|";
				}
				zeile1 += zeile;
				zeile2 += spalte;
			}
		}
		zeile1 += "|";
		zeile2 += "|";
		kopf.add(zeile1);
		kopf.add(zeile2);
		return kopf;
	}

	// ============================================================
	private VersuchStarts versuchStarts;
	private InfoSudoku infoSudoku;

	public Loesung(VersuchStarts versuchStarts, InfoSudoku sudoku) {
		this.versuchStarts = versuchStarts;
		infoSudoku = sudoku;
	}

	public InfoSudoku gibSudoku() {
		return this.infoSudoku;
	}

	public VersuchStarts gibVersuchStarts() {
		return this.versuchStarts;
	}

	public String gibText() {
		String s = String.format("%s %s", gibTextEintraege(), versuchStarts.gibText());
		return s;
	}

	private String gibTextEintraege() {
		ArrayList<FeldInfo> zahlen = new ArrayList<FeldInfo>(9 * 9);
		for (int i = 0; i < 9 * 9; i++) {
			zahlen.add(null);
		}

		// zahlen f�llen
		// Im InfoSudoku kann es "leere" Stellen geben?: FeldInfo == null!
		for (int i = 0; i < zahlen.size(); i++) {
			int zeile = 1 + i / 9;
			int spalte = 1 + i - (zeile - 1) * 9;
			FeldNummer feldNummer = new FeldNummer(spalte, zeile);
			FeldInfo feldInfo = infoSudoku.get(feldNummer);
			zahlen.set(i, feldInfo);
		}

		String txt = new String();
		for (int i = 0; i < zahlen.size(); i++) {
			FeldInfo feldInfo = zahlen.get(i);
			if ((i % 9) == 0) {
				txt += "|";
			}
			if (feldInfo == null) {
				txt += " ";
			} else {
				if (feldInfo.istVorgabe()) {
					txt += feldInfo.gibVorgabe();
				} else if (feldInfo.istEintrag()) {
					txt += feldInfo.gibEintrag();
				} else
					txt += " ";
			}
		} // for
		txt += "|";

		return txt;
	}
}
