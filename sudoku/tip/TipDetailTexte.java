package sudoku.tip;

import sudoku.logik.tipinfo.EinTipText;

public class TipDetailTexte {
	public String ueberschrift;
	public EinTipText[] tipTexte;

	public TipDetailTexte(String ueberschrift, EinTipText[] tipTexte) {
		this.ueberschrift = ueberschrift;
		this.tipTexte = tipTexte;
	}

	/**
	 * @return Alle Texte m�glichst schmal als ein String mit Zeilenvorsch�ben
	 *         zwischen ihnen.
	 */
	public String gibTexteSchmal() {
		String text = new String();
		String einschub = "     ";
		text += ueberschrift;
		for (int i = 0; i < tipTexte.length; i++) {
			EinTipText tipTexte1 = tipTexte[i];
			text += "\n";
			text += tipTexte1.s1;
			if (tipTexte1.s2 != null) {
				text += "\n";
				text += einschub;
				text += tipTexte1.s2;
			}
		}
		return text;
	}

	/**
	 * @return Alle Texte als m�glichst wenige Zeilen als ein String mit
	 *         Zeilenvorsch�ben zwischen ihnen.
	 */
	public String gibTexteInWenigenZeilen() {
		String text = new String();
		String einschub = "     ";
		text += ueberschrift;
		for (int i = 0; i < tipTexte.length; i++) {
			EinTipText tipTexte1 = tipTexte[i];
			text += "\n";
			text += einschub;
			text += tipTexte1.s1;
			if (tipTexte1.s2 != null) {
				text += " ";
				text += tipTexte1.s2;
			}
		}
		return text;
	}

	public int gibTexteAnzahl() {
		return tipTexte.length + 1; // �berschrift
	}
}
