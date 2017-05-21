package sudoku.neu.pool;

/**
 * @author heroe
 */
public enum NeuTypOption {
	/** Zufällig ein Sudoku auswählen
	 */
	MIX, 
	/** Ein Sudoku mit geringer Lösungszeit auswählen
	 */
	KURZ, 
	/** Ein Sudoku mit mittlerer Lösungszeit auswählen
	 */
	NORMAL, 
	/** Ein Sudoku mit großer Lösungszeit auswählen
	 */
	LANGWIERIG
}
