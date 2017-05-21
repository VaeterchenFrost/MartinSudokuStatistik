package sudoku;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ArrayListInt extends ArrayList<Integer>{

	public ArrayListInt() {
	}

	public ArrayListInt(ArrayList<Integer> other) {
		this.addAll(other);
	}

	/**
	 * @param verbindung hiermit werden die einzelnen Nummern miteinander verbunden
	 * @return
	 */
	public String gibKette(String verbindung){
		String s = new String("");
		
		for (int iNummer = 0; iNummer < this.size(); iNummer++) {
			if (s.length() > 0){
				s += verbindung;
			}
			s += this.get(iNummer);
		}
			
		return s;
	}

}
