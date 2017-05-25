package sudoku.tip;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;

public class Tip {
	// --------------------------------------------------------------------------------------------------
	private class TipDetail {
		String ueberschrift;
		TipInfo tipInfo;

		TipDetail(TipInfo tipInfo, String ueberschrift) {
			super();
			this.ueberschrift = ueberschrift;
			this.tipInfo = tipInfo;
		}

		String gibUeberschrift() {
			return this.ueberschrift;
		}

		void setzeInfosInSudoku(boolean istAbschlussSudoku, String sudokuTitel) {
			InfoSudoku sudoku = tipInfo.gibSudoku();
			sudoku.setzeTitel(sudokuTitel);
			if (!istAbschlussSudoku) {
				sudoku.markiereAllePassivAusser(tipInfo.gibMitSpieler());
			}
			sudoku.markiereAktiv(tipInfo.gibAktiveFelder());
			if (tipInfo.gibLoeschZahlen() != null) {
				ZahlenListe markierZahlen = tipInfo.gibLoeschZahlen();
				sudoku.markiereMoeglicheZahlen(markierZahlen);
			}
		}

		EinTipText[] gibTipTexteArray() {
			return tipInfo.gibTip();
		}
	}
	// --------------------------------------------------------------------------------------------------

	private ArrayList<TipDetail> details;

	/**
	 * @param tipBericht
	 *            Alles Quatsch: Es wird vorausgesetzt, dass der erste Eintrag
	 *            im Bericht das Ausgangs-Sudoku ist. Das letzte Sudoku besitzt
	 *            den Stand mit der ersten klaren Zahl oder es gibt keine.
	 */
	public Tip(TipBericht tipBericht) {
		super();

		// Details-Array erstellen
		ArrayList<TipInfo> tipInfos = tipBericht.gibInfos();
		details = new ArrayList<>();

		for (int iTipInfo = 0; iTipInfo < tipInfos.size(); iTipInfo++) {
			TipInfo tipInfo = tipInfos.get(iTipInfo);
			int tipNummer = iTipInfo + 1;
			String ueberschrift = tipInfo.gibUeberschrift(tipNummer);

			TipDetail tipDetail = new TipDetail(tipInfo, ueberschrift);
			details.add(tipDetail);
		}

		// Die Zusatzinfos in die Sudokus einf�gen und deren Titel setzen
		for (int iDetail = 0; iDetail < details.size(); iDetail++) {
			TipDetail tipDetail = details.get(iDetail);
			String titel = String.format("%d. Tip", iDetail + 1);
			tipDetail.setzeInfosInSudoku(iDetail == details.size() - 1, titel);
		}
	}

	public InfoSudoku[] gibInfoSudokus() {
		InfoSudoku[] sudokus = new InfoSudoku[details.size()];
		for (int iDetail = 0; iDetail < details.size(); iDetail++) {
			TipDetail tipDetail = details.get(iDetail);
			sudokus[iDetail] = tipDetail.tipInfo.gibSudoku();
		}
		return sudokus;
	}

	/**
	 * @return Der Tip in Textform: je TipDetail
	 */
	public TipDetailTexte[] gibTexte() {
		TipDetailTexte[] detailTexte = new TipDetailTexte[details.size()];
		for (int iDetail = 0; iDetail < details.size(); iDetail++) {
			TipDetail tipDetail = details.get(iDetail);
			String ueberschrift = tipDetail.gibUeberschrift();
			EinTipText[] tipTexteArray = tipDetail.gibTipTexteArray();
			TipDetailTexte detailTexte1 = new TipDetailTexte(ueberschrift, tipTexteArray);
			detailTexte[iDetail] = detailTexte1;
		} // for (iDetail
		return detailTexte;
	}

}
