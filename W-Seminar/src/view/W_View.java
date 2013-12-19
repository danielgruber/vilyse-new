package view;

/**
 * this is the basic class for all views
 * implements the observer-design-pattern
 * and it implements a windowed experience
 * 
 *@package Videoanalyse
 *@license: LGPL http://www.gnu.org/copyleft/lesser.html
 *@author Daniel Gruber
 * last modified: 03.05.2013
 * $Version 1.0
*/

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import controller.W_Controller;

public class W_View extends JFrame {
	/**
	 * for serialisation we need this UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * this list contains all event-listeners, which listen to events of this class
	 * 
	 *@name listeners
	 *@access private
	 */
	private List<ViewChangeListener> listeners = new ArrayList<ViewChangeListener>();
	
	/**
	 * default close operation.
	 * 
	 * default: JFrame.EXIT_ON_CLOSE
	 * 
	 * @var int
	 */
	int defaultClose = JFrame.EXIT_ON_CLOSE;
	
	/**
	 * this is the controller, which manages this class
	 * 
	 *@name controller
	 *@access private
	 */
	private W_Controller controller;
	
	/**
	 * Inits the window
	 */
	public void Init() {
		setLocation(50, 50);
		setDefaultCloseOperation(defaultClose);
		
		
	}
	
	/**
	 * constructor without titlte
	 */
	public W_View() {
		super("");
		Init();
	}
	
	/**
	 * constructor with title
	 */
	public W_View(String title) {
		super(title);
		Init();
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
		for (ViewChangeListener name : listeners) {
			Method m = null;
			try{
                Class c = name.getClass();            
		        m = c.getDeclaredMethod(method,types);                    
		    }
		    catch(Exception e){}
			
			try{
				if(m != null)
					m.invoke(name,params);
			} catch(Exception e) {
				e.printStackTrace();
			}
        }
		
		if(this.controller instanceof W_Controller) {
			Method m = null;
			try{
                Class c = this.controller.getClass();            
		        m = c.getDeclaredMethod(method,types);                    
		    }
		    catch(Exception e){}
			
			try{
				if(m != null)
					m.invoke(this.controller,params);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * adds a event-listener to the list of listeners
	 * 
	 *@name addChangeListener
	 *@param newListener
	 */
	public void addChangeListener(ViewChangeListener newListener) {
        listeners.add(newListener);
    }
	
	/**
	 * sets the Controller of this class
	 * 
	 *@name setController
	 *@param control
	 */
	public void setController(W_Controller control) {
		this.controller = control;
	}
}
