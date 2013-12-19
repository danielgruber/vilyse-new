package videoTrancoder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import view.ViewManager;
import view.WindowView;

public class ProgressView extends WindowView implements ActionListener {
	
	/**
	 * the progress-bar.
	 */
	JProgressBar bar;
	
	/**
	 * label for text to show.
	 */
	JLabel label;
	
	/**
	 * cancel-button
	 */
	JButton cancel;
	
	/**
	 * the length of the task.
	 */
	int length = 100;
	
	String title;
	
	/**
	 * constructor.
	 * 
	 * @param String title
	 */
	public ProgressView(String title, int maximum) {
		this.title = title;
		this.length = maximum;
	}
	
	/**
	 * gets the title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * inits the form with the progressbar
	 *
	 */
	public void getWindowContent(Container c) {
		
		c.setLayout(new BorderLayout(10, 10));
		bar = new JProgressBar(0, this.length);
		
		label = new JLabel("");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.BOTTOM);
		label.setPreferredSize(new Dimension(0, 40));
		
		cancel = new JButton("cancel");
		cancel.setVisible(true);
		cancel.setHorizontalAlignment(JLabel.CENTER);
		cancel.addActionListener(this);
		cancel.setMargin(new Insets(10, 10, 10, 10));
		
		Container cancelContainer = new JPanel();
		cancelContainer.setLayout(new BorderLayout());
		cancelContainer.add(cancel, BorderLayout.CENTER);
		((JComponent) cancelContainer).setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		
		c.add(label, BorderLayout.NORTH);
		c.add(bar, BorderLayout.CENTER);
		c.add(cancelContainer, BorderLayout.SOUTH);
	}
	
	@Override
	public void manipulateWindow(ViewManager w) {
        super.manipulateWindow(w);
        
        w.window.setResizable(false);
        w.window.setSize(new Dimension(300, 200));
        
    }
	
	/**
	 * sets the value of the progress-bar.
	 * 
	 * @param int newval
	 */
	public void setValue(int newval) {
		if(newval < 0)
			bar.setIndeterminate(true);
		else
			bar.setIndeterminate(false);
		
		bar.setValue(newval);
		if(newval == 100)
			cancel.setVisible(false);
		else
			cancel.setVisible(true);
	}
	
	/**
	 * returns the current value.
	 * 
	 * @return int
	 */
	public int getValue() {
		return bar.getValue();
	}
	
	/**
	 * sets the string shown in the progress-bar.
	 * 
	 * @param String value
	 */
	public void setString(String value) {
		label.setText(value);
	}
	
	/**
	 * returns the current string-value.
	 * 
	 * @return String
	 */
	public String getString() {
		return bar.getString();
	}
	
	public void actionPerformed( ActionEvent e ) {
		int more = JOptionPane.showConfirmDialog(null, "Do you really want to cancel?", "Input", JOptionPane.YES_NO_OPTION);
    	if(more == JOptionPane.YES_OPTION) {
    		callEvent("cancelThread", new Class[] {}, new Object[] {});
    	}
	}
}
