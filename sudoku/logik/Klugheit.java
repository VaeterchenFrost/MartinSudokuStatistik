package sudoku.logik;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Diese Klasse realisiert die Klugheit eines Sudoku-L�sers
 * 
 * @author heroe
 */
public class Klugheit {
	/**
	 * meineLogiken beinhaltet f�r jede Logik, die laufen soll (die beherrscht
	 * wird), einen Eintrag.
	 */
	private Set<Logik_ID> meineLogiken;

	/**
	 * Optional schlaue Instanz mit allen Klugheiten oder ziemlich unschlaue
	 * Instanz mit minimaler Klugheit
	 */
	public Klugheit(boolean schlau) {
		super();
		meineLogiken = EnumSet.noneOf(Logik_ID.class);
		setzeExtrem(schlau);
	}

	/**
	 * Erstellt eine Instanz mit genau den Klugheiten von other
	 */
	public Klugheit(Klugheit other) {
		meineLogiken = EnumSet.noneOf(Logik_ID.class);

		Iterator<Logik_ID> iterator = other.meineLogiken.iterator();
		while (iterator.hasNext()) {
			Logik_ID logik = iterator.next();
			meineLogiken.add(logik);
		}
	}

	/**
	 * @param maxLogik
	 *            Die Klugheit bekommt alle Logiken einschlie�lich maxLogik,
	 *            falls dies null ist: alle.
	 */
	public Klugheit(Logik_ID maxLogik) {
		meineLogiken = EnumSet.noneOf(Logik_ID.class);

		Logik_ID[] logikArray = Logik_ID.values();
		int maxLogikOrdinal = logikArray.length - 1;
		if (maxLogik != null) {
			maxLogikOrdinal = maxLogik.ordinal();
		}

		for (int iLogik = 0; iLogik < logikArray.length; iLogik++) {
			Logik_ID logik = logikArray[iLogik];
			if (logik.ordinal() > maxLogikOrdinal) {
				break;
			}
			meineLogiken.add(logik);
		}
	}

	/**
	 * @return true wenn die Klugheit erh�ht werden konnte um eine weitere Logik
	 */
	public boolean erhoehe() {
		Logik_ID[] logikArray = Logik_ID.values();
		for (int i = 0; i < logikArray.length; i++) {
			Logik_ID logik = logikArray[i];
			if (!meineLogiken.contains(logik)) {
				// dann kann ich hier die Klugheit f�r diese Logik setzen
				meineLogiken.add(logik);
				return true;
			}
		}

		return false;
	}

	/**
	 * @return Die gr��te meiner Logiken, auch null bei kompletter Dummheit!
	 */
	public Logik_ID gibGroessteLogik() {
		Logik_ID logiken[] = Logik_ID.values();
		for (int i = logiken.length - 1; i >= 0; i--) {
			Logik_ID logik = logiken[i];
			if (meineLogiken.contains(logik)) {
				return logik;
			}
		}

		return null;
	}

	public String gibTextKurz() {
		return this.toString();
	}

	/**
	 * @return true wenn nur die leichteste Logik beherrscht wird
	 */
	public boolean istMinimum() {
		Logik_ID mini = Logik_ID.gibMinimum();

		boolean exsistiertMinimum = meineLogiken.contains(mini);
		boolean istMini = (meineLogiken.size() == 1) & exsistiertMinimum;
		return istMini;
	}

	/**
	 * @param klugheit
	 * @return true wenn ich die logik beherrsche (anwende).
	 */
	public boolean istSoKlug(Logik_ID logik) {
		boolean istSoKlug = meineLogiken.contains(logik);
		return istSoKlug;
	}

	/**
	 * Setzt die kleinste m�gliche Klugheit und die "andere" dazu
	 * 
	 * @param andere
	 */
	public void setze(Klugheit andere) {
		this.setzeExtrem(false);

		Iterator<Logik_ID> iterator = andere.meineLogiken.iterator();
		while (iterator.hasNext()) {
			Logik_ID logik = iterator.next();
			this.setzeLogik(logik, true);
		}
	}

	/**
	 * @param istMax
	 *            Bei true wird die maximale Klugheit eingestellt, ansonsten die
	 *            ziemlich (unschlaue) minimale Klugheit.
	 */
	public void setzeExtrem(boolean istMax) {
		meineLogiken.clear();

		Logik_ID[] logikArray = Logik_ID.values();

		if (!istMax) {
			// Minimum ist gefordert
			meineLogiken.add(logikArray[0]);
			return;
		}

		for (int iLogik = 0; iLogik < logikArray.length; iLogik++) {
			meineLogiken.add(logikArray[iLogik]);
		}
	}

	/**
	 * @param logik
	 *            Diese eine Logik wird gesetzt entsprechend "istSoKlug".
	 * @param istSoKlug
	 */
	public void setzeLogik(Logik_ID logik, boolean istSoKlug) {
		boolean exsistiert = meineLogiken.contains(logik);

		if (istSoKlug) {
			if (!exsistiert) {
				meineLogiken.add(logik);
			}
		} else {
			if (exsistiert) {
				meineLogiken.remove(logik);
			}
		}
	}

	@Override
	public String toString() {
		// Entweder "Minimum" oder "Maximum" oder die Klugheit-Kurznamen
		// getrennt durch Leerzeichen
		if (this.istMinimum()) {
			return SudokuLogik.gibNameExtrem(false);
		}

		String text = new String();
		boolean istMaximum = true;

		Logik_ID logiken[] = Logik_ID.values();
		for (int i = 0; i < logiken.length; i++) {
			Logik_ID logik = logiken[i];
			if (meineLogiken.contains(logik)) {
				text += " " + SudokuLogik.gibNameKurz(logik);
			} else {
				istMaximum = false;
			}

		}

		if (istMaximum) {
			text = SudokuLogik.gibNameExtrem(true);
		}
		return text;
	}
}
