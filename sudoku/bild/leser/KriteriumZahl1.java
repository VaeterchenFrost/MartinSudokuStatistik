package sudoku.bild.leser;

class KriteriumZahl1 extends KriteriumZahl_BildInfo {

	public KriteriumZahl1() {
		super(1);
	}

	@Override
	protected KriteriumBildInfo[] gibKriterien() {
		KriteriumBildInfo[] kriterien = { BasisKriterium.senkrechterStrichRechts14(),
				BasisKriterium.ausgangLinksOben14(), BasisKriterium.ausgangLinksUnten1() };
		return kriterien;
	}

}
