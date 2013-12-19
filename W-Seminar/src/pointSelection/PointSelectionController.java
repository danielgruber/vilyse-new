package pointSelection;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.csvreader.CsvWriter;

import model.DBManager;
import model.Picture;
import model.PicturePointGroup;
import model.Project;
import controller.Core;
import controller.ViewController;

public class PointSelectionController extends ViewController {

	/**
	 * the pointer to the view.
	 */
	public PointSelectionView _view;
	
	public Project project;
	
	public PointSelectionController(Project p) {
		
		super();
		
		this.project = p;
		
		this._view = new PointSelectionView(DBManager.getPictures());
		this._view.setController(this);
	}

	@Override
	public void showView() {
		Core.ViewManager().setView(_view);
	}
	
	public void export(List<Picture> pictures, List<PicturePointGroup> pointGroups) {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");   
		 
		
		
		File f = new File("data.csv");
		fileChooser.setSelectedFile(f);
		 
		fileChooser.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                return f.getName().toLowerCase().endsWith(".csv");
            }

            public String getDescription() {
                return "csv-File (*.csv)";
            }
        });
		
		int userSelection = fileChooser.showSaveDialog(Core.ViewManager().window);
		
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		    
		    CsvWriter writer = new CsvWriter(fileToSave.getAbsolutePath());
		    try {
    		    writer.setUseTextQualifier(true);
    		    writer.setDelimiter(';');
    		    writer.write("t");
    		    
    		    for(PicturePointGroup p : pointGroups) {
    		    	writer.write("");
    		    	writer.write(p.getTitle() + "-x");
    		    	writer.write(p.getTitle() + "-y");
    		    	writer.write(p.getTitle() + "-s");
    		    }
    		    
    		    writer.endRecord();
    		    
    		    double scale = project.getScale();
    		    System.out.println(scale);
    		    
    		    for(Picture p: pictures) {
    		    	double time = (p.getPicture() - project.getFirstPicture()) / project.getFps();
    		    	
    		    	if(p.getPoints().size() > 0) {
    		    	
    		    		writer.write(Double.toString(_view.roundPrec(time, 2)));
    		    		
    		    		for(PicturePointGroup pg : pointGroups) {
    		    			ResultSet rs = DBManager.get("picturePoint", new String[] { "id", "posX", "posY",
    		    					"picturePointGroupID", "pictureID" }, new String[] { "pictureID", "picturePointGroupID" },
    		    					new String[] { Integer.toString(p.getId()), Integer.toString(pg.getId()) });
    		    			
    		    			try {
    		    				writer.write("");
    		    				if (rs.next()) {
    		    					
    		    					double x = rs.getDouble("posX");
    		    					double y = rs.getDouble("posY");
    		    					
    		    					double xCm = x * scale;
    		    					
    		    					double yCm = y  * scale;
    		    					
    		    					double s = Math.sqrt(x * x + y * y);
    		    					
    		    					writer.write(Double.toString(_view.roundPrec(xCm, 2)));
    		    					writer.write(Double.toString(_view.roundPrec(yCm, 2)));
    		    					writer.write(Double.toString(_view.roundPrec(s, 2)));
    		    				} else {
    		    					writer.write("");
    		    					writer.write("");
    		    					writer.write("");
    		    				}
    		    				rs.close();
    		    			} catch (SQLException e) {
    		    				System.err.println("Couldn't fetch data");
    		    				e.printStackTrace();
    		    			}

    		    		}
    		    		writer.endRecord();
    		    			
    		    	}
    		    	
    		    	
    		    	
    		    }
    		    

    		    writer.close();
    		    
    		    
		    } catch(Exception ex) {
		    	ex.printStackTrace();
		    }
		}
	}
	
}
