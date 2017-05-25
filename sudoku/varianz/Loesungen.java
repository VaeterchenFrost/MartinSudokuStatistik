package sudoku.varianz;

import java.util.ArrayList;

import sudoku.kern.info.InfoSudoku;

/**
 * @author heroe Die Gesamtheit der L�sungen des Sudoku
 */
public class Loesungen {

	private boolean istSystemOut = false;
	private ArrayList<Loesung> loesungen;

	public Loesungen() {
		loesungen = new ArrayList<>();
	}

	public void add(Loesung loesung) {
		loesungen.add(loesung);
	}

	public int gibAnzahl() {
		return loesungen.size();
	}

	/**
	 * @return F�r jede L�sung in loesungen den Index des Basis-Sudokus f�r
	 *         einen Vergleich. Der Index im R�ckgabe-Array entspricht genau
	 *         denen der loesungen. Der BasisIndex f�r Loesung[0] ist null!!
	 */
	private Integer[] gibBasisSudokuIndizees() {
		Integer[] basisIndizees = new Integer[loesungen.size()];

		// MIt allen L�sungen
		for (int iLoesung = 0; iLoesung < this.loesungen.size(); iLoesung++) {
			Loesung loesung = loesungen.get(iLoesung);
			VersuchStarts versuchStarts = loesung.gibVersuchStarts();

			switch (iLoesung) {
			case 0:
				basisIndizees[iLoesung] = null;
				break;
			case 1:
				basisIndizees[iLoesung] = new Integer(0);
				break;
			default: // Es ist wirklich ein Basis-Sudoku zu suchen
				Integer basisIndex = null;

				// Mit immer weniger VersuchStarts ein Basis-Sudoku suchen
				for (int nStarts = versuchStarts.gibAnzahl() - 1; nStarts > 0; nStarts--) {
					// Mit allen L�sungen vor loesung
					for (int iBasis = 0; iBasis < iLoesung; iBasis++) {
						Loesung basisLoesung = loesungen.get(iBasis);
						VersuchStarts basisStarts = basisLoesung.gibVersuchStarts();
						if (versuchStarts.istGleicherEintrag(basisStarts, nStarts)) {
							basisIndex = new Integer(iBasis);
							break;
						}
					} // for (int iBasis
					if (basisIndex != null) {
						break;
					}
				} // for (nStarts

				if (basisIndex == null) {
					basisIndizees[iLoesung] = new Integer(0);
				} else {
					basisIndizees[iLoesung] = basisIndex;
				}
				break; // default
			} // switch
			if (istSystemOut) {
				Integer basisIndex = basisIndizees[basisIndizees.length - 1];
				String sBasis = new String("");
				if (basisIndex != null) {
					sBasis = String.format("hat Basis %2d", basisIndex);
				}
				System.out.println(String.format("L�sung %2d %s", iLoesung, sBasis));
			}
		} // for (int i = 0; i < this.loesungen.size()
		return basisIndizees;
	}

	/**
	 * @return F�r jede L�sung in loesungen das Basis-Sudoku Der Index im
	 *         R�ckgabe-Array entspricht genau dem der loesungen.
	 */
	public InfoSudoku[] gibBasisSudokus() {
		Integer[] indizees = gibBasisSudokuIndizees();

		InfoSudoku[] basisSudokus = new InfoSudoku[loesungen.size()];
		// MIt allen L�sungen
		for (int iLoesung = 0; iLoesung < this.loesungen.size(); iLoesung++) {
			Integer iBasis = indizees[iLoesung];

			if (iBasis == null) {
				basisSudokus[iLoesung] = null;
			} else {
				basisSudokus[iLoesung] = loesungen.get(iBasis).gibSudoku();
			}
		}
		return basisSudokus;
	}

	/**
	 * @return F�r jede L�sung in loesungen das Sudoku. Der Index im
	 *         R�ckgabe-Array entspricht genau dem der loesungen. Die Titel der
	 *         Sudokus verweisen auf ihr Basis-Sudoku
	 */
	public InfoSudoku[] gibInfoSudokusMitDifferenzTitel() {
		InfoSudoku[] sudokus = new InfoSudoku[loesungen.size()];
		Integer[] basisSudokuIndizees = gibBasisSudokuIndizees();

		// MIt allen L�sungen
		for (int iLoesung = 0; iLoesung < this.loesungen.size(); iLoesung++) {
			InfoSudoku sudoku = loesungen.get(iLoesung).gibSudoku();

			String titel = String.format("Variante %d", iLoesung);
			Integer basisIndex = basisSudokuIndizees[iLoesung];
			if (basisIndex != null) {
				titel = String.format("Variante %d -> Variante %d", iLoesung, basisIndex);
			}
			sudoku.setzeTitel(titel);
			sudokus[iLoesung] = sudoku;
		}
		return sudokus;
	}

	public void systemOut(boolean istAusgabe) {
		if (!istAusgabe) {
			return;
		}
		for (Loesung loesung : loesungen) {
			System.out.println(loesung.gibText());
		}
	}
}
