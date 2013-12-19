package pointSelection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import classes.ImageScaler;

public class PictureView extends JPanel implements MouseListener, ImageObserver, MouseMotionListener {
	
	final public double PICTURE_POINTS_HEIGHT = 1200;
	public double PICTURE_POINTS_WIDTH;
	
	/**
	 * the image which is shown in this pictureView.
	 */
	private String img;
	
	/**
	 * we need the imagescaler to fast update the image to the correct size.
	 */
	private ImageScaler scaler = new ImageScaler();
	
	/**
	 * controller for the events.
	 */
	Object listener;
	
	/**
	 * the image.
	 */
	BufferedImage image;

	private final LinkedList<PictureLine> lines = new LinkedList<PictureLine>();
	
	private final LinkedList<ViewPicturePoint> points = new LinkedList<ViewPicturePoint>();
	
	int currentImgWidth;
	
	int currentImgHeight;
	
	int currentMarginLeft;
	
	int currentMarginTop;
	
	public PictureView(String img) {
		super();
		
		this.setImg(img);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		this.setPreferredSize(new Dimension(400, 300));
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
		File i = new File(img);
		if(i.exists()) {
			try {
				image = ImageIO.read(i);
				repaint();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * adds a black Line to the picture.
	 * 
	 * @param int 	x1 start x
	 * @param int 	y1 start y
	 * @param int 	x2 end x 
	 * @param int 	y2 end y
	 * 
	 * @return the added line, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public PictureLine addLine(int x1, int y1, int x2, int y2) {
	    return addLine(x1, y1, x2, y2, Color.black);
	}

	/**
	 * adds a Line to the picture.
	 * 
	 * @param int 	x1 start x
	 * @param int 	y1 start y
	 * @param int 	x2 end x 
	 * @param int 	y2 end y
	 * @param Color color
	 * 
	 * @return the added line, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public PictureLine addLine(int x1, int y1, int x2, int y2, Color color) {
		PictureLine l = new PictureLine(x1,y1,x2,y2, color, this);
	    lines.add(l);        
	    repaint();
	    return l;
	}

	public void clearLines() {
	    lines.clear();
	    repaint();
	}
	
	/**
	 * adds a Point given by ViewPicturePoint
	 * 
	 * @param Point where to add in grid.
	 * 
	 * @return the added point, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public ViewPicturePoint addPoint(ViewPicturePoint p) {
		points.add(p);
		
		repaint();
		
		return p;
	}
	
	
	/**
	 * adds a Point with size 4 and color black to the picture.
	 * 
	 * @param Point where to add in grid.
	 * 
	 * @return the added point, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public ViewPicturePoint addPoint(Point p) {
		return addPoint(p, Color.black);
	}
	
	/**
	 * adds a Point with size 4 to the picture.
	 * 
	 * @param Point where to add in grid.
	 * @param Color of the point.
	 * 
	 * @return the added point, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public ViewPicturePoint addPoint(Point p, Color c) {
		ViewPicturePoint pp = new ViewPicturePoint(p, c, this);
		
		points.add(pp);
		repaint();
		
		return pp;
	}
	
	/**
	 * adds a Point to the picture.
	 * 
	 * @param Point where to add in grid.
	 * @param Color of the point.
	 * @param int	size of this point.
	 * 
	 * @return the added point, so you can update the point on the fly and the repaint is automatically done on the view.
	 */
	public ViewPicturePoint addPoint(Point p, Color c, int s) {
		ViewPicturePoint pp = new ViewPicturePoint(p, c, s, this);
		
		points.add(pp);
		repaint();
		
		return pp;
	}
	
	
	public void clearPoints() {
	    points.clear();
	    repaint();
	}
	
	public int getImageWidth() {
		return currentImgWidth;
	}
	public int getImageHeight() {
		return currentImgHeight;
	}

	/**
	 * @return the listener
	 */
	public Object getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(Object listener) {
		this.listener = listener;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		//System.out.println("paint pictureView");
		
	    super.paintComponent(g);
	    
	    if(image != null) {
		    int maxWidth = this.getWidth();
		    int maxHeight = this.getHeight();
		    
		    int imgWidth = image.getWidth();
		    int imgHeight = image.getHeight();
		    
		    int height, width, marginLeft, marginTop;
		    
		    //System.out.println(maxWidth + "/" + maxHeight + "/" + imgWidth + "/" + imgHeight);
		    
		    int heightByWidth = Math.round(imgHeight *  maxWidth / imgWidth);
		    if(heightByWidth > maxHeight) {
		    	height = maxHeight;
		    	width = Math.round(imgWidth *  maxHeight / imgHeight);
		    	marginLeft = Math.round((maxWidth - width) / 2);
		    	marginTop = 0;
		    } else {
		    	height = heightByWidth;
		    	width = maxWidth;
		    	marginTop = Math.round((maxHeight - height) / 2);
		    	marginLeft = 0;
		    }
		    
		    currentImgWidth = width;
		    currentImgHeight = height;
		    currentMarginLeft = marginLeft;
		    currentMarginTop = marginTop;
		    
		    PICTURE_POINTS_WIDTH = imgWidth * PICTURE_POINTS_HEIGHT / imgHeight;
		    
		    Image newImg = scaler.scaleImage(image, new Dimension(width, height));
		    g.drawImage(newImg, marginLeft, marginTop, width, height, this);
		    
		    for (PictureLine line : lines) {
		        g.setColor(line.getColor());
		        
		        int x1 = Integer.parseInt(Long.toString(Math.round(line.getX1() * width / PICTURE_POINTS_WIDTH + marginLeft)));
		        int x2 = Integer.parseInt(Long.toString(Math.round(line.getX2() * width / PICTURE_POINTS_WIDTH + marginLeft)));
		        int y1 = Integer.parseInt(Long.toString(Math.round(line.getY1() * height / PICTURE_POINTS_HEIGHT + marginTop)));
		        int y2 = Integer.parseInt(Long.toString(Math.round(line.getY2() * height / PICTURE_POINTS_HEIGHT + marginTop)));
		        
		        //System.out.println("Line " + x1 + "/" + y1 + " -> " + x2 + "/" + y2);
		        
		        g.drawLine(x1,y1,x2,y2);
		    }
		    
		    for (ViewPicturePoint point : points) {
		        g.setColor(point.getColor());
		        
		        int x = Integer.parseInt(Long.toString(Math.round(point.getPoint().x * width / PICTURE_POINTS_WIDTH + marginLeft))) - Math.round(point.getSize() / 2);
		        int y = Integer.parseInt(Long.toString(Math.round(point.getPoint().y * width / PICTURE_POINTS_WIDTH + marginTop))) - Math.round(point.getSize() / 2);
		        
		        //System.out.println(x + "/" + y);
		        
		        g.drawOval(x,y,point.getSize(),point.getSize());
		    }
		    
		   
	    }
	    
	    //System.out.println("Infos: currentImgWidth: " + currentImgWidth + "; currentImgHeight: " + currentImgHeight + ";currentMarginTop:" + currentMarginTop + ";currentMarginLeft:" + currentMarginLeft);
	    
	}
	
	public int getPixelPosX(int x) {
		return Integer.parseInt(Long.toString(Math.round(x * currentImgWidth / PICTURE_POINTS_WIDTH + currentMarginLeft)));
	}
	
	public int getPixelPosY(int y) {
		return Integer.parseInt(Long.toString(Math.round(y * currentImgHeight / PICTURE_POINTS_HEIGHT + currentMarginTop)));
	}
	
	
	/**
	 * calls a given event with given params of given types
	 * it calls on all event-handlers first and then on the controller
	 *
	 *@name callEvent
	 *@param String - name of the method
	 *@param Object - list of types of the params
	 *@param Object - params
	 */
	public void callEvent(String method, Class[] types, Object[] params) {
		
		if(this.listener instanceof Object) {
			Method m = null;
			try{
                Class c = this.listener.getClass();            
		        m = c.getDeclaredMethod(method,types);                    
		    }
		    catch(Exception e){}
			
			try{
				if(m != null)
					m.invoke(this.listener,params);
			} catch(Exception e) {
				e.printStackTrace();
				e.getCause().printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseClicked", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseEntered", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseExited", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mousePressed", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseReleased", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseDragged", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = Integer.parseInt(Long.toString(Math.round((e.getPoint().getX() - currentMarginLeft) / currentImgWidth * PICTURE_POINTS_WIDTH)));
		int y = Integer.parseInt(Long.toString(Math.round((e.getPoint().getY() - currentMarginTop) / currentImgHeight * PICTURE_POINTS_HEIGHT)));;
		Point p = new Point(x,y);
		
		if(x < 0 || x > PICTURE_POINTS_WIDTH) {
			return;
		} else if(y < 0 || y > PICTURE_POINTS_HEIGHT) {
			return;
		}
		
		callEvent("mouseMoved", new Class[]{MouseEvent.class, Point.class}, new Object[]{e, p});
	}
}

