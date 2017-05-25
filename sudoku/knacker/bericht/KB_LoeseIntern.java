package sudoku.knacker.bericht;

public class KB_LoeseIntern extends KB_Eintrag1Zeile {
	private int durchlauf;

	public KB_LoeseIntern(int durchlauf) {
		this.durchlauf = durchlauf;
	}

	@Override
	public String toString() {
		return "LoeseIntern [durchlauf=" + durchlauf + "]";
	}

}
