package sudoku.bedienung;

/**
 * @author Hendrick Das ist jemand, der das Sudoku oder einen Teil dessen
 *         anzeigen mu�. Oder jemand m�chte sich z.B. auf Basis des
 *         Sudoku-Zustandes sperren/entsperren.
 */
public interface AnzeigeElement {
	/**
	 * Wird gerufen nach einer �nderung am Sudoku
	 * 
	 * @param sudoku
	 */
	public abstract void zeige(SudokuBedienung sudoku);
}
