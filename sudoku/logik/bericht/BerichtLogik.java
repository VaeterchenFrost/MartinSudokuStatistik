package sudoku.logik.bericht;

import java.util.ArrayList;

/**
 * @author Hendrick
 * Der Bericht dient der Ermittlung der Lösungszeit des Menschen für das Sudoku. 
 * Der Bericht bekommt eine Info für jede Logik, die Anteil am Zustandekommen der Lösung des Sudoku hat:
 * Entweder das Erkennen eines neuen Soll-Eintrags oder das Löschen von Möglichen.
 * 
 * Die Elemente des Berichts dürfen keine Verweise auf Felder beinhalten,
 * denn der Bericht wird rausgegeben (an Schwierigkeit). Dort könnten die Felder geändert werden!
 */
/**
 * @author heroe
 * Dieser Bericht beinhaltet die Infos zu den Logikläufen innerhalb eines SudokuLogik.setzeMoegliche().
 * Er wird insbesondere genutzt für die Ermittlung der Sudoku-Lösungszeit.
 */
@SuppressWarnings("serial")
public class BerichtLogik extends ArrayList<Object>{

	public BerichtLogik() {
	}

	public void systemOut(){
		System.out.println("BerichtLogik.systemOut() -------------------------------------------");
		for (int i= 0; i< this.size(); i++) {
			System.out.println(this.get(i));
		}
	}

}
