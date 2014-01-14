package pointSelection;

import java.awt.Color;

import javax.swing.JPanel;

public class PictureLine {
	private double x1; 
    private double y1;
    private double x2;
    private double y2;   
    private Color color;
    private JPanel view;
    
    
    public PictureLine(double x1, double y1, double x2, double y2, Color color, JPanel parent) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.view = parent;
    }


	/**
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}


	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(double x1) {
		this.x1 = x1;
		view.repaint();
	}


	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}


	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(double y1) {
		this.y1 = y1;
		view.repaint();
	}


	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}


	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(double x2) {
		this.x2 = x2;
		view.repaint();
	}


	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}


	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(double y2) {
		this.y2 = y2;
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
	 * @return the view
	 */
	public JPanel getView() {
		return view;
	}

    
    
}
