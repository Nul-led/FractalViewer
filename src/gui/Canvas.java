package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private Dimension dimension;
	private Point mouse_location;
	private BufferedImage image;
	private Gui parent;
	
	public Canvas(Dimension dimension, Gui parent) {
		this.dimension = dimension;
		this.parent = parent;
		this.image = new BufferedImage(this.dimension.width, this.dimension.height, BufferedImage.TYPE_INT_RGB);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setFocusable(true);
	}
	
	public Point getCurrentMouseLocation() {
		return this.mouse_location;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setImage(BufferedImage buffer) {
		this.image = buffer;
		repaint();
	}
	
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouse_location = e.getPoint();
		parent.updateMouse(mouse_location);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			parent.zoomFractal(e.getPoint(), "in");
		} else if(SwingUtilities.isRightMouseButton(e)) {
			parent.zoomFractal(e.getPoint(), "out");
		} else if(SwingUtilities.isMiddleMouseButton(e)) {
			parent.showJuliaSet(e.getPoint());
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
			case 'w':
				parent.moveFractal("up");
				break;
			case 's':
				parent.moveFractal("down");
				break;
			case 'a':
				parent.moveFractal("left");
				break;
			case 'd':
				parent.moveFractal("right");
				break;
			case 'f':
				parent.saveScreenshot();
				break;
		}
	}
	
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}
}
