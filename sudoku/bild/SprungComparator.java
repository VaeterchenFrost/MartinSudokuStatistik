package sudoku.bild;

import java.util.Comparator;

/**
 * @author heroe Vergleicht zwei Spr�nge miteinander. Die Vergleichsart ist
 *         vorgebbar.
 */
class SprungComparator implements Comparator<Sprung> {
	public enum Art {
		/**
		 * Vergleich nach Index
		 */
		INDEX,
		/**
		 * Vergleich nach der vorhandenen Sprungh�he
		 */
		HOEHE,
		/**
		 * Vergleich nach dem Absolutwert der Sprungh�he
		 */
		HOEHE_ABSOLUT
	};

	private Art art;

	public SprungComparator(Art art) {
		this.art = art;
	}

	@Override
	public int compare(Sprung o1, Sprung o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.getClass() != o2.getClass()) {
			return 1;
		}

		int o1Wert = 0;
		int o2Wert = 0;
		switch (art) {
		case INDEX:
			o1Wert = o1.gibVonIndex();
			o2Wert = o2.gibVonIndex();
			break;
		case HOEHE:
			o1Wert = o1.gibSprungHoehe();
			o2Wert = o2.gibSprungHoehe();
			break;
		case HOEHE_ABSOLUT:
			o1Wert = Math.abs(o1.gibSprungHoehe());
			o2Wert = Math.abs(o2.gibSprungHoehe());
			break;
		default:
			break;
		}

		if (o1Wert < o2Wert) {
			return -1;
		}
		if (o1Wert > o2Wert) {
			return 1;
		}
		return 0;
	}
}
