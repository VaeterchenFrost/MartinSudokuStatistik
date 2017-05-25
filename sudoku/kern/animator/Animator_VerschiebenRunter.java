package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

public class Animator_VerschiebenRunter extends Animator_Verschiebe1Kasten implements Animator {
	@Override
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax) {
		int neueSpalte = feldNummer.gibSpalte();
		int neueZeile = 3 + feldNummer.gibZeile();

		FeldNummer neueFeldNummer = garantiereBereich(new FeldNummer(neueSpalte, neueZeile), nummerMax);
		return neueFeldNummer;
	}

	@Override
	public String gibName() {
		return "Verschieben nach unten";
	}
}
