package fractales;

import java.awt.Dimension;
import java.awt.Point;

import util.Complex;

public class Mandelbrot {
	private double zoom_factor;
	private Complex start_point;
	private int max_iter;
	
	public Mandelbrot(double zoom_factor, Complex start_point, int max_iter) {
		this.zoom_factor = zoom_factor;
		this.start_point = start_point;
		this.max_iter = max_iter;
	}
	
	public void calculateZoom(Point point, Dimension dimension, double new_zoom_factor) {
		double x = start_point.getReal(), y = start_point.getImaginary();
		x += point.x / zoom_factor;
		y -= point.y / zoom_factor;
		zoom_factor = new_zoom_factor;
		x -= (dimension.width / 2) / zoom_factor;
		y += (dimension.height / 2) / zoom_factor;
		this.start_point = new Complex(x, y);
	}
	
	public void calculateComplexZoom(Complex point, Dimension dimension, double new_zoom_factor) {
		zoom_factor = new_zoom_factor;
		double x = start_point.getReal(), y = start_point.getImaginary();
		x += point.getReal() - start_point.getReal();
		y -= point.getImaginary() + start_point.getImaginary();
		x -= (dimension.width / 2) / zoom_factor;
		y += (dimension.height / 2) / zoom_factor;
		this.start_point = new Complex(x, y);
	}
	
	public Complex calculatePoint(Point point) {
		return new Complex((double) point.x / zoom_factor + start_point.getReal(), (double) point.y / zoom_factor - start_point.getImaginary());
	}
	
	public double getZoomFactor() {
		return this.zoom_factor;
	}
	
	public Complex getStartPoint() {
		return this.start_point;
	}
	
	public int getMaxIterations() {
		return this.max_iter;
	}
	
	public void setZoomFactor(double new_zoom_factor) {
		this.zoom_factor = new_zoom_factor;
	}
	
	public void setStartPoint(Complex new_start_point) {
		this.start_point = new_start_point;
	}
	
	public void setMaxIterations(int max_iter) {
		this.max_iter = max_iter;
	}
	
	public void moveFractale(String type, Dimension dimension) {
		switch(type) {
			case "up":
				this.start_point = new Complex(this.start_point.getReal(), this.start_point.getImaginary() + dimension.height / zoom_factor / 6.0d);
				break;
			case "down":
				this.start_point = new Complex(this.start_point.getReal(), this.start_point.getImaginary() - dimension.height / zoom_factor / 6.0d);
				break;
			case "left":
				this.start_point = new Complex(this.start_point.getReal() - dimension.width / zoom_factor / 6.0d, this.start_point.getImaginary());
				break;
			case "right":
				this.start_point = new Complex(this.start_point.getReal() + dimension.width / zoom_factor / 6.0d, this.start_point.getImaginary());
				break;
			default:
				throw new Error("No such movement type: " + type);
		}
	}
}
