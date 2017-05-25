package sudoku.tip;

import java.util.ArrayList;

import sudoku.bedienung.SudokuBedienung;
import sudoku.logik.tipinfo.TipInfo;

public class TipKomprimierer {
	/**
	 * @param sudoku
	 * @param basisBericht
	 * @return null wenn es keinen komprimierten Bericht zu basisBericht gibt
	 *         oder ein Tipbericht der nur die Tips enth�lt, die f�r das Finden
	 *         der klaren Zahl unbedingt n�tig sind.
	 */
	public static TipBericht gibKomprimiert(SudokuBedienung sudoku, TipBericht basisBericht) {
		if (!basisBericht.istKomprimierbar()) {
			return null;
		}

		TipInfo[] kontrollTips = basisBericht.gibKontrollInfos();
		if (istSystemOut) {
			System.out.println("TipBericht gibKomprimiert() ================================");
			sysOutKontrollTips(kontrollTips);
		}
		ArrayList<TipInfo> unnuetzeTips = new ArrayList<>();
		TipBericht komprimierterBericht = null;

		for (int iKontrollZahlen = 0; iKontrollZahlen < kontrollTips.length; iKontrollZahlen++) {
			// Alle gel�schten Zahlen eines Tipps
			TipInfo testTip = kontrollTips[iKontrollZahlen];
			if (istSystemOut) {
				System.out.println(" ------------------- Test des Tip " + testTip);
			}

			unnuetzeTips.add(testTip);
			TipBericht testBericht = new TipBericht(unnuetzeTips);
			sudoku.setzeTip(testBericht);
			if (testBericht.istKomprimiertZu(basisBericht)) {
				if (istSystemOut) {
					System.out.println("Das Ignorieren dieses Tips beeinflusst nicht das Tip-Ergebnis.");
					sysOutTipBericht(testBericht);
				}
				komprimierterBericht = testBericht;
			} else {
				if (istSystemOut) {
					System.out.println("Dieser Tip ist f�r das Tip-Ergebnis wichtig: "
							+ unnuetzeTips.get(unnuetzeTips.size() - 1));
				}
				unnuetzeTips.remove(unnuetzeTips.size() - 1);
			}
		}

		return komprimierterBericht;
	}

	private static boolean istSystemOut = false; // true;

	private static void sysOutKontrollTips(TipInfo[] kontrollTips) {
		for (int i = 0; i < kontrollTips.length; i++) {
			System.out.println("KontrollTip[" + i + "]: " + kontrollTips[i]);
		}
	}

	private static void sysOutTipBericht(TipBericht tipBericht) {
		Tip tip = new Tip(tipBericht);
		TipDetailTexte[] tipDetailTexte = tip.gibTexte();

		// System.out.println(" ------------------------------ Bericht:");
		for (int i = 0; i < tipDetailTexte.length; i++) {
			System.out.println(tipDetailTexte[i].gibTexteInWenigenZeilen());
		}
	}
}
