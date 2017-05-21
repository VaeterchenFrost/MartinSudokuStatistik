package sudoku.langerprozess;

public interface FortschrittZeiger {
	/**
	 * @param fortschritt Zeigt den Fortschritt an (z.B. in der Form eines Fortschritt-Balkens). 
	 */
	public void zeigeFortschritt(int fortschritt);
	/**
	 * @param info Zeigt die Information an.
	 * Das könnte statt einer AnZEIGE selbstverständlich auch in der Form z.B. einer Sprachausgabe passieren. 
	 */
	public void zeigeInfo(String info);
	/**
	 * @return true wenn Abbruch gefordert wurde.
	 */
	public boolean istAbbruchGefordert();
}
