/**
 * this class generates images and writes them to given location.
 * 
 *@package Videoanalyse\Transcoder
 *@license: LGPL http://www.gnu.org/copyleft/lesser.html
 *@author Daniel Gruber
 *
 *@version 1.0
*/

package videoTrancoder;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.DBManager;
import model.Picture;
import classes.ImageScaler;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;

import controller.Core;

public class TranscodePictureGenerator extends MediaToolAdapter {
	
	/**
	 * destination-path.
	 */
	private String destination;
	
	/**
	 * numbers of images
	 */
	public int i = 1;
	
	/**
	 * the file
	 */
	File f;
	
	ImageScaler scaler = new ImageScaler();
	
	/**
	 * generates the Transcode-Picture-Object.
	 * 
	 * @param String destination the folder to put the files in
	 */
	public TranscodePictureGenerator(String destination) {
		// TODO Auto-generated constructor stub
		this.destination = destination;
		File folder = new File(destination);
		folder.mkdirs();
		
		//DBManager.executeSQL("DELETE FROM picture");
		
		System.out.println(destination);
	}

	@Override
    public void onVideoPicture(IVideoPictureEvent event) {
        
        BufferedImage image = event.getJavaData();
        if(image == null)
        	image = event.getImage();
       
        //System.out.println(event.getPicture());
        
        System.out.println(this.destination + "/" + i + ".jpg");
        
        File f = new File(this.destination + "/" + i + ".jpg");
        File thumb = new File(this.destination + "/" + i + "-200.jpg");
        try {
        	f.createNewFile();
        	ImageIO.write(image, "jpg", f);
        	
        	float c = (float) image.getWidth() / (float) image.getHeight();
        	int width          = Math.round(c * 200);
			Image newimg = scaler.scaleImage(image, new Dimension(width, 200));
			ImageIO.write(Core.getBufferedImage(newimg), "jpg", thumb);
			
			// try to find active picture
			Picture p = DBManager.getPictureByTime(i);
			if(p != null) {
				p.setThumbnail(thumb.getAbsolutePath());
				p.setFilename(f.getAbsolutePath());
				DBManager.WritePicture(p);
				
				System.out.println("found, so update");
			} else {
				
				// recreate picture
				p = new Picture(f.getAbsolutePath());
				p.setPicture(i);
	        	p.setThumbnail(thumb.getAbsolutePath());
	        	DBManager.WritePicture(p);
			}
        	
        	
        	newimg = null;
        	image = null;
        	p = null;
        } catch(IOException e) {
        	
        }
        
        i++;
        
        super.onVideoPicture(event);
    }
}
