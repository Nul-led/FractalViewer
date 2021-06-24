package util;

import java.awt.Color;

public class ColorUtils {
	private static final Color[] COLOR_PALETTE = {
		new Color(66, 30, 15), 
		new Color(25, 7, 26), 
		new Color(9, 1, 47),
		new Color(4, 4, 73),
		new Color(0, 7, 100),
		new Color(12, 44, 138),
		new Color(24, 82, 177),
		new Color(57, 125, 209),
		new Color(134, 181, 229),
		new Color(211, 236, 248),
		new Color(241, 233, 191),
		new Color(248, 201, 95),
		new Color(255, 170, 0),
		new Color(204, 128, 0),
		new Color(153, 87, 0),
		new Color(106, 52, 3)
	};
	
	public static int getColor(String type, int iterations, int max_iter, double offset, int theme) {
		switch(type) {
			case "time_escape":
				return timeEscape(iterations, max_iter, theme);
			case "smooth_coloring":
				return smoothSpectrum(iterations, max_iter, offset);
			case "palette":
				return palette(iterations, max_iter);
			default:
				throw new Error("No such coloring algorithm: " + type);
		}
	}
	
	private static int smoothSpectrum(int iterations, int max_iter, double offset) {
		// https://stackoverflow.com/questions/369438/smooth-spectrum-for-mandelbrot-set-rendering
		if(iterations == 0 || iterations == max_iter) return 0;
		int colorInt = ( iterations + (int) (offset * max_iter) ) % max_iter;
		float hsbColor = colorInt / (float) max_iter;
		return Color.getHSBColor(hsbColor, 0.8f, 1).getRGB();
	}
	
	private static int palette(int iterations, int max_iter) {
		if(iterations == 0 || iterations == max_iter) return 0;
		return COLOR_PALETTE[iterations % COLOR_PALETTE.length].getRGB();
	}
	
	private static int timeEscape(int iterations, int max_iter, int theme) {
		return iterations == max_iter ? 0 : new Color(iterations * theme).getRGB();
	}
}
