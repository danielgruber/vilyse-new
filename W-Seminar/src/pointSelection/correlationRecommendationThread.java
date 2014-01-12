package pointSelection;

import model.Picture;
import model.PicturePoint;

public class correlationRecommendationThread extends Thread {
	
	correlationRecommendationProtocol notifier;
	
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

	public void updatePoint(PicturePoint p) {
		
	}
	
	public void updateImage(Picture p) {
		
	}
	
	/**
	 * the running-method of the thread.
	 */
	public void run() {
		try {
			while(true) {
				
				
					sleep(100);
				
			}
		} catch(Exception e) {
			
		}
	}
}

interface correlationRecommendationProtocol {
	public void updatePointOnImage(PicturePoint p);
}