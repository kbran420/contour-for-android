package dao;

import java.util.ArrayList;

public interface DAOTrack {
	
	/**
	 * This method returns all (unique) tracknames in a certain language
	 * @param language languagecode (e.g. en, de)
	 * @return list of all tracknames defined in the database
	 */
	public ArrayList<String> getTracks(String language);
	
	
}
