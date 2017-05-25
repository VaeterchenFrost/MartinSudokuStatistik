package sudoku.bild.leser;

import sudoku.bild.leser.AusgangInfo.Ausgang;

class KriteriumZahl8 extends KriteriumZahl_PerAusgang {

	public KriteriumZahl8() {
		super(8, new AusgangInfo(Ausgang.KEINER, Ausgang.KEINER));
	}
}
