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
 * @author heroe Analysiert die Schwierigkeit eines Sudoku
 */
public class Analysator {

	/**
	 * @param vorgaben
	 *            Definiert die Vorgaben des Sudoku
	 * @return Schwierigkeit des Sudoku
	 * @throws Exc
	 */
	public static SudokuSchwierigkeit gibSchwierigkeit(InfoSudoku vorgaben) throws Exc {
		return gibSchwierigkeit(vorgaben, null);
	}

	/**
	 * @param vorgaben
	 *            Definiert die Vorgaben des Sudoku
	 * @param knackerBericht,
	 *            der soeben erstellt wurde f�r das Sudoku oder null
	 * @return Schwierigkeit des Sudoku
	 * @throws Exc
	 */
	public static SudokuSchwierigkeit gibSchwierigkeit(InfoSudoku vorgaben, BerichtKnacker knackerBericht) throws Exc {
		// Bei trivialer Nutzung der max. Klugheit ist es durchaus typisch, dass
		// eine falsche (zu gro�e)
		// Schwierigkeit erkannt wird.
		Analysator analisator = new Analysator();
		if (knackerBericht == null) {
			knackerBericht = analisator.erstelleKnackerBericht(vorgaben);
		}
		if (knackerBericht.isEmpty()) {
			return SudokuSchwierigkeit.unbestimmt(vorgaben.size());
		} else {
			SudokuSchwierigkeit schwierigkeit = null;
			if (knackerBericht.istVersuch()) {
				schwierigkeit = new SudokuSchwierigkeit(knackerBericht);
			} else {
				// Dann lohnt es sich, die Klare-Logik-Schwierigkeit richtig
				// (als Minimum) zu erstellen!
				BerichtKnacker berichtKlare = analisator.erstelleKnackerBerichtKlare(vorgaben);
				schwierigkeit = new SudokuSchwierigkeit(berichtKlare);
			}
			return schwierigkeit;
		}
	}

	private Klugheit klugheit;
	private EintragsEbenen ebenen;
	private SudokuLogik feldmatrix;

	private Protokoll protokoll;

	private Knacker knacker;

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
	private BerichtKnacker erstelleKnackerBericht(InfoSudoku vorgaben) throws Exc {
		feldmatrix.reset(vorgaben);
		klugheit.setzeExtrem(true);

		// Die Schwierigkeitsermittlung erfolgt f�r normale Sudoku: Deshalb nur
		// VersuchsEbenen.EINE.
		// Unbestimmte werden als solche benannt: N�mlich wenn sie hier nicht
		// l�sbar sind.
		// Mit "Knacke" k�nnen (typisch) auch diese Sudoku gel�st werden.
		VersuchsEbenen versuchsEbenen = VersuchsEbenen.EINE;

		Ergebnis ergebnis = knacker.loese(versuchsEbenen, "Schwierigkeitsermittlung");
		if (ergebnis.gibArt() != Ergebnis.Art.FERTIG) {
			return new BerichtKnacker();
		}
		BerichtKnacker bericht = knacker.gibBericht();
		return bericht;
	}

	/**
	 * Diese Schwierigkeitsermittlung erfolgt f�r logisch l�sbare Sudokus:
	 * Deshalb nur VersuchsEbenen.KEINE. Es wird die kleinste erfolgreiche
	 * Klugheit ermittelt.
	 * 
	 * @param vorgaben
	 * @return bericht, kann leer sein
	 * @throws Exc
	 */
	private BerichtKnacker erstelleKnackerBerichtKlare(InfoSudoku vorgaben) throws Exc {
		feldmatrix.reset(vorgaben);
		klugheit.setzeExtrem(false);

		do {
			Ergebnis ergebnis = knacker.loese(VersuchsEbenen.KEINE,
					"Schwierigkeitsermittlung Ohne Versuch Klugheit " + klugheit);
			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG) {
				return knacker.gibBericht();
			}
		} while (klugheit.erhoehe());

		return new BerichtKnacker();
	}

	protected EintragsEbenen gibEbenen() {
		return ebenen;
	}

	protected SudokuLogik gibFeldmatrix() {
		return feldmatrix;
	}

	protected Klugheit gibKlugheit() {
		return klugheit;
	}

	protected Knacker gibKnacker() {
		return knacker;
	}

	protected Protokoll gibProtokoll() {
		return protokoll;
	}

	protected void setKlugheit(Klugheit klugheit) {
		this.klugheit.setze(klugheit);
	}
}
