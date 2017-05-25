package sudoku.knacker.bericht;

public class KB_KlareSetzeKlare1 extends KB_Eintrag1Zeile {
	private int nKlare;

	public KB_KlareSetzeKlare1(int nKlare) {
		this.nKlare = nKlare;
	}

	@Override
	public String toString() {
		return "TreibeKlare Klare1: " + nKlare + " Feld(er)";
	}

	/**
	 * @return Anzahl der Felder, die bei diesem Klare-Setzen gesetzt wurden
	 */
	public int gibAnzahlKlare() {
		return nKlare;
	}
}
