package controller;

import view.*;

public class W_Controller implements ViewChangeListener {
	/**
	 * the pointer to the view.
	 */
	protected W_View _view;
	
	/**
     * sets the view visible.
     */
    public void showView() {
    	System.out.println("show view");
    	this._view.setVisible(true);
    }
}
