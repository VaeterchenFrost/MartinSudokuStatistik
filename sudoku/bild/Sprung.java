package sudoku.bild;

/**
 * @author heroe Ein Sprung ab Index zu (Index+1) der H�he X. Der Vergleich
 *         zweier Instanzen erfolgt auf abs(sprung).
 */
class Sprung {
	public int gibVonIndex() {
		return vonIndex;
	}

	public int gibSprungHoehe() {
		return sprung;
	}

	@Override
	public String toString() {
		return vonIndex + ":" + sprung;
	}

	/**
	 * Index ab dem der Sprung beginnt zu (Index+1)
	 */
	private int vonIndex;
	/**
	 * H�he des Sprunges als Color-Int
	 */
	private int sprung;

	/**
	 * @param vonIndex
	 * @param sprung
	 */
	Sprung(int vonIndex, int sprung) {
		super();
		this.vonIndex = vonIndex;
		this.sprung = sprung;
	}

	public void transformiereIndex(int neuerUrsprung) {
		this.vonIndex -= neuerUrsprung;
	}
}
