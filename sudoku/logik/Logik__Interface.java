package sudoku.logik;

import java.util.List;

import sudoku.kern.exception.Exc;
import sudoku.logik.tipinfo.TipInfo;

/**
 * @author heroe Eine Sudoku-Logik bzw. Sudoku-L�sungsstrategie
 */
interface Logik__Interface extends Logik__Infos {
	/**
	 * Ehrenkodex: Die Logik ist eine ausschlie�lich beratende Instanz. Diese
	 * Methode greift nicht aktiv in die FeldMatrix: Sie setzt weder einen
	 * Eintrag noch l�scht sie m�gliche Zahlen. Dies lie�e sich erzwingen durch
	 * �bergabe eines InfoSudoku als Arbeitsbasis. Aber das w�re zus�tzliche
	 * Rechenzeit: Und das wollen wir doch alle nicht! Au�erdem ist die Logik
	 * Bestandteil der Programm-Zentrale und da kann man hier wohl Liebigkeit
	 * erwarten!
	 * 
	 * Die Logik hat die Aufgabe, einen n�chsten Soll-Eintrag oder zu l�schende
	 * m�gliche Zahlen zu suchen. Falls eines von beiden gefunden wird, soll die
	 * Methode (sofort) verlassen werden (Es existiert im Sudoku eine neue
	 * Situation). Die Infos zu den Suchergebnissen werden im R�ckgabeergebnis
	 * weitergegeben.
	 * 
	 * Falls m�gliche Zahlen durch einen Logiklauf als zu l�schen erkannt
	 * wurden, MUSS die Methode verlassen werden aus 2 Gr�nden: 1. Der
	 * Tip-Organisator kann ein L�schverbot f�r m�gliche Zahlen aussprechen zum
	 * Zwecke der Tip-Komprimierung. 2. Nach dem L�schen von m�glichen Zahlen,
	 * soll wieder ab der einfachsten Logik begonnen werden, nach der L�sung zu
	 * suchen!
	 * 
	 * @param istTip
	 *            Bei true wird ein Tip angefordert. Falls ein Tip angefordert
	 *            wird, muss eine erfolgreiche Logik (Eintrag oder zu l�schende
	 *            m�gliche Zahlen gefunden) eine TipInfo in dem
	 *            Rueckgabeergebnis eingestellen.
	 * @param ignorierTips
	 *            F�r die Tip-Komprimierung. Auch null. In ignorierTips
	 *            angezeigte Situationen soll die Logik nicht als Suchergebnis
	 *            melden, sondern ignorieren! Die Logik darf davon ausgehen,
	 *            dass nur durch sie selbst erstellte TipInfos hier reingereicht
	 *            werden. Also nur von genau diesem bekannten Typ sind. F�r eine
	 *            Logik, die als Ergebnis "nur" einen Eintrag benennt, ist
	 *            dieser Parameter nicht relevant (sollte auch null sein).
	 * @return - null falls gar nichts getan wurde (keine Logik-L�ufe) -
	 *         Ergebnis mit der Liste der Gruppenl�ufe und Eintrag falls ein
	 *         solcher gefunden wurde. - Ergebnis mit der Liste der Gruppenl�ufe
	 *         und zu l�schenden m�glichen Zahlen falls solche gefunden wurden.
	 *         - Ergebnis mit der Liste der Gruppenl�ufe und nichts weiter,
	 *         falls keine Situation f�r diese Logik vorgefunden wurde. (Die
	 *         Info zu den Gruppenl�ufen wird f�r die L�sungszeit-Ermittlung
	 *         benutzt.)
	 * @throws Exc
	 */
	LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc;
}
