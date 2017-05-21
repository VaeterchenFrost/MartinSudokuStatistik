package sudoku.kern.exception;

/**
 * @author heroe
 * Dies ist weniger Ausnahme als vielmehr die praktische Meldung "Das Sudoku ist fertig gelöst".
 * Eine Ausnahme nur technisch, um aus der kompletten (z.B. rekursiven) Logik auszusteigen.
 */
@SuppressWarnings("serial")
public class SudokuFertig  extends Exception {
	public SudokuFertig() {
		super();
	}

}
