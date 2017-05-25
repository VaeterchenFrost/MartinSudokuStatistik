package sudoku.knacker;

import sudoku.kern.feldmatrix.Problem;

public class Ergebnis {

	public enum Art {
		FERTIG, UNGELOEST, PROBLEM
	};

	private Art art;
	private Problem problem;

	private Ergebnis(Art art, Problem problem) {
		this.art = art;
		this.problem = problem;
	}

	public static Ergebnis fertig() {
		Ergebnis ergebnis = new Ergebnis(Art.FERTIG, null);
		return ergebnis;
	}

	public static Ergebnis ungeloest() {
		Ergebnis ergebnis = new Ergebnis(Art.UNGELOEST, null);
		return ergebnis;
	}

	public static Ergebnis problem(Problem problem) {
		Ergebnis ergebnis = new Ergebnis(Art.PROBLEM, problem);
		return ergebnis;
	}

	public Art gibArt() {
		return art;
	}

	public Problem gibProblem() {
		return problem;
	}

	@Override
	public String toString() {
		String s = "[" + art;
		if (problem != null) {
			s += "; problem=";
			s += problem;
		}
		s += "]";
		return s;
	}
}
