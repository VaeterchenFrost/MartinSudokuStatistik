package sudoku.neu;

import java.util.ArrayList;

import sudoku.logik.Schwierigkeit;

/**
 * @author Hendrick Der Typ eines neuen angeforderten Sudoku
 */
public class NeuTyp {
	/**
	 * Der Typ eines angeforderten neuen Sudoku
	 */
	public enum Typ {
		LEER, VOLL, VORLAGE, SCHWER
	};

	/**
	 * @return alle verf�gbaren Typen in der Reihenfolge LEER, VOLL, VORLAGE,
	 *         SCHWER
	 */
	public static ArrayList<NeuTyp> gibAlleTypen() {
		ArrayList<NeuTyp> neuTypen = new ArrayList<>();
		for (NeuTyp.Typ neuTyp : NeuTyp.Typ.values()) {
			switch (neuTyp) {
			case LEER:
				neuTypen.add(new NeuTyp(Typ.LEER));
				break;
			case VOLL:
				neuTypen.add(new NeuTyp(Typ.VOLL));
				break;
			case VORLAGE:
				neuTypen.add(new NeuTyp(Typ.VORLAGE));
				break;
			case SCHWER:
				for (Schwierigkeit typ : Schwierigkeit.values()) {
					neuTypen.add(new NeuTyp(typ));
				}
				break;
			default:
				;
			}
		}
		return neuTypen;
	}

	private Typ typ;
	private Schwierigkeit wieSchwer;

	/**
	 * @param typ
	 *            typisch SCHWER
	 * @param wieSchwer
	 */
	public NeuTyp(Schwierigkeit wieSchwer) {
		super();
		this.typ = NeuTyp.Typ.SCHWER;
		this.wieSchwer = wieSchwer;
	}

	/**
	 * @param typ
	 *            LEER oder VOLL
	 */
	public NeuTyp(Typ typ) {
		super();
		this.typ = typ;
		this.wieSchwer = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NeuTyp other = (NeuTyp) obj;
		if (this.typ != other.typ) {
			return false;
		}
		if (wieSchwer != other.wieSchwer) {
			return false;
		}
		return true;
	}

	public String gibName() {
		switch (typ) {
		case LEER:
			return "Leer";
		case VOLL:
			return "Voll";
		case VORLAGE:
			return "Nach Vorlage";
		case SCHWER:
			return Schwierigkeit.gibName(this.wieSchwer);
		default:
			return "???";
		}
	}

	public Typ gibTyp() {
		return this.typ;
	}

	public Schwierigkeit gibWieSchwer() {
		return this.wieSchwer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typ == null) ? 0 : typ.hashCode());
		result = prime * result + ((wieSchwer == null) ? 0 : wieSchwer.hashCode());
		return result;
	}

	@Override
	public String toString() {
		if (wieSchwer == null) {
			return " NeuTyp=" + typ;
		} else {
			return " NeuTyp=" + typ + ": " + wieSchwer;
		}
	}

}
