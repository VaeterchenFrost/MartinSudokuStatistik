package sudoku.neu.pool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sudoku.kern.exception.UnerwarteterNeuTyp;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.Schwierigkeit;
import sudoku.neu.NeuTyp;
import sudoku.neu.NeuTyp.Typ;

/**
 * @author Hendrick
 *
 */
class Pool implements Pool0 {
	private static boolean istSystemOut = false;

	/**
	 * @param angeforderterTyp
	 * @param topfTyp
	 *            oder null z.B. wenn kein Sudoku gegeben werden konnte
	 * @param istNeuesErstellt
	 *            false bedeutet: Es wurde versucht, ein Sudoku zu entnehmen
	 */
	private static void systemout(NeuTyp angeforderterTyp, NeuTyp topfTyp, boolean istNeuesErstellt) {
		if (!istSystemOut) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String uhrzeit = sdf.format(new Date());
		String sErstellt = istNeuesErstellt ? "erstellt" : "entnommen";
		String sTopfTyp = "null";
		if (topfTyp != null) {
			sTopfTyp = topfTyp.gibName();
		}
		String s = String.format("Pool.systemout(): %s %s angefordert=%s Topf=%s", uhrzeit, sErstellt,
				angeforderterTyp.gibName(), sTopfTyp);
		System.out.println(s);
	}
	// ---------------------------------------------------------------------------------------------

	// In der Startphase wird erstmal f�r jeden Typ ein Sudoku bereitgestellt
	private boolean istStartPhase;
	private ArrayList<Topf> pool;

	Pool() {
		this.istStartPhase = true;
		this.pool = new ArrayList<>();
		ArrayList<NeuTyp> alleTypen = this.gibErstellungsTypen();
		for (NeuTyp neuTyp : alleTypen) {
			Topf topf = new Topf(neuTyp);
			pool.add(topf);
		}
	}

	/**
	 * @return alle zu erstellenden Typen
	 */
	private ArrayList<NeuTyp> gibErstellungsTypen() {
		ArrayList<NeuTyp> neuTypen = new ArrayList<>();

		neuTypen.add(new NeuTyp(Typ.VOLL));
		for (Schwierigkeit wieSchwer : Schwierigkeit.values()) {
			neuTypen.add(new NeuTyp(wieSchwer));
		}
		return neuTypen;
	}

	public NeuTyp gibForderung() {
		synchronized (pool) {
			for (int i = 0; i < pool.size(); i++) {
				if (i == (pool.size() - 1)) {
					istStartPhase = false;
				}
				Topf topf = pool.get(i);
				int nVoll = 1;
				if (!istStartPhase) {
					nVoll = topf.gibSollAnzahl();
				}
				int anzahl = topf.gibAnzahl();

				if (anzahl < nVoll) {
					return topf.gibNeuTyp();
				}
			}
		}
		return null;
	}

	@Override
	public PoolInfo gibPoolInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sudoku.schwer.neu.pool.Pool0#gibSudoku(sudoku.schwer.neu.NeuTyp,
	 * sudoku.schwer.neu.pool.NeuTypOption)
	 */
	public InfoSudoku gibSudoku(NeuTyp neuTyp, NeuTypOption option) {
		synchronized (pool) {
			for (Topf topf : pool) {
				if (neuTyp.equals(topf.gibNeuTyp())) {
					systemout(neuTyp, topf.gibNeuTyp(), false);
					return topf.gibSudoku();
				}
			}
		}
		systemout(neuTyp, null, false);
		return null;
	}

	@Override
	public String gibTopfName(NeuTyp neuTyp) {
		return null;
	}

	public Boolean setze(NeuTyp neuTyp, InfoSudoku sudoku, int loesungsZeit) {
		// Fehler:
		if (neuTyp == null) {
			throw new UnerwarteterNeuTyp("null");
		}
		if (sudoku == null) {
			throw new NullPointerException("sudoku = null");
		}
		synchronized (pool) {
			for (Topf topf : pool) {
				if (neuTyp.equals(topf.gibNeuTyp())) {
					topf.setze(sudoku);
					systemout(neuTyp, topf.gibNeuTyp(), true);
					return new Boolean(true);
				}
			}
		}
		throw new UnerwarteterNeuTyp(neuTyp.gibName());
	}

}
