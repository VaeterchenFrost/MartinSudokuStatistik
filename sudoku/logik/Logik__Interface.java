package sudoku.logik;

import java.util.List;

import sudoku.kern.exception.Exc;
import sudoku.logik.tipinfo.TipInfo;

/**
 * @author heroe
 * Eine Sudoku-Logik bzw. Sudoku-Lösungsstrategie
 */
interface Logik__Interface extends Logik__Infos{
	/**
	 * Ehrenkodex:
	 * Die Logik ist eine ausschließlich beratende Instanz. 
	 * Diese Methode greift nicht aktiv in die FeldMatrix: Sie setzt weder einen Eintrag noch löscht sie mögliche Zahlen.
	 * Dies ließe sich erzwingen durch Übergabe eines InfoSudoku als Arbeitsbasis. 
	 * Aber das wäre zusätzliche Rechenzeit: Und das wollen wir doch alle nicht!
	 * Außerdem ist die Logik Bestandteil der Programm-Zentrale und da kann man hier wohl Liebigkeit erwarten!
	 * 
	 * Die Logik hat die Aufgabe, einen nächsten Soll-Eintrag oder zu löschende mögliche Zahlen zu suchen.
	 * Falls eines von beiden gefunden wird, soll die Methode (sofort) verlassen werden (Es existiert im Sudoku eine neue Situation).
	 * Die Infos zu den Suchergebnissen werden im Rückgabeergebnis weitergegeben.
	 *  
	 * Falls mögliche Zahlen durch einen Logiklauf als zu löschen erkannt wurden, MUSS die Methode verlassen werden aus 2 Gründen:
	 * 	1. Der Tip-Organisator kann ein Löschverbot für mögliche Zahlen aussprechen zum Zwecke der Tip-Komprimierung.
	 * 	2. Nach dem Löschen von möglichen Zahlen, soll wieder ab der einfachsten Logik begonnen werden, nach der Lösung zu suchen!
	 * 
	 * @param istTip Bei true wird ein Tip angefordert. 
	 * 					Falls ein Tip angefordert wird, muss eine erfolgreiche Logik (Eintrag oder zu löschende mögliche Zahlen gefunden)
	 *  					eine TipInfo in dem Rueckgabeergebnis eingestellen. 
	 * @param ignorierTips Für die Tip-Komprimierung. Auch null.
	 * 					In ignorierTips angezeigte Situationen soll die Logik nicht als Suchergebnis melden, sondern ignorieren!
	 * 					Die Logik darf davon ausgehen, dass nur durch sie selbst erstellte TipInfos hier reingereicht werden. 
	 * 						Also nur von genau diesem bekannten Typ sind.
	 * 					Für eine Logik, die als Ergebnis "nur" einen Eintrag benennt, ist dieser Parameter nicht relevant (sollte auch null sein).
	 * @return 
	 *  - null falls gar nichts getan wurde (keine Logik-Läufe)
	 * 	- Ergebnis mit der Liste der Gruppenläufe und Eintrag falls ein solcher gefunden wurde.
	 * 	- Ergebnis mit der Liste der Gruppenläufe und zu löschenden möglichen Zahlen falls solche gefunden wurden.
	 *  - Ergebnis mit der Liste der Gruppenläufe und nichts weiter, falls keine Situation für diese Logik vorgefunden wurde. 
	 *  		(Die Info zu den Gruppenläufen wird für die Lösungszeit-Ermittlung benutzt.)
	 * @throws Exc
	 */
	LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips)  throws Exc;
}
