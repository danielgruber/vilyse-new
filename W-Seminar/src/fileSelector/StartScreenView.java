package fileSelector;

/**
 * this is the first screen or window the user interacts with.
 * 
 *@package Videoanalyse\SelectVideo
 *@license: LGPL http://www.gnu.org/copyleft/lesser.html
 *@author Daniel Gruber
 *
 *@version 1.0
*/

import view.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import controller.Core;

public class StartScreenView extends WindowView implements ActionListener {
	
	/**
	 * The filechooser is the first view for the user, there he can select the file he wants.
	 * 
	 * The dot as an param defines, that the working directory is the current directory. maybe we'll change it to ~ in future released.
	 * 
	 * @access public
	 */
	public JFileChooser fc;
	
	JLabel textLabel;
	
	Container c;
	
	public String text;
	
	/**
	 * constructor for startscreen-view.
	 */
	public StartScreenView() {
		super();
		
		System.out.println("trying to get pref");
		
		try {
			String start = Core.Prefs().get("start-folder", ".");
			System.out.println(start);
			File startFile = new File(start);
			if(startFile != null && startFile.exists()) {
				System.out.println("got it");
				fc = new JFileChooser(startFile.getParentFile().getPath());
			} else {
				System.out.println("falling back because not existing");
				Core.Prefs().put("start-folder", ".");
				fc = new JFileChooser(".");
			}
		} catch(Exception e) {
			System.out.println("falling back");
			fc = new JFileChooser(".");
		}
		
	}
	
	/**
	 * gets the title.
	 */
	public String getTitle() {
		return "Select Video";
	}
	
	/**
	 * sets the file-filter for the filechooser. 
	 * 
	 * with a filefilter you can filter the results the chooser shows. in out software, just video-files are allowed.
	 * 
	 * 
	 * @param FileFilter f
	 */
	public void setFileFilter(FileFilter f) {
		fc.setFileFilter(f);
	}
	
	/**
	 * inits the form with the progressbar
	 *
	 */
	public void getWindowContent(Container c) {
		
		
		
		textLabel = new JLabel(text, JLabel.CENTER);
		textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		c.add(textLabel, BorderLayout.NORTH);
		
		fc.setMultiSelectionEnabled(false);
		fc.addActionListener(this);
		fc.setApproveButtonText("Select");
		c.add(fc, BorderLayout.CENTER);
		
		this.c = c;
	}
	
	/**
	 * defines the text for the approve button.
	 * 
	 * @param text
	 */
	public void setApproveButtonText(String text) {
		fc.setApproveButtonText(text);
	}
	
	/**
	 * is called when the selection is approved and the event is triggered to the controller.
	 */
	public void approveSelection() {
		Core.Prefs().put("start-folder", fc.getSelectedFile().getAbsolutePath());
		callEvent("selectFile", new Class[] {File.class}, new Object[] {fc.getSelectedFile()});
		
		System.out.println("Perform approve");
	}
	
	/**
	 * is called when the cancel-button is called.
	 */
	public void cancelSelection() {
		c.remove(fc);
		callEvent("cancelSelect", new Class[] {}, new Object[] {});
		
		System.out.println("Perform cancel");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Perform: " + e.getActionCommand());
		if(e.getActionCommand() == "CancelSelection")
			cancelSelection();
		else if(e.getActionCommand() == "ApproveSelection")
			approveSelection();
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}