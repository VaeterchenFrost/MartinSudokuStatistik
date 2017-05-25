package sudoku.kern.feldmatrix;

import sudoku.kern.exception.Exc;

/**
 * @author Hendrick Erm�glicht das Ver�ndern des Feld.eintrag von extern (f�r
 *         Protokoll). Aber nur innerhalb des Packkages "feldmatrix" ist diese
 *         Klasse instanziierbar.
 */
public class ProtokollEintragSetzer {
	private FeldMatrix feldMatrix;

	ProtokollEintragSetzer(FeldMatrix feldMatrix) {
		this.feldMatrix = feldMatrix;
	}

	public void setzeEintrag(FeldNummer feldNummer, Eintrag eintrag) throws Exc {
		Feld feld = feldMatrix.gibFeld(feldNummer);

		feld.setzeEintragUnbedingt(eintrag);
	}
}
