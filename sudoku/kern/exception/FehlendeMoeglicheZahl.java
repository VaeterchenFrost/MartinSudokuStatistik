package sudoku.kern.exception;

import sudoku.kern.feldmatrix.FeldNummerMitZahl;

@SuppressWarnings("serial")
public class FehlendeMoeglicheZahl extends RuntimeException {

	public FehlendeMoeglicheZahl(FeldNummerMitZahl feldNummerMitZahl) {
		super(String.format("Fehlende m�gliche Zahl %s", feldNummerMitZahl));
	}
}
