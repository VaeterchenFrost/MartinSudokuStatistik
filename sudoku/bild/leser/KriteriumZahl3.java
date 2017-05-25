package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl3 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl3() {
		super(3, new AusgangInfo(Ausgang.LINKS, Ausgang.LINKS));
	}

}
