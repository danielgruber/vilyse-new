package model;

import java.awt.Color;

public class PicturePointGroup {

	int id = 0;
	
	String title = "x1";
	
	public Color color;
	
	public int i;
	
	public PicturePointGroup() {
	}
	
	public PicturePointGroup(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public PicturePointGroup(String title) {
		this.title = title;
	}

	/**
	 * @return int id
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
	 * @return String title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
