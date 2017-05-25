package sudoku.knacker.bericht;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.KnackerPartner;

public class KB_VersuchePaare extends KB_Eintrag1Zeile {
	private int anzahlPaare;
	private boolean istFelderMit2Moeglichen;
	private boolean istKontrollVersuchErlaubt;

	public KB_VersuchePaare(ArrayList<KnackerPartner> partnerListe, boolean istKontrollVersuchErlaubt) {
		this.anzahlPaare = partnerListe.size();
		this.istFelderMit2Moeglichen = false;
		if (this.anzahlPaare > 0) {
			KnackerPartner partner = partnerListe.get(0);
			FeldNummerMitZahl eintrag = partner.gibBasis();
			ZahlenListe alternativen = partner.gibAlternativen();
			this.istFelderMit2Moeglichen = eintrag.gibFeldNummer() == alternativen.get(0).gibFeldNummer();
		}
		this.istKontrollVersuchErlaubt = istKontrollVersuchErlaubt;
	}

	@Override
	public String toString() {
		return "VersuchePaare [anzahlPaare=" + anzahlPaare + ", felderMit2Moeglichen=" + istFelderMit2Moeglichen
				+ ", istKontrollVersuchErlaubt=" + istKontrollVersuchErlaubt + "]";
	}

	public boolean benutzteFeldPaare() {
		return !istFelderMit2Moeglichen;
	}

	public boolean benutzteKontrollVersuch() {
		return istKontrollVersuchErlaubt;
	}
}
