package sudoku.langerprozess;

import sudoku.Paar;

public interface FortschrittAnzeige extends FortschrittZeiger {
	/**
	 * Die FortschrittAnzeige wird beendet. Falls der Programm-Lauf des
	 * Aufrufers bis zum Ende der FortschrittAnzeige festgehalten worden war,
	 * wird er ab jetzt fortgesetzt.
	 */
	public void beenden();

	/**
	 * @param titel
	 *            Der Titel der FortschrittAnzeige.
	 * @param fortschrittBereich
	 *            = null: Es erfolgt einfach die Darstellung eines animierten
	 *            Bildes. fortschrittBereich != null: Pair.Key ist das Minimum,
	 *            Pair.Value das Maximum des anzuzeigenden Bereiches.
	 */
	public void starten(String titel, Paar<Integer, Integer> fortschrittBereich);
}
