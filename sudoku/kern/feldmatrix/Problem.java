package sudoku.kern.feldmatrix;

/**
 * @author Hendrick Zeigt ein Problem innerhalb des Sudoku an.
 */
public class Problem {
	public static Problem freiesFeldOhneMoegliche(FeldNummer feldNummer) {
		String text = String.format("Das freie Feld %s besitzt keine m�glichen Zahlen", feldNummer);

		return new Problem(text);
	}

	public static Problem gruppeOhneZahl(String gruppeInText, ZahlenFeldNummern alleVorhandeneZahlen) {
		String text = String.format("%s besitzt nur %d Zahlen: %s", gruppeInText,
				alleVorhandeneZahlen.gibAnzahlVorhandene(), alleVorhandeneZahlen.toStringVorhandeneZahlen());

		return new Problem(text);
	}

	public static Problem nichtAlleZahlenInDerGruppe(String gruppeInText, ZahlenFeldNummern vorhandeneZahlen) {
		String text = String.format("%s fehlt die Zahl %s", gruppeInText,
				vorhandeneZahlen.toStringNichtVorhandeneZahlen());

		return new Problem(text);
	}

	public static Problem zuwenigMoeglicheInFreienFeldern(String gruppeInText, int zahl, int anzahlFelder,
			int anzahlMoeglicheZahlen) {
		String text = String.format("%s: In den %d Feldern mit Zahl %d gibt es nur %d M�gliche", gruppeInText,
				anzahlFelder, zahl, anzahlMoeglicheZahlen);

		return new Problem(text);
	}

	// public static Problem sudokuException(Exc e){
	// String text = String.format("Sudoku-Exception %s", e.getMessage());
	//
	// return new Problem(text);
	// }

	private String problem;

	public Problem(String text) {
		problem = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Problem other = (Problem) obj;
		if (problem == null) {
			if (other.problem != null) {
				return false;
			}
		} else if (!problem.equals(other.problem)) {
			return false;
		}
		return true;
	}

	public String gibProblem() {
		return problem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((problem == null) ? 0 : problem.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return problem;
	}

}
