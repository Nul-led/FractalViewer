package util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import fractales.JuliaSet;
import gui.Gui;

public class FractaleCalculator {
	public static BufferedImage computeMandelbrot(BufferedImage buffer, Complex point, int max_iter, double zoom_factor, Gui parent) {
		for(int x = 0; x < buffer.getWidth(); x++) {
			for(int y = 0; y < buffer.getHeight(); y++) {
				Complex c = parent.computePoint(new Point(x, y));
				int iterations = calculateMandelbrotPointIterations(c, max_iter);
				buffer.setRGB(x, y, ColorUtils.getColor(parent.getColoringAlgorithm(), iterations, max_iter, parent.getOffset(), parent.getTheme()));
			}
		}
		return buffer;
	}
	
	public static int calculateMandelbrotPointIterations(Complex c, int max_iter) {	
		Complex temp = c;
		for(int i = 0; i < max_iter; i++) {
			if(temp.absolute() > 2.0d) 
				return i; 
			temp = temp.multiply(temp).add(c)
		}
		return max_iter; 
	}
	
	public static BufferedImage computeJuliaSet(BufferedImage buffer, JuliaSet set) {
		for(int x = 0; x < buffer.getWidth(); x++) {
			for(int y = 0; y < buffer.getHeight(); y++) {
				Complex c = set.calculatePoint(new Point(x, y));
				int iterations = calculateJuliaSetPointIterations(c, set.getMaxIterations(), set.getPoint());
				buffer.setRGB(x, y, ColorUtils.getColor("smooth_coloring", iterations, set.getMaxIterations(), 0.0d, 0));
			}
		}
		return buffer;
	}
	
	public static int calculateJuliaSetPointIterations(Complex c, int max_iter, Complex point) {
		Complex temp = c;
		for(int i = 0; i < max_iter; i++) {
			Complex oldTemp = temp;
			temp = new Complex(oldTemp.getReal() * oldTemp.getReal() - oldTemp.getImaginary() * oldTemp.getImaginary() + point.getReal(), 2 * oldTemp.getReal() * oldTemp.getImaginary() + point.getImaginary());
			if(temp.absolute() > 2.0d)
				return i;
		}
		return max_iter;
	}
	
}
