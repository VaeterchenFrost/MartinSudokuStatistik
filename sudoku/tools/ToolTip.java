package sudoku.tools;

public class ToolTip {

	/**
	 * @param texte
	 * @return Mehrzeiligen ToolTip
	 */
	public static String gibToolTip(String[] texte) {
		String tip = "<html>" + texte[0];
		for (int i = 1; i < texte.length; i++) {
			tip += "<br>" + texte[i];
		}
		tip += "</html>";
		return tip;
	}
}
