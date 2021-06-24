package util;

public class Complex {
	private double r;
	private double i;

	public Complex(double real, double imaginary) {
		this.r = real;
		this.i = imaginary;
	}
	
	public double absolute() {
		double pow_r = r * r;
		double pow_i = i * i; 
		return Math.sqrt(pow_i + pow_r);
	}
	
	public Complex add(Complex toAdd) {
		Complex c = this;
		double realSum = c.r + toAdd.r; 
		double imaginarySum = c.i + toAdd.i; 
		return new Complex(realSum, imaginarySum);
	}
	
	public Complex subtract(Complex toSubtract) {
		Complex c = this;
		double realSub = c.r - toSubtract.r;
		double imaginarySub = c.i - toSubtract.i;
		return new Complex(realSub, imaginarySub);
	}
	
	// z1 · z2 = (x1 * x2 - y1 * y2) + i(x1 * y2 + x2 * y1)
	public Complex multiply(Complex toMultiply) {
		Complex c = this;
		double realMult = c.r * toMultiply.r - c.i * toMultiply.i; //x1 * x2 - y1 * y2
		double imaginaryMult = c.r * toMultiply.i + c.i * toMultiply.r; // x1 * y2 + x2 * y1
		return new Complex(realMult, imaginaryMult);
	}
	
	public double getReal() {
		return this.r;
	}
	
	public double getImaginary() {
		return this.i;
	}
}
