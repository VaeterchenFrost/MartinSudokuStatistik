package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

public class Animator_SpiegelnOben implements Animator {
	@Override
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax) {
		int neueSpalte = feldNummer.gibSpalte();
		int neueZeile = nummerMax + 1 - feldNummer.gibZeile();
		FeldNummer neueFeldNummer = new FeldNummer(neueSpalte, neueZeile);
		return neueFeldNummer;
	}

	@Override
	public String gibName() {
		return "Spiegeln an oberer Kante";
	}
}
