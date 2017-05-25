package sudoku.logik.bericht;

public class BE_Durchlauf {
	int durchLauf;

	public BE_Durchlauf(int durchLauf) {
		super();
		this.durchLauf = durchLauf;
	}

	/**
	 * @return the durchLauf
	 */
	public int gibDurchLauf() {
		return durchLauf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BE_Duchlauf[ durchLauf=" + durchLauf + "]";
	}

}
