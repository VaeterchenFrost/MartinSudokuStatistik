package sudoku.kern.exception;

@SuppressWarnings("serial")
public class UnerwarteterInhalt extends RuntimeException {

	public UnerwarteterInhalt(String inhalt) {
		super(String.format("Unerwarteter Inhalt: %s", inhalt));
	}
}
