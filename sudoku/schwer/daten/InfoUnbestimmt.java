package sudoku.schwer.daten;

import sudoku.schwer.AnzeigeInfo;

public class InfoUnbestimmt implements AnzeigeInfo {
	int nVorgaben;

	public InfoUnbestimmt(int nVorgaben) {
		this.nVorgaben = nVorgaben;
	}

	@Override
	public String gibAnzeigeText() {
		return String.format("Unbestimmt (%d)", this.nVorgaben);
	}

	@Override
	public String gibToolTip() {
		String sNur = "";
		if (nVorgaben < 24) {
			sNur = "nur ";
		}
		String s1 = String.format("Das Sudoku ist sehr unbestimmt. Es besitzt %s%d Vorgaben.", sNur, this.nVorgaben);
		String s = String.format("<html>%s</html>", s1);
		return s;
	}
}
