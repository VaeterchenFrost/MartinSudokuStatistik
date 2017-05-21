package sudoku.kern.protokoll;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;

public interface ProtokollMarkierer {
	/**
	 * Setzt eine Markierung auf die aktuelle Kursor-Position
	 * @return Den Identifikator der Markierung
	 */
	public int markierungSetzen();

	/**
	 * Setzt den Kursor auf die Position (zurück), die durch die id angezeigt wird.
	 * (Das geschieht durch schrittweises Rückwärtsgehen im Sudoku, denn dessen
	 * Zustand wird ja auf den der id zurüchgestellt.)
	 * @param id
	 * @throws Exc wenn eine Markierung mit der id nicht existiert.
	 */
	public void markierungAnsteuern(int id) throws Exc;

	/**
	 * @param id Die id der Markierung die der Externe mit markierungSetzen() definiert hatte.
	 * @return Vom Eintrag nach der markierung das was gesetzt wurde (feldInfo-Neu) oder null
	 * @throws Exc wenn eine Markierung mit der id nicht existiert.
	 */
	public FeldNummerMitZahl markierungGibZahlTip (int id) throws Exc;
}
