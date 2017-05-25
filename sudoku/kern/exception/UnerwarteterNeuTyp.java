package sudoku.kern.exception;

@SuppressWarnings("serial")
public class UnerwarteterNeuTyp extends RuntimeException {

	public UnerwarteterNeuTyp(String neuTypName) {
		super(String.format("Unerwarteter NeuTyp %s", neuTypName));
	}

}
