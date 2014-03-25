package domain;

import java.util.ArrayList;

public class Session {
	
	private Track track;
	private float score;
	private ArrayList<Drawing> userDrawings;
	
	public Session(Track track) {
		this.track = track;
		this.score = 0f;
		this.userDrawings = new ArrayList<Drawing>();
	}
	
	public Track getTrack() {
		return this.track;
	}
	
	public void addToScore(float value) {
		this.score += value;
	}
	
	public float getScore() {
		return this.score;
	}
	
	public void saveUserDrawing(Drawing drawing) {
		this.userDrawings.add(drawing);
	}
}
