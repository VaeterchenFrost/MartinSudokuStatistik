package sudoku.logik.bericht;

import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;

public class BE_Ende {
	Problem problem;
	FeldNummerMitZahl eintrag;

	public BE_Ende(Problem problem, FeldNummerMitZahl eintrag) {
		super();
		this.problem = problem;
		this.eintrag = eintrag;
	}

	public FeldNummerMitZahl gibEintrag() {
		return eintrag;
	}

	public Problem gibProblem() {
		return problem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BE_Ende [problem=" + problem + " eintrag=" + eintrag + "]";
	}

}
