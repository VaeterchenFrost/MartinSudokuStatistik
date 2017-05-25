package sudoku.varianz;

import java.util.ArrayList;

import sudoku.kern.info.FeldInfo;

public class VersuchStarts {

	private ArrayList<VersuchStart> starts;

	public VersuchStarts() {
		starts = new ArrayList<VersuchStart>();
	}

	public VersuchStarts(VersuchStarts erfolgteStarts, VersuchStart neuerStart) {
		starts = new ArrayList<VersuchStart>();
		starts.addAll(erfolgteStarts.starts);
		starts.add(neuerStart);
	}

	public int gibAnzahl() {
		return this.starts.size();
	}

	public String gibText() {
		String txt = new String();
		String sVersuche = new String();

		if (!starts.isEmpty()) {
			for (int i = 0; i < starts.size(); i++) {
				if (i > 0) {
					txt += ".";
				}
				txt += starts.get(i).gibVersuchNr();

				VersuchStart versuchStart = starts.get(i);
				FeldInfo versuch = versuchStart.gibFeldInfo();
				sVersuche += String.format(" %s =%d", versuch.gibFeldNummer().toString(), versuch.gibEintrag());
			} // for
		} // if

		String sReturn = String.format("%s: %s", sVersuche, txt);
		return sReturn;
	}

	/**
	 * @param anderer
	 *            Ich soll mich mit ihm vergleichen: FeldNummer und Eintrag
	 *            identisch
	 * @param anzahl
	 *            Der Vergleich soll "nur" �ber diese Anzahl Versuchstarts
	 *            laufen
	 * @return true wenn ich auf den ersten anzahl Pl�tzen zu versuchStart
	 *         identisch bin.
	 */
	public boolean istGleicherEintrag(VersuchStarts anderer, int anzahl) {
		if (anderer == null) {
			return false;
		}
		if (anzahl > starts.size()) {
			return false;
		}
		for (int i = 0; i < anzahl; i++) {
			VersuchStart meinStart = starts.get(i);
			VersuchStart start2 = anderer.starts.get(i);

			if (!meinStart.equals(start2)) {
				return false;
			}
		}
		return true;
	}
}
