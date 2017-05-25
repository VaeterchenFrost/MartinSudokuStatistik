package sudoku.logik.tipinfo;

import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.Logik_ID;

public interface TipInfo {
	/**
	 * @return null oder aktive Felder: Das Feld mit dem zu setzenden Eintrag
	 *         oder die Felder, in denen m�gliche Zahlen als zu l�schen benannt
	 *         werden.
	 */
	public FeldNummerListe gibAktiveFelder();

	/**
	 * @return null oder die durch die Logik empfohlenen zu l�schenden m�glichen
	 *         Zahlen
	 */
	public ZahlenListe gibLoeschZahlen();

	/**
	 * @return Der Identifikator der Logik, die zu diesem Tip gef�hrt hat
	 */
	public Logik_ID gibLogik();

	/**
	 * @return null oder die Mitspieler (alle Felder), auf deren Basis die Logik
	 *         lief
	 */
	public FeldNummerListe gibMitSpieler();

	/**
	 * @return null oder Sudoku
	 */
	public InfoSudoku gibSudoku();

	/**
	 * @return null oder die konkrete Aktion
	 */
	public EinTipText[] gibTip();

	/**
	 * @param tipNummer
	 * @return �berschrift, die auch die tipNummer beinhaltet
	 */
	public String gibUeberschrift(int tipNummer);

	/**
	 * @return null oder FeldNummer mit der Zahl, die den empfohlenen Eintrag
	 *         darstellt
	 */
	public FeldNummerMitZahl gibZahlFeld();

	/**
	 * @return true wenn diese Info eine zu setzende Zahl benenntt.
	 */
	public boolean istZahl();

	/**
	 * @return null oder Sudoku
	 */
	public void setzeSudoku(InfoSudoku infoSudoku);
}
