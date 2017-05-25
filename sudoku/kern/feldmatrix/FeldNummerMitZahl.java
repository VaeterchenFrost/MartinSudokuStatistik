package sudoku.kern.feldmatrix;

public class FeldNummerMitZahl implements Comparable<FeldNummerMitZahl> {
	private FeldNummer feldNummer;
	private int zahl;

	public FeldNummerMitZahl(FeldNummer aFeldNummer, int aZahl) {
		feldNummer = aFeldNummer;
		zahl = aZahl;
	}

	/**
	 * @param aZahl
	 *            erstellt intern Kopien
	 */
	public FeldNummerMitZahl(FeldNummerMitZahl aZahl) {
		this.feldNummer = new FeldNummer(aZahl.feldNummer.spalte, aZahl.feldNummer.zeile);
		zahl = aZahl.zahl;
	}

	public FeldNummer gibFeldNummer() {
		return feldNummer;
	}

	public int gibZahl() {
		return zahl;
	}

	@Override
	public String toString() {
		String s = String.format("%d in %s", this.zahl, this.feldNummer);
		return s;
	}

	public String gibBeschreibung() {
		String s = String.format("Zahl %d in Feld %s", this.zahl, this.feldNummer.gibBeschreibung());
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((feldNummer == null) ? 0 : feldNummer.hashCode());
		result = prime * result + zahl;
		return result;
	}

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
		FeldNummerMitZahl other = (FeldNummerMitZahl) obj;
		if (feldNummer == null) {
			if (other.feldNummer != null) {
				return false;
			}
		} else if (!feldNummer.equals(other.feldNummer)) {
			return false;
		}
		if (zahl != other.zahl) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(FeldNummerMitZahl other) {
		if (this == other) {
			return 0;
		}
		if (other == null) {
			return 1;
		}
		if (getClass() != other.getClass()) {
			return 1;
		}
		int feldNummernErgebnis = this.feldNummer.compareTo(other.feldNummer);
		if (feldNummernErgebnis != 0) {
			return feldNummernErgebnis;
		}
		if (this.zahl < other.zahl) {
			return -1;
		}
		if (this.zahl > other.zahl) {
			return 1;
		}
		return 0;
	}

}
