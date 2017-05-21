package sudoku.bedienung;

import sudoku.kern.protokoll.ProtokollKursorInfo;

/**
 * @author Hendrick
 * Das ist ein Bedienelement für das Sudoku, das gesperrt sein muss 
 * während eine Sudoku-Aktion jäuft
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
