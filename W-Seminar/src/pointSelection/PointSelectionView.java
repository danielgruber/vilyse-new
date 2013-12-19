package pointSelection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import model.DBManager;
import model.Picture;
import model.PicturePoint;
import model.PicturePointGroup;
import model.Project;
import view.ViewManager;
import view.WindowView;
import classes.ImageScaler;

import com.csvreader.CsvWriter;

import controller.Core;
import controller.ViewController;
import fileSelector.StartScreenController;

public class PointSelectionView extends WindowView  implements ActionListener, MouseListener {

	/**
	 * the title.
	 */
	String title = "Pictures";
	
	final public double PICTURE_POINTS_HEIGHT = 1200;
	public double PICTURE_POINTS_WIDTH;
	
	/**
	 * the pictures-model.
	 */
	List<Picture> pictures;
	
	protected PointSelectionController c;
	
	/**
	 * colors for the points.
	 */
	Color[] colors = new Color[]{Color.RED, new Color(113, 174, 0), Color.BLUE, new Color(179,79,0), new Color(228, 44, 141), new Color(217, 171, 25), new Color(153, 39, 178)};
	
	
	/**
	 * from here there are all view-elements defined.
	 * 
	 * panel with big picture.
	 */
	PictureView selectPanel;
	
	/**
	 * this panel is for all pictures in the bottom area of the screen.
	 */
	JPanel picturePanel = new JPanel();
	
	/**
	 * actions-panel in top-area of screen.
	 */
	JPanel selectPanelAction = new JPanel();
	
	/**
	 * panel for point-groups.
	 */
	JPanel pointGroupPanel;
	
	/**
	 * list for pointgroups.
	 */
	public final LinkedList<PicturePointGroup> pointGroups = new LinkedList<PicturePointGroup>();
	
	/**
	 * list for points.
	 */
	public final LinkedList<PicturePoint> points = new LinkedList<PicturePoint>();
	
	/**
	 * the right panel for the point-controls in top-area of screen.
	 */
	JPanel pointPanel = new JPanel();
	
	/**
	 * bottom-area of screen.
	 */
	JScrollPane pictureScrollPane = new JScrollPane(picturePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	JLabel activeImage;
	JLabel firstImage;
	
	BufferedImage tempImage;
	
	JMenu fileMenu = new JMenu("File");
	JMenu pictureMenu = new JMenu("Picture");
	JMenu editMenu = new JMenu("Edit");
	
	JMenuItem export = new JMenuItem("Export");
	JMenuItem removePoints = new JMenuItem("Remove points");
	JMenuItem setStart = new JMenuItem("Set as first Picture");
	
	JMenuItem selectScale = new JMenuItem("Set scale");
	JMenuItem selectFps = new JMenuItem("Set fps");
	
	boolean selectDistanceStartOnClick = false;
	Point selectDistanceStart = null;
	
	/**
	 * shows the number of the current point to be selected.
	 */
	PicturePointGroup currentPointSelect = null;
	
	ViewPicturePoint currentPointSelection = null;
	
	ImageScaler scaler = new ImageScaler();
	
	/**
	 * generates the view with the pictures-model.
	 * 
	 * @param pictures
	 */
	public PointSelectionView(List<Picture> pictures) {
		this.pictures = pictures;
	}

	/**
	 * generates the view the pictures-model and a title.
	 * 
	 * @param pictures
	 * @param title
	 */
	public PointSelectionView(List<Picture> pictures, String folder, String title) {
		super();
		this.pictures = pictures;
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void getWindowContent(Container c) {
		this.c = (PointSelectionController) this.controller;
		
		System.out.println("Getting window content.");
		c.setLayout(new BorderLayout());
		
		selectPanel = new PictureView("");
		selectPanel.setListener(this);
		
		c.add(selectPanel, BorderLayout.CENTER);
		c.add(pictureScrollPane, BorderLayout.SOUTH);
		
		// add data to picture panel
		pictureScrollPane.setPreferredSize(new Dimension(0, 160));
		pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(15);
		pictureScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		picturePanel.setBackground(new Color(255, 255, 255));
		picturePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		picturePanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(220, 220, 220)));
		
		
		InitActionPanel(c);
		
		renderPictures();
	}
	
	/**
	 * renders the pictures.
	 */
	public void renderPictures() {
		
		picturePanel.removeAll();
		
		int i = 0;
		int left = 0;
		for(Picture p: this.pictures) {
			try {
				// get width
				BufferedImage img = ImageIO.read(new File(p.getThumbnail()));
				int width          = Math.round((float)img.getWidth() / (float)img.getHeight() * 110);
				
				
				Image newimg = scaler.scaleImage(img, new Dimension(width, 110));
				ImageIcon image = new ImageIcon(newimg);  
				
				// draw image to UI
				JLabel imageButton = new JLabel(image);
				imageButton.setSize(width, 110);
				imageButton.putClientProperty("image",p.getFilename());
				imageButton.putClientProperty("id",p.getId());
				imageButton.putClientProperty("i", i);
				imageButton.putClientProperty("x", left);
				
				left += width + 10;
				
				imageButton.setBorder(new EmptyBorder(5, 5, 5, 5));
				
				JPanel imagePanel = new JPanel();
				imagePanel.setBackground(new Color(255, 255, 255));
				imagePanel.setLayout(new BorderLayout());
				
				imagePanel.setToolTipText(String.format("Seconds, Picture %02d", p.getPicture()));
				imagePanel.addMouseListener(this);
				
				int picture = p.getPicture(), seconds;
				if(this.c.project.getFps() > 0) {
					seconds = (int) Math.floor(picture / this.c.project.getFps());
					picture = (int) Math.round(picture - seconds * this.c.project.getFps());
				} else {
					seconds = 0;
				}
				
				JLabel imageLabel =  new JLabel(String.format("%d:%02d", seconds, picture), JLabel.CENTER);
				
				
				imageLabel.setBorder(new EmptyBorder(5,5,5,5));
				imageLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
				
				imagePanel.add(imageLabel, BorderLayout.NORTH);
				imagePanel.add(imageButton, BorderLayout.CENTER);
				
				if(this.c.project.getFirstPicture() == p.getId()) {
					imageLabel.setBorder(BorderFactory.createLineBorder(new Color(134, 195, 81), 2));
					firstImage = imageButton;
					activeImage = imageButton;
				} else {
					imageLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
				} 
				
				if(i == 0) {
					activeImage = imageButton;
				}
				
				picturePanel.add(imagePanel);
				
				i++;
			} catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * returns picturePointGroup by id.
	 */
	public PicturePointGroup getPicturePointGroup(int id) {
		for(PicturePointGroup p : pointGroups) {
			if(id == p.getId()) {
				return p;
			}
		}
		
		return null;
	}
	
	/**
	 * returns picturePoint by pointGroupID.
	 */
	public PicturePoint getPicturePointByGroup(int id) {
		for(PicturePoint p : points) {
			if(id == p.getPicturePointGroupID()) {
				return p;
			}
		}
		
		return null;
	}
	
	/**
	 * starts to select a picturepoint by groupid.
	 */
	public void StartSelectPoint(int id) {
		
		
		if(getPicturePointGroup(id) != null) {
			System.out.println("start selecting pioint...");
			currentPointSelect = getPicturePointGroup(id);
			PicturePoint p = getPicturePointByGroup(id);
			if(p != null) {
				points.remove(p);
				renderPoints();
				
				// move mouse to point
				try {
					Robot r = new Robot();
					r.mouseMove(selectPanel.getPixelPosX(p.getX()) + selectPanel.getLocationOnScreen().x, selectPanel.getPixelPosY(p.getY()) + selectPanel.getLocationOnScreen().y);
				} catch(Exception e) {}
				
				currentPointSelection = selectPanel.addPoint(new Point(p.getX(),p.getY()), currentPointSelect.color);
			} else {
			
				currentPointSelection = selectPanel.addPoint(new Point(0,0), currentPointSelect.color);
			}
		}
	}
	
	/**
	 * renders the point-group-panel.
	 */
	public void renderPointGroupPanel() {
		pointGroupPanel.removeAll();
		pointGroupPanel.setLayout(new BoxLayout(pointGroupPanel,BoxLayout.PAGE_AXIS));
		
		
		ImageIcon icon = new ImageIcon(Core.class.getResource("/images/left.png"));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		ImageIcon iconDelete = new ImageIcon(Core.class.getResource("/images/delete.png"));
		iconDelete.setImage(iconDelete.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		int i = 0;
		for(PicturePointGroup p : pointGroups) {
			
			p.color = colors[i];
			p.i = i;
			
			JPanel j = new JPanel();
			j.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			j.setLayout(new BorderLayout());
			
			JPanel panelPoint = new JPanel();
			panelPoint.setLayout(new BorderLayout());
			
			JLabel labelSelect = new JLabel(icon);
			labelSelect.setToolTipText("select " + p.getTitle());
			labelSelect.putClientProperty("id", p.getId());
			panelPoint.add(labelSelect, BorderLayout.WEST);
			labelSelect.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel source = (JLabel) e.getSource();
					int id = Integer.parseInt(source.getClientProperty("id").toString());
					StartSelectPoint(id);
				}
			});
			
			/*JLabel labelDeletePoint = new JLabel(iconDelete);
			labelDeletePoint.setToolTipText("Delete point from picture: " + p.getTitle());
			labelDeletePoint.putClientProperty("id", p.getId());
			labelDeletePoint.putClientProperty("title", p.getTitle());
			labelDeletePoint.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			panelPoint.add(labelDeletePoint, BorderLayout.CENTER);
			labelDeletePoint.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel source = (JLabel) e.getSource();
					int id = Integer.parseInt(source.getClientProperty("id").toString());
					String title = source.getClientProperty("title").toString();
					String currentId = activeImage.getClientProperty("id").toString();
					if(Core.confirm("Do you really want to remove the point on THIS picture of the group '"+title+"'? Attention: you can't undo this operation.")) {
						DBManager.executeSQL("DELETE FROM picturePoint WHERE picturePointGroupID = '"+id+"' AND pictureID = '"+currentId+"'");
						
						reloadPoints();
					}
				}
			});*/ 
			
			j.add(panelPoint, BorderLayout.WEST);
			
			JLabel title = new JLabel(p.getTitle(), JLabel.CENTER);
			title.setForeground(colors[i]);
			title.setFont(new Font(Font.SERIF, Font.BOLD, 18));
			j.add(title, BorderLayout.CENTER);
			
			JLabel labelDelete = new JLabel(iconDelete);
			labelDelete.setToolTipText("delete all points from all pictures of " + p.getTitle());
			labelDelete.putClientProperty("id", p.getId());
			labelDelete.putClientProperty("title", p.getTitle());
			labelDelete.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel source = (JLabel) e.getSource();
					String title = source.getClientProperty("title").toString();
					if(Core.confirm("Do you really want to remove this point-group and ALL points on all pictures of the group '"+title+"'? Attention: you can't undo this operation.")) {
						
						int id = Integer.parseInt(source.getClientProperty("id").toString());
						PicturePointGroup toDelete = getPicturePointGroup(id);
						
						DBManager.executeSQL("DELETE FROM picturePointGroup WHERE id = '"+id+"'");
						DBManager.executeSQL("DELETE FROM picturePoint WHERE picturePointGroupID = '"+id+"'");
						
						reloadPointGroups();
					}
					
				}
			});
			j.add(labelDelete, BorderLayout.EAST);
			
			pointGroupPanel.add(j);
			
			if(i + 1 < colors.length)
				i++;
		}
		
		Core.ViewManager().window.setVisible(true);
	}
	
	/**
	 * renders the points into the image.
	 */
	public void renderPoints() {
		selectPanel.clearPoints();
		
		for(PicturePoint p : points) {
			if(getPicturePointGroup(p.getPicturePointGroupID()) != null) {
				PicturePointGroup g = getPicturePointGroup(p.getPicturePointGroupID());
				selectPanel.addPoint(new Point(p.getX(), p.getY()), g.color, 4);
			}
		}
		
		if(currentPointSelection != null) {
			selectPanel.addPoint(currentPointSelection);
		}
	}
	
	/**
	 * inits the ActionPanel.
	 */
	public void InitActionPanel(Container c) {
		selectPanelAction = new JPanel();
		selectPanelAction.setLayout(new BorderLayout());
		c.add(selectPanelAction, BorderLayout.EAST);
		
		pointGroupPanel = new JPanel();
		selectPanelAction.add(pointGroupPanel, BorderLayout.NORTH);
		
		
		
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.PAGE_AXIS));
		
		JButton addGroup = new JButton("Add new Point-Group");
		addGroup.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(pointGroups.size() >= colors.length) {
					Core.alert("You've reached the maximum number of points.");
					return;
				}
				String prompt = Core.prompt("What's the title of the new group?");
				if(prompt != "") {
					PicturePointGroup p = new PicturePointGroup(prompt);
					DBManager.writePointGroup(p);
					reloadPointGroups();
				}
			}
		});
		actionPanel.add(addGroup);
		
		JButton selectScale = new JButton("Set scale");
		selectScale.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		Core.alert("Click on the big picture to select start-point of Scale.");
        		selectDistanceStartOnClick = true;
        	}
        });
		
		actionPanel.add(selectScale);
		
		selectPanelAction.add(actionPanel, BorderLayout.SOUTH);
		
		reloadPointGroups();
	}
	
	/**
	 * renders the main-image to the top panel and updates the window.
	 * 
	 * @name drawImageMain
	 */
	public void drawImageMain() {
		
		if(activeImage != null) {
			
			
			selectPanel.setImg((String) activeImage.getClientProperty("image"));
			
			reloadPoints();
			
			
			Core.ViewManager().window.setVisible(true);
		}
	}
	
	/**
	 * action when a picture is selected.
	 * 
	 * @param picPanel
	 */
	public void selectPicture(JPanel picPanel) {
		activeImage.setBorder(new EmptyBorder(5, 5, 5, 5));
		activeImage = (JLabel) picPanel.getComponent(1);
		activeImage.setBorder(BorderFactory.createLineBorder(new Color(0, 175, 255), 5));
		
		tempImage = null;
		
		// scroll pane to correct picture
		JScrollBar hsb = pictureScrollPane.getHorizontalScrollBar();
		int left = Integer.parseInt(activeImage.getClientProperty("x").toString());

		if((hsb.getValue() + pictureScrollPane.getWidth() - activeImage.getWidth()) < left) {
			hsb.setValue(left - pictureScrollPane.getWidth() + activeImage.getWidth() + 10);
		} else if(hsb.getValue() > left) {
			hsb.setValue(left - 10);
		}
		
		drawImageMain();
	}
	
	/**
	 * select previous picture on screen.
	 */
	public void goToPrev() {
		int i = 0;
		if(activeImage != null)
			i = Integer.parseInt(Integer.toString((Integer) activeImage.getClientProperty("i")));
		
		i--;
		
		try {
			JPanel newActive = (JPanel) activeImage.getParent().getParent().getComponent(i);
			
			selectPicture(newActive);
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * select next picture on screen.
	 */
	public void goToNext() {
		int i = 0;
		if(activeImage != null)
			i = Integer.parseInt(Integer.toString((Integer) activeImage.getClientProperty("i")));
		
		i++;
		
		try {
			JPanel newActive = (JPanel) activeImage.getParent().getParent().getComponent(i);
			
			selectPicture(newActive);
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * sets the current image as starting point.
	 */
	public void setStart() {
		if(firstImage != null) {
			JLabel label = (JLabel) firstImage.getParent().getComponent(0);
			label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		}
		int currentId = Integer.parseInt(activeImage.getClientProperty("id").toString());
		
		this.c.project.setFirstPicture(currentId);
		firstImage = activeImage;
		JLabel label = (JLabel) firstImage.getParent().getComponent(0);
		label.setBorder(BorderFactory.createLineBorder(new Color(134, 195, 81), 2));
		
		DBManager.writeProject(this.c.project);
	}
	
	/**
	 * removes all points from the current image.
	 */
	public void removePoints() {
		String currentId = activeImage.getClientProperty("id").toString();
		
		DBManager.executeSQL("DELETE FROM picturePoint WHERE pictureId = "+currentId+"");
		
		reloadPointGroups();
	}
	
	
	/**
	 * set basic information about the window and add the menubar to the window.
	 * 
	 * @param ViewManager w
	 */
	@Override
	public void manipulateWindow(ViewManager w) {
        super.manipulateWindow(w);
        
        w.window.setResizable(true);
        w.window.setSize(new Dimension(800, 600));
        w.window.setMinimumSize(new Dimension(640, 500));
        
        selectPicture((JPanel) activeImage.getParent());
        
        // add menu-bar
        Core.ViewManager().menuBar.add(fileMenu);
        
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		int action = JOptionPane.showConfirmDialog(null, "Do you want to save and load a new file?", "Input", JOptionPane.YES_NO_OPTION);
        		if(action == JOptionPane.YES_OPTION) {
        			ViewController controller = new StartScreenController();

                    controller.showView();
        		}
        	}
        });
        fileMenu.add(load);
        
        JMenuItem save = new JMenuItem("Autosaving...");
        save.setEnabled(false);
        
        fileMenu.add(save);
        
        export.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		export();
        		
        	}
        });
        fileMenu.add(export);
        
        Core.ViewManager().menuBar.add(pictureMenu);
        
        removePoints.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		removePoints();
        	}
        });
        
        pictureMenu.add(removePoints);
        
        setStart.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		setStart();
        	}
        });
        pictureMenu.add(setStart);
        
        Core.ViewManager().menuBar.add(editMenu);
        
        selectScale.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		Core.alert("Click on the big picture to select start-point of Scale.");
        		selectDistanceStartOnClick = true;
        	}
        });
        
        editMenu.add(selectScale);
        
        selectFps.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		String fps = Core.prompt("What's the frame-rate of this video?");
				if(fps != "") {
					fps = fps.replace(',', '.');
					Double dfps = 0.0;
					try {
						dfps = Double.parseDouble(fps);
					} catch(Exception exception) {
						Core.alert("There's a problem with your given number: " + exception.getMessage());
					}
					
					if(dfps > 0.0) {
						c.project.setFps(dfps);
						DBManager.writeProject(c.project);
						selectFps.setText("Set fps (currently: "+c.project.getFps()+")");
						renderPictures();
					}
				}
				
        	}
        });
        
        selectFps.setText("Set fps (currently: "+c.project.getFps()+")");
        
        editMenu.add(selectFps);
    }
	
	/**
	 * events
	 */
	@Override
	public void actionPerformed( ActionEvent e ) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JPanel source = (JPanel) e.getSource();
		selectPicture(source);
		
	}
	
	public void mouseClicked(MouseEvent e, Point p) {
		System.out.println(p.x + "." + p.y);
		
		if(selectDistanceStartOnClick) {
			selectDistanceStart = p;
			selectDistanceStartOnClick = false;
			
			System.out.println("Point found");
		} else if(selectDistanceStart != null) {
			// calculate diffs
			int x = Math.abs(selectDistanceStart.x - p.x);
			int y = Math.abs(selectDistanceStart.y - p.y);
			
			if(y < x / 20) {
				y = 0;
			} 
			
			if(x < y / 20) {
				x = 0;
			}
			
			int dist = getDistance();
			if(dist == 0) {
				selectDistanceStart = null;
			} else {
				Project project = this.c.project;
				project.setxScaleStart(selectDistanceStart.x);
				project.setyScaleStart(selectDistanceStart.y);
				project.setxScaleStop(p.x);
				project.setyScaleStop(p.y);
				
				System.out.println(dist);
				
				// get correct scale-values with pythagoras
				double pixels = Math.sqrt(x * x + y * y);
				
				System.out.println(pixels);
				
				double cmPerPixel = Double.parseDouble(Integer.toString(dist)) / pixels;
				project.setScale(cmPerPixel);
				DBManager.writeProject(project);
				
				Core.alert("The scale was set successfully!");
				selectPanel.clearLines();
				
				selectDistanceStart = null;
			}
		} else if(currentPointSelect != null) {
			
			int picID = Integer.parseInt(activeImage.getClientProperty("id").toString());
			PicturePoint point = new PicturePoint();
			point.setPosX(p.getX());
			point.setPosY(p.getY());
			point.setPicturePointGroupID(currentPointSelect.getId());
			point.setPicId(picID);
			DBManager.executeSQL("DELETE FROM picturePoint WHERE pictureID = '"+picID+"' AND picturePointGroupId = '"+currentPointSelect.getId()+"'");
			DBManager.writePoint(point);
			
			currentPointSelect = null;
			currentPointSelection = null;
			
			points.clear();
        	for(PicturePoint pi : DBManager.getPicturePoints(Integer.parseInt(activeImage.getClientProperty("id").toString()))) {
        		points.add(pi);
        	}
        	
		}
	}
	
	public int getDistance() {
		String dist = Core.prompt("Whats the distance represented by the red line in cm? (Only integers are allowed.)");
		if(dist == "")
			return 0;
		
		try {
			int distint = Integer.parseInt(dist);
			return distint;
		} catch(Exception e) {
			Core.alert("You're value isn't a number.");
			return getDistance();
		}
	}
	
	public void mouseMoved(MouseEvent e, Point p) {

		if(selectDistanceStart != null) {
			selectPanel.clearLines();
			
			selectPanel.addLine(selectDistanceStart.x, selectDistanceStart.y, p.x, p.y, Color.RED);
		}
		
		if(currentPointSelection != null) {
			currentPointSelection.setPoint(p);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * on key-press we catch some keys for key-controlling.
	 * 
	 * @param KeyEvent e
	 */
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
			if(currentPointSelect != null) {
				try {
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int x = (int) b.getX();
					int y = (int) b.getY();
					
					Robot r = new Robot();
					r.mouseMove(x + 1, y);
				} catch(Exception ex) {
					
				}
				//r.mouseMove(selectPanel.getPixelPosX(p.getX()) + selectPanel.getLocationOnScreen().x, selectPanel.getPixelPosY(p.getY()) + selectPanel.getLocationOnScreen().y);
			} else {
				goToNext();
			}
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	if(currentPointSelect == null) {
        		goToPrev();
        	} else {
        		try {
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int x = (int) b.getX();
					int y = (int) b.getY();
					
					Robot r = new Robot();
					r.mouseMove(x - 1, y);
				} catch(Exception ex) {
					
				}
        	}
        } else if(e.getKeyCode() == KeyEvent.VK_UP && currentPointSelect != null) {
        	try {
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				
				Robot r = new Robot();
				r.mouseMove(x, y - 1);
			} catch(Exception ex) {
				
			}
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && currentPointSelect != null) {
        	try {
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				
				Robot r = new Robot();
				r.mouseMove(x, y + 1);
			} catch(Exception ex) {
				
			}
        } else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// scroll pane to correct picture
    		JScrollBar hsb = pictureScrollPane.getHorizontalScrollBar();
    		int left = Integer.parseInt(activeImage.getClientProperty("x").toString());
    		
    		hsb.setValue(left - (pictureScrollPane.getWidth() / 2) + (activeImage.getWidth() / 2));
    		
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	if(selectDistanceStart != null) {
        		selectDistanceStart = null;
        		selectPanel.clearLines();
        	} else if(selectDistanceStartOnClick == true) {
        		selectDistanceStartOnClick= false;
        	}
        	
        	currentPointSelect = null;
        	currentPointSelection = null;
        	
        	reloadPoints();
        } else if(e.getKeyCode() == KeyEvent.VK_ENTER&& currentPointSelect != null) {
        	try {
				Robot r = new Robot();
				r.mousePress(InputEvent.BUTTON1_MASK);
				r.mouseRelease(InputEvent.BUTTON1_MASK);
			} catch(Exception ex) {
				
			}
        }
	}
	
	@Override
	public boolean unloadView() {
		Core.ViewManager().menuBar.remove(fileMenu);
		Core.ViewManager().menuBar.remove(pictureMenu);
		Core.ViewManager().menuBar.remove(editMenu);
		return super.unloadView();
	}
	
	public void mouseDragged(MouseEvent e) {
		
		
	}
	
	public void reloadPointGroups() {
		
		pointGroups.clear();
		for(PicturePointGroup p : DBManager.getPicturePointGroups()) {
			pointGroups.add(p);
		}
		
		renderPointGroupPanel();
		
		reloadPoints();
		
	}
	
	public void reloadPoints() {
		if(activeImage != null) {
			points.clear();
	    	for(PicturePoint p : DBManager.getPicturePoints(Integer.parseInt(activeImage.getClientProperty("id").toString()))) {
	    		points.add(p);
	    	}
	    	
	    	renderPoints();
		}
	}
	
	public void export() {
		c.export(this.pictures, this.pointGroups);
	}
	
	public double roundPrec(double org, int precision) {
		BigDecimal bd = new BigDecimal(org);
	    BigDecimal rounded = bd.setScale(precision, RoundingMode.HALF_UP);
	    return rounded.doubleValue();
	}
}
