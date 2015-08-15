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

	private static final int HEIGHT = 270;
	private static final int WIDTH = 200;
	
	final int width;
	final int height;
	
	public PictureConverter() {
		this.width = WIDTH;
		this.height = HEIGHT;
	}
	
	public PictureConverter(int width, int height) {
		this.width = width;
		this.height = height;
	}

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
		} catch (IOException e){
			System.err.println("Read error");
		} catch (IllegalArgumentException e) {
			System.err.println("No file selected");
		} catch (NullPointerException e) {
		}
				
		return resizedImage;

	}
	
	public byte[] resizeAsByteArray(File file) {
		BufferedImage bi = resizeAsBufferedImage(file);
		return ((DataBufferByte) bi.getData().getDataBuffer()).getData();
	}

	public BufferedImage writeImage(byte[] bb) {

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(bb, bb.length), new Point()));

		return img;
	}
}
