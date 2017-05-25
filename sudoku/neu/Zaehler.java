package sudoku.neu;

import java.util.ArrayList;

public class Zaehler {
	private ArrayList<Integer> history;
	private int n;

	public Zaehler() {
		history = new ArrayList<>();
		n = 0;
	}

	private void auswerten() {
		if (n > 0) {
			history.add(new Integer(n));
			n = 0;
		}
	}

	public void start() {
		auswerten();
	}

	public void inc() {
		n++;
	}

	public void inc(int inc) {
		n += inc;
	}

	public int gib() {
		return n;
	}

	public int gibMax() {
		auswerten();
		int nMax = 0;
		for (Integer i : history) {
			if (i > nMax) {
				nMax = i;
			}
		}
		return nMax;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		auswerten();
		int nLast = 0;
		if (!history.isEmpty()) {
			nLast = history.get(history.size() - 1);
		}
		return " n=" + nLast + " nMax=" + gibMax();
	}

}
