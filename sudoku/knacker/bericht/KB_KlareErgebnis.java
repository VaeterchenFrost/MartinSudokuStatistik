package sudoku.knacker.bericht;

import sudoku.knacker.Ergebnis;

public class KB_KlareErgebnis extends KB_Eintrag1Zeile {
	private Ergebnis ergebnis;

	public KB_KlareErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}

	@Override
	public String toString() {
		return "TreibeKlare Ergebnis: " + ergebnis;
	}

}
