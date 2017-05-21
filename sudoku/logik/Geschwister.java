package sudoku.logik;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.ZahlenListe;

/**
 * @author Hendrick
 * In einer Gruppe gibt es n Felder mit genau denselben n möglichen Zahlen. 
 * Daraus folgt: 
 * - Diese n Zahlen belegen unbedingt die Felder. 
 * - Andere Zahlen in diesen Feldern sind nicht möglich.  
 */
class Geschwister {
	/** Zahlen, die die Felder unbedingt belegen
	 */
	private ArrayList<Integer> zahlen;
	/** Felder, die unbedingt belegt sind
	 */
	private FeldNummerListe felder;
	/** Mögliche Zahlen in diesen Feldern, die zu löschen sind
	 */
	private ZahlenListe loeschZahlen;
	
	/**
	 * @param zahlen Anzahl und Index genau wie felder
	 * @param felder Anzahl und Index genau wie zahlen
	 */
	public Geschwister(ArrayList<Integer> zahlen, FeldNummerListe felder, ZahlenListe loeschZahlen)  
	{
		this.zahlen = zahlen;
		this.felder = felder;
		this.loeschZahlen = loeschZahlen;
	}

	@Override
	public String toString() {
		return "[ " + felder + ": (" + zahlen + ") ]";
	}

	public ArrayList<Integer> gibZahlen() {
		return zahlen;
	}

	public FeldNummerListe gibFelder() {
		return felder;
	}
	
	public ZahlenListe gibLoeschZahlen(){
		return this.loeschZahlen;
	}
	
}
