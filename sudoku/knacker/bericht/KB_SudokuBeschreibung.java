package sudoku.knacker.bericht;

import sudoku.logik.SudokuLogik;

public class KB_SudokuBeschreibung extends KB_Eintrag1Zeile {
	String name;
	String sKlugheit;
	int nVorgaben;

	public KB_SudokuBeschreibung(String sKlugheit, SudokuLogik feldMatrix, String name) {
		this.name = name;
		if (name == null) {
			name = "";
		}
		this.sKlugheit = sKlugheit;
		this.nVorgaben = feldMatrix.gibAnzahlVorgaben();
	}

	@Override
	public String toString() {
		return "====================== SudokuBeschreibung [name=" + name + ", klugheit=" + sKlugheit + ", nVorgaben="
				+ nVorgaben + "]";
	}

}
