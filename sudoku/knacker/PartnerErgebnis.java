package sudoku.knacker;

public class PartnerErgebnis {

	private boolean istProblemEintrag;
	private boolean istProblemAlternative;

	public PartnerErgebnis(boolean istProblemEintrag, boolean istProblemAlternative) {
		super();
		this.istProblemEintrag = istProblemEintrag;
		this.istProblemAlternative = istProblemAlternative;
	}

	public boolean beidePartnerBringenProbem() {
		return (istProblemEintrag && istProblemAlternative);
	}

	public boolean istProblemfrei() {
		return ((istProblemEintrag == false) && (istProblemAlternative == false));
	}

	public boolean istProblemEintrag() {
		return istProblemEintrag;
	}

}
