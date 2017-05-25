package sudoku.knacker.bericht;

import sudoku.knacker.Ergebnis;

public class KB_LoeseInternEnde extends KB_Eintrag1Zeile {
	private Ergebnis ergebnis;

	private int ebeneNummer;

	public KB_LoeseInternEnde(Ergebnis ergebnis, int ebeneNummer) {
		this.ergebnis = ergebnis;
		this.ebeneNummer = ebeneNummer;
	}

	public int gibEbeneNummer() {
		return ebeneNummer;
	}

	public Ergebnis gibErgebnis() {
		return ergebnis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoeseInternEnde [ergebnis=" + ergebnis + ", ebeneNummer=" + ebeneNummer + "]";
	}

}
