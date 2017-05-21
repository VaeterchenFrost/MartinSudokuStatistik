package sudoku.bild;

import java.util.Comparator;

public class WerteGruppeComparator implements Comparator<WerteGruppe> {

	public enum VergleichsArt {
		DURCHSCHNITT, WERTEANZAHL,
	};

	private VergleichsArt vergleichsArt;
	private boolean istInvers;

	WerteGruppeComparator(VergleichsArt vergleichsArt, boolean istInvers) {
		this.vergleichsArt = vergleichsArt;
		this.istInvers = istInvers;
	}

	@Override
	public int compare(WerteGruppe o1, WerteGruppe o2) {
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
		switch (vergleichsArt) {
		case DURCHSCHNITT:
			o1Wert = o1.gibDurchschnitt();
			o2Wert = o2.gibDurchschnitt();
			break;
		case WERTEANZAHL:
			o1Wert = o1.gibWerteAnzahl();
			o2Wert = o2.gibWerteAnzahl();
			break;
		default:
			break;
		}

		int ergebnis = 0;
		if (o1Wert < o2Wert) {
			ergebnis = -1;
		}
		if (o1Wert > o2Wert) {
			ergebnis = 1;
		}

		if (istInvers) {
			ergebnis *= -1;
		}
		return ergebnis;
	}

}
