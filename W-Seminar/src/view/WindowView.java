package view;
import java.awt.Container;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import controller.ViewController;
import controller.W_Controller;
/**
 * Beschreiben Sie hier die Klasse View.
 * 
 * @author Daniel Gruber
 * @version 20.06.2013
 */
abstract public class WindowView extends Thread
{
	
	/**
	 * this list contains all event-listeners, which listen to events of this class
	 * 
	 *@name listeners
	 *@access private
	 */
	private List<ViewChangeListener> listeners = new ArrayList<ViewChangeListener>();
	
	/**
	 * this is the controller, which manages this class
	 * 
	 *@name controller
	 *@access private
	 */
	protected ViewController controller;
	
	/**
	 * this is a reference to the ViewManager.
	 */
	public ViewManager viewManager;
	
	/**
	 * Inits the window
	 */
	public void Init() {
		
	}
	
	/**
	 * constructor without titlte
	 */
	public WindowView() {
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
		
		if(this.controller instanceof ViewController) {
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
	public void setController(ViewController control) {
		this.controller = control;
	}
    
    /**
     * returns the title.
     */
    abstract public String getTitle();
    
    public void manipulateWindow(ViewManager w) {
    	viewManager = w;
        w.window.setTitle(this.getTitle());
    }
    
    abstract public void getWindowContent(Container c);
    
    public boolean unloadView() {
    	return this.controller.unload();
    }
}
