package sudoku.kern.info;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.ZahlenFeldNummern;

/**
 * Ein anderes Feld einer Gruppen ist mit einem Basis-Feld ein Feld-Paar, wenn
 * es in der Gruppe eine m�gliche Zahl genau zweimal gibt: Das eine Exemplar
 * dieser m�glichen Zahl besitzt das Basis-Feld, das andere das andere Feld. In
 * der Sudokul�sung MUSS das Basis-Feld oder das andere diese Zahl als Eintrag
 * besitzen! Die Feld-Paare sind der letzte Strohhalm des Knacker an den er sich
 * klammert, um doch noch zu einer L�sung des Sudoku zu kommen, nachdem er
 * bereits alle Felder mit genau zwei m�glichen Zahlen durchprobiert hat.
 * feldPaare != null sagt: Zu der m�glichen Zahl X bildet das Basis-Feld mit
 * folgenden Feldern (FeldNummern) ein FeldPaar.
 */
public class FeldPartnerTexte {
	/**
	 * @param basisFeldNummer
	 * @return Die Liste der Strings, einer f�r jeden Partner
	 */
	static public String[] gibPaareTexte(FeldNummer basisFeldNummer, ZahlenFeldNummern partner) {
		ArrayList<String> texte = new ArrayList<String>();
		int[] zahlen = partner.gibZahlen();
		if (zahlen != null) {
			for (int iZahl = 0; iZahl < zahlen.length; iZahl++) {
				int zahl = zahlen[iZahl];
				FeldNummerListe andereFelder = partner.gibFeldNummern(zahl);
				if (andereFelder != null) {
					String sZahlList = String.format("Mit Zahl %d bin ich Feld-Paar ", zahl);
					// sZahlList += "->";
					for (int i = 0; i < andereFelder.size(); i++) {
						FeldNummer andereFeldNummer = andereFelder.get(i);
						String sFeldNummer = gibPartnerString(basisFeldNummer, andereFeldNummer);
						if (i > 0) {
							sZahlList += " und ";
						}
						sZahlList += sFeldNummer;
					}
					texte.add(sZahlList);
				}
			} // for
		}
		String[] texteArray = texte.toArray(new String[texte.size()]);
		return texteArray;
	}

	/**
	 * @param basisFeldNummer
	 * @param partner
	 * @return Text, der die Lage des partners bezogen auf das basisFeld
	 *         benennt. Bemerkenswert ist vielleicht, dass es sich hier "nur" um
	 *         den Lage-Bezug handelt und nicht etwa um die Zuordnung zu einer
	 *         Gruppe! Es kann also z.B. sein, dass ein Partner auf Basis eines
	 *         Kastens mit dem BasisFeld auf einer Zeile liegt.
	 */
	static private String gibPartnerString(FeldNummer basisFeldNummer, FeldNummer partner) {
		if (basisFeldNummer.zeile == partner.gibZeile()) {
			String s = String.format("in dieser Zeile mit %s S%d ",
					gibSpaltenBezug(basisFeldNummer.spalte, partner.gibSpalte()), partner.gibSpalte());
			return s;
		}

		if (basisFeldNummer.spalte == partner.gibSpalte()) {
			String s = String.format("in dieser Spalte mit %s Z%d ",
					gibZeilenBezug(basisFeldNummer.zeile, partner.gibZeile()), partner.gibZeile());
			return s;
		}

		String s = String.format("hier im Kasten mit %s %s %s ",
				gibSpaltenBezug(basisFeldNummer.spalte, partner.gibSpalte()),
				gibZeilenBezug(basisFeldNummer.zeile, partner.gibZeile()), partner);
		return s;
	}

	static private String gibSpaltenBezug(int basisSpalte, int partnerSpalte) {
		if (basisSpalte < partnerSpalte) {
			return "rechts";
		}
		return "links";
	}

	static private String gibZeilenBezug(int basisZeile, int partnerZeile) {
		if (basisZeile < partnerZeile) {
			return "unten";
		}
		return "oben";
	}

}
