package sudoku.langerprozess;

import sudoku.Paar;

public interface LangerProzess {
	/**
	 * @return Titel, der in der FortschrittAnzeige erscheint
	 */
	public String gibTitel();

	/**
	 * @return null: Es erfolgt einfach die Darstellung eines animierten Bildes.
	 *         != null: Pair.Key ist das Minimum, Pair.Value das Maximum des
	 *         anzuzeigenden Bereiches.
	 */
	public Paar<Integer, Integer> gibFortschrittBereich();

	/**
	 * Dies ist die lang dauernde Routine. Hierin muss zyklisch
	 * Thread.currentThread().isInterrupted() abgefragt werden: Bei true sollte
	 * der Prozess beendet werden!
	 * 
	 * @param fortschrittZeiger
	 *            Auch er muss zyklisch auf
	 *            fortschrittZeiger.istAbbruchGefordert() abgefragt werden!
	 */
	public void prozess(FortschrittZeiger fortschrittZeiger);
}
