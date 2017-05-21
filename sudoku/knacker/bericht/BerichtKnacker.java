package sudoku.knacker.bericht;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class BerichtKnacker extends ArrayList<KB_BerichtEintrag>{
	private static boolean istSystemOut =false; //true;
	public static boolean istSystemOut(){
		return istSystemOut;
	}
	// ---------------------------------------------------------------
	
	public BerichtKnacker() {
	}

	
	public void systemOut(){
		if (istSystemOut){
			System.out.println("BerichtKnacker.systemOut()");
			for (int i= 0; i< this.size(); i++) {
				KB_BerichtEintrag berichtEintrag = this.get(i);
				berichtEintrag.systemOut();
			}
		}
	}

	@Override
	public boolean add(KB_BerichtEintrag berichtEintrag) {
		if (istSystemOut){
			berichtEintrag.systemOut();
		}
		return super.add(berichtEintrag);
	}

	/**
	 * @return true wenn der Bericht einen Versuch dokumentiert
	 */
	public boolean istVersuch(){
		for (KB_BerichtEintrag eintrag: this){
			if (eintrag instanceof KB_VersuchStart){
				return true;
			}
		}
		return false;
	}
}
