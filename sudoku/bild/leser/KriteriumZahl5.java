package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl5 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl5() {
		super(5, new AusgangInfo(Ausgang.RECHTS, Ausgang.LINKS));
	}
}
