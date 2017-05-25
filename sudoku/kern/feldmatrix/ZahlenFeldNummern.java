package sudoku.kern.feldmatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author heroe Welche (m�gliche) Zahl ist in welchen Feldern zu finden,
 *         dadurch nat�rlich auch: wie oft. Dies ist die Auflistung der Felder
 *         je m�gliche Zahl einer Gruppe. Das ist die andere Variante zu den
 *         Feld-Klassen: Dort handelt es sich um die Auflistung der m�glichen
 *         Zahlen je Feld.
 */
public class ZahlenFeldNummern {
	private Map<Integer, FeldNummerListe> map;

	public ZahlenFeldNummern() {
		map = new HashMap<Integer, FeldNummerListe>();
	}

	/**
	 * @param src
	 *            Hieraus wird eine Kopie erstellt
	 */
	public ZahlenFeldNummern(ZahlenFeldNummern src) {
		map = new HashMap<Integer, FeldNummerListe>();

		Set<Entry<Integer, FeldNummerListe>> eintraege = src.map.entrySet();

		for (Entry<Integer, FeldNummerListe> eintrag : eintraege) {
			Integer zahl = new Integer(eintrag.getKey());
			FeldNummerListe feldNummerListe = new FeldNummerListe(eintrag.getValue());
			map.put(zahl, feldNummerListe);
		}
	}

	/**
	 * F�gt einen neuen Eintrag hinzu.
	 * 
	 * @param zahl
	 * @param feldNummer
	 */
	public void add(int zahl, FeldNummer feldNummer) {
		FeldNummerListe feldNummerListe = map.get(zahl);
		if (feldNummerListe == null) {
			feldNummerListe = new FeldNummerListe();
		}
		feldNummerListe.add(feldNummer);
		map.put(zahl, feldNummerListe);
	}

	public void addNurNeue(int zahl, FeldNummer feldNummer) {
		if (!istVorhanden(zahl, feldNummer)) {
			add(zahl, feldNummer);
		}
	}

	/**
	 * @return Die Zahlen. Auch null wenn f�r keine Zahl FeldNummern vermerkt
	 *         sind.
	 */
	public int[] gibZahlen() {
		Set<Integer> keySet = map.keySet();
		if (keySet.size() == 0) {
			return null;
		}
		Integer[] intArray = new Integer[keySet.size()];
		keySet.toArray(intArray);
		int[] zahlen = new int[keySet.size()];
		for (int i = 0; i < intArray.length; i++) {
			zahlen[i] = intArray[i];
		}
		return zahlen;
	}

	/**
	 * @param zahl
	 * @return Auch null wenn f�r zahl keine FeldNummern vermerkt sind.
	 */
	public FeldNummerListe gibFeldNummern(int zahl) {
		FeldNummerListe feldNummerListe = map.get(zahl);
		return feldNummerListe;
	}

	/**
	 * @param zahl
	 * @return Die Anzahl der FeldNummern, die f�r diese Zahl existieren.
	 */
	public int gibFeldNummernAnzahl(int zahl) {
		int anzahl = 0;
		FeldNummerListe feldNummern = gibFeldNummern(zahl);
		if (feldNummern != null) {
			anzahl = feldNummern.size();
		}
		return anzahl;
	}

	public boolean istVorhanden(int zahl) {
		FeldNummerListe feldNummerListe = map.get(zahl);
		boolean vorhanden = feldNummerListe != null;
		if (vorhanden) {
			vorhanden = !feldNummerListe.isEmpty();
		}
		return vorhanden;
	}

	public boolean istVorhanden(int zahl, FeldNummer feldNummer) {
		if (!istVorhanden(zahl)) {
			return false;
		}
		FeldNummerListe feldNummerListe = gibFeldNummern(zahl);
		if (feldNummerListe.contains(feldNummer)) {
			return true;
		}
		return false;
	}

	public int gibAnzahlVorhandene() {
		return map.size();
	}

	/**
	 * @return Erste vorhandene Zahl oder 0 wenn es keine gibt
	 */
	public int gibErsteVorhandene() {
		for (int zahl = 0; zahl < 10; zahl++) {
			if (istVorhanden(zahl)) {
				return zahl;
			}
		}
		return 0;
	}

	/**
	 * @return Die vorhandenen Zahlen aus 1 bis 9
	 */
	public String toStringVorhandeneZahlen() {
		String s = new String();

		// F�r jede nicht vorhandene Zahl schreiben
		for (int zahl = 1; zahl < 10; zahl++) {
			if (istVorhanden(zahl)) {
				s += " " + zahl;
			}
		}

		return s;
	}

	/**
	 * @return Die nicht vorhandenen Zahlen aus 1 bis 9
	 */
	public String toStringNichtVorhandeneZahlen() {
		String s = new String();

		// F�r jede nicht vorhandene Zahl schreiben
		for (int zahl = 1; zahl < 10; zahl++) {
			if (!istVorhanden(zahl)) {
				s += " " + zahl;
			}
		}

		return s;
	}

	@Override
	public String toString() {
		String s = " ";
		for (int zahl = 1; zahl <= FeldMatrix.feldNummerMax; zahl++) {
			if (istVorhanden(zahl)) {
				FeldNummerListe feldNummern = gibFeldNummern(zahl);
				String s1 = String.format("(%d->%s)", zahl, feldNummern.gibKette("+"));
				if (s.length() > 1) {
					s += " + ";
				}
				s += s1;
			}
		}

		return s;
	}

	public void systemOut() {
		for (int zahl = 1; zahl <= FeldMatrix.feldNummerMax; zahl++) {
			if (istVorhanden(zahl)) {
				FeldNummerListe feldNummern = gibFeldNummern(zahl);
				String s = String.format(" Die %d nach %s", zahl, feldNummern.gibKette("+"));
				System.out.println(s);
			}
		}
	}
}
