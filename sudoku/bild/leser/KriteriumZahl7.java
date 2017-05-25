package sudoku.bild.leser;

class KriteriumZahl7 extends KriteriumZahl_BildInfo {

	public KriteriumZahl7() {
		super(7);
	}

	@Override
	protected KriteriumBildInfo[] gibKriterien() {
		KriteriumBildInfo[] kriterien = { BasisKriterium.waagerechterStrichOben7(), BasisKriterium.ausgangLinks7(),
				BasisKriterium.ausgangRechts7() };
		return kriterien;
	}

}
