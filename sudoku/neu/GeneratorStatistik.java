package sudoku.neu;

import sudoku.kern.info.InfoSudoku;

public interface GeneratorStatistik {
	/**
	 * Dieser Statistik wird ein neu erstelltes Sudoku mitgeteilt.
	 * @param forderung Der Typ, der angefordert wurde.
	 * @param infoSudoku Bei null hat die Erstellung eines Sudokus des (angeforderten) NeuTyps nicht geklappt.
	 * @param neuTyp Der Typ des erstellten Sudoku.
	 * @param laufNummer Nummer des Generator-Laufes, der erfolgreich dies Sudoku erzeugt hat.
	 * @param istErstesDerLoesungsZeit
	 * 	- null wenn das Sudoku erstellt, aber nicht im Pool abgelegt wurde.
	 *  - true wenn das Sudoku als 1. Sudoku mit dieser Lösungszeit im Pool abgelegt wurde.
	 *  - false wenn das Sudoku im Pool abgelegt wurde, aber NICHT als 1. Sudoku mit dieser Lösungszeit.  
	 * @param loesungsZeit (menschlich) des Sudoku 
	 * @param topfName Im Falle des Dateipools ist dies die vollständige Pfadangabe zum Topf des neuTyps
	 */
	public void neuesSudoku(final NeuTyp forderung, final InfoSudoku infoSudoku, final NeuTyp neuTyp,
			final int laufNummer, final Boolean istErstesDerLoesungsZeit, final int loesungsZeit, final String topfName);
}
