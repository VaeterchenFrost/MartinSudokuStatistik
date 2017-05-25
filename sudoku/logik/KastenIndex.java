package sudoku.logik;

/**
 * @author heroe Spalte / Zeile 0, 1, 2
 */
public class KastenIndex {
	final int iSpalte;
	final int iZeile;

	public KastenIndex(int iSpalte, int iZeile) {
		this.iSpalte = iSpalte;
		this.iZeile = iZeile;
	}

	// private static boolean istIndexOK(int index){
	// return ( (index >= 1) && (index <= 9) );
	// }

	public int gibSpaltenIndex() {
		return iSpalte;
	}

	public int gibZeilenIndex() {
		return iZeile;
	}

	// public void kontrolliere() throws Exc{
	// if (! istIndexOK(this.spalte)){
	// throw Exc.unerlaubteZeile(this.spalte);
	// }
	// if (! istIndexOK(this.zeile)){
	// throw Exc.unerlaubteZeile(this.zeile);
	// }
	// }

	@Override
	public String toString() {
		return "[Z" + iZeile + ",S" + iSpalte + "]";
	}

	// public String gibBeschreibung() {
	// String s = String.format("[Zeile%d,Spalte%d]", zeile, spalte);
	// return s;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 10 * (iZeile) + iSpalte;
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
		KastenIndex other = (KastenIndex) obj;
		if (iSpalte != other.iSpalte) {
			return false;
		}
		if (iZeile != other.iZeile) {
			return false;
		}
		return true;
	}

	public boolean istOK() {
		if ((iSpalte < 0) | (iSpalte > 2)) {
			return false;
		}
		if ((iZeile < 0) | (iZeile > 2)) {
			return false;
		}
		return true;

	}
}
