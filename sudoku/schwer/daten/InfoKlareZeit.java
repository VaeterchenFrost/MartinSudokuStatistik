package sudoku.schwer.daten;

import java.util.ArrayList;

import sudoku.logik.Schwierigkeit;
import sudoku.schwer.AnzeigeInfo;

public class InfoKlareZeit  implements AnzeigeInfo{
	private double zeit;
	private Schwierigkeit wieSchwer;
	
	public InfoKlareZeit(ArrayList<InfoKlareDetail> infos, Schwierigkeit wieSchwer) {
		this.zeit = AnalysatorKlare.gibZeit(infos);
		this.wieSchwer = wieSchwer;
	}

	@Override
	public String gibAnzeigeText() {
		String sWieSchwer = Schwierigkeit.gibName(this.wieSchwer);
//		Integer intZeit = AnalysatorKlare.gibAnzeigeZeit(zeit, false);		
		Integer intZeitGerastert = AnalysatorKlare.gibAnzeigeZeit(zeit, true);		
//		String s = String.format("%s (%d %2d Minuten)", sWieSchwer, intZeit, intZeitGerastert);
		String s = String.format("%s (%2d)", sWieSchwer, intZeitGerastert);
		return s;
	}

	@Override
	public String gibToolTip() {
		String s1 = "Schwierigkeit (Maustaste auf Details erklärt)";
		String s2 = ""; //super.gibToolTipAnzeigeTextBeschreibung();
		return String.format("<html>%s%s</html>", s1, s2);
	}

	/**
	 * @return Zeit in Minuten gerastert (auf 5 Minuten)
	 */
	public int gibAnzeigeZeit(){
		return  AnalysatorKlare.gibAnzeigeZeit(zeit, true);
	}

	/**
	 * @return Zeit in Minuten
	 */
	public int gibZeit(){
		return  AnalysatorKlare.gibAnzeigeZeit(zeit, false);
	}
}
