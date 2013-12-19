package fileSelector;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileAcceptController extends FileFilter {
	
	/**
	 * a simple array of strings matching.
	 * 
	 * @var Array of Strings
	 */
	String[] allowed = new String[] {"mov", "avi", "mpg", "mpeg", "mp4", "vil"};
	
	/**
	 * it returns true if the provided file matches a valid type.
	 * 
	 * @param File f 
	 */
	public boolean accept(File f) {
			
		
		if(f.isDirectory())
			return true;
		
		String lowername = f.getName().toLowerCase();
		for(int i = 0; i < allowed.length; i++) {
			if(lowername.endsWith("." + allowed[i]))
				return true;
		}
		
		return false;
	}
	
	/**
	 * generates the description for the user-selection.
	 */
	public String getDescription() {
		
		String files = "";
		for(int i = 0; i < allowed.length; i++) {
			if(i != 0) {
				files += ", ";
			}
			
			files += allowed[i];
		}
		
		return "Video-files ("+files+")";
	}
}