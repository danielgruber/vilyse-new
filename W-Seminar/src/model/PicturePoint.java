package model;

public class PicturePoint {
	
	int id = 0;
	
	double posX = 0;
	
	double posY = 0;
	
	int picturePointGroupID = 0;
	
	int picId = 0;
	
	public PicturePoint() {
		// TODO Auto-generated constructor stub
	}

	public PicturePoint(int id, double posX, double posY, int group, int picId) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.picturePointGroupID = group;
		this.picId = picId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public int getPicturePointGroupID() {
		return picturePointGroupID;
	}

	public void setPicturePointGroupID(int picturePointGroupID) {
		this.picturePointGroupID = picturePointGroupID;
	}
	
	public PicturePointGroup PicturePointGroup() {
		return DBManager.getPicturePointGroup(picturePointGroupID);
	}
	
	public int getX() {
		return Integer.parseInt(Long.toString(Math.round(posX)));
	}
	
	public int getY() {
		return Integer.parseInt(Long.toString(Math.round(posY)));
	}

	/**
	 * @return the picId
	 */
	public int getPicId() {
		return picId;
	}

	/**
	 * @param picId the picId to set
	 */
	public void setPicId(int picId) {
		this.picId = picId;
	}

}
