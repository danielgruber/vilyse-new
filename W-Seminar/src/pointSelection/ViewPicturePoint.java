package pointSelection;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JPanel;

public class ViewPicturePoint {
	private Point point;
	
	private Color color;
	
	private JPanel view;
	
	private int size;
	    
	
	public ViewPicturePoint(Point p, Color c, int s, JPanel v) {
		point = p;
		color = c;
		view = v;
		size = s;
	}
	
	public ViewPicturePoint(Point p, Color c, JPanel v) {
		point = p;
		color = c;
		view = v;
		size = 4;
	}

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
		view.repaint();
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		view.repaint();
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * sets the view which should be repainted if this point changes
	 * @param v
	 */
	public void setView(JPanel v) {
		view = v;
	}
}
