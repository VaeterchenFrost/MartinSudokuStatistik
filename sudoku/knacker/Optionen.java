package sudoku.knacker;

import sudoku.knacker.Knacker.VersuchsEbenen;

public class Optionen {
	/**
	 * Die maximal erlaubte VersuchsEbene
	 */
	private VersuchsEbenen maxErlaubteVersuchsEbene;

	/**
	 * Bei true reicht das Setzen eines einzigen Eintrags aus: F�r gibTip
	 * n�mlich.
	 */
	private boolean istGibTip;

	public Optionen() {
		super();
		this.maxErlaubteVersuchsEbene = VersuchsEbenen.EINE;
		this.istGibTip = false;
	}

	public VersuchsEbenen gibMaxErlaubteVersuchsEbene() {
		return maxErlaubteVersuchsEbene;
	}

	public boolean istGibTip() {
		return istGibTip;
	}

	public void setzeOptionenGibTip() {
		this.maxErlaubteVersuchsEbene = VersuchsEbenen.UNBEGRENZT;
		this.istGibTip = true;
	}

	public void setzeOptionenKnacke() {
		this.maxErlaubteVersuchsEbene = VersuchsEbenen.UNBEGRENZT;
		this.istGibTip = false;
	}

	public void setzeOptionenLoese(VersuchsEbenen versuchsEbenen) {
		this.maxErlaubteVersuchsEbene = versuchsEbenen;
		this.istGibTip = false;
	}

}
