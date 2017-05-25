package sudoku.kern.info;

import java.util.ArrayList;

import sudoku.kern.exception.FehlendeMoeglicheZahl;
import sudoku.kern.feldmatrix.Eintrag;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenFeldNummern;

// Ist gedacht zum Geben der Informationen zu einem Feld nach au�en
/**
 * @author Hendrick
 *
 */
public class FeldInfo {

	public static FeldInfo gibVorgabeInstanz(FeldNummer feldNummer, int vorgabe) {
		return new FeldInfo(feldNummer, vorgabe);
	}

	private FeldNummer feldNummer;
	// 0 == keine
	private int vorgabe;
	// die durch reset gesetzte Vorgabe
	private Integer resetVorgabe;
	// null == keiner
	private Eintrag eintrag;
	private ArrayList<MoeglicheZahl> moegliche;
	// null=keine, false=passiv, true=aktiv
	private Boolean markierung;

	private ZahlenFeldNummern feldPartner;

	public FeldInfo(Feld feld) {
		kopiereInhalt(feld, true); // mitFeldPaaren);
	}

	public FeldInfo(Feld feld, boolean mitFeldPaaren) {
		kopiereInhalt(feld, mitFeldPaaren);
	}

	private FeldInfo(FeldNummer feldNummer, int vorgabe) {
		super();
		this.feldNummer = feldNummer;
		this.vorgabe = vorgabe;
		this.resetVorgabe = vorgabe;
		this.eintrag = null;
		moegliche = null; // new ArrayList<Integer>();
		markierung = null;
		feldPartner = null;
	}

	private void kopiereInhalt(Feld feld, boolean mitFeldPaaren) {
		feldNummer = new FeldNummer(feld.gibFeldNummer());
		vorgabe = feld.gibVorgabe();
		this.resetVorgabe = feld.gibResetVorgabe();
		eintrag = null;

		int feldEintrag = feld.gibEintrag();
		if (feldEintrag > 0) {
			eintrag = new Eintrag(feldEintrag, feld.gibEintragEbene(), feld.istEintragEbenenStart(),
					feld.istEintragAlsTip());
		}

		moegliche = new ArrayList<MoeglicheZahl>();
		ArrayList<Integer> zahlen = feld.gibMoegliche();
		if (zahlen != null) {
			for (Integer zahl : zahlen) {
				moegliche.add(new MoeglicheZahl(zahl.intValue()));
			}
		}

		feldPartner = null;
		if (mitFeldPaaren) {
			ZahlenFeldNummern originalPaare = feld.gibFeldPartner();
			if (originalPaare != null) {
				feldPartner = new ZahlenFeldNummern(originalPaare);
			}
		}

		this.markierung = feld.gibMarkierung();
	}

	public FeldNummer gibFeldNummer() {
		return feldNummer;
	}

	public int gibZeile() {
		return feldNummer.zeile;
	}

	public int gibSpalte() {
		return feldNummer.spalte;
	}

	public int gibVorgabe() {
		return vorgabe;
	}

	public Integer gibResetVorgabe() {
		return resetVorgabe;
	}

	public int gibEintrag() {
		if (eintrag == null) {
			return 0;
		}
		return eintrag.gibZahl();
	}

	public int gibEintragEbene() {
		if (eintrag == null) {
			return 0;
		}
		return eintrag.gibEbene();
	}

	public Eintrag gibEintragObjekt() {
		return eintrag;
	}

	public ZahlenFeldNummern gibFeldParter() {
		return feldPartner;
	}

	public boolean istEintragEbenenStart() {
		if (eintrag == null) {
			return false;
		}
		return eintrag.istEbenenStart();
	}

	public ArrayList<Integer> gibMoegliche() {
		ArrayList<Integer> zahlen = new ArrayList<Integer>();
		if (moegliche == null) {
			return zahlen;
		}
		for (MoeglicheZahl moeglicheZahl : moegliche) {
			zahlen.add(new Integer(moeglicheZahl.gibZahl()));
		}
		return zahlen;
	}

	public ArrayList<MoeglicheZahl> gibMoeglicheZahlen() {
		if (moegliche == null) {
			return new ArrayList<MoeglicheZahl>();
		}
		return moegliche;
	}

	/**
	 * @return null oder die eine m�gliche Zahl
	 */
	public FeldNummerMitZahl gibKlareZahl() {
		FeldNummerMitZahl ergebnis = null;
		ArrayList<Integer> moegliche = this.gibMoegliche();
		if (!moegliche.isEmpty()) {
			if (moegliche.size() == 1) {
				int zahl = moegliche.get(0);
				ergebnis = new FeldNummerMitZahl(feldNummer, zahl);
			}
		}
		return ergebnis;
	}

	public boolean existiertMarkierungMoeglicherZahl() {
		if (istVorgabe()) {
			return false;
		}
		if (istEintrag()) {
			return false;
		}
		for (MoeglicheZahl moeglicheZahl : moegliche) {
			if (moeglicheZahl.istMarkiert()) {
				return true;
			}
		}
		return false;
	}

	public Boolean gibMarkierung() {
		return markierung;
	}

	public boolean istVorgabe() {
		return vorgabe > 0;
	}

	public boolean istEintrag() {
		return eintrag != null;
	}

	public boolean istEintragAlsTipZahl() {
		if (eintrag == null) {
			return false;
		} else {
			return eintrag.istTipZahl();
		}
	}

	public boolean istFrei() {
		return (vorgabe == 0) && (eintrag == null);
	}

	public boolean istFeldPaar() {
		return (istFrei() && (feldPartner != null));
	}

	public boolean istMoeglich(int zahl) {
		ArrayList<Integer> mZahlen = gibMoegliche();
		return (istFrei() && mZahlen.contains(new Integer(zahl)));
	}

	public boolean istNur1Moeglich() {
		return (istFrei() && (moegliche.size() == 1));
	}

	/**
	 * Hier erfolgen keinerlei Kontrollen.
	 * 
	 * @param zahl
	 *            wird unbedingt gesetzt und diese aus den M�glichen entfernt
	 */
	public void setzeEintrag(int zahl) {
		eintrag = new Eintrag(zahl, 0, false, false);
		for (int i = 0; i < this.moegliche.size(); i++) {
			MoeglicheZahl moeglich = this.moegliche.get(i);
			if (moeglich.gibZahl() == zahl) {
				this.moegliche.remove(i);
			}
		}
	}

	/**
	 * Hier erfolgen keinerlei Kontrollen.
	 * 
	 * @param unmoeglicheZahl
	 * @return true wenn eine Zahl gel�scht wurde
	 */
	public boolean loescheUnmoeglicheZahl(int unmoeglicheZahl) {
		for (int i = 0; i < moegliche.size(); i++) {
			if (moegliche.get(i).gibZahl() == unmoeglicheZahl) {
				moegliche.remove(i);
				return true;
			}
		}
		return false;
	}

	public void setzeMarkierung(boolean setzen) {
		markierung = new Boolean(setzen);
	}

	public void loescheMarkierung() {
		markierung = null;
	}

	public void markiereMoeglicheZahl(int zahl) {
		if (moegliche == null) {
			throw new FehlendeMoeglicheZahl(new FeldNummerMitZahl(new FeldNummer(feldNummer), zahl));
		}
		MoeglicheZahl moeglicheZahl = null;
		for (MoeglicheZahl moeZahl : moegliche) {
			if (moeZahl.gibZahl() == zahl) {
				moeglicheZahl = moeZahl;
			}
		}
		if (moeglicheZahl == null) {
			throw new FehlendeMoeglicheZahl(new FeldNummerMitZahl(new FeldNummer(feldNummer), zahl));
		}
		moeglicheZahl.setzeMarkiert();
	}

	public boolean istGleicherEintrag(FeldInfo info2) {
		// if (eintrag == null){return false;}
		if (info2 == null) {
			return false;
		}
		if (!info2.istEintrag()) {
			return false;
		}
		int eintrag = gibEintrag();
		int eintrag2 = info2.gibEintrag();
		return eintrag == eintrag2;
	}

	public String[] gibFeldPartnerTexte() {
		if (!this.istFeldPaar()) {
			return null;
		}
		String[] sArray = FeldPartnerTexte.gibPaareTexte(this.feldNummer, this.feldPartner);

		return sArray;
	}

}
