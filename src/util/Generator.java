package util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import fractales.JuliaSet;
import gui.Gui;

public class Generator {
	public static BufferedImage getFractal(String type, Dimension dimension, Complex point, int max_iter, double zoom_factor, Gui parent) {
		BufferedImage buffer = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		switch(type) {
			case "Mandelbrot":
				return FractaleCalculator.computeMandelbrot(buffer, point, max_iter, zoom_factor, parent);
			case "Julia":
				return FractaleCalculator.computeJuliaSet(buffer, new JuliaSet(point, dimension, max_iter, zoom_factor));
			default:
				throw new Error("No such fractal generator");
		}
	}
}
