package sudoku.bild;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 * @author heroe
 * Routinen zur Arbeit mit einem Bild (Image)
 */
public class Bild {
//    RenderingHints hints = ImageUtilities.getRenderingHints(image);

	/**
	 * @param source
	 * @return Eine Kopie, deren Körper nichts mehr mit source zu tun hat, also ein echter Schnappschuß ist.
	 */
	static public BufferedImage gibBildKopie(BufferedImage source) {
		if(source!=null) {
			BufferedImage copy = new BufferedImage(source.getWidth(),
			source.getHeight(),
			source.getType());
			copy.setData(source.getData());
			return copy;
		}
		return null;
	}
	
	/**
	 * @param src (farbiges) Bild
	 * @return Eine Schwarz-Weiss-Kopie von src
	 */
	public static BufferedImage schwarzWeiss(BufferedImage src) {
		BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);
		ColorConvertOp grayScaleConversionOp = new ColorConvertOp(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

		grayScaleConversionOp.filter(src, dest);
		return dest;
	}

	/**
	 * @param src Quell-Bild, dass grau oder schwarz/weiss sein kann oder sonstwie
	 * @param schwarzWeissGrenze Grauwert von 0 bis 255
	 * @return Eine Schwarz-Weiss-Kopie von src
	 */
	public static BufferedImage schwarzWeiss(BufferedImage src, byte schwarzWeissGrenze) {
		int srcBildTyp = src.getType();
		BufferedImage dest = null;
		
		// Handelt es sich bei src um ein Schwarz-Weiss-Bild?
		if (srcBildTyp == BufferedImage.TYPE_BYTE_BINARY){
			dest = src;
		}
		else{
			// Das Graubild ist der Ausgangspunkt
			BufferedImage grauBild = null;
			if (srcBildTyp == BufferedImage.TYPE_BYTE_GRAY){
				grauBild = src;
			}
			else{
				// grauBild aus irgendeinem Bild-Typ von src erzeugen
				grauBild = Bild.grau(src);
			}
			dest = Bild.schwarzWeissAusGrau(grauBild, schwarzWeissGrenze);
		}
		return dest;
	}

	/**
	 * @param grauBild
	 * @param schwarzWeissGrenze Grauwert von 0 bis 255
	 * @return Schwarz-Weiss-Bild
	 */
	public static BufferedImage schwarzWeissAusGrau(BufferedImage grauBild, byte schwarzWeissGrenze){
		BufferedImage dest = grauBild.getSubimage(0, 0, grauBild.getWidth(), grauBild.getHeight());
		
		// Grenz-Pixelwert erstellen
		Color grenzColor = new Color(schwarzWeissGrenze, schwarzWeissGrenze, schwarzWeissGrenze); //, 255);
		int grenzInt = grenzColor.getRGB();

		// Weiss-Pixelwert erstellen
		int hell = 255;
		Color weissColor = new Color(hell, hell, hell); //, 255);
		int weissInt = weissColor.getRGB();

		// Schwarz-Pixelwert erstellen
		Color schwarzColor = new Color(0, 0, 0); //, 255);
		int schwarzInt = schwarzColor.getRGB();

		for (int iY = 0; iY < dest.getHeight(); iY++) {
			for (int iX = 0; iX < dest.getWidth(); iX++) {
				int intRGB = dest.getRGB(iX, iY);

				if (intRGB > grenzInt) {
					dest.setRGB(iX, iY, weissInt);
				}
				else{
					dest.setRGB(iX, iY, schwarzInt);
				}
			}
		} // for (int iZeile

		return dest;
	}
	
	/**
	 * @param src (farbiges) Bild
	 * @return Eine Kopie von src in Grautönen
	 */
	public static BufferedImage grau(BufferedImage src) {
		BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		ColorConvertOp grayScaleConversionOp = new ColorConvertOp(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

		grayScaleConversionOp.filter(src, dest);
		return dest;
	}

	/**
	 * @return Das Monitorbild
	 * @throws AWTException falls das Lesen des Monitorbildes nicht klappt
	 */
	public static BufferedImage gibMonitorBild() throws AWTException	{
		// Screenshot erstellen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRect = new Rectangle(screenSize);
		BufferedImage image = new Robot().createScreenCapture(screenRect);
		
		return image;
	}
	

	/**
	 * @param image
	 * @param ausschnitt
	 * @return Bild, das nur den Ausschnitt beinhaltet. Und auf image basiert!
	 */
	static public BufferedImage gibBildAusschnitt(BufferedImage image,
			Rectangle ausschnitt) {
		BufferedImage bildAusschnitt = image.getSubimage(ausschnitt.x,
				ausschnitt.y, ausschnitt.width, ausschnitt.height);

		return bildAusschnitt;
	}

//	/**
//	 * @param image
//	 * @param grad Drehwinkel in Grad z.b. 90
//	 * @return um 90 Grad im Uhrzeigersinn gedrehtes Bild
//	 */
//	static public BufferedImage rotiere90Grad(BufferedImage image){
//		int xAlt = image.getWidth();
//		int yAlt = image.getHeight();
//		
//		BufferedImage newImage = new BufferedImage(yAlt, xAlt, image.getType());
//		Graphics2D graphics = (Graphics2D) newImage.getGraphics();
//		
//		int xDrehPunkt = yAlt / 2;
//		int yDrehPunkt = xAlt / 2;
//		graphics.rotate(Math.toRadians(90), xDrehPunkt, yDrehPunkt);
//		
//		int xT = (yAlt - xAlt) / 2;
//		int yT = (xAlt - yAlt) / 2;
//		
////		Wo Falsch ?:
////		int xVorDrehPunktIst = yAlt - yAlt/2 -1; 
////		int yVorDrehPunktIst = xAlt - xAlt/2 -1;
////		int xT = xVorDrehPunktIst - yDrehPunkt; // kleiner schiebt das Bild nach rechts
////		int yT = yVorDrehPunktIst - xDrehPunkt - 10;
//		
//		graphics.translate(xT, yT);
//		graphics.drawImage(image, 0, 0, xAlt, yAlt, null);
//		return newImage;
//	}

	static public BufferedImage rotiere90Grad(BufferedImage srcImage){
		BufferedImage rotatedImage = new BufferedImage(srcImage.getHeight(), srcImage.getWidth(), srcImage.getType());
		AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(90), rotatedImage.getWidth() / 2d, srcImage.getHeight() / 2d);
		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		g.setTransform(affineTransform);
		g.drawImage(srcImage, 0, 0, null);
		return rotatedImage;
	}

	/**
	 * @param bildUngedreht
	 * @param rechteck
	 * @return neues Rechteck so, dass rechteck im 90 Grad gedrehten Bild an seiner Stelle verbleibt 
	 */
	static public Rectangle rotiere90Grad(Dimension bildUngedreht, Rectangle rechteck){
		int x = bildUngedreht.height - rechteck.y - rechteck.height;
		int y = rechteck.x;
		int breite = rechteck.height;
		int hoehe = rechteck.width;
		return new Rectangle(x, y, breite, hoehe);
	}
}
