package pointSelection;

import java.awt.Color;

import javax.swing.JPanel;

public class PictureLine {
	private int x1; 
    private int y1;
    private int x2;
    private int y2;   
    private Color color;
    private JPanel view;
    
    
    public PictureLine(int x1, int y1, int x2, int y2, Color color, JPanel parent) {
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
	public int getX1() {
		return x1;
	}


	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
		view.repaint();
	}


	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}


	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
		view.repaint();
	}


	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}


	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
		view.repaint();
	}


	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}


	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(int y2) {
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
