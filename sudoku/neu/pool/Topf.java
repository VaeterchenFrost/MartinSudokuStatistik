package sudoku.neu.pool;

import java.util.ArrayList;

import sudoku.kern.info.InfoSudoku;
import sudoku.neu.NeuTyp;

class Topf {
	private NeuTyp neuTyp;
	private ArrayList<InfoSudoku> sudokus;

	Topf(NeuTyp neuTyp) {
		this.neuTyp = neuTyp;
		this.sudokus = new ArrayList<>();
	}

	NeuTyp gibNeuTyp() {
		return neuTyp;
	}

	int gibAnzahl() {
		return this.sudokus.size();
	}

	InfoSudoku gibSudoku() {
		if (sudokus.isEmpty()) {
			return new InfoSudoku();
		}
		InfoSudoku sudoku = sudokus.get(0);
		sudokus.remove(0);
		return sudoku;
	}

	void setze(InfoSudoku sudoku) {
		this.sudokus.add(sudoku);
	}

	int gibSollAnzahl() {
		if (neuTyp.equals(new NeuTyp(NeuTyp.Typ.VOLL))) {
			return 1;
		}
		// Hier gibt es nach oben leider noch keine Verkn�pfung: Zu
		// winapp.druckinfo.extras.Drucke6
		return 6;
	}

}
