package sudoku.logik.bericht;

import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.Logik_ID;
import sudoku.logik.SudokuLogik;

/**
 * @author heroe
 * So vermerkt sich eine Logik
 */
public class BE_Logik {
	public final Logik_ID logik; 
	public final GruppenLaeufeListe gruppenlaeufeListe;  
	public final FeldNummerMitZahl eintrag;
	public final ZahlenListe loeschZahlen;
	
	public BE_Logik(Logik_ID logik, GruppenLaeufeListe gruppenlaeufeListe, 
			FeldNummerMitZahl eintrag, ZahlenListe loeschZahlen){ 
		this.logik = logik;
		this.gruppenlaeufeListe = gruppenlaeufeListe;
		this.eintrag = eintrag;
		this.loeschZahlen = loeschZahlen;
	}

	@Override
	public String toString() {
		String sEintrag = eintrag == null ? "" : " Eintrag="+ eintrag;
		String sLoeschZahlen = loeschZahlen == null ? "" : " Löschzahlen="+ loeschZahlen;
		return "BE_Logik [logik=" + logik + " " + gruppenlaeufeListe + sEintrag + sLoeschZahlen + "]";
	}
	
	public String gibKurzText() {
		String sLogikKurz = SudokuLogik.gibNameKurz(logik);
		String sEintrag = eintrag == null ? "" : " Eintrag="+ eintrag;
		String sLoeschZahlen = loeschZahlen == null ? "" : " Löschzahlen="+ loeschZahlen;
		return "BE_Logik [" + sLogikKurz + " " + gruppenlaeufeListe.gibKurzText() + " " + sEintrag + " " + sLoeschZahlen + "]";
	}
	
}
