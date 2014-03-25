package domain;

import android.graphics.Bitmap;

public class Drawing {
	
	private String name;
	private Bitmap image;
	
	public Drawing(String name, Bitmap image) {
		this.name = name;
		this.image = image;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public Bitmap getImage() {
		return this.image;
	}
}
