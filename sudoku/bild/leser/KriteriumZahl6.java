package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl6 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl6() {
		super(6, new AusgangInfo(Ausgang.RECHTS, Ausgang.KEINER));
	}

}
