package sudoku.bild.leser;

class KriteriumZahl_PerAusgang {
	final int zahl;
	private AusgangInfo ausgangInfo;

	KriteriumZahl_PerAusgang(int zahl, AusgangInfo ausgangInfo) {
		this.zahl = zahl;
		this.ausgangInfo = ausgangInfo;
	}

	boolean istErfuellt(AusgangInfo ausgangInfo) {
		boolean istErfuellt = this.ausgangInfo.equals(ausgangInfo);
		return istErfuellt;
	}
}
