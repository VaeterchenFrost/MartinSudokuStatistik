package sudoku.logik.tipinfo;

import java.util.List;

import sudoku.logik.Logik_ID;

/**
 * @author heroe Nimmt die Details zu einem Tip entgegen
 */
public interface TipKurier {
	/**
	 * @param info
	 *            Sofern der TipKurier noch offen ist, vermerkt er die info.
	 *            Allerdings nur die Teil-Tips, die keine L�schzahlen
	 *            beinhalten, deren L�schung nicht verboten ist.
	 * @return Die Zahlen, deren L�schung der TipKurier verbietet.
	 */
	public void add(TipInfo info);

	/**
	 * @param logikID
	 *            Es werden nur Tips dieser Logik zur�ckgegeben.
	 * @return Auch null. Die Tips, deren Situation die Logik ignorieren, also
	 *         nicht melden soll.
	 */
	public List<TipInfo> gibIgnorierTips(Logik_ID logikID);
}
