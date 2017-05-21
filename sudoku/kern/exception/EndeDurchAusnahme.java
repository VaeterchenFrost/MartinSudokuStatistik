package sudoku.kern.exception;

/**
 * @author heroe
 * Diese Ausnahme dient der Herbeiführung des Programmendes, weil eine nicht behandelbare Exception aufgetreten ist.
 */
@SuppressWarnings("serial")
public class EndeDurchAusnahme extends RuntimeException{ //Error{ //
	public EndeDurchAusnahme(Exception e) {
		super (null, e,
            true, //boolean enableSuppression,
            false); //boolean writableStackTrace)		this.endeAusnahme = e;
	}
}
