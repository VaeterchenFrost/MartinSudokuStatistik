package sudoku.varianz;

import sudoku.kern.info.FeldInfo;

/**
 * @author heroe
 * Nummer des Versuchs (auf dieser Ebene) und Feld (nach Start) 
 */
public class VersuchStart {

	public int gibVersuchNr() {
		return versuchNr;
	}

	public FeldInfo gibFeldInfo() {
		return feldInfo;
	}

	private int versuchNr;
	private FeldInfo feldInfo;
	
	/**
	 * @param versuchNr
	 * @param feldInfo
	 */
	public VersuchStart(int versuchNr, FeldInfo feldInfo) {
		super();
		this.versuchNr = versuchNr;
		this.feldInfo = feldInfo;
	}

	/* (non-Javadoc)
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
		VersuchStart other = (VersuchStart) obj;
		FeldInfo feldInfoOther = other.gibFeldInfo();
		if (this.feldInfo == feldInfoOther) {
			return true;
		}
		if (! this.feldInfo.gibFeldNummer().equals(feldInfoOther.gibFeldNummer())) {
			return false;
		}
		if (this.feldInfo.gibEintrag() != feldInfoOther.gibEintrag()) {
			return false;
		}
		return true;
	}

}
