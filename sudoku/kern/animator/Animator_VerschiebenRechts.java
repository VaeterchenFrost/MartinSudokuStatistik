package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

public class Animator_VerschiebenRechts extends Animator_Verschiebe1Kasten implements Animator {
	@Override
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax) {
		int neueSpalte = 3 + feldNummer.gibSpalte();
		int neueZeile = feldNummer.gibZeile();

		FeldNummer neueFeldNummer = garantiereBereich(new FeldNummer(neueSpalte, neueZeile), nummerMax);
		return neueFeldNummer;
	}

	@Override
	public String gibName() {
		return "Verschieben nach rechts";
	}
}
