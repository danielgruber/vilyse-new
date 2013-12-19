package controller;

import view.ViewChangeListener;

abstract public class ViewController implements ViewChangeListener {
	/**
     * sets the view visible.
     */
    abstract public void showView();
    
    /**
     * unload
     */
    public boolean unload() {
    	return true;
    }
}
