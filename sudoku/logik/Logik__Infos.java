package sudoku.logik;

/**
 * @author heroe
 * Eine Sudoku-Logik bzw. Sudoku-Lösungsstrategie
 */
public interface Logik__Infos {
	Logik_ID gibLogikID();
	/**
	 * @return Name der Logik als Zweibuchstaber
	 */
	String gibKurzName();
	/**
	 * @return Treffende Bezeichnung.
	 */
	String gibName();
	/**
	 * @return Wo ist die Situation vorzufinden?
	 */
	String[] gibWo();
	/**
	 * @return Die Situation in abstakter logischer Betrachtung benannt.
	 */
	String[] gibSituationAbstrakt();
	/**
	 * @return Die Situation in praktischer Betrachtung benannt.
	 */
	String[] gibSituation();
	/**
	 * @return Das Ergebnis der Situation.
	 */
	String gibErgebnis();
	
	/**
	 * @return Die Zeit der die Mensch für die Erfassung der Situation benötigt in Sekunden.
	 * 				In der Logik_Feld (Betrachtung eines Feldes und seiner 3 Gruppen)
	 * 					ist das z.B. die Zeit zur Klärung der Frage, ob das eine freie Feld bezogen auf 
	 * 					Kasten, Zeile und Spalte, in denen dies Feld liegt, nur eine Zahl hat, die in ihm möglich ist.
	 * 				In der Logik_OrtFest1 (Betrachtung einer Gruppe)
	 * 					ist das z.B. die Zeit zur Klärung der Frage, ob eine der noch fehlenden Zahlen der Gruppe
	 * 					nur in einem Feld der Gruppe möglich ist.  
	 * 				In der Logik_KreuzFluegel (Betrachtung einer Gruppe, Suche einer Partner-Gruppe und Betrachtung der Senkrechten)
	 * 					ist das z.B. die Zeit zur Klärung der Frage, ob in einer Gruppe (Zeile bzw. Spalte)
	 * 					eine Zahl auf (nur) genau 2 Feldern möglich ist.
	 * 					Wenn ja, ob in einer anderen Gruppe dieses Typs (Zeile bzw. Spalte) diese Flügel existieren und
	 * 					wenn ja, ob es zu löschende dieser Zahl gibt (in Spalten bzw. Zeilen).  
	 */
	double gibKontrollZeit1();
}
