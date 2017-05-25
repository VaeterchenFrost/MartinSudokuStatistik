package sudoku.logik.bericht;

import java.util.ArrayList;

/**
 * @author Hendrick
 * Der Bericht dient der Ermittlung der L�sungszeit des Menschen f�r das Sudoku. 
 * Der Bericht bekommt eine Info f�r jede Logik, die Anteil am Zustandekommen der L�sung des Sudoku hat:
 * Entweder das Erkennen eines neuen Soll-Eintrags oder das L�schen von M�glichen.
 * 
 * Die Elemente des Berichts d�rfen keine Verweise auf Felder beinhalten,
 * denn der Bericht wird rausgegeben (an Schwierigkeit). Dort k�nnten die Felder ge�ndert werden!
 */
/**
 * @author heroe Dieser Bericht beinhaltet die Infos zu den Logikl�ufen
 *         innerhalb eines SudokuLogik.setzeMoegliche(). Er wird insbesondere
 *         genutzt f�r die Ermittlung der Sudoku-L�sungszeit.
 */
@SuppressWarnings("serial")
public class BerichtLogik extends ArrayList<Object> {

	public BerichtLogik() {
	}

	public void systemOut() {
		System.out.println("BerichtLogik.systemOut() -------------------------------------------");
		for (int i = 0; i < this.size(); i++) {
			System.out.println(this.get(i));
		}
	}

}
