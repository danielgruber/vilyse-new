package model;

public class Project {
	
	int id;
	
	double fps;
	
	String video;
	
	String videomd5;
	
	double xScaleStart;
	
	double xScaleStop;
	
	double yScaleStart;
	
	double yScaleStop;
	
	double scale;

	int firstPicture = 0;
	
	public Project(int id, double fps, String video, String videomd5, double xScaleStart, double xScaleStop, double yScaleStart, double yScaleStop, double Scale, int firstPicture) {
		this.id = id;
		this.fps = fps;
		this.video = video;
		this.videomd5 = videomd5;
		this.xScaleStart = xScaleStart;
		this.xScaleStop = xScaleStop;
		this.yScaleStart = yScaleStart;
		this.yScaleStop = yScaleStop;
		this.scale = scale;
		this.firstPicture = firstPicture;
	}
	
	/**
	 * @return the fps
	 */
	public double getFps() {
		return fps;
	}

	/**
	 * @param fps the fps to set
	 */
	public void setFps(double fps) {
		this.fps = fps;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the video
	 */
	public String getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	public void setVideo(String video) {
		this.video = video;
	}

	/**
	 * @return the videomd5
	 */
	public String getVideomd5() {
		return videomd5;
	}

	/**
	 * @param videomd5 the videomd5 to set
	 */
	public void setVideomd5(String videomd5) {
		this.videomd5 = videomd5;
	}

	/**
	 * @return the xScaleStart
	 */
	public double getxScaleStart() {
		return xScaleStart;
	}

	/**
	 * @param xScaleStart the xScaleStart to set
	 */
	public void setxScaleStart(double xScaleStart) {
		this.xScaleStart = xScaleStart;
	}

	/**
	 * @return the xScaleStop
	 */
	public double getxScaleStop() {
		return xScaleStop;
	}

	/**
	 * @param xScaleStop the xScaleStop to set
	 */
	public void setxScaleStop(double xScaleStop) {
		this.xScaleStop = xScaleStop;
	}

	/**
	 * @return the yScaleStart
	 */
	public double getyScaleStart() {
		return yScaleStart;
	}

	/**
	 * @param yScaleStart the yScaleStart to set
	 */
	public void setyScaleStart(double yScaleStart) {
		this.yScaleStart = yScaleStart;
	}

	/**
	 * @return the yScaleStop
	 */
	public double getyScaleStop() {
		return yScaleStop;
	}

	/**
	 * @param yScaleStop the yScaleStop to set
	 */
	public void setyScaleStop(double yScaleStop) {
		this.yScaleStop = yScaleStop;
	}



	/**
	 * @return the firstPicture
	 */
	public int getFirstPicture() {
		return firstPicture;
	}

	/**
	 * @param firstPicture the firstPicture to set
	 */
	public void setFirstPicture(int firstPicture) {
		this.firstPicture = firstPicture;
	}

	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
}
