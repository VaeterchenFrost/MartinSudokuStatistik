package sudoku.langerprozess;

public interface FortschrittZeiger {
	/**
	 * @return true wenn Abbruch gefordert wurde.
	 */
	public boolean istAbbruchGefordert();

	/**
	 * @param fortschritt
	 *            Zeigt den Fortschritt an (z.B. in der Form eines
	 *            Fortschritt-Balkens).
	 */
	public void zeigeFortschritt(int fortschritt);

	/**
	 * @param info
	 *            Zeigt die Information an. Das k�nnte statt einer AnZEIGE
	 *            selbstverst�ndlich auch in der Form z.B. einer Sprachausgabe
	 *            passieren.
	 */
	public void zeigeInfo(String info);
}
