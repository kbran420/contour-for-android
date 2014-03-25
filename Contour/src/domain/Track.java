package domain;

import java.util.ArrayList;

public class Track {
	
	private String name;
	private ArrayList<Drawing> images;
	
	public Track(String name, ArrayList<Drawing> images) {
		this.name = name;
		this.images = images;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setImages(ArrayList<Drawing> images) {
		this.images = images;
	}
	
	public ArrayList<Drawing> getImages() {
		return this.images;
	}
}
