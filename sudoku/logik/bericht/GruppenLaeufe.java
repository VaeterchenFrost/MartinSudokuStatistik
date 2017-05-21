package sudoku.logik.bericht;

import sudoku.logik.Gruppe;
import sudoku.logik.Gruppe.Typ;

/**
 * @author heroe
 *
 */
public class GruppenLaeufe {
	public final Gruppe.Typ gruppenTyp;
	private int anzahlLaeufe;

	/**
	 * @param gruppenTyp auch null fals es sich nicht um eine Logik bezogen auf einen GruppenTyp handelt
	 * @param anzahlLaeufe Anzahl der Logik-Einheiten, die gelaufen sind
	 */
	public GruppenLaeufe(Typ gruppenTyp, int anzahlLaeufe) {
		super();
		this.gruppenTyp = gruppenTyp;
		this.anzahlLaeufe = anzahlLaeufe;
	} 
	
	public void plus1(){
		anzahlLaeufe++;
	}
	
	public int gibAnzahlLaeufe(){
		return anzahlLaeufe;
	}

	public String gibTextKurz(){
		return Gruppe.gibTypZeichen(gruppenTyp) + "=" + anzahlLaeufe;
	}
	
	@Override
	public String toString() {
		return "GruppenLaeufe [gruppenTyp=" + gruppenTyp + ", anzahlLaeufe=" + anzahlLaeufe + "]";
	}
	
}
