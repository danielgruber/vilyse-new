package videoTrancoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.DBManager;
import model.Project;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;

public class VideoTranscodeThread extends Thread {
	
	/**
	 * the current project.
	 */
	Project project;
	
	/**
	 * the video-file-
	 */
	File file;
	
	/**
	 * the controller which owns this thread.
	 */
	TranscodeController controller;
	
	/**
	 * whether to run through the next bits of data.
	 */
	boolean run = true;
	
	/**
	 * destination-folder.
	 */
	protected String destination;
	
	/**
	 * generates this thread.
	 * 
	 * @param Project p
	 * @param TranscodeController c
	 */
	public VideoTranscodeThread(Project p, TranscodeController c) {
		super();
		project = p;
		file = new File(p.getVideo());
		controller = c;
		destination = this.file.getParentFile().getAbsolutePath() + "/" + this.file.getName() + "-frames";
	}
	
	/**
	 * generates this thread.
	 * 
	 * @param Project p
	 * @param TranscodeController c
	 * @param String destination
	 */
	public VideoTranscodeThread(Project p, TranscodeController c, String d) {
		super();
		project = p;
		file = new File(p.getVideo());
		controller = c;
		destination = d;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * runs this thread.
	 */
	public void run() {
		
		// on shutdown we remove data from destination-folder.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					delete(new File(destination));
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			void delete(File f) throws IOException {
			  if (f.isDirectory()) {
			    for (File c : f.listFiles())
			      delete(c);
			  }
			  if (!f.delete())
			    throw new FileNotFoundException("Failed to delete file: " + f);
			}
		});
		
		try {
			// first try to get some meta-data.
			IContainer container = IContainer.make();
			container.open(this.file.getAbsolutePath(), IContainer.Type.READ, null);
			
			long duration = container.getDuration();
			double fps = container.getStream(0).getFrameRate().getDouble();
			
			if(fps > 0.0)
				project.setFps(fps);
			DBManager.writeProject(project);
			
			System.out.println(duration);
			System.out.println(fps);
			int pictures = Math.round(Math.round(fps) * Math.round(duration) / 1000000);
			
			// clean-up
			container.close();
			
			// create the reader
			IMediaReader reader = ToolFactory.makeReader(this.file.getAbsolutePath());
			// configure it to generate BufferImages
			reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

			System.out.println("Reader made for file " + this.file);
			
			// create the handler to handle pictures
			TranscodePictureGenerator handler = new TranscodePictureGenerator(destination);
			
			System.out.println("listening...");
			
			// add the handler
			reader.addListener(handler);
			
			// begin to read
			while (run == true && reader.readPacket() == null) {
				if(pictures != 0) {
					this.controller._view.setString("Processing Picture " + Integer.toString(handler.i) + " of " + Integer.toString(pictures));
					
					this.controller._view.setValue(Math.round(handler.i * 100 / pictures));
				} else {
					this.controller._view.setString("Processing Picture " + Integer.toString(handler.i));
				}
				sleep(10);
			}
			
			// clean-up
			reader.close();
			
			if(run == false) {
				this.controller._view.setValue(100);
				this.controller._view.setString("cancelled");
				return;
			}
			
			System.out.println("done");
			
			// set to finished
			this.controller._view.setValue(-1);
			this.controller._view.setString("finishing...");
			this.controller.finish();
		} catch(Exception e) {
			this.controller._view.setString("Transcoding failed: " + e.getMessage());
			this.controller._view.setValue(100);
			System.out.println("Transcoding failed");
			e.printStackTrace();
		}
	}

}
