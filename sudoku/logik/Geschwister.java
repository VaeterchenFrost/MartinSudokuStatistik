package sudoku.logik;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.ZahlenListe;

/**
 * @author Hendrick In einer Gruppe gibt es n Felder mit genau denselben n
 *         m�glichen Zahlen. Daraus folgt: - Diese n Zahlen belegen unbedingt
 *         die Felder. - Andere Zahlen in diesen Feldern sind nicht m�glich.
 */
class Geschwister {
	/**
	 * Zahlen, die die Felder unbedingt belegen
	 */
	private ArrayList<Integer> zahlen;
	/**
	 * Felder, die unbedingt belegt sind
	 */
	private FeldNummerListe felder;
	/**
	 * M�gliche Zahlen in diesen Feldern, die zu l�schen sind
	 */
	private ZahlenListe loeschZahlen;

	/**
	 * @param zahlen
	 *            Anzahl und Index genau wie felder
	 * @param felder
	 *            Anzahl und Index genau wie zahlen
	 */
	public Geschwister(ArrayList<Integer> zahlen, FeldNummerListe felder, ZahlenListe loeschZahlen) {
		this.zahlen = zahlen;
		this.felder = felder;
		this.loeschZahlen = loeschZahlen;
	}

	public FeldNummerListe gibFelder() {
		return felder;
	}

	public ZahlenListe gibLoeschZahlen() {
		return this.loeschZahlen;
	}

	public ArrayList<Integer> gibZahlen() {
		return zahlen;
	}

	@Override
	public String toString() {
		return "[ " + felder + ": (" + zahlen + ") ]";
	}

}
