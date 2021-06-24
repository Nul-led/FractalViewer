package util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fractales.Mandelbrot;
import gui.Gui;

public class AutoZoom {
	private Complex point;
	private int totalFrames;
	private String outputPath;
	private String algorithm;
	private int max_iter;
	private int theme;
	private double offset;
	private Mandelbrot fractal;
	private Dimension dimension;
	private int currentFrame = 0;
	private double zoom_factor;

	public AutoZoom() throws IOException {
		openDialogue();
		if(outputPath.length() == 0) return;
		centerPoint();
	}
	
	private void openDialogue() {
		String amountString = JOptionPane.showInputDialog("How many Images should be generated?");
		if(amountString == null || !amountString.matches("\\d+")) {
			this.totalFrames = 1;
			System.err.println("Not a number, DEFAULT: 1");
		}
		else this.totalFrames = Integer.valueOf(amountString);
		
		String zoomString = JOptionPane.showInputDialog("Zoom factor to be used?");
		if(zoomString == null) {
			this.zoom_factor = 1.1d;
			System.err.println("Not a number, DEFAULT: 1.1d");
		}
		else this.zoom_factor = Double.valueOf(zoomString);
		
		String pointString = JOptionPane.showInputDialog("Insert the point to zoom on (real:imaginary)");
		if(pointString == null) {
			this.point = new Complex(0.0d, 0.0d);
			System.err.println("Not a Point, DEFAULT: (0.0d:0.0d)");
		}
		else this.point = parsePoint(pointString);
		
		String algorithm = JOptionPane.showInputDialog("What coloring algorithm to use? (time_escape, smooth_coloring, palette)");
		
		if(algorithm == null) {
			this.algorithm = "time_escape";
			System.err.println("Not a valid algorithm, DEFAULT: time_escape");
		}
		this.algorithm = algorithm;
		
		switch(this.algorithm) {
			case "time_escape": 
				String theme = JOptionPane.showInputDialog("Insert theme factor");
				if(theme == null || !theme.matches("0x[0-9A-F]{6}+")) {
					this.theme = 0x100000;
					System.err.println("Not a valid number, DEFAULT: 0x100000");
				}
				this.theme = Integer.decode(theme);
				break;
			case "smooth_coloring":
				String offset = JOptionPane.showInputDialog("Input new Smooth Coloring Offset");
				if(offset == null || !offset.matches("\\d+.\\d+")) {
					this.offset = 0.0d;
					System.err.println("Not a valid number, DEFAULT: 0.0d");
				}
				this.offset = Double.valueOf(offset);
				break;
			case "palette":
				break;
			default:
				this.algorithm = "time_escape";
				System.err.println("Not a valid algorithm, DEFAULT: time_escape");
		}
		
		String maxIterString = JOptionPane.showInputDialog("How high should the maximum iteration size be?");
		if(maxIterString == null || !maxIterString.matches("\\d+")) {
			this.max_iter = 100;
			System.err.println("Not a number, DEFAULT: 100");
		}
		else this.max_iter = Integer.valueOf(amountString);
		
		JFileChooser jfc = new JFileChooser();
	    	if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	    		this.outputPath = jfc.getSelectedFile().getParent();
	}
	
	private Complex parsePoint(String s) {
		s.trim();
		int markerPosition = s.indexOf(":");
		double real = Double.valueOf(s.substring(0, markerPosition));
		double imaginary = Double.valueOf(s.substring(1 + markerPosition));
		return new Complex(real, imaginary);
	}
	
	private void centerPoint() throws IOException {
		this.dimension = new Dimension(1920, 1080);
		this.fractal = new Mandelbrot(400.0d, new Complex(-3.0d, 1.25d), this.max_iter);
		this.fractal.calculateComplexZoom(point, dimension, 400.0d);
		saveFrame();
		currentFrame++;
		zoom();
	}
	
	private void zoom() throws IOException {
		for(; currentFrame < totalFrames; currentFrame++) {
			this.fractal.calculateComplexZoom(point, dimension, fractal.getZoomFactor() * this.zoom_factor);
			saveFrame();
		}
	}

	public static String addZeroPadding(int value, int paddingLength) {
        return String.format("%0" + paddingLength + "d", value);
    }
    
    private void saveFrame() throws IOException {
    	int padding = String.valueOf(totalFrames).length();
		String fileName = addZeroPadding(currentFrame, padding) + ".png";
		File current = new File(outputPath + "/" + fileName);
		BufferedImage buffer = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		buffer = computeMandelbrot(buffer, fractal.getStartPoint(), max_iter, fractal.getZoomFactor());
		ImageIO.write(buffer, "png", current);
	}
    
    private BufferedImage computeMandelbrot(BufferedImage buffer, Complex point, int max_iter, double zoom_factor) {
		for(int x = 0; x < buffer.getWidth(); x++) {
			for(int y = 0; y < buffer.getHeight(); y++) {
				Complex c = fractal.calculatePoint(new Point(x, y));
				int iterations = calculateMandelbrotPointIterations(c, max_iter);
				buffer.setRGB(x, y, ColorUtils.getColor(algorithm, iterations, max_iter, offset, theme));
			}
		}
		return buffer;
	}
	
	private int calculateMandelbrotPointIterations(Complex c, int max_iter) {	
		Complex temp = c;
		for(int i = 0; i < max_iter; i++) {
			if(temp.absolute() > 2.0d)
				return i;
			temp = temp.multiply(temp).add(c);
		}
		return max_iter;
	}
}

