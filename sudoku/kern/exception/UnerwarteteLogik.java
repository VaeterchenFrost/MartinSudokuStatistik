package sudoku.kern.exception;

@SuppressWarnings("serial")
public class UnerwarteteLogik extends RuntimeException {

	public UnerwarteteLogik(String logikName) {
		super(String.format("Unerwartete Logik %s", logikName));
	}

}
