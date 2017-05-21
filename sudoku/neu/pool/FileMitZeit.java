package sudoku.neu.pool;

import java.io.File;

class FileMitZeit implements Comparable<FileMitZeit>{
	File file;
	int zeit;

	FileMitZeit(File file, int zeit) {
		super();
		this.file = file;
		this.zeit = zeit;
	}

	@Override
	public int compareTo(FileMitZeit other) {
		if (this == other) {
			return 0;
		}
		if (other == null) {
			return 1;
		}
		if (getClass() != other.getClass()) {
			return 1;
		}
		if (this.zeit < other.zeit){
			return -1;
		}
		if (this.zeit > other.zeit){
			return 1;
		}
		return 0;
	}
	
}
