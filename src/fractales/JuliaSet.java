package fractales;

import java.awt.Dimension;
import java.awt.Point;

import util.Complex;

public class JuliaSet {
	private int max_iter;
	private Complex point;
	private Dimension dimension;
	private double zoom_factor;

	public JuliaSet(Complex point, Dimension dimension, int max_iter, double zoom_factor) {
		this.point = point;
		this.dimension = dimension;
		this.max_iter = max_iter;
		this.zoom_factor = zoom_factor;
	}
	
	public double getZoomFactor() {
		return this.zoom_factor;
	}
	
	public void setZoomFactor(double zoom_factor) {
		this.zoom_factor = zoom_factor;
	}
	
	public int getMaxIterations() {
		return this.max_iter;
	}
	
	public void setMaxIterations(int max_iter) {
		this.max_iter = max_iter;
	}
	
	public Complex getPoint() {
		return this.point;
	}
	
	public void setPoint(Complex point) {
		this.point = point;
	}
	
	public Complex calculatePoint(Point p) {
		double real = 1.5 * ((double) p.x - (double) dimension.width / 2) / (0.5 * zoom_factor * (double) dimension.width); 
		double imaginary = ((double) p.y - (double) dimension.height / 2) / (0.5 * zoom_factor * (double) dimension.height);
		return new Complex(real, imaginary);
	}
}
