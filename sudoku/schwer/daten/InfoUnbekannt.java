package sudoku.schwer.daten;

import sudoku.schwer.AnzeigeInfo;

public class InfoUnbekannt implements AnzeigeInfo {

	public InfoUnbekannt() {
	}

	@Override
	public String gibAnzeigeText() {
		return new String("   ");
	}

	@Override
	public String gibToolTip() {
		return null;
	}

}
