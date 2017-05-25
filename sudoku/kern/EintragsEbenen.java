package sudoku.kern;

import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;

/**
 * @author Hendrick Die Klasse verwaltet Eintragsebenen. Jeder Eintrag befindet
 *         sich auf einer Eintragsebene. Das ist n�tig f�r das Zeigen und
 *         R�ckg�ngigmachen von Versuchen. Ein Versuch startet (eine neue Ebene)
 *         wenn ein Feld mit mehreren M�glichen einen Eintrag erh�lt. Der erste
 *         Eintrag �berhaupt startet unbedingt eine Ebene, auch wenn dies Feld
 *         eines mit nur 1 m�glichen Zahl ist. Eine Ebene verschwindet mit dem
 *         L�schen des letzten Eintrags auf dieser Ebene oder wenn sie
 *         ausdr�cklich als Gesamtheit gel�scht wird.
 *
 *         Die Ebene Nr. 1 ist den Eintr�gen in freien Feldern mit nur 1
 *         m�glichen Zahl vorbehalten. Versuche laufen auf den Ebenen ab Nr. 2.
 */
public class EintragsEbenen {

	/**
	 * @return Die Ebene Nr. 1 ist den Eintr�gen in freien Feldern mit 1
	 *         m�glichen Zahl vorbehalten. (Versuche laufen auf den Ebenen ab
	 *         Nr. 2.)
	 */
	static public int gibStandardEbene1() {
		return 1;
	}

	private int aktuelleEbene;

	public EintragsEbenen() {
		reset();
	}

	public int gibNummer() {
		return aktuelleEbene;
	}

	public boolean laeuftEine() {
		return aktuelleEbene > 0;
	}

	public void loesche() {
		aktuelleEbene--;

		if (aktuelleEbene < 0) {
			aktuelleEbene = 0;
		}
	}

	public void reset() {
		aktuelleEbene = 0;
	}

	/**
	 * F�hrt die Eintrags-Ebene mit je Anzahl der M�glichen im Feld: Anzahl=0:
	 * Exc; Anzahl=1: a) keine Eintr�ge=>1 @see gibStandardEbene1() b)Eintr�ge
	 * sind=>return; Anzahl>1 Starte Versuch (++)
	 * 
	 * @param feld
	 * @param zahl
	 *            Sie soll dann gesetzt werden
	 * @return true wenn eine neue Ebene f�r diesen Eintrag erstellt wurde
	 * @throws Exc
	 *             Falls das Setzen der zahl als Eintrag anhand der M�glichen
	 *             des Feldes gar nicht zul�ssig ist
	 */
	public boolean setzeEintragsEbene(Feld feld, int zahl) throws Exc {
		ArrayList<Integer> moegliche = feld.gibMoegliche();

		if (0 == moegliche.size()) {
			throw Exc.setzeEintragNichtOhneMoegliche(feld, zahl);
		}
		// Ist dieser Eintrag m�glich?
		if (!moegliche.contains(zahl)) {
			throw Exc.setzeEintragNichtOhneDieseMoegliche(feld, zahl);
		}

		boolean istNeueEbene = false;
		// Eventuell ist Ebenen-Start
		if (1 == moegliche.size()) {
			if (!laeuftEine()) {
				starteEbene();
				istNeueEbene = true;
			}
		} else {
			starteVersuch();
			istNeueEbene = true;
		}
		return istNeueEbene;
	}

	public void setzeEintragsEbeneUnbedingt(int zahl) {
		this.aktuelleEbene = zahl;
	}

	/**
	 * Start des Eintrag-Setzens auf einem Feld mit nur einer m�glichen Zahl
	 */
	private void starteEbene() {
		aktuelleEbene++;
	}

	/**
	 * Ein Feld mit mehreren M�glichen erh�lt einen Eintrag
	 */
	private void starteVersuch() {
		if (aktuelleEbene == 0) {
			// Ebene 1 �berspringen
			aktuelleEbene++;
		}
		aktuelleEbene++;
	}
}
