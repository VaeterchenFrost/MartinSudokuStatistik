package sudoku.bild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sudoku.ArrayListInt;
import sudoku.bild.WerteGruppeComparator.VergleichsArt;

/**
 * @author heroe Eine Gruppe von �hnlichen integer-Werten. Ihre �hnlichkeit wird
 *         anhand eines tolerierten Wertebereiches festgemacht. Ihr
 *         Durchschnittswert +/- Toleranz ergibt den Wertebereich, den diese
 *         Gruppe aufnimmt.
 */
public class WerteGruppe {

	/**
	 * @param werte
	 *            Zu gruppierende Werte
	 * @param toleranz
	 *            in Prozent des Durchschnitts der Werte einer Gruppe. "Gleiche"
	 *            (genau: �hnliche) Werte werden anhand dieser Toleranz
	 *            gruppiert.
	 * @return Der am h�ufigsten auftretende Wert (dessen Durchschnitt) oder
	 *         null bei leerem Array werte.
	 */
	static public WerteGruppe gibHaeufigstenWert(int[] werte, int toleranz) {
		boolean istInvers = true;
		WerteGruppeComparator comparator = new WerteGruppeComparator(VergleichsArt.WERTEANZAHL, istInvers);
		List<WerteGruppe> werteGruppen = gibGruppen(werte, toleranz, comparator);

		WerteGruppe besteGruppe = null;
		if (!werteGruppen.isEmpty()) {
			besteGruppe = werteGruppen.get(0);
		}
		return besteGruppe;
	}

	/**
	 * @param werte
	 *            Zu gruppierende Werte
	 * @param toleranz
	 *            in Prozent des Durchschnitts der Werte einer Gruppe. "Gleiche"
	 *            (genau: �hnliche) Werte werden anhand dieser Toleranz
	 *            gruppiert.
	 * @return Der am h�ufigsten auftretende Wert (dessen Durchschnitt) oder
	 *         null bei leerem Array werte.
	 */
	static public WerteGruppe gibGroesstenWert(int[] werte, int toleranz) {
		boolean istInvers = true;
		WerteGruppeComparator comparator = new WerteGruppeComparator(VergleichsArt.DURCHSCHNITT, istInvers);
		List<WerteGruppe> werteGruppen = gibGruppen(werte, toleranz, comparator);

		WerteGruppe besteGruppe = null;
		if (!werteGruppen.isEmpty()) {
			besteGruppe = werteGruppen.get(0);
		}
		return besteGruppe;
	}

	/**
	 * @param werte
	 * @param toleranz
	 *            in Prozent des Durchschnitts der Werte einer Gruppe.
	 * @param comparator
	 *            Falls != null ist der R�ckgabewert sortiert
	 * @return Die in Gruppen einsortierten werte
	 */
	static public List<WerteGruppe> gibGruppen(int[] werte, int toleranz, WerteGruppeComparator comparator) {
		List<WerteGruppe> werteGruppen = new ArrayList<WerteGruppe>();

		for (int i = 0; i < werte.length; i++) {
			int dieserWert = werte[i];

			WerteGruppe dieseGruppe = null;
			for (WerteGruppe werteGruppe : werteGruppen) {
				if (werteGruppe.istAufnehmbar(dieserWert)) {
					dieseGruppe = werteGruppe;
					break;
				}
			}
			if (dieseGruppe == null) {
				WerteGruppe neueGruppe = new WerteGruppe(dieserWert, toleranz);
				werteGruppen.add(neueGruppe);
			} else {
				dieseGruppe.add(dieserWert);
			}
		}

		if (comparator != null) {
			Collections.sort(werteGruppen, comparator);
		}
		return werteGruppen;
	}

	// ======================================================
	private ArrayListInt werte;
	private int toleranzProzent;

	/**
	 * @param wert
	 *            Erster Wert dieser Gruppe
	 * @param toleranz
	 *            in Prozent des Durchschnitts der Werte einer Gruppe. Der
	 *            tolerierte Wertebereich der Gruppe geht von Durchschnitt -
	 *            toleranz bis Durchschnitt + toleranz
	 */
	public WerteGruppe(int wert, int toleranz) {
		this.werte = new ArrayListInt();
		this.werte.add(new Integer(wert));
		this.toleranzProzent = toleranz;
	}

	public int gibDurchschnitt() {
		int durchschnitt = 0;
		for (Integer wert : werte) {
			durchschnitt += wert;
		}
		durchschnitt = durchschnitt / werte.size();
		return durchschnitt;
	}

	/**
	 * @return Der kleinste in dieser Gruppe vorkommende Wert
	 */
	public int gibMinimum() {
		int min = werte.get(0);
		for (Integer wert : werte) {
			if (wert < min) {
				min = wert;
			}
		}
		return min;
	}

	/**
	 * @return Der gr��te in dieser Gruppe vorkommende Wert
	 */
	public int gibMaximum() {
		int max = werte.get(0);
		for (Integer wert : werte) {
			if (wert > max) {
				max = wert;
			}
		}
		return max;
	}

	/**
	 * @return Der Minimalwert, der f�r diese Gruppe erlaubt ist
	 */
	public int gibMinErlaubt() {
		int durchschnitt = gibDurchschnitt();
		int toleranz = (durchschnitt * toleranzProzent) / 100;
		int wertMin = durchschnitt - toleranz;
		return wertMin;
	}

	public int gibMaxErlaubt() {
		int durchschnitt = gibDurchschnitt();
		int toleranz = (durchschnitt * toleranzProzent) / 100;
		int wertMax = durchschnitt + toleranz;
		return wertMax;
	}

	private boolean istAufnehmbar(int wert) {
		int durchschnitt = gibDurchschnitt();
		int toleranz = (durchschnitt * toleranzProzent) / 100;
		int wertMin = durchschnitt - toleranz;
		int wertMax = durchschnitt + toleranz;
		boolean ok = wert >= wertMin;
		if (ok) {
			ok = wert <= wertMax;
		}
		return ok;
	}

	/**
	 * @param wert
	 * @return true wenn der Wert in die Gruppe aufgenommen wurde.
	 */
	private boolean add(int wert) {
		boolean aufnehmbar = istAufnehmbar(wert);
		if (aufnehmbar) {
			werte.add(new Integer(wert));
		}
		return aufnehmbar;
	}

	public int gibWerteAnzahl() {
		return this.werte.size();
	}

	@Override
	public String toString() {
		// return "WerteGruppe [werte=" + werte + ", toleranzProzent=" +
		// toleranzProzent + "]";
		String s = String.format("Wertegruppe erlaubt von %d bis %d: %d Werte mit Durchschnitt=%d (von %d bis %d)",
				gibMinErlaubt(), gibMaxErlaubt(), gibWerteAnzahl(), gibDurchschnitt(), gibMinimum(), gibMaximum());
		return s;
	}

}
