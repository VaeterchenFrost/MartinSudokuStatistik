package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

public class Animator_SpiegelnMittelPunkt implements Animator {
	@Override
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax) {
		int neueSpalte = nummerMax + 1 - feldNummer.gibSpalte();
		int neueZeile = nummerMax + 1 - feldNummer.gibZeile();
		FeldNummer neueFeldNummer = new FeldNummer(neueSpalte, neueZeile);
		return neueFeldNummer;
	}

	@Override
	public String gibName() {
		return "Spiegeln am Mittelpunkt";
	}
}
