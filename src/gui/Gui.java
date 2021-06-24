package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import fractales.Mandelbrot;
import util.AutoZoom;
import util.Complex;
import util.Generator;

public class Gui extends JFrame implements ActionListener {
	/*
	 * Configuration
	 */
	private static final double DEFAULT_ZOOM_FACTOR = 300.0d;
	private static final Complex DEFAULT_STARTING_POINT = new Complex(-3.0d, 1.25d);
	private static final int DEFAULT_MAX_ITERATIONS = 200;
	private static final Dimension DEFAULT_FRACTAL_DIMENSION = new Dimension(1600, 800);
	private static final String DEFAULT_FRACTAL = "Mandelbrot";
	private static final int DEFAULT_THEME = 0x100000;
	private static final String DEFAULT_COLORING_ALGORITHM = "time_escape";
	private static final double DEFAULT_OFFSET = 0.0d;
	
	/*
	 * Globals
	 */
	
	private double offset = DEFAULT_OFFSET;
	private int theme = DEFAULT_THEME;
	private String coloring_algorithm = DEFAULT_COLORING_ALGORITHM;

	private JMenuBar bar;
	private JMenu mn_getValues, mn_setValues, mn_move, mn_coloring, mn_more;
	private JMenuItem it_get_iter, it_get_zoom, it_get_theme, it_get_offset;
	private JMenuItem it_set_iter, it_set_theme, it_set_offset;
	private JMenuItem it_move_up, it_move_down, it_move_left, it_move_right;
	private JMenuItem it_color_time_escape, it_color_smooth, it_color_palette;
	private JMenuItem it_auto_zoom, it_show_julia;
	
	private JPanel pn_bottom; 
	private JLabel lb_mouse, lb_complexMouse;
	private Canvas canvas;
	
	private Mandelbrot fractal;
	
	public Gui() {
		fractal = new Mandelbrot(DEFAULT_ZOOM_FACTOR, DEFAULT_STARTING_POINT, DEFAULT_MAX_ITERATIONS);

		setTitle("Fractal Viewer - " + DEFAULT_FRACTAL);
		setLayout(new BorderLayout());
		
		initComponents();
		setMinimumSize(new Dimension(DEFAULT_FRACTAL_DIMENSION.width, DEFAULT_FRACTAL_DIMENSION.height + 100));
		setSize(new Dimension(DEFAULT_FRACTAL_DIMENSION.width, DEFAULT_FRACTAL_DIMENSION.height + 100));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void initComponents() {
		bar = new JMenuBar();
		
		mn_getValues = new JMenu("Get Values");
		mn_setValues = new JMenu("Set Values");
		mn_move = new JMenu("Move");
		mn_coloring = new JMenu("Coloring");
		mn_more = new JMenu("More");
		
		it_get_iter = new JMenuItem("Max Iterations");
		it_get_theme = new JMenuItem("Theme");
		it_get_zoom = new JMenuItem("Zoom");
		it_get_offset = new JMenuItem("Offset");
		
		mn_getValues.add(it_get_iter);
		mn_getValues.add(it_get_theme);
		mn_getValues.add(it_get_offset);
		mn_getValues.add(it_get_zoom);
		
		it_set_offset = new JMenuItem("Offset");
		it_set_iter = new JMenuItem("Max Iterations");
		it_set_theme = new JMenuItem("Theme");
		
		mn_setValues.add(it_set_iter);
		mn_setValues.add(it_set_theme);
		mn_setValues.add(it_set_offset);
		
		it_move_up = new JMenuItem("Up");
		it_move_down = new JMenuItem("Down");
		it_move_left = new JMenuItem("Left");
		it_move_right = new JMenuItem("Right");
		
		mn_move.add(it_move_up);
		mn_move.add(it_move_down);
		mn_move.add(it_move_left);
		mn_move.add(it_move_right);
		
		it_color_time_escape = new JMenuItem("Time Escape");
		it_color_smooth = new JMenuItem("Smooth Coloring");
		it_color_palette = new JMenuItem("Palette Coloring");
		
		mn_coloring.add(it_color_time_escape);
		mn_coloring.add(it_color_smooth);
		mn_coloring.add(it_color_palette);
		
		it_auto_zoom = new JMenuItem("Auto Zoom");
		it_show_julia = new JMenuItem("Show Julia Set");
		
		mn_more.add(it_auto_zoom);
		mn_more.add(it_show_julia);
		
		bar.add(mn_getValues);
		bar.add(mn_setValues);
		bar.add(mn_move);
		bar.add(mn_coloring);
		bar.add(mn_more);
		
		setJMenuBar(bar);
		
		canvas = new Canvas(DEFAULT_FRACTAL_DIMENSION, this);
		add(canvas, BorderLayout.CENTER);
		
		pn_bottom = new JPanel();
		
		lb_mouse = new JLabel("Current Mouse Position: x = 0, y = 0");
		lb_complexMouse = new JLabel("Current Complex Point for Mouse Position: real = 0.0, imaginär = 0.0");
		
		pn_bottom.setLayout(new GridLayout(2, 1));
		pn_bottom.add(lb_mouse);
		pn_bottom.add(lb_complexMouse);
		add(pn_bottom, BorderLayout.SOUTH);
		
		initListeners();
		
		calculateFractal();
	}
	
	private void initListeners() {
		it_get_iter.addActionListener(this);
		it_get_theme.addActionListener(this);
		it_get_offset.addActionListener(this);
		it_get_zoom.addActionListener(this);
		it_set_iter.addActionListener(this);
		it_set_theme.addActionListener(this);
		it_set_offset.addActionListener(this);
		it_move_up.addActionListener(this);
		it_move_down.addActionListener(this);
		it_move_left.addActionListener(this);
		it_move_right.addActionListener(this);
		it_color_time_escape.addActionListener(this);
		it_color_smooth.addActionListener(this);
		it_color_palette.addActionListener(this);
		it_auto_zoom.addActionListener(this);
		it_show_julia.addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == it_get_iter) {
			JOptionPane.showMessageDialog(null, "Current Max Iterations: " + fractal.getMaxIterations());
		}
		else if(e.getSource() == it_get_theme) {
			JOptionPane.showMessageDialog(null, "Current Theme (Time Escape): 0x" + Integer.toHexString(theme));
		}
		else if(e.getSource() == it_get_offset) {
			JOptionPane.showMessageDialog(null, "Current Offset (Smooth Coloring): " + offset);
		}
		else if(e.getSource() == it_get_zoom) {
			JOptionPane.showMessageDialog(null, "Current Zoom Factor: " + fractal.getZoomFactor());
		}
		else if(e.getSource() == it_set_iter) {
			String s = JOptionPane.showInputDialog("Input new Max Iterations");
			if(s == null || !s.matches("\\d+")) return;
			fractal.setMaxIterations(Integer.valueOf(s));
			calculateFractal();
		}
		else if(e.getSource() == it_set_theme) {
			String s = JOptionPane.showInputDialog("Input new Time Escape Theme");
			if(s == null || !s.matches("0x[0-9A-F]{6}+")) return;
			theme = Integer.decode(s);
			calculateFractal();
		}
		else if(e.getSource() == it_set_offset) {
			String s = JOptionPane.showInputDialog("Input new Smooth Coloring Offset");
			if(s == null || !s.matches("\\d+.\\d+")) return;
			offset = Double.valueOf(s);
			calculateFractal();
		}
		else if(e.getSource() == it_move_up) {
			moveFractal("up");
		}
		else if(e.getSource() == it_move_down) {
			moveFractal("down");
		}
		else if(e.getSource() == it_move_left) {
			moveFractal("left");
		}
		else if(e.getSource() == it_move_right) {
			moveFractal("right");
		}
		else if(e.getSource() == it_color_time_escape) {
			coloring_algorithm = "time_escape";
			calculateFractal();
		}
		else if(e.getSource() == it_color_smooth) {
			coloring_algorithm = "smooth_coloring";
			calculateFractal();	
		}
		else if(e.getSource() == it_color_palette) {
			coloring_algorithm = "palette";
			calculateFractal();
		}
		else if(e.getSource() == it_auto_zoom) {
			try {
				new AutoZoom();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == it_show_julia) {
			String pointString = JOptionPane.showInputDialog("Insert the point to show julia set for (real:imaginary)");
			if(pointString == null) return;
			pointString.trim();
			int markerPosition = pointString.indexOf(":");
			double real = Double.valueOf(pointString.substring(0, markerPosition));
			double imaginary = Double.valueOf(pointString.substring(1 + markerPosition));
			showJuliaSetWithComplex(new Complex(real, imaginary));
		}
	}
	
	public void zoomFractal(Point p, String type) {
		switch(type) {
			case "in":
				fractal.calculateZoom(p, DEFAULT_FRACTAL_DIMENSION, fractal.getZoomFactor() * 2.0d);
				calculateFractal();
				break;
			case "out":
				fractal.calculateZoom(p, DEFAULT_FRACTAL_DIMENSION, fractal.getZoomFactor() / 2.0d);
				calculateFractal();
				break;
			default:
				throw new Error("No such zooming option");
		}
	}
	
	public Complex computePoint(Point p) {
		return fractal.calculatePoint(p);
	}
	
	public void updateMouse(Point p) {
		lb_mouse.setText("Current Mouse Position: x = " + p.x + ", y = " + p.y);
		Complex cPoint = fractal.calculatePoint(p);
		lb_complexMouse.setText("Current Complex Point for Mouse Position: real = " + cPoint.getReal() + ", imaginary = " + cPoint.getImaginary());
	}
	
	public void moveFractal(String type) {
		fractal.moveFractale(type, DEFAULT_FRACTAL_DIMENSION);
		calculateFractal();
	}
	
	public String getColoringAlgorithm() {
		return coloring_algorithm;
	}
	
	public int getTheme() {
		return theme;
	}
	
	public double getOffset() {
		return offset;
	}
	
	public void saveScreenshot() {
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(imageFilter);
		fileChooser.setDialogTitle("Specify a file to save");
		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File outputfile = fileChooser.getSelectedFile();
		    try {
				ImageIO.write(canvas.getImage(), "png", outputfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void calculateFractal() {
		BufferedImage buffer = Generator.getFractal(DEFAULT_FRACTAL, DEFAULT_FRACTAL_DIMENSION, fractal.getStartPoint(), fractal.getMaxIterations(), fractal.getZoomFactor(), this);
		canvas.setImage(buffer);
	}

	public void showJuliaSet(Point point) {
		BufferedImage buffer = Generator.getFractal("Julia", new Dimension(800, 600), fractal.calculatePoint(point), fractal.getMaxIterations(), 1.0d, null);
		JFrame f = new JFrame("Fractal Viewer - Julia Set");
		f.setSize(new Dimension(800, 600));
		ImageIcon icon = new ImageIcon(buffer);
		JLabel lb = new JLabel(icon);
		f.add(lb);
		f.addMouseListener(new MouseListener() {
			double julia_zoom_factor = 1.0d;
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == 1) julia_zoom_factor *= 2;
				else if(e.getButton() == 3) julia_zoom_factor /= 2;
				BufferedImage buffer = Generator.getFractal("Julia", new Dimension(800, 600), fractal.calculatePoint(point), fractal.getMaxIterations(), julia_zoom_factor, null);
				ImageIcon icon = new ImageIcon(buffer);
				lb.setIcon(icon);
			}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
		});
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public void showJuliaSetWithComplex(Complex point) {
		BufferedImage buffer = Generator.getFractal("Julia", new Dimension(800, 600), point, fractal.getMaxIterations(), 1.0d, null);
		JFrame f = new JFrame("Fractal Viewer - Julia Set");
		f.setSize(new Dimension(800, 600));
		ImageIcon icon = new ImageIcon(buffer);
		JLabel lb = new JLabel(icon);
		f.add(lb);
		f.addMouseListener(new MouseListener() {
			double julia_zoom_factor = 1.0d;
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == 1) julia_zoom_factor *= 2;
				else if(e.getButton() == 3) julia_zoom_factor /= 2;
				BufferedImage buffer = Generator.getFractal("Julia", new Dimension(800, 600), point, fractal.getMaxIterations(), julia_zoom_factor, null);
				ImageIcon icon = new ImageIcon(buffer);
				lb.setIcon(icon);
			}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
		});
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
