package sudoku.kern.protokoll;

public class ProtokollKursorInfo {
	private MinMax eintrag;
	private MinMax ebene;

	private int kursorEintrag;
	private int kursorEbene;

	private boolean istVorwaertsMoeglich;
	private boolean istRueckwaertsMoeglich;

	public ProtokollKursorInfo(MinMax eintrag, MinMax ebene, int kursorEintrag, int kursorEbene,
			boolean istVorwaertsMoeglich, boolean istRueckwaertsMoeglich) {
		super();
		this.eintrag = eintrag;
		this.ebene = ebene;
		this.kursorEintrag = kursorEintrag;
		this.kursorEbene = kursorEbene;
		this.istVorwaertsMoeglich = istVorwaertsMoeglich;
		this.istRueckwaertsMoeglich = istRueckwaertsMoeglich;
	}

	public MinMax gibEintrag() {
		return eintrag;
	}

	public MinMax gibEbene() {
		return ebene;
	}

	public int gibKursorEintrag() {
		return kursorEintrag;
	}

	public int gibKursorEbene() {
		return kursorEbene;
	}

	public boolean istVorwaertsMoeglich() {
		return istVorwaertsMoeglich;
	}

	public boolean istRueckwaertsMoeglich() {
		return istRueckwaertsMoeglich;
	}

}
