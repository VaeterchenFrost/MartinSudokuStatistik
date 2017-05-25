package sudoku.kern.feldmatrix;

import sudoku.kern.EintragsEbenen;

public class Eintrag {

	// Der Eintrag im sudoku oder 0
	private int zahl;
	// Siehe Klass EintragsEbenen
	private int ebene;
	// Wenn durch diesen Eintrag eine Ebene gestartet wurde = true
	private boolean istEbenenStart;
	// True wenn dieser Eintrag als TipZahl zustandegekommen ist
	private boolean istTipZahl;

	public Eintrag() {
		reset();
	}

	/**
	 * @param anderer
	 *            Von ihm wird eine Kopie erzeugt.
	 */
	public Eintrag(Eintrag anderer) {
		super();
		this.zahl = anderer.zahl;
		this.ebene = anderer.ebene;
		this.istEbenenStart = anderer.istEbenenStart;
		this.istTipZahl = anderer.istTipZahl;
	}

	public Eintrag(int zahl, int ebene, boolean istEbenenStart, boolean istTip) {
		this.zahl = zahl;
		this.ebene = ebene;
		this.istEbenenStart = istEbenenStart;
		this.istTipZahl = istTip;
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
		Eintrag other = (Eintrag) obj;
		if (ebene != other.ebene) {
			return false;
		}
		if (istEbenenStart != other.istEbenenStart) {
			return false;
		}
		if (istTipZahl != other.istTipZahl) {
			return false;
		}
		if (zahl != other.zahl) {
			return false;
		}
		return true;
	}

	public int gibEbene() {
		return ebene;
	}

	public int gibZahl() {
		return zahl;
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
		result = prime * result + ebene;
		result = prime * result + (istEbenenStart ? 1231 : 1237);
		result = prime * result + (istTipZahl ? 1231 : 1237);
		result = prime * result + zahl;
		return result;
	}

	public boolean istEbenenStart() {
		return istEbenenStart;
	}

	public boolean istEintrag() {
		return (zahl != 0);
	}

	public boolean istTipZahl() {
		return istTipZahl;
	}

	public boolean istVersuchsStart() {
		return istEbenenStart && (ebene > EintragsEbenen.gibStandardEbene1());
	}

	/**
	 * Die Ebeneninfo bleibt bestehen
	 */
	public void loescheEintrag() {
		zahl = 0;
		istEbenenStart = false;
	}

	public void markiereAlsTip() {
		istTipZahl = true;
	}

	public void reset() {
		zahl = 0;
		ebene = EintragsEbenen.gibStandardEbene1() - 1;
		istEbenenStart = false;
		istTipZahl = false;
	}

}
