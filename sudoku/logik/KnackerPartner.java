package sudoku.logik;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;

/**
 * @author heroe Es handelt sich entweder um ein Feld, das genau zwei m�gliche
 *         Zahlen beinhaltet. Oder um ein (Basis-) Feld mit EINER Zahl, in
 *         dessen Gruppe (Zeile, Spalte, Kasten) genau ein anderes Feld
 *         existiert mit dieser Zahl. In diesem Fall kann es zu dem Basisfeld
 *         mehrere Partner geben. Beispiel: Zahl 5 im Basis-Feld [Z5,S5]. Dazu
 *         die Zahl 5 als Partner in den Feldern [Z5,S1] (Zeile) und [Z1, S5]
 *         (Spalte) und [Z4,S4] (Kasten).
 */
public class KnackerPartner {
	/**
	 * @param partnerList
	 * @return true wenn das Feld neuer bereits als Alternative vermerkt ist in
	 *         partnerList
	 */
	public static boolean istAlternativeIn(ArrayList<KnackerPartner> partnerList, FeldNummerMitZahl neuer) {

		for (int iPartner = 0; iPartner < partnerList.size(); iPartner++) {

			KnackerPartner partner = partnerList.get(iPartner);
			ZahlenListe alternativen = partner.gibAlternativen();

			for (int iAlternativen = 0; iAlternativen < alternativen.size(); iAlternativen++) {

				FeldNummerMitZahl alternative = alternativen.get(iAlternativen);
				FeldNummer altFeldNummer = alternative.gibFeldNummer();
				FeldNummer neuFeldNummer = neuer.gibFeldNummer();

				if (altFeldNummer.equals(neuFeldNummer)) {
					return true;
				}
			}
		}
		return false;
	}

	private FeldNummerMitZahl basis;

	private ZahlenListe alternativen;

	/**
	 * @param feld
	 *            Ist ein Feld mit genau zwei m�glichen Zahlen. F�r dies Feld
	 *            wird eine Partner-Instanz erzeugt.
	 */
	public KnackerPartner(Feld feld) {
		ArrayList<Integer> moegliche = feld.gibMoegliche();

		basis = new FeldNummerMitZahl(feld.gibFeldNummer(), moegliche.get(0));

		alternativen = new ZahlenListe();
		FeldNummerMitZahl partner = new FeldNummerMitZahl(feld.gibFeldNummer(), moegliche.get(1));
		alternativen.add(partner);
	}

	/**
	 * @param zahl
	 * @param aBasis
	 *            Es handelt sich um ein Feld, f�r dessen m�gliche Zahl "zahl"
	 *            Partenerfelder existieren.
	 * @param aPartnerList
	 *            Diese Felder (einer Gruppe von aBasis) besitzen ebnfalls die
	 *            m�gliche Zahl "zahl".
	 */
	public KnackerPartner(int zahl, Feld aBasis, FeldNummerListe aPartnerList) {
		basis = new FeldNummerMitZahl(aBasis.gibFeldNummer(), zahl);

		alternativen = new ZahlenListe();
		for (int iPartner = 0; iPartner < aPartnerList.size(); iPartner++) {
			FeldNummerMitZahl partner = new FeldNummerMitZahl(aPartnerList.get(iPartner), zahl);
			alternativen.add(partner);

		}
	}

	public ZahlenListe gibAlternativen() {
		return alternativen;
	}

	public int gibAnzahlAlternativen() {
		return alternativen.size();
	}

	public FeldNummerMitZahl gibBasis() {
		return basis;
	}

	/**
	 * vertauscht basis mit alternativen. Wird nur angewendet bei nur einer
	 * Alternative
	 */
	public void tauschen() {
		if (alternativen.size() > 1) {
			return;
		}
		FeldNummerMitZahl alteBasis = basis;
		basis = alternativen.get(0);
		alternativen.set(0, alteBasis);
	}

	@Override
	public String toString() {
		String sBasisFeld = this.basis.toString();

		String sAlternativen = new String();
		for (int iAlternativen = 0; iAlternativen < alternativen.size(); iAlternativen++) {

			FeldNummerMitZahl alternative = alternativen.get(iAlternativen);
			sAlternativen += " ";
			sAlternativen += alternative;
		}

		String s = String.format("%s <=> %s) ", sBasisFeld, sAlternativen);

		return s;
	}
}
