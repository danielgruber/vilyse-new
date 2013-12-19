/**
 * this is the transcoder, which transcodes the video and shows the progress to the user.
 * 
 *@package Videoanalyse\Transcoder
 *@license: LGPL http://www.gnu.org/copyleft/lesser.html
 *@author Daniel Gruber
 *
 *@version 1.0
*/
package videoTrancoder;


import java.io.File;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.DBManager;
import model.Project;
import pointSelection.PointSelectionController;
import controller.Core;
import controller.ViewController;

/**
 * @author Daniel gruber
 *
 */
public class TranscodeController extends ViewController {
	/**
	 * the pointer to the view.
	 */
	public ProgressView _view;
	
	/**
	 * the file to transcode.
	 */
	private File file;
	
	/**
	 * the thread to transcode
	 */
	VideoTranscodeThread thread;
	
	public Project project;
	
	/**
	 * 
	 */
	public TranscodeController(Project p) {
		super();
		
		project = p;
		file = new File(p.getVideo());
		
		this._view = new ProgressView("Transcoding Video", 100);
		this._view.setController(this);
	}
	
	/**
	 * this is the handler who transcodes the video.
	 */
	public void TranscodeVideo() {
		System.out.println("Transcoding...");
		this._view.setValue(-1);
		this._view.setString("Loading file " + project.getVideo());
		
		try {
			if(System.getProperty("java.io.tmpdir") != "") {
				thread = new VideoTranscodeThread(project, this, System.getProperty("java.io.tmpdir") + "/vilyse." + file.getName() + "-frames/");
			} else {
				thread = new VideoTranscodeThread(project, this);
			}
			thread.start();
			
			if(project.getFps() == 0.0) {
				String fps = Core.prompt("What's the frame-rate of this video?");
				if(fps != "") {
					fps = fps.replace(',', '.');
					project.setFps(Double.parseDouble(fps));
					DBManager.writeProject(project);
				}
				
			}
		} catch(Exception e) {
			
		}
	}
	
	public void finish() {
		/*try {
			FileWriter f = new FileWriter(thread.destination + "/project.vyl");
			BufferedWriter out = new BufferedWriter(f);
			
			out.write("");
			//Close the output stream
			out.close();
		} catch(Exception e) {}*/
		
		thread.run = false;
		PointSelectionController c = new PointSelectionController(project);
		c.showView();
	}
	
	public void cancelThread() {
		System.out.println("stopthread");
		thread.run = false;
	}
	
	/**
     * sets the view visible.
     */
    public void showView() {
    	Core.ViewManager().setView(_view);
    	TranscodeVideo();
    }
    
    /**
     * adds events and listeners to the view.
     */
    void addListener() {
    	
    }
    
    public boolean unload() {
    	if(thread.run) {
    		return Core.confirm("Do you really want to cancel?");
    	}
        
    	return true;
    }

}
