package controller;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import view.ViewManager;
import fileSelector.StartScreenController;

public class Core {
	
	/**
	 * the icon of the app.
	 */
	Image logo;
	
	/**
	 * the active ViewManager
	 */
	public static ViewManager viewManager;
	
	/**
	 * indicator if OSX or not.
	 */
	public static boolean MAC_OS_X = (System.getProperty("os.name")
			.toLowerCase().startsWith("mac os x"));
	
	/**
	 * menu for vilyse.
	 */
	JMenu appmenu = new JMenu(APP_NAME);
	
	/**
	 * App-Specific Infos.
	 */
	public static String VERSION = "0.4.3";
	public static String APP_NAME = "Viylse";
	public static String LOGO = "/images/logo.png";
	
	/**
	 * preference-API
	 */
	private static Preferences prefs;
	
	/**
	 * about-box.
	 */
	public static JDialog aboutBox;
	protected JLabel labelName;
	protected JLabel labelCopy;
	protected JLabel labelVersion;
	protected JLabel labelIcon;
	protected JLabel labelLicense;

	static StartScreenController controller;
  	
    /**
     * Diese Klasse wird nur dazu benutzt alle nštigen
     * Komponenten zu Initialisieren und die erste
     * View anzuzeigen
     */
    public static void main(final String [] args){
    	System.setProperty("com.apple.mrj.application.apple.menu.about.name", APP_NAME);
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    	
    	new Core();
    }
	
	/**
	 * inits some Basic APIs every Application should use.
	 * 
	 * - preferences
	 * - OSX-Specific Stuff
	 * - About-Window
	 * - About-Window for Windows and Linux
	 * - Menubar on top
	 */
	public Core() {
		
		viewManager = new ViewManager();
		
		// init preferences
		prefs = Preferences.userRoot().node(this.getClass().getName());
		
		InitAbout();
		
		// init OSX-Specific settings
		InitOSX();
		
		// add Vilyse to menu-bar on non-OSX-Computers
		if (!MAC_OS_X) {
			ViewManager().menuBar.add(appmenu);
			
			JMenuItem about = new JMenuItem("About " + APP_NAME);
			about.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					aboutBox.setVisible(true);
				}
			});
			appmenu.add(about);
			
			JMenuItem quit = new JMenuItem("Quit " + APP_NAME);
			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			appmenu.add(quit);
		}
		
		
		controller = new StartScreenController();

        controller.showView();
	}
	
	/**
	 * the preference-API to handle some user-specific settings. 
	 * 
	 * @return Preferences
	 */
	public static Preferences Prefs() {
		return prefs;
	}

	/**
	 * the viewmanager handles all WindowsViews currently showing on the application-frame.
	 * 
	 * @return ViewManager
	 */
	public static ViewManager ViewManager() {
		return viewManager;
	}
	
	/**
	 * converts a normal image to a buffered image on all platforms.
	 * 
	 * @param img the image to convert
	 * @return BufferedImage
	 */
	public static BufferedImage getBufferedImage(Image img) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = bi.getGraphics();
		graphics.drawImage(img, 0, 0, null);
		graphics.dispose();

		return bi;
	}
	
	/**
	 * inits the about-box for showing copyright, icon and name of app.
	 */
	public void InitAbout() {
		// init about-box
		aboutBox = new JDialog(new JFrame(), "About " + APP_NAME);
        aboutBox.getContentPane().setLayout(new BorderLayout());
        aboutBox.getContentPane().add(labelName = new JLabel(APP_NAME, JLabel.CENTER), BorderLayout.CENTER);
        aboutBox.addKeyListener(new KeyAdapter(){
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        			aboutBox.setVisible(false);
        		}
        	}
        });
        
        // icon of the application
 		try {
 		    logo = ImageIO.read(getClass().getResource(LOGO));
 		    Image newimg = logo.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
 		    
 		    ImageIcon image = new ImageIcon(newimg);  
 			// draw image to UI
 	        labelIcon = new JLabel(image);
 	        labelIcon.setBorder(new EmptyBorder(10, 10, 10, 10));
 	        aboutBox.getContentPane().add(labelIcon, BorderLayout.NORTH);
 		} catch(Exception e) {
 			e.printStackTrace();
 		}
        
 		// generate text at the bottom
 		JPanel copyVersionPanel = new JPanel();
        copyVersionPanel.setLayout(new BorderLayout());
        copyVersionPanel.add(labelVersion = new JLabel("Version " + VERSION, JLabel.CENTER), BorderLayout.NORTH);
        copyVersionPanel.add(labelCopy = new JLabel("\u00A9 2013 Daniel Gruber", JLabel.CENTER), BorderLayout.CENTER);
        copyVersionPanel.add(labelLicense = new JLabel("License: GPL v3 http://www.gnu.org/copyleft/gpl.html", JLabel.CENTER), BorderLayout.SOUTH);
        aboutBox.getContentPane().add(copyVersionPanel, BorderLayout.SOUTH);
        
        labelLicense.setBorder(new EmptyBorder(10, 10, 10, 10));
        labelName.setFont(new Font("Helvetica", Font.BOLD, 16));
        labelName.setBorder(new EmptyBorder(10, 10, 10, 10));
        labelVersion.setFont(new Font("Helvetica", Font.PLAIN, 12));
        labelVersion.setBorder(new EmptyBorder(10, 10, 10, 10));
        labelCopy.setFont(new Font("Helvetica", Font.PLAIN, 12));
        labelLicense.setFont(new Font("Helvetica", Font.PLAIN, 10));
        
        labelLicense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelLicense.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				browse("http://www.gnu.org/copyleft/gpl.html");
			}
		});
        
        aboutBox.setSize(240, 210);
        aboutBox.setLocation(50, 50);
        aboutBox.setResizable(false);
	}
	
	/**
     * inits some basic optimisations for Mac OS X.
     */
    public void InitOSX() {
    	
    	if (MAC_OS_X) {
    		System.setProperty("apple.laf.useScreenMenuBar", "true");
    		
    		
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                //OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
                //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }
    
    // General quit handler; fed to the OSXAdapter as the method to call when a system quit event occurs
    // A quit event is triggered by Cmd-Q, selecting Quit from the application or Dock menu, or logging out
    public void quit() { 
    	System.exit(0);
    }
    
    // General info dialog; fed to the OSXAdapter as the method to call when 
    // "About OSXAdapter" is selected from the application menu
    public void about() {
        Core.aboutBox.setVisible(true);

    }
    
    public static void alert(String text) {
    	ImageIcon icon = new ImageIcon(Core.class.getResource(LOGO));
    	icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    	JOptionPane.showMessageDialog(null, text, "", JOptionPane.DEFAULT_OPTION, icon);
    }
    
    public static boolean confirm(String text) {
    	ImageIcon icon = new ImageIcon(Core.class.getResource(LOGO));
    	icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
    	int action = JOptionPane.showConfirmDialog(null, text, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
    	return (action == JOptionPane.YES_OPTION);
    }
    
    public static String prompt(String text) {
    	return JOptionPane.showInputDialog(null, text, "", JOptionPane.QUESTION_MESSAGE);
    }
    
    private static void browse(String url) {
        // first try the Java Desktop
        
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE))
                try {
                    desktop.browse(URI.create(url));
                    return;
                } catch (IOException e) {
                    // handled below
                }
        }
        // Next try rundll32 (only works on Windows)
        
        try {
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
            return;
        } catch (IOException e) {
            // handled below
        }
        // Next try browsers
       
        //BareBonesBrowserLaunch.openURL(url);
    }
}