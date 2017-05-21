package sudoku.kern.exception;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.Eintrag;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;
import sudoku.kern.protokoll.ProtokollEintrag;
import sudoku.kern.protokoll.Protokoll.Markierung;

/**
 * @author Hendrick
 * Zeigt einen Programm-Fehler an
 */
@SuppressWarnings("serial")
public class Exc extends Exception{
	private Exc(String s){
		super(s);
	}

	static public Exc ausnahme(String text){
		return new Exc(text);
	}

	static public Exc falscheGeschwisterAnzahl(int anzahlGeschwister, int nAnzahlGeschwisterMin, int nAnzahlGeschwisterMax){
		String text = String.format("falsche Geschwisteranzahl %d (min=%d, max=%d)"
				, anzahlGeschwister, nAnzahlGeschwisterMin, nAnzahlGeschwisterMax); 
		return new Exc(text);
	}
	
	static public Exc setzeMoeglicheNicht(ArrayList<Integer> moegliche, ArrayList<Integer> neueMoegliche){
		String text = String.format("setze Mögliche nicht: meine=%s, neue=%s", moegliche, neueMoegliche); 
		
		return new Exc(text);
	}
	
	static public Exc falscheVersuchsEbeneEntstanden(int ordinal){
		String text = String.format("falsche VersuchsEbene entstanden: %d)", ordinal); 
		
		return new Exc(text);
	}
	
	static public Exc unerwarteterTip(FeldNummerMitZahl tip){
		String text = String.format("nicht erwarteter Tip: %s)", tip); 
		
		return new Exc(text);
	}
	
	static public Exc unerwartetesProblem(Problem problem){
		String text = String.format("nicht erwartetes Problem: '%s'", problem.toString()); 
		
		return new Exc(text);
	}
	
	static public Exc protokollMarkierungExistiertNicht(int markierung, ArrayList<Markierung> markierungen){
		String text = String.format("Protokoll-Markierung %d existiert nicht: %s", 
				markierung, markierungen.toString());
		
		return new Exc(text);
	}

	static public Exc protokollMarkierungIstOhneNachfolger(int markierungId, int markierungKursor, int anzahlEintraege){
		String text = String.format("Protokoll-Markierung (id=%d, kursor=%d) ist ohne Nachfolger: Anzahl Einträge=%d", 
				markierungId, markierungKursor, anzahlEintraege);
		
		return new Exc(text);
	}

	static public Exc protokollTipOhneEintrag(FeldNummer feldNummer, int tipIndex){
		String text = String.format("Protokoll-Tip (kursor=%d, Feld[Z%d,S%d]) besitzt keinen Eintrag", 
				tipIndex, feldNummer.zeile, feldNummer.spalte);
		
		return new Exc(text);
	}

	static public Exc setzeVorgabeNurOhneEintraege(Feld feld, int vorgabe, int aktuelleEbene){
		String text = String.format("%s: Setze Vorgabe %d nur ohne Einträge (Ebene=%d)", 
				feld.gibName(), vorgabe, aktuelleEbene);
		
		return new Exc(text);
	}

	static public Exc unerlaubteZahl(Feld feld, int zahl){
		String text = String.format("%s bekommt die nicht erlaubte Zahl %d", 
				feld.gibName(), zahl);
		
		return new Exc(text);
	}
	
	static public Exc unerlaubteZeile(int zeile){
		String text = String.format("Bekomme nicht erlaubte Zeile=%d", zeile);
		
		return new Exc(text);
	}

	static public Exc unerlaubteSpalte(int spalte){
		String text = String.format("Bekomme nicht erlaubte Spalte=%d", spalte);
		
		return new Exc(text);
	}
	
	
	static public Exc setzeEintragNichtAufVorgabe(Feld feld, int zahl){
		String text = String.format("%s: Setze Eintrag %d nicht auf Vorgabe %d", 
				feld.gibName(), zahl, feld.gibVorgabe());
		
		return new Exc(text);
	}

	static public Exc loescheEintragNichtAufEbene(Feld feld, int aktuelleEbene){
		String text = String.format("%s: Lösche Eintrag %d nicht: EintragsEbene=%d aktuelle Ebene=%d", 
				feld.gibName(), feld.gibEintrag(), feld.gibEintragEbene(), aktuelleEbene);
		
		return new Exc(text);
	}
	
	static public Exc setzeEintragNichtAufEintrag(Feld feld, int zahl){
		String text = String.format("%s: Setze Eintrag %d nicht auf meinen Eintrag %d => Modus der Eingaben umschalten!", 
				feld.gibName(), zahl, feld.gibEintrag());
		
		return new Exc(text);
	}
	
	static public Exc setzeEintragNichtOhneMoegliche(Feld feld, int zahl){
		String text = String.format("%s: Setze Eintrag %d nicht ohne mögliche Zahlen", 
				feld.gibName(), zahl, feld.gibVorgabe());
		
		return new Exc(text);
	}
	
	static public Exc setzeEintragNichtOhneDieseMoegliche(Feld feld, int zahl){
		String text = String.format("%s: Setze Eintrag %d nicht: Ist nicht unter den Möglichen %s", 
				feld.gibName(), zahl, feld.gibVorgabe(),  feld.gibMoeglicheAlsString());
		
		return new Exc(text);
	}
	
	static public Exc ebeneSpieltNichtMit(Feld feld, Eintrag neuerEintrag, int neueEbene){
		String text = String.format("%s (Mögliche=%s): EintragsObjekt (Ebene=%d, Zahl=%d) erzeugt falsche Ebene=%d", 
				feld.gibName(), feld.gibMoeglicheAlsString(), neuerEintrag.gibEbene(), neuerEintrag.gibZahl(), neueEbene);
		
		return new Exc(text);
	}
	
	static public Exc endlosSchleife(){
		return new Exc ("Endlosschleife"); 
	}
	static public Exc ergebnisloseSchleife(){
		return new Exc ("Ergebnislose Schleife"); 
	}
	
	static public Exc falscheAnzahl(int falsch, int richtig){
		String text = String.format("Falsche Anzahl=%d. Erforderlich ist %d.", 
				falsch, richtig);
		return new Exc(text);
	}

	static public Exc aktionnurOhneEintrag (String aktionsName){
		String text = String.format("Die Aktion '%s' ist nur im Sudoku ohne Einträge möglich", aktionsName);
		return new Exc(text);
	}

	static public Exc verzeichnisErstellen(String pfad){
		String text = String.format("Das Verzeichnis '%s' kann nicht erstellt werden", pfad);
		return new Exc(text);
	}

	static public Exc verzeichnisLeeren (String pfad){
		String text = String.format("Das Verzeichnis '%s' kann nicht geleert werden", pfad);
		return new Exc(text);
	}

	static public Exc protokollEintragOhneEintrag(ProtokollEintrag eintrag){
		return new Exc (eintrag.toString()); 
	}
	
	static public Exc protokoll_IOWertFehlt(String wertname){
		String text = String.format("Beim Lesen fehlt der Wert %s", wertname);
		return new Exc(text);
	}	
}
