package sudoku.bild.leser;

class KriteriumZahl4 extends KriteriumZahl_BildInfo {

	public KriteriumZahl4() {
		super(4);
	}

	@Override
	protected KriteriumBildInfo[] gibKriterien() {
		KriteriumBildInfo[] kriterien = { BasisKriterium.senkrechterStrichRechts14(),
				BasisKriterium.waagerechterStrich4(), BasisKriterium.ausgangLinksOben14(),
				BasisKriterium.ausgangLinksUnten4(),
				// BasisKriterium.ausgangRechtsOben4(),
				// BasisKriterium.ausgangRechtsUnten4()
		};
		return kriterien;
	}

}
