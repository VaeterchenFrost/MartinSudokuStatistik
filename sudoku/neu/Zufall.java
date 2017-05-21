package sudoku.neu;

import java.util.Random;

import sudoku.kern.animator.Animator;
import sudoku.kern.feldmatrix.FeldMatrix;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;

public class Zufall {
    private static Random zufallsgenerator = new Random();

    /**
     * @param maxPlus1
     * @return Zufallszahl zwischen 0 und maxPlus1-1
     */
    public static int gib(int maxPlus1){
        return zufallsgenerator.nextInt(maxPlus1);
    }
    
	/**
	 * @param felder
	 * @param anzahl gewünschte Felder
	 * @param symmetrierer Falls != null soll das Sudoku symmetrisch aussehen. 
	 * 					In diesem Fall werden jeweils 2 Vorgaben gelöscht, falls noch 2 Vorgaben zur Verfügung stehen! 
	 * @return Zufällig ausgewählte Felder, deren Anzahl kleiner als die vorgegebene Anzahl sein kann
	 */
	public static FeldNummerListe gibAuswahlFelder(FeldNummerListe felder, int anzahl, Animator symmetrierer){
		FeldNummerListe zufallsFelder = new FeldNummerListe();
		FeldNummerListe freie = new FeldNummerListe();
		freie.addAll(felder);
    	if (freie.size() < anzahl){
    		anzahl = freie.size();
    	}
    	
    	if ( (symmetrierer != null) & (anzahl > 1) ){
    		anzahl /= 2;
    	}
    	
    	for (int i = 0; i < anzahl; i++) {
        	int iFeld = Zufall.gib (freie.size());
        	FeldNummer feldNummer = freie.get(iFeld);

    		zufallsFelder.add(feldNummer);
    		freie.remove(feldNummer);
    		
    		if (symmetrierer != null){
    			FeldNummer feldNummerSymmetrisch = symmetrierer.gibFeldNummer(feldNummer, FeldMatrix.feldNummerMax);
        		zufallsFelder.add(feldNummerSymmetrisch);
        		freie.remove(feldNummerSymmetrisch);
    		}
		}
    	return zufallsFelder;
	}

	
}
