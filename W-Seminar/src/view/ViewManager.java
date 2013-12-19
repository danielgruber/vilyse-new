package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The viewmanager handes some WindowViews and can make transitions between
 * them.
 * 
 * @author Daniel Gruber
 * @version 1.2
 */
public class ViewManager extends Thread implements WindowListener,
		MouseMotionListener {

	/**
	 * current view.
	 */
	protected WindowView view;

	/**
	 * the window, which is managed by this WindowManager
	 */
	public JFrame window;

	Container currentC;

	/**
	 * the public menu-bar of the application.
	 */
	public JMenuBar menuBar = new JMenuBar();

	/**
	 * Konstruktor für Objekte der Klasse WindowManager
	 */
	public ViewManager() {
		window = new JFrame("");
		JFrame.setDefaultLookAndFeelDecorated(true);
		window.setLocation(30, 30);
		window.setSize(640, 480);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		showLoading();

		window.addWindowListener(this);

		/*
		 * window.addComponentListener(new ComponentAdapter() { public void
		 * componentResized(ComponentEvent e) { onWindowResize(e); } });
		 */

		window.setJMenuBar(menuBar);

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
	}

	/**
	 * shows the loading screen
	 */
	public void showLoading() {
		// icon of the application
		try {

			ImageIcon image = new ImageIcon(getClass().getResource(
					"/images/loading.gif"));
			// draw image to UI

			System.out.println("show loading");

			currentC = new JLabel(image);
			currentC.setSize(64, 64);
			currentC.setLocation(0, 0);
			currentC.setVisible(true);
			window.add(currentC, BorderLayout.CENTER);
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sets the view.
	 */
	public boolean setView(WindowView _view) {
		if (unload() == false) {
			return false;
		}

		this.view = _view;
		updateWindow();

		return true;
	}

	public boolean unload() {
		if (this.view != null) {
			if (this.view.unloadView() == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * gets the view.
	 */
	public WindowView getView() {
		return this.view;
	}

	/**
	 * updates the window from the view. this is called frequently when the view
	 * changes or some other data should be changed.
	 */
	public void updateWindow() {

		System.out.println("updating Window");
		if (this.view instanceof WindowView) {
			// get the container
			Container newC = new Container();

			newC.setLayout(new BorderLayout(10, 10));
			newC.addMouseMotionListener(this);

			System.out.println("created container");

			this.view.getWindowContent(newC);

			System.out.println("got content");

			// remove all old containers from the view
			if (currentC instanceof Container)
				this.window.remove(currentC);

			// add the new container
			this.window.add(newC, BorderLayout.CENTER);

			System.out.println("manipulate");
			// let the view manipulate the window
			this.view.manipulateWindow(this);

			System.out.println("component listener");

			newC.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					onWindowResize(e);
				}
			});

			// lets go
			System.out.println("Window updated");
			currentC = newC;

			System.out.println("Showing window");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					window.setVisible(true);
				}
			});

		}
	}
	
	public void onWindowResize(ComponentEvent e) {
		callEvent("onWindowResize", new Class[] { ComponentEvent.class },
				new Object[] { e });
	}

	public void windowClosing(WindowEvent e) {
		if (unload() == true) {
			System.exit(0);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		callEvent("windowActivated", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	@Override
	public void windowClosed(WindowEvent e) {
		callEvent("windowClosed", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		callEvent("windowDeactivated", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		callEvent("windowDeiconified", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	@Override
	public void windowIconified(WindowEvent e) {
		callEvent("windowIconified", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	@Override
	public void windowOpened(WindowEvent e) {
		callEvent("windowOpened", new Class[] { WindowEvent.class },
				new Object[] { e });
	}

	/**
	 * call the given event on the current view.
	 * 
	 * @name callEvent
	 * @param String
	 *            - name of the method
	 * @param Object
	 *            - list of types of the params
	 * @param Object
	 *            - params
	 */
	public void callEvent(String method, Class[] types, Object[] params) {
		if (this.view != null) {
			Method m = null;
			try {
				Class c = this.view.getClass();
				m = c.getDeclaredMethod(method, types);
			} catch (Exception e) {
			}

			try {
				if (m != null)
					m.invoke(this.view, params);
			} catch (Exception e) {
				e.getCause().printStackTrace();
			}

		}
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				keyPressed(e);
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				keyReleased(e);
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				keyTyped(e);
			}
			return false;
		}
	}

	public void keyPressed(KeyEvent e) {
		callEvent("keyPressed", new Class[] { KeyEvent.class },
				new Object[] { e });
	}

	public void keyReleased(KeyEvent e) {
		callEvent("keyReleased", new Class[] { KeyEvent.class },
				new Object[] { e });
	}

	public void keyTyped(KeyEvent e) {
		callEvent("keyTyped", new Class[] { KeyEvent.class },
				new Object[] { e });
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		callEvent("mouseDragged", new Class[] { MouseEvent.class },
				new Object[] { e });

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		callEvent("mouseMoved", new Class[] { MouseEvent.class },
				new Object[] { e });

	}
}
