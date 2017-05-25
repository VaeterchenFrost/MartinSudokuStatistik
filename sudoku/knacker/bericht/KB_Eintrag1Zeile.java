package sudoku.knacker.bericht;

/**
 * @author Hendrick Der Bericht-Eintrag, der einzeilig ausschreibt
 */
public class KB_Eintrag1Zeile implements KB_BerichtEintrag {
	@Override
	public void systemOut() {
		System.out.println(this);
	}
}
