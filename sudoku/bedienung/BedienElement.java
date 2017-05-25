package sudoku.bedienung;

import sudoku.kern.protokoll.ProtokollKursorInfo;

/**
 * @author Hendrick Das ist ein Bedienelement f�r das Sudoku, das gesperrt sein
 *         muss w�hrend eine Sudoku-Aktion j�uft
 */
public interface BedienElement {
	/**
	 * Sperrt das Bedienelement (gegen Bedienung)
	 */
	public abstract void sperre();

	/**
	 * Entsperrt das Bedienelement
	 */
	public abstract void entsperre(boolean istVorgabenMin, ProtokollKursorInfo protokollKursorInfo);

}
