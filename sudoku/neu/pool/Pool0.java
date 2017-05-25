package sudoku.neu.pool;

import sudoku.kern.info.InfoSudoku;
import sudoku.neu.NeuTyp;

public interface Pool0 {
	/**
	 * @param neuTyp
	 * @param option
	 * @return Sudoku des angeforderten Typs oder null wenn ein solches nicht
	 *         zur Verf�gung steht.
	 */
	public InfoSudoku gibSudoku(NeuTyp neuTyp, NeuTypOption option);

	/**
	 * @return Angeforderter Typ wenn ein Sudoku ben�tigt wird oder null
	 */
	public NeuTyp gibForderung();

	/**
	 * Setzt in den Pool das Sudoku zur Aufbewahrung
	 * 
	 * @param neuTyp
	 * @param sudoku
	 * @return - null wenn das Sudoku nicht im Pool abgelegt wurde. - true wenn
	 *         das Sudoku als 1. Sudoku mit dieser L�sungszeit im Pool abgelegt
	 *         wurde. - false wenn das Sudoku im Pool abgelegt wurde, aber NICHT
	 *         als 1. Sudoku mit dieser L�sungszeit.
	 */
	public Boolean setze(NeuTyp neuTyp, InfoSudoku sudoku, int loesungsZeit);

	/**
	 * @return Info zum aktuellen Zustand des Pools
	 */
	public PoolInfo gibPoolInfo();

	/**
	 * @param neuTyp
	 * @return Name des dem neuTyp entsprechenden Topfes im Pool.
	 */
	public String gibTopfName(NeuTyp neuTyp);
}
