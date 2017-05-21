package sudoku.schwer;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.exception.Exc;
import sudoku.kern.info.InfoSudoku;
import sudoku.kern.protokoll.Protokoll;
import sudoku.knacker.Ergebnis;
import sudoku.knacker.Knacker;
import sudoku.knacker.Knacker.VersuchsEbenen;
import sudoku.knacker.bericht.BerichtKnacker;
import sudoku.logik.Klugheit;
import sudoku.logik.SudokuLogik;


/**
 * @author heroe
 * Analysiert die Schwierigkeit eines Sudoku
 */
public class Analysator {
	
	private Klugheit klugheit;
	private EintragsEbenen ebenen;
	private SudokuLogik feldmatrix;
	private Protokoll protokoll;
	private Knacker knacker;

	/**
	 * @param vorgaben Definiert die Vorgaben des Sudoku
	 * @param knackerBericht, der soeben erstellt wurde für das Sudoku oder null
	 * @return Schwierigkeit des Sudoku
	 * @throws Exc
	 */
	public static SudokuSchwierigkeit gibSchwierigkeit(InfoSudoku vorgaben, 	BerichtKnacker knackerBericht) throws Exc{
		// Bei trivialer Nutzung der max. Klugheit ist es durchaus typisch, dass eine falsche (zu große)
		// Schwierigkeit erkannt wird.
		Analysator analisator = new Analysator();
		if (knackerBericht == null){
			knackerBericht 	= analisator.erstelleKnackerBericht(vorgaben);
		}
		if (knackerBericht.isEmpty()){
			return SudokuSchwierigkeit.unbestimmt(vorgaben.size());
		} 
		else 
		{
			SudokuSchwierigkeit schwierigkeit = null;
			if (knackerBericht.istVersuch()){
				schwierigkeit = new SudokuSchwierigkeit(knackerBericht);
			}
			else{
				// Dann lohnt es sich, die Klare-Logik-Schwierigkeit richtig (als Minimum) zu erstellen!
				BerichtKnacker berichtKlare = analisator.erstelleKnackerBerichtKlare(vorgaben);
				schwierigkeit = new SudokuSchwierigkeit(berichtKlare);
			}
			return schwierigkeit;
		}
	}

	/**
	 * @param vorgaben Definiert die Vorgaben des Sudoku
	 * @return Schwierigkeit des Sudoku
	 * @throws Exc
	 */
	public static SudokuSchwierigkeit gibSchwierigkeit(InfoSudoku vorgaben) throws Exc{
		return gibSchwierigkeit(vorgaben, 	null);
	}
	
	protected Analysator() {
		klugheit = new Klugheit(true);
		ebenen = new EintragsEbenen();
		feldmatrix = new SudokuLogik(ebenen);
		protokoll = new Protokoll(feldmatrix);
		knacker = new Knacker(klugheit, feldmatrix, protokoll);
	}

	/**
	 * @param vorgaben
	 * @return bericht, kann leer sein
	 * @throws Exc
	 */
	private BerichtKnacker erstelleKnackerBericht (InfoSudoku vorgaben) throws Exc{
		feldmatrix.reset(vorgaben);
		klugheit.setzeExtrem(true);
		
		// Die Schwierigkeitsermittlung erfolgt für normale Sudoku: Deshalb nur VersuchsEbenen.EINE.
		// Unbestimmte werden als solche benannt: Nämlich wenn sie hier nicht lösbar sind.
		// Mit "Knacke" können (typisch) auch diese Sudoku gelöst werden.
		VersuchsEbenen versuchsEbenen = VersuchsEbenen.EINE;
		
		Ergebnis ergebnis = knacker.loese(versuchsEbenen, "Schwierigkeitsermittlung");
		if (ergebnis.gibArt() != Ergebnis.Art.FERTIG) {
			return new BerichtKnacker(); 
		}
		BerichtKnacker bericht = knacker.gibBericht();
		return bericht;
	}

	/**
	 *	Diese Schwierigkeitsermittlung erfolgt für logisch lösbare Sudokus: Deshalb nur VersuchsEbenen.KEINE.
	 * Es wird die kleinste erfolgreiche Klugheit ermittelt.
	 * @param vorgaben
	 * @return bericht, kann leer sein
	 * @throws Exc
	 */
	private BerichtKnacker erstelleKnackerBerichtKlare (InfoSudoku vorgaben) throws Exc{
		feldmatrix.reset(vorgaben);
		klugheit.setzeExtrem(false);
		
		do
		{
			Ergebnis ergebnis = knacker.loese(VersuchsEbenen.KEINE, "Schwierigkeitsermittlung Ohne Versuch Klugheit " + klugheit);
			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG) {
				return knacker.gibBericht();
			}
		} while (klugheit.erhoehe());
		
		return new BerichtKnacker(); 
	}


	protected Klugheit gibKlugheit() {
		return klugheit;
	}

	protected EintragsEbenen gibEbenen() {
		return ebenen;
	}

	protected SudokuLogik gibFeldmatrix() {
		return feldmatrix;
	}

	protected Protokoll gibProtokoll() {
		return protokoll;
	}

	protected Knacker gibKnacker() {
		return knacker;
	}

	protected void setKlugheit(Klugheit klugheit) {
		this.klugheit.setze(klugheit);
	}
}
