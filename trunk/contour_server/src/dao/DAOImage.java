package dao;

import java.util.ArrayList;

public interface DAOImage {
	
	/**
	 * This method returns all images assigned to a specific track.
	 * @param trackname name of track
	 * @param language	languagecode (e.g. en, de)
	 * @return list of images within the track and language (important for name)
	 */
	public ArrayList<String> getImages(String trackname, String language);
}
