package sudoku;

public class Paar<K, V> {
	final private K key;
	final private V value;

	public Paar(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

}
