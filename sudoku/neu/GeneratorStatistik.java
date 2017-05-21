package sudoku.neu;

import sudoku.kern.info.InfoSudoku;

public interface GeneratorStatistik {
	/**
	 * Dieser Statistik wird ein neu erstelltes Sudoku mitgeteilt.
	 * @param neuTyp
	 * @param infoSudoku Bei null hat die Erstellung eines Sudokus des (angeforderten) NeuTyps nicht geklappt.
	 * @param istErstesDerLoesungsZeit
	 * 	- null wenn das Sudoku erstellt, aber nicht im Pool abgelegt wurde.
	 *  - true wenn das Sudoku als 1. Sudoku mit dieser L�sungszeit im Pool abgelegt wurde.
	 *  - false wenn das Sudoku im Pool abgelegt wurde, aber NICHT als 1. Sudoku mit dieser L�sungszeit.  
	 * @param loesungsZeit
	 * @param topfName Im Falle des Dateipools ist dies die vollst�ndige Pfadangabe zum Topf des neuTyps
	 */
	public void neuesSudoku( final NeuTyp neuTyp, final InfoSudoku infoSudoku, final Boolean istErstesDerLoesungsZeit, 
			final int loesungsZeit, final String topfName);
}
