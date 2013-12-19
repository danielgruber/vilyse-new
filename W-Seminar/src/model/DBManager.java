package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

	private static Connection connection;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load the JDBC-Driver.");
			e.printStackTrace();
		}
	}

	/**
	 * creates a new Database-Connection for your given dbFile.
	 * 
	 * @param dbFile
	 */
	public static void Init(String dbFile) {
		// TODO Auto-generated constructor stub
		try {

			System.out.println("Creating Connection to Database...");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			if (!connection.isClosed())
				System.out.println("...Connection established");
			
			InitTables();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (!connection.isClosed() && connection != null) {
						connection.close();
						if (connection.isClosed())
							System.out.println("Connection to Database closed");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * inits a project by a given file. this can be either a video-file or a project-file.
	 * 
	 * @param File the file
	 * @return Project
	 */
	public static Project InitByFile(File f) {
		int i = f.getName().lastIndexOf('.');
		String extension = "";
		if (i > 0) {
		    extension = f.getName().substring(i+1);
		}
		
		// check if it is a project-file
		if(extension.equals("vil")) {
			
			// try to init DB
			Init(f.getAbsolutePath());
			
			Project p = getProject();
			
			// check if file exists
			File videofile = new File(p.getVideo());
			if(videofile.exists()) {
				// check md5 hash
				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
			        InputStream is=new FileInputStream(new File(p.getVideo()));
			        byte[] buffer=new byte[8192];
			        int read=0;
			        while( (read = is.read(buffer)) > 0)
			                md.update(buffer, 0, read);
			        byte[] md5 = md.digest();
			        BigInteger bi=new BigInteger(1, md5);
			        String output = bi.toString(16);
			        
			        if(output.equals(p.getVideomd5())) {
			        	return p;
			        } else {
			        	p.setVideo("");
			        	return p;
			        }
			        
				} catch(Exception e) {
					return p;
				}
				
			} else {
				p.setVideo("");
	        	return p;
			}
		}
		
		// init DB for new file
		String destination = f.getParentFile().getAbsolutePath();
		DBManager.Init(destination + "/"+f.getName()+".vil");
		Project p = getProject();
		p.setVideo(f.getAbsolutePath());
		
		// get md5 hash
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        InputStream is=new FileInputStream(f);
	        byte[] buffer=new byte[8192];
	        int read=0;
	        while( (read = is.read(buffer)) > 0)
	                md.update(buffer, 0, read);
	        byte[] md5 = md.digest();
	        BigInteger bi=new BigInteger(1, md5);
	        String output = bi.toString(16);
	        
	        p.setVideomd5(output);
		} catch(Exception e) {
			
		}
		
		// init DataBase
		DBManager.writeProject(p);
        
        return p;
	}
	
	/**
	 * closes the current DB-Connection.
	 */
	public static void close() {
		try {
			if (!connection.isClosed() && connection != null) {
				connection.close();
				if (connection.isClosed())
					System.out.println("Connection to Database closed");

				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * generates new tables if not existing ones.
	 */
	public static void InitTables() {
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS picture (id INTEGER PRIMARY KEY AUTOINCREMENT, filename varchar(200), thumbnail varchar(200), picture INTEGER, disabled INTEGER)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS picturePointGroup (id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(200))");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS picturePoint (id INTEGER PRIMARY KEY AUTOINCREMENT, pictureID INTEGER, posX double, posY double, picturePointGroupID INTEGER)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS project (id INTEGER PRIMARY KEY AUTOINCREMENT, fps double, video varchar(500), videomd5 varchar(250), xScaleStart double, xScaleStop double, yScaleStart double, yScaleStop double, scale double, firstPicture INTEGER)");
		} catch (SQLException e) {
			System.err.println("Couldn't create DB");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static ResultSet get(String table, String[] fields,
			String[] filterKeys, String[] filterValues) {
		try {
			Statement stmt = connection.createStatement();

			String sql = " SELECT ";
			for (int i = 0; i < fields.length; i++) {
				if (i != 0)
					sql += ",";
				sql += " " + fields[i];
			}

			sql += " FROM " + table;
			if (filterKeys.length > 0
					&& filterKeys.length == filterValues.length) {
				sql += " WHERE ";

				for (int i = 0; i < filterKeys.length; i++) {
					if (i != 0) {
						sql += " AND ";
					}
					sql += " " + filterKeys[i] + " = " + filterValues[i];
				}
			}
			
			// System.out.println(sql);
			
			ResultSet rs = stmt.executeQuery(sql);
			return rs;

		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * returns all PicturePoints known for a given Pictureid.
	 * 
	 * 
	 * @param int 	picID
	 * @return List<PicturePoint>
	 */
	public static List<PicturePoint> getPicturePoints(int picID) {
		ResultSet rs = get("picturePoint", new String[] { "id", "posX", "posY",
				"picturePointGroupID", "pictureID" }, new String[] { "pictureID" },
				new String[] { Integer.toString(picID) });

		List<PicturePoint> l = new ArrayList<PicturePoint>();
		try {
			while (rs.next()) {
				l.add(new PicturePoint(rs.getInt("id"), rs.getDouble("posX"),
						rs.getDouble("posY"), rs.getInt("picturePointGroupID"), rs.getInt("pictureID")));
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}

		return l;
	}
	
	/**
	 * gets a picture by the time in the video.
	 * 
	 * @return Picture or null
	 */
	public static Picture getPictureByTime(int picture) {
		ResultSet rs = get("picture", new String[] { "id", "filename", "thumbnail","picture", "disabled" },
				new String[] {"picture"}, new String[] {String.valueOf(picture)});
		try {
			if(rs.next()) {
				Picture p = new Picture(rs.getInt("id"), rs.getString("filename"), rs.getString("thumbnail"), rs.getInt("picture"), (rs.getInt("disabled") != 0));
				rs.close();
				return p;
			} else {
				rs.close();
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * gets all Pictures from the database.
	 * 
	 * @return List<Picture>
	 */
	public static List<Picture> getPictures() {
		ResultSet rs = get("picture", new String[] { "id", "filename", "thumbnail", "picture", "disabled" },
				new String[] {}, new String[] {});

		List<Picture> l = new ArrayList<Picture>();
		try {
			while (rs.next()) {
				l.add(new Picture(rs.getInt("id"), rs.getString("filename"), rs.getString("thumbnail"), rs.getInt("picture"), (rs.getInt("disabled") != 0)));
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}

		return l;
	}
	
	/**
	 * gets a specfic Picture from the Database.
	 * 
	 * @param int 	id
	 * 
	 * @return Picture
	 */
	public static Picture getPicture(int id) {
		ResultSet rs = get("picture", new String[] { "id", "filename", "thumbnail", "picture", "disabled" },
				new String[] {"id"}, new String[] {Integer.toString(id)});

		try {
			if (rs.next()) {
				rs.close();
				return new Picture(rs.getInt("id"), rs.getString("filename"), rs.getString("thumbnail"), rs.getInt("picture"), (rs.getInt("disabled") != 0));
			}
			rs.close();
			
			return null;
		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}


	}

	/**
	 * gets a specfic PicturePoint from the DataBase.
	 * 
	 * @param int 	id
	 * 
	 * @return PicturePointGroup
	 */
	public static PicturePointGroup getPicturePointGroup(int id) {
		ResultSet rs = get("picturePointGroup", new String[] { "id", "title" },
				new String[] { "id" }, new String[] { Integer.toString(id) });

		try {
			if (rs.next()) {
				PicturePointGroup r = new PicturePointGroup(rs.getInt("id"),
						rs.getString("title"));
				rs.close();
				return r;
			} else {
				rs.close();
				return null;
			}

		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * gets all known PicturePoints from the DataBase.
	 * 
	 * @return List<PicturePointGroup>
	 */
	public static List<PicturePointGroup> getPicturePointGroups() {
		ResultSet rs = get("picturePointGroup", new String[] { "id", "title" },
				new String[] {  }, new String[] { });

		List<PicturePointGroup> l = new ArrayList<PicturePointGroup>();
		try {
			while (rs.next()) {
				l.add(new PicturePointGroup(rs.getInt("id"), rs.getString("title")));
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("Couldn't execute Query");
			e.printStackTrace();
			return null;
		}

		return l;
	}

	/**
	 * writes a picture to the database.
	 * 
	 * @param Picture p
	 * @return boolean
	 */
	public static boolean WritePicture(Picture p) {
		try {
			Statement st = connection.createStatement();
			
			String filename = p.getFilename().replace("'", "\\'");
			String thumb = p.getThumbnail().replace("'", "\\'");
			String pic = Integer.toString(p.getPicture()).replace("'", "\\'");
			String disabled = (p.isDisabled()) ? "1" : "0";
			
			if (p.getId() == 0) {
				st.executeUpdate("INSERT INTO picture (filename, thumbnail, picture, disabled) VALUES ('"+filename+"', '"+thumb+"', '"+pic+"', "+disabled+")");
				
				ResultSet keys = st.getGeneratedKeys();
				if(keys.next()) {
					p.setId(keys.getInt(1));
				}
				keys.close();
			} else {			
				st.executeUpdate("UPDATE picture SET filename = '"+filename+"', thumbnail = '"+thumb+"', picture = '"+pic+"', disabled = "+disabled+" WHERE id = '" + p.getId() + "'");
			}
			st.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * gets the current project from the database or null if none is found.
	 * 
	 * @return Project|null
	 */
	public static Project getProject() {
		try {
			ResultSet rs = get("project", new String[] { "id", "fps", "video", "videomd5", "xScaleStart", "xScaleStop", "scale", "yScaleStart", "yScaleStop", "firstPicture" },
					new String[] {}, new String[] {});
			
			Project p;
			
			if(rs.next()) {
				p = new Project(rs.getInt("id"), rs.getDouble("fps"), rs.getString("video"), rs.getString("videomd5"), rs.getDouble("xScaleStart"), rs.getDouble("xScaleStop"), rs.getDouble("yScaleStart"), rs.getDouble("yScaleStop"), rs.getDouble("scale"), rs.getInt("firstPicture"));
				rs.close();
			} else {
				p = new Project(0, 0.0, "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
			}
			
			return p;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	/**
	 * writes the current project.
	 * 
	 * @param Project 
	 * @return boolean
	 */
	public static boolean writeProject(Project p) {
		try {
			Statement st = connection.createStatement();
			
			String video = p.getVideo().replace("'", "\\'");
			String videomd5 = p.getVideomd5().replace("'", "\\'");
			String xScaleStart = String.valueOf(p.getxScaleStart()).replace("'", "\\'");

			String yScaleStart = String.valueOf(p.getyScaleStart()).replace("'", "\\'");
			
			String xScaleStop = String.valueOf(p.getxScaleStop()).replace("'", "\\'");
			String yScaleStop = String.valueOf(p.getyScaleStop()).replace("'", "\\'");
			
			String scale = String.valueOf(p.getScale()).replace("'", "\\'");
			
			String firstPicture = String.valueOf(p.getFirstPicture()).replace("'", "\\'");
			
			if (p.getId() == 0) {
				st.executeUpdate("INSERT INTO project (video, fps, videomd5, xScaleStart, xScaleStop, yScaleStart, yScaleStop, scale, firstPicture) VALUES ('"+video+"', "+p.getFps()+", '"+videomd5+"', '"+xScaleStart+"', '"+xScaleStop+"','"+yScaleStart+"', '"+yScaleStop+"', '"+scale+"', '"+firstPicture+"')");
				
				ResultSet keys = st.getGeneratedKeys();
				if(keys.next()) {
					p.setId(keys.getInt(1));
				}
				keys.close();
			} else {
				
				System.out.println("UPDATE project SET video = '"+video+"', fps = "+p.getFps()+", videomd5 = '"+videomd5+"', xScaleStart = '"+xScaleStart+"', xScaleStop = '"+xScaleStop+"', yScaleStart = '"+yScaleStart+"', yScaleStop = '"+yScaleStop+"', scale = '"+scale+"', firstPicture = '"+firstPicture+"' WHERE id = '" + p.getId());
				st.executeUpdate("UPDATE project SET video = '"+video+"', fps = "+p.getFps()+", videomd5 = '"+videomd5+"', xScaleStart = '"+xScaleStart+"', xScaleStop = '"+xScaleStop+"', yScaleStart = '"+yScaleStart+"', yScaleStop = '"+yScaleStop+"', scale = '"+scale+"', firstPicture = '"+firstPicture+"' WHERE id = '" + p.getId() + "'");
			}
			st.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * writes a point to DB.
	 * 
	 * @param ViewPicturePoint
	 * @return boolean
	 */
	public static boolean writePoint(PicturePoint p) {
		try {
			Statement st = connection.createStatement();
			
			String posX = Double.toString(p.getPosX()).replace("'", "\\'");
			String posY = Double.toString(p.getPosY()).replace("'", "\\'");
			String picturePointGroupID = Integer.toString(p.getPicturePointGroupID()).replace("'", "\\'");
			String picId = Integer.toString(p.getPicId()).replace("'", "\\'");
			
			if (p.getId() == 0) {
				st.executeUpdate("INSERT INTO picturePoint (pictureID, posX, posY, picturePointGroupID) VALUES ('"+picId+"', '"+posX+"', '"+posY+"', '"+picturePointGroupID+"')");
				
				ResultSet keys = st.getGeneratedKeys();
				if(keys.next()) {
					p.setId(keys.getInt(1));
				}
				keys.close();
			} else {			
				st.executeUpdate("UPDATE picturePoint SET pictureID = '"+picId+"', posX = '"+posX+"', posY = '"+posY+"', picturePointGroupID = '"+picturePointGroupID+"' WHERE id = '" + p.getId() + "'");
			}
			st.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * writes a point to DB.
	 * 
	 * @param ViewPicturePoint
	 * @return boolean
	 */
	public static boolean writePointGroup(PicturePointGroup p) {
		try {
			Statement st = connection.createStatement();
			
			String title = p.getTitle().replace("'", "\\'");
			
			if (p.getId() == 0) {
				st.executeUpdate("INSERT INTO picturePointGroup (title) VALUES ('"+title+"')");
				
				ResultSet keys = st.getGeneratedKeys();
				if(keys.next()) {
					p.setId(keys.getInt(1));
				}
					
				keys.close();
			} else {			
				st.executeUpdate("UPDATE picturePointGroup SET title = '"+title+"' WHERE id = '" + p.getId() + "'");
			}
			st.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	/**
	 * execute any SQL-Query without result.
	 * 
	 * @param sql
	 * @return boolean
	 */
	public static boolean executeSQL(String sql) {
		try {
			Statement st = connection.createStatement();
			return st.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
