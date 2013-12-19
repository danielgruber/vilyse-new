package fileSelector;
import java.io.File;

import model.DBManager;
import model.Project;
import videoTrancoder.TranscodeController;
import controller.Core;
import controller.ViewController;

public class StartScreenController extends ViewController {
    //private WurzelModel _model;
	/**
	 * the pointer to the view.
	 */
	protected StartScreenView _view;
	
	/**
	 * the current project
	 */
	Project p;
	
	/**
	 * constructor.
	 */
    public StartScreenController(){
    	super();
    	
        this._view = new StartScreenView();
    	this._view.setController(this);
        
        addListener();
        
    }
    
    /**
     * adds events and listeners to the view.
     */
    void addListener() {
    	this._view.setFileFilter(new FileAcceptController());
    }
    
    /**
     * is called when the user selects an allowed file.
     * 
     * @param File f
     */
    public void selectFile(File f) {
		System.out.println("Select: " + f.getAbsolutePath());
		
		if(p == null) {
			p = DBManager.InitByFile(f);
		
			if(p.getVideo() == "") {
				this._view.setText("Please select the video for the project-file.");
				showView();
				return;
			}
		
		} else {
			p.setVideo(f.getAbsolutePath());
		}
		
		
		TranscodeController c = new TranscodeController(p);
		c.showView();
	}
    
    public void cancelSelect() {
    	System.exit(0);
    }
    
    /**
     * sets the view visible.
     */
    public void showView() {
    	System.out.println("show view");
    	Core.viewManager.setView(this._view);
    }
}
