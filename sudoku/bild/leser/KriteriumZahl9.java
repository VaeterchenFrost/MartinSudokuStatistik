package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl9 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl9() {
		super(9, new AusgangInfo(Ausgang.KEINER, Ausgang.LINKS));
	}
}
