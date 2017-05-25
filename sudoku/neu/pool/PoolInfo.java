package sudoku.neu.pool;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;

import sudoku.logik.Schwierigkeit;

public class PoolInfo {
	static public TopfInfo gibSumme(Collection<TopfInfo> toepfe) {
		TopfInfo summe = new TopfInfo();
		Iterator<TopfInfo> iterator = toepfe.iterator();

		while (iterator.hasNext()) {
			TopfInfo next = iterator.next();
			summe.add(next);
		}
		return summe;
	}

	// ====================================================
	public final EnumMap<Schwierigkeit, TopfInfo> verfuegbare;
	public final EnumMap<Schwierigkeit, TopfInfo> entnommene;

	/**
	 * @param verfuegbare
	 * @param entnommene
	 */
	PoolInfo(EnumMap<Schwierigkeit, TopfInfo> verfuegbare, EnumMap<Schwierigkeit, TopfInfo> entnommene) {
		super();
		this.verfuegbare = verfuegbare;
		this.entnommene = entnommene;
	}

	/**
	 * @return Summe über alles
	 */
	TopfInfo gibSumme() {
		TopfInfo summe = new TopfInfo();

		TopfInfo summeVerfuegbare = gibSumme(this.verfuegbare.values());
		summe.add(summeVerfuegbare);

		if (entnommene != null) {
			TopfInfo summeEntnommene = gibSumme(this.entnommene.values());
			summe.add(summeEntnommene);
		}

		return summe;
	}
}
