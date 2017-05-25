package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

public class Animator_SpiegelnObenLinks implements Animator {
	@Override
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax) {
		int neueSpalte = nummerMax + 1 - feldNummer.gibZeile();
		int neueZeile = nummerMax + 1 - feldNummer.gibSpalte();
		FeldNummer neueFeldNummer = new FeldNummer(neueSpalte, neueZeile);
		return neueFeldNummer;
	}

	@Override
	public String gibName() {
		return "Spiegeln an oberer linker Ecke";
	}
}
