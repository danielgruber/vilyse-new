package pointSelection;

import java.util.LinkedList;
import model.Picture;
import model.PicturePoint;

public class correlationRecommendationThread extends Thread {
	
	correlationRecommendationProtocol notifier;
	
	LinkedList <Picture> list = new LinkedList <Picture> ();
	LinkedList <PicturePoint> listp = new LinkedList <PicturePoint> ();
	LinkedList <PicturePoint> listr = new LinkedList <PicturePoint> ();
	boolean updateMatchingData = false ;
	
	/**
	 * constructor.
	 */
	public correlationRecommendationThread() {
		
	}
	
	public correlationRecommendationProtocol getNotifier() {
		return notifier;
	}




	public void setNotifier(correlationRecommendationProtocol notifier) {
		this.notifier = notifier;
	}

	public void updateImage(Picture p) {
		for (Picture picture : list) {
			if	(p.id == picture.id) {
				list.remove (picture);
				list.add(p);
				return;
			
			}
		}
		
		list.add (p);
	}
	
	public void updateKonwnPoint (PicturePoint p) {
		updateMatchingData = true;
		for (PicturePoint picturePoint : listr){
			if (p.id == picturePoint.id) {
				listr.remove (picturePoint);
			}
		
		}
		for (PicturePoint picturePoint : listp) {
			if (p.id == picturePoint.id) {
				listp.remove (picturePoint);
				listp.add (p);
				return;
			}
		}
		listp.add(p);
		
	}
	
	/**
	 * the running-method of the thread.
	 */
	public void run() {
		try {
			while(true) {
				if (listp.size()>0){
					if ( updateMatchingData == true)
					{
						// update MatchingData = false
						updateMatchingData = false;
					}
				}
				
					sleep(100);
				
			}
		} catch(Exception e) {
			
		}
	}
}

interface correlationRecommendationProtocol {
	public void updatePointOnImage(correlationRecommendationThread thread, PicturePoint p);
}
