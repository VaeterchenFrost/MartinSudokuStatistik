package sudoku.tools;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class MaximaleFensterGroesse {
	public static Rectangle gibMaxGroesse() {
		// Zugang zum Bildschirm holen
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maxRectangle = ge.getMaximumWindowBounds();
		// Dimension dMax =
		// ge.getDefaultScreenDevice().getFullScreenWindow().getSize();
		// Rectangle maxRectangle = new Rectangle(dMax);
		// Dimension dMax = Toolkit.getDefaultToolkit().getScreenSize();
		// Rectangle maxRectangle = new Rectangle(dMax);
		return maxRectangle;
	}

}
