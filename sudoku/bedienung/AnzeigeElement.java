package sudoku.bedienung;



/**
 * @author Hendrick
 * Das ist jemand, der das Sudoku oder einen Teil dessen anzeigen muß.
 * Oder jemand möchte sich z.B. auf Basis des Sudoku-Zustandes sperren/entsperren.
 */
public interface AnzeigeElement {
	/**
	 * Wird gerufen nach einer Änderung am Sudoku
	 * @param sudoku
	 */
	public abstract void zeige(SudokuBedienung sudoku);
}
