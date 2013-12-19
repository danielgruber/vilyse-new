package model;

import java.util.Date;
import java.util.List;

public class Picture {
	
	/**
	 * id of this picture.
	 */
	int id = 0;
	
	/**
	 * filename for this picture.
	 */
	String filename;
	
	String thumbnail;
	
	int picture;
	
	boolean disabled;
	
	public Picture() {
	}

	public Picture(int id, String filename, String thumbnail, int picture, boolean disabled) {
		this.id = id;
		this.filename = filename;
		this.thumbnail = thumbnail;
		this.picture = picture;
		this.disabled = disabled;
	}
	
	public Picture( String filename) {
		this.filename = filename;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public List<PicturePoint> getPoints() {
		return DBManager.getPicturePoints(id);
	}

	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return the picture
	 */
	public int getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(int picture) {
		this.picture = picture;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
