package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl2 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl2() {
		super(2, new AusgangInfo(Ausgang.LINKS, Ausgang.RECHTS));
	}
}
