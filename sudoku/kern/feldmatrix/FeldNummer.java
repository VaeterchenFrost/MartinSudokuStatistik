package sudoku.kern.feldmatrix;

public class FeldNummer implements Comparable<FeldNummer> {
	final public int spalte;
	final public int zeile;

	public FeldNummer(int aSpalte, int aZeile) {
		spalte = aSpalte;
		zeile = aZeile;
	}

	public int gibSpalte() {
		return spalte;
	}

	public int gibZeile() {
		return zeile;
	}

	public FeldNummer(FeldNummer feldNummer) {
		spalte = feldNummer.spalte;
		zeile = feldNummer.zeile;
	}

	@Override
	public String toString() {
		return "[Z" + zeile + ",S" + spalte + "]";
	}

	public String gibBeschreibung() {
		String s = String.format("[Zeile%d,Spalte%d]", zeile, spalte);
		return s;
	}

	@Override
	public int hashCode() {
		// final int prime = 31;
		// int result = 1;
		// result = prime * result + spalte;
		// result = prime * result + zeile;
		// return result;
		return 10 * zeile + spalte;
	}

	@Override
	public boolean equals(Object obj) {
		// Es gibt die Besonderheit der FeldNummern mit Zeile oder Spalte = 0:
		// Die "0" erzeugt Gleichheit: Sie zeigt an, dass dieser Wert
		// unbedeutend sein soll!
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FeldNummer other = (FeldNummer) obj;
		if ((spalte != 0) & (other.spalte != 0) & (spalte != other.spalte)) {
			return false;
		}
		if ((zeile != 0) & (other.zeile != 0) & (zeile != other.zeile)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(FeldNummer other) {
		if (this == other) {
			return 0;
		}
		if (other == null) {
			return 1;
		}
		if (getClass() != other.getClass()) {
			return 1;
		}
		if (this.zeile < other.zeile) {
			return -1;
		}
		if (this.zeile > other.zeile) {
			return 1;
		}
		if (this.spalte < other.spalte) {
			return -1;
		}
		if (this.spalte > other.spalte) {
			return 1;
		}
		return 0;
	}

}
