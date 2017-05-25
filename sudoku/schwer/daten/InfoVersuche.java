package sudoku.schwer.daten;

import sudoku.schwer.AnzeigeInfo;

public class InfoVersuche implements AnzeigeInfo {
	// Anzahl Versuchs-Starts
	private int nStarts;
	private boolean istFelderMit2Moeglichen;

	public InfoVersuche(int nStarts, boolean istFelderMit2Moeglichen) {
		super();
		this.nStarts = nStarts;
		this.istFelderMit2Moeglichen = istFelderMit2Moeglichen;
	}

	@Override
	public String gibAnzeigeText() {
		String sArt = "1 Feld";

		if (!this.istFelderMit2Moeglichen) {
			sArt = "FeldPaar";
		}

		if (nStarts == 1) {
			return String.format("1 Versuchstart (%s)", sArt);
		} else {
			return String.format("%d Versuchstarts (%s)", nStarts, sArt);
		}
	}

	@Override
	public String gibToolTip() {
		return null;
	}
}
