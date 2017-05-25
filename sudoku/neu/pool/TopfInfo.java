package sudoku.neu.pool;

public class TopfInfo {
	/**
	 * @return F�llstand des Topfes in % der max. m�glichen Anzahl Sudokus in
	 *         diesem Topfe
	 */
	static private int gibFuellstand(int maxAnzahl, int anzahl) {
		int fuellstand = 0;
		if (anzahl < 20) {
			fuellstand = anzahl / 2;
		} else {
			if (anzahl <= maxAnzahl) {
				fuellstand = (anzahl * 100) / maxAnzahl;
			}
		}
		return fuellstand;
	}

	/**
	 * Anzahl aller Sudokus
	 */
	private int anzahl;
	/**
	 * Anzahl der zweiten Sudokus mit gleicher Schwierigkeit
	 */
	private int anzahlDoppel;
	/**
	 * Gr��e auf Datetr�ger
	 */
	private long groesse;
	/**
	 * L�sungszeit
	 */
	private Integer leichtestes;
	/**
	 * L�sungszeit
	 */
	private Integer schwerstes;
	/**
	 * Vom File.lastModified
	 */
	private Long aeltestes;

	/**
	 * Vom File.lastModified
	 */
	private Long juengstes;

	/**
	 * 
	 */
	public TopfInfo() {
		super();
		this.anzahl = 0;
		this.anzahlDoppel = 0;
		this.groesse = 0;
		this.leichtestes = null;
		this.schwerstes = null;
		this.aeltestes = null;
		this.juengstes = null;
	}

	/**
	 * @param anzahl
	 * @param anzahlDoubel
	 * @param groesse
	 * @param leichtestes
	 * @param schwerstes
	 * @param aeltestes
	 * @param juengstes
	 */
	public TopfInfo(int anzahl, int anzahlDoppel, long groesse, Integer leichtestes, Integer schwerstes, Long aeltestes,
			Long juengstes) {
		super();
		this.anzahl = anzahl;
		this.anzahlDoppel = anzahlDoppel;
		this.groesse = groesse;
		this.leichtestes = leichtestes;
		this.schwerstes = schwerstes;
		this.aeltestes = aeltestes;
		this.juengstes = juengstes;
	}

	/**
	 * @param topf2
	 *            F�gt die Werte des Topf2 zu meinen dazu.
	 */
	public void add(TopfInfo topf2) {
		this.anzahl += topf2.anzahl;
		this.anzahlDoppel += topf2.anzahlDoppel;
		this.groesse += topf2.groesse;

		if (topf2.leichtestes != null) {
			if (leichtestes == null) {
				this.leichtestes = topf2.leichtestes;
			} else {
				if (topf2.leichtestes < leichtestes) {
					leichtestes = topf2.leichtestes;
				}
			}
		}
		if (topf2.schwerstes != null) {
			if (this.schwerstes == null) {
				this.schwerstes = topf2.schwerstes;
			} else {
				if (topf2.schwerstes > schwerstes) {
					schwerstes = topf2.schwerstes;
				}
			}
		}

		if (topf2.aeltestes != null) {
			if (this.aeltestes == null) {
				this.aeltestes = topf2.aeltestes;
			} else {
				if (topf2.aeltestes < aeltestes) {
					aeltestes = topf2.aeltestes;
				}
			}
		}

		if (topf2.juengstes != null) {
			if (this.juengstes == null) {
				this.juengstes = topf2.juengstes;
			} else {
				if (topf2.juengstes > juengstes) {
					juengstes = topf2.juengstes;
				}
			}
		}
	}

	public Long gibAeltestes() {
		return aeltestes;
	}

	public int gibAnzahl() {
		return anzahl;
	}

	public int gibAnzahlDoppel() {
		return anzahlDoppel;
	}

	/**
	 * @return F�llstand der Basis-Sudokus des Topfes in % der max. m�glichen
	 *         Anzahl Bais-Sudokus in diesem Topfe
	 */
	public int gibFuellstand1() {
		int fuellstand = 0;

		if ((schwerstes != null) & (leichtestes != null)) {
			int maxAnzahl = schwerstes - leichtestes + 1;
			int meineAnzahl = anzahl - anzahlDoppel;
			fuellstand = gibFuellstand(maxAnzahl, meineAnzahl);
		}
		return fuellstand;
	}

	/**
	 * @return F�llstand der Doppel-Sudokus des Topfes in % der max. m�glichen
	 *         Anzahl Basis-Sudokus in diesem Topfe
	 */
	public int gibFuellstand2() {
		int fuellstand = 0;

		if ((schwerstes != null) & (leichtestes != null)) {
			int maxAnzahl = schwerstes - leichtestes + 1;
			fuellstand = gibFuellstand(maxAnzahl, anzahlDoppel);
		}
		return fuellstand;
	}

	public long gibGroesse() {
		return groesse;
	}

	public Long gibJuengstes() {
		return juengstes;
	}

	public Integer gibLeichtestes() {
		return leichtestes;
	}

	public Integer gibSchwerstes() {
		return schwerstes;
	}
}
