package controller.data;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PictureConverter {

    /**
     * Feste Höhe des resultierenden Bildes, wird bei Aufruf des
     * Default-Konstruktors angewendet.
     */
    private static final int HEIGHT = 270;

    /**
     * Feste Breite des resultierenden Bildes, wird bei Aufruf des
     * Default-Konstruktors angewendet.
     */
    private static final int WIDTH = 200;

    /**
     * Festgelegte Höhe
     */
    final int height;

    /**
     * Festgelegte Breite
     */
    final int width;

    /**
     * Konstruktor für feste Breite und Höhe des Bildes
     */
    public PictureConverter() {
	this.width = WIDTH;
	this.height = HEIGHT;
    }

    /**
     * Konstruktor für beliebige Breite und Höhe des Bildes
     * 
     * @param width
     *            die gewünschte Breite
     * @param height
     *            die gewünschte Höhe
     */
    public PictureConverter(int width, int height) {
	this.width = width;
	this.height = height;
    }

    /**
     * Unter Angabe eines Bildes wird dieses auf die festgelegte Höhe und Breite
     * verkleiner und in ein Binärbild konvertiert.
     * 
     * @param file
     *            der Pfad zum Bild (jpg, png)
     * @return das konvertierte Bild als BufferedImage
     */
    public BufferedImage resizeAsBufferedImage(File file) {

	BufferedImage old = null;
	BufferedImage resizedImage = null;

	try {
	    old = ImageIO.read(file);
	    resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(old, 0, 0, width, height, null);
	    g.dispose();
	} catch (FileNotFoundException e) {
	    System.err.println("File not found!");
	} catch (IOException e) {
	    System.err.println("Read error");
	} catch (IllegalArgumentException e) {
	    System.err.println("No file selected");
	} catch (NullPointerException e) {
	}

	return resizedImage;

    }

    /**
     * Unter Angabe eines Bildes wird dieses auf die festgelegte Höhe und Breite
     * verkleiner und in ein Binärbild konvertiert. Die Rückgabe erfolgt als
     * Byte-Array ohne Headerinformationen des Bildes.
     * 
     * @param file
     *            der Pfad zum Bild (jpg, png)
     * @return das konvertierte Bild als Byte-Array
     */
    public byte[] resizeAsByteArray(File file) {
	BufferedImage bi = resizeAsBufferedImage(file);
	return ((DataBufferByte) bi.getData().getDataBuffer()).getData();
    }

    /**
     * Unter Angabe eine Byte-Arrays wird ein Binärbild erzeugt, welches als
     * BufferedImage zurückgegeben wird
     * 
     * @param bb
     *            das Byte-Array mit den Bildinformationen (ohne
     *            Headerinformationen)
     * @return das erstellte Bild als BufferedImage
     */
    public BufferedImage writeImage(byte[] bb) {

	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
	img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(bb, bb.length), new Point()));

	return img;
    }
}
