package com.ten.dao.implementation;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.ten.beans.*;

import model.Permission;
import model.Role;
import org.apache.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.TripleStoreConstants;

/**
 * @author Nita Karande
 * This class contains implementation method for access Relational database Mysql and functionality related to it
 * @author Jay Vyas
 * @version 2.0
 * Modified file upload logic for video content.
 * Added video file storage functionality.
 * Works in collaboration with existing CLO logic.
 */
public class DbAccessDaoImpl implements DbAccessDaoInterface{

	static Logger log = Logger.getLogger(DbAccessDaoImpl.class);
	
	private ServletContext context;

	private static DataSource  m_ds = null;
	//create datasource
    static  
    {  
        try
        {
        	Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			m_ds = (DataSource)envContext.lookup(DaoConstants.DB_JNDI_LOOKUP_NAME);
        }
        catch (Exception e)
        {
            log.error(e);
            m_ds = null;
        }
    }
    
    /**
     * get connection to database from connection pooled datasource
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {  
        return m_ds.getConnection();             
    }
	
	@Override
	/**
	 * This method is invoked by uploadImageAction to store the image to database.
	 * Method returns an integer which is the primary key of the image stored in database.
	 * This image is related to its annotations stored in triple store through the image id primary key.
	 */
	public int saveImage(File file, String fileName, String fileType,  boolean annotated) throws Exception
	{
		String LOG_METHOD_NAME = "int saveImage(File, String, String, boolean)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		CallableStatement callableStatement = null;
		
		// declare FileInputStream object to store binary stream of given image.
		FileInputStream fis = null;		
		int image_id = 0;		
		try {
			connection = getConnection();
			
			String sql_call = DaoConstants.INSERT_IMAGE_PROCEDURE_CALL;
			callableStatement = connection.prepareCall(sql_call);
			fis = new FileInputStream(file);
			callableStatement.setBinaryStream(1, (InputStream)fis, (int)(file.length()));
			callableStatement.setString(2, fileType);
			callableStatement.setString(3, fileName);
			callableStatement.setInt(4, annotated?1:0);
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			 
			// execute store procedure
			callableStatement.executeUpdate();
			
			image_id = callableStatement.getInt(5);
			
			if(image_id == 0){
				throw new Exception("Image cannot be uploaded");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally{
			// close all the connections.
			connection.close();
			callableStatement.close();
			fis.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return image_id;
	}

    @Override
    public void updateLearningObjectReferenceId(int id, String referenceId) throws Exception {
        String LOG_METHOD_NAME = "updateLearningObjectReferenceId(int, String)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            String sql_call = DaoConstants.UPDATE_CLO_REFERENCE_ID;
            preparedStatement = connection.prepareStatement(sql_call);
            preparedStatement.setString(1, referenceId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            log.error(ex);
            throw ex;
        } finally {
            preparedStatement.close();
            connection.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }
	
	@Override
	/**
	 * This method is invoked by annotateImageAction to change the status of image as being annotated.
	 */
	public boolean updateImage(int id) throws Exception
	{
		String LOG_METHOD_NAME = "boolean updateImage(int)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		boolean result = false;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.UPDATE_IMAGE_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, id); //set as annotated
			int rowsChanged = preparedStatement.executeUpdate();
			
			if(rowsChanged == 0){
				throw new Exception("Image cannot be updated");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return result;
	}
	
	@Override
	/**
	 * This method is invoked by annotateVideoAction to change the status of video as being annotated.
	 */
	public boolean updateVideo(int id) throws Exception
	{
		String LOG_METHOD_NAME = "boolean updateVideo(int)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		boolean result = false;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.UPDATE_VIDEO_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 1); //set as annotated
			preparedStatement.setInt(2, id); 
			int rowsChanged = preparedStatement.executeUpdate();
			
			if(rowsChanged == 0){
				throw new Exception("Video cannot be updated");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return result;
	}
	
	@Override
	/**
	 * This method is invoked by annotateAudioAction to change the status of audio as being annotated.
	 */
	public boolean updateAudio(int id) throws Exception
	{
		String LOG_METHOD_NAME = "boolean updateAudio(int)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		boolean result = false;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.UPDATE_AUDIO_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 1); //set as annotated
			preparedStatement.setInt(2, id);
			int rowsChanged = preparedStatement.executeUpdate();
			
			if(rowsChanged == 0){
				throw new Exception("Audio cannot be updated");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return result;
	}

	@Override
	/**
	 * This method is invoked by annotateTextAction to change the status of text as being annotated.
	 */
	public boolean updateText(int id) throws Exception
	{
		String LOG_METHOD_NAME = "boolean updateText(int)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		boolean result = false;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.UPDATE_TEXT_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 1); //set as annotated
			preparedStatement.setInt(2, id);
			int rowsChanged = preparedStatement.executeUpdate();
			
			if(rowsChanged == 0){
				throw new Exception("Text cannot be updated");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return result;
	}
	
	@Override
	/**
	 * This method is invoked by uploadVideoAction to store the video to database.
	 * Method returns an integer which is the primary key of the video stored in database.
	 * This video is related to its annotations stored in triple store through the video id primary key.
	 */
	public int saveVideo(File file, String fileName, String fileType, boolean annotated)
			throws Exception 
	{
		String LOG_METHOD_NAME = "int saveVideo(File, String, String, boolean)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		CallableStatement callableStatement = null;
		
		int video_id = 0;
		
		try {
			connection = getConnection();
			
			String sql_call = DaoConstants.INSERT_VIDEO_PROCEDURE_CALL;
			callableStatement = connection.prepareCall(sql_call);
			String targetPath = DaoConstants.FILES_DIR + fileName;
		    System.out.println("source file path :: " + file.getAbsolutePath());
	        System.out.println("saving file to :: " + targetPath);
	        File destinationFile = new File(targetPath);
	        
	        copyFileUsingStream(file, destinationFile);

			////////////////////////CHANGED HERE////////////////////////////////
			callableStatement.setNull(1, java.sql.Types.BLOB);
			//callableStatement.setBinaryStream(1, (InputStream)fis, (int)(file.length()));
			callableStatement.setString(2, fileType);
			callableStatement.setString(3, fileName);
			callableStatement.setInt(4, annotated?1:0);
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			 
			// execute store procedure
			callableStatement.executeUpdate();
			
			video_id = callableStatement.getInt(5);	
			
			if(video_id == 0){
				throw new Exception("Video cannot be uploaded");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			callableStatement.close();

			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}
		return video_id;
	}

	/**
	 * This method implements File acquisition logic.
	 * and faster file copy.
	 * @author Jay Vyas
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private static void copyFileUsingStream(File source, File dest) throws IOException 
	{
	    InputStream is = null;
	    OutputStream os = null;
	    try 
	    {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        //byte[] buffer = new byte[1024];
	        byte[] buffer = new byte[16384]; //16KB buffer
	        //byte[] buffer = new byte[1048576]; //1MB buffer
	        int length;
	        while ((length = is.read(buffer)) > 0) 
	        {
	            os.write(buffer, 0, length);
	        }
	    } 
	    finally 
	    {
	        is.close();
	        os.close();
	    }
	}
	
	@Override
	/**
	 * This method is invoked by uploadAudioAction to store the audio to database.
	 * Method returns an integer which is the primary key of the audio stored in database.
	 * This audio is related to its annotations stored in triple store through the audio id primary key.
	 */
	public int saveAudio(File file, String fileName, String fileType, boolean annotated)
			throws Exception 
	{
		String LOG_METHOD_NAME = "int saveAudio(File, String, String, boolean)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		CallableStatement callableStatement = null;
		
		// declare FileInputStream object to store binary stream of given image.
		FileInputStream fis = null;		
		int audio_id = 0;
		
		try {
			connection = getConnection();
			
			String sql_call = DaoConstants.INSERT_AUDIO_PROCEDURE_CALL;
			callableStatement = connection.prepareCall(sql_call);
			fis = new FileInputStream(file);
			callableStatement.setBinaryStream(1, (InputStream)fis, (int)(file.length()));
			callableStatement.setString(2, fileType);
			callableStatement.setString(3, fileName);
			callableStatement.setInt(4, annotated?1:0);
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			 
			// execute store procedure
			callableStatement.executeUpdate();
			
			audio_id = callableStatement.getInt(5);		
			if(audio_id == 0){
				throw new Exception("Audio cannot be uploaded");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			callableStatement.close();
			fis.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}
		return audio_id;
	}

	public Map<String, Object> processRecord(File file) {
//	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
//	        HttpGet httpGet = new HttpGet(url);
//	        HttpResponse response = httpclient.execute(httpGet);
//	        HttpEntity entity = response.getEntity();
	    	// declare FileInputStream object to store binary stream of given text.
			FileInputStream fis = new FileInputStream(file);
//	        InputStream input = null;
	                if (fis != null) {
	                    try{
	                    	
//	                        input = entity.getContent();
	                        BodyContentHandler handler = new BodyContentHandler();
	                        Metadata metadata = new Metadata();
	                        AutoDetectParser parser = new AutoDetectParser();
	                        ParseContext parseContext = new ParseContext();
	                        parser.parse(fis, handler, metadata, parseContext);
	                        map.put("text", handler.toString().replaceAll("\n|\r|\t", " "));
	                        map.put("title", metadata.get(TikaCoreProperties.TITLE));
	                        map.put("pageCount", metadata.get("xmpTPg:NPages"));
	                } catch (Exception e) {                     
	                    e.printStackTrace();
	                }finally{
	                    if(fis != null){
	                        try {
	                        	fis.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	                }
	            }catch (Exception exception) {
	                exception.printStackTrace();
	            }
	    return map;
	}
	
	@Override
	/**
	 * This method is invoked by uploadTextAction to store the text to database.
	 * Method returns an integer which is the primary key of the text stored in database.
	 * This text is related to its annotations stored in triple store through the text id primary key.
	 */
	public int saveText(File file, String fileName, String fileType, boolean annotated)
			throws Exception 
	{
		String LOG_METHOD_NAME = "int saveText(File, String, String, boolean)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		CallableStatement callableStatement = null;
		
		// declare FileInputStream object to store binary stream of given image.
		FileInputStream fis = null;		
		int text_id = 0;
		
//		Test Tika
		Map<String, Object> extractedMap = processRecord(file);
		String contextText = extractedMap.get("text").toString();
	    System.out.println("Text in " + fileType + ": " + contextText);
		
		try {
			connection = getConnection();
			
			String sql_call = DaoConstants.INSERT_TEXT_PROCEDURE_CALL;
			callableStatement = connection.prepareCall(sql_call);
			fis = new FileInputStream(file);
			callableStatement.setBinaryStream(1, (InputStream)fis, (int)(file.length()));
			callableStatement.setString(2, fileType);
			callableStatement.setString(3, fileName);
			callableStatement.setInt(4, annotated?1:0);
			callableStatement.setString(5, contextText);
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			 
			// execute store procedure
			callableStatement.executeUpdate();
			
			text_id = callableStatement.getInt(6);	
			if(text_id == 0){
				throw new Exception("Text cannot be uploaded");
			}
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			connection.close();
			callableStatement.close();
			fis.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return text_id;
	}

	@Override
	public ArrayList<LearningObjectBean> getUnannotatedImages()
			throws Exception {
		String LOG_METHOD_NAME = "ArrayList<LearningObjectBean> getUnannotatedImages()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		ArrayList<LearningObjectBean> listLearningObjects = new ArrayList<LearningObjectBean>();
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_UNANNOTATED_IMAGES_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 0);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				LearningObjectBean learningObjectBean = new LearningObjectBean();
				learningObjectBean.setId(rset.getInt(1));
				learningObjectBean.setFileName(rset.getString(2));
				listLearningObjects.add(learningObjectBean);
		     }    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return listLearningObjects;
	}
	
	@Override
	public LearningObjectDetailsBean getImage(int id)
			throws Exception {
		String LOG_METHOD_NAME = "LearningObjectDetailsBean getImage()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		LearningObjectDetailsBean learningObjectDetailsBean = null;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_IMAGE_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				learningObjectDetailsBean = new LearningObjectDetailsBean();
				learningObjectDetailsBean.setId(id);
				learningObjectDetailsBean.setFileName(rset.getString(1));
				learningObjectDetailsBean.setFileType(rset.getString(2));
				Blob blob = rset.getBlob(3);
				learningObjectDetailsBean.setContent(blob.getBytes(1,(int)blob.length()));
			}    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return learningObjectDetailsBean;
	}
	
	@Override
	public ArrayList<LearningObjectBean> getUnannotatedVideos()
			throws Exception {
		String LOG_METHOD_NAME = "ArrayList<LearningObjectBean> getUnannotatedVideos()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		ArrayList<LearningObjectBean> listLearningObjects = new ArrayList<LearningObjectBean>();
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_UNANNOTATED_VIDEOS_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 0);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				LearningObjectBean learningObjectBean = new LearningObjectBean();
				learningObjectBean.setId(rset.getInt(1));
				learningObjectBean.setFileName(rset.getString(2));
				listLearningObjects.add(learningObjectBean);
		     }    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return listLearningObjects;
	}
	
	@Override
	public LearningObjectDetailsBean getVideo(int id)
			throws Exception {
		String LOG_METHOD_NAME = "LearningObjectDetailsBean getVideo()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		LearningObjectDetailsBean learningObjectDetailsBean = null;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_VIDEO_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				learningObjectDetailsBean = new LearningObjectDetailsBean();
				learningObjectDetailsBean.setId(id);
				learningObjectDetailsBean.setFileName(rset.getString(1));
				learningObjectDetailsBean.setFileType(rset.getString(2));
	
				File getFile = new File(DaoConstants.FILES_DIR + learningObjectDetailsBean.getFileName());
				System.out.println("File Path to get for Annotation: " + getFile.toPath());
				
				byte[] data = java.nio.file.Files.readAllBytes(getFile.toPath());//(getFile.toPath());
						
				learningObjectDetailsBean.setContent(data);
			}    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return learningObjectDetailsBean;
	}
	
	@Override
	public ArrayList<LearningObjectBean> getUnannotatedAudios()
			throws Exception {
		String LOG_METHOD_NAME = "ArrayList<LearningObjectBean> getUnannotatedAudios()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		ArrayList<LearningObjectBean> listLearningObjects = new ArrayList<LearningObjectBean>();
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_UNANNOTATED_AUDIOS_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 0);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				LearningObjectBean learningObjectBean = new LearningObjectBean();
				learningObjectBean.setId(rset.getInt(1));
				learningObjectBean.setFileName(rset.getString(2));
				listLearningObjects.add(learningObjectBean);
		     }    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return listLearningObjects;
	}
	
	@Override
	public LearningObjectDetailsBean getAudio(int id)
			throws Exception {
		String LOG_METHOD_NAME = "LearningObjectDetailsBean getAudio()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		LearningObjectDetailsBean learningObjectDetailsBean = null;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_AUDIO_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				learningObjectDetailsBean = new LearningObjectDetailsBean();
				learningObjectDetailsBean.setId(id);
				learningObjectDetailsBean.setFileName(rset.getString(1));
				learningObjectDetailsBean.setFileType(rset.getString(2));
				Blob blob = rset.getBlob(3);
				learningObjectDetailsBean.setContent(blob.getBytes(1,(int)blob.length()));
			}    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return learningObjectDetailsBean;
	}
	
	@Override
	public ArrayList<LearningObjectBean> getUnannotatedTexts()
			throws Exception {
		String LOG_METHOD_NAME = "ArrayList<LearningObjectBean> getUnannotatedTexts()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		ArrayList<LearningObjectBean> listLearningObjects = new ArrayList<LearningObjectBean>();
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_UNANNOTATED_TEXTS_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 0);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				LearningObjectBean learningObjectBean = new LearningObjectBean();
				learningObjectBean.setId(rset.getInt(1));
				learningObjectBean.setFileName(rset.getString(2));
				listLearningObjects.add(learningObjectBean);
		     }    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return listLearningObjects;
	}
	
	@Override
	public LearningObjectDetailsBean getText(int id)
			throws Exception {
		String LOG_METHOD_NAME = "LearningObjectDetailsBean getText()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		LearningObjectDetailsBean learningObjectDetailsBean = null;
		
		try {
			connection = getConnection();
			
			String sql = DaoConstants.GET_TEXT_SQL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				learningObjectDetailsBean = new LearningObjectDetailsBean();
				learningObjectDetailsBean.setId(id);
				learningObjectDetailsBean.setFileName(rset.getString(1));
				learningObjectDetailsBean.setFileType(rset.getString(2));
				Blob blob = rset.getBlob(3);
				learningObjectDetailsBean.setContent(blob.getBytes(1,(int)blob.length()));
			}    
		} catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		} finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return learningObjectDetailsBean;
	}
    
    @Override
    public CourseBean getCourse(int courseId) throws SQLException {
        String LOG_METHOD_NAME = "getCourse(int courseId)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        CourseBean course = null;

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_COURSES_BY_ID_SQL;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            rset = preparedStatement.executeQuery();

            if (rset.next ())
            {
                course = new CourseBean();
                int id = rset.getInt(1);
                course.setId(id);

                course.setOwnerId(rset.getInt(2));

                String name = rset.getString(3);
                course.setName(name);

                course.setDescription(rset.getString(4));
                course.setPrerequisites(rset.getString(5));
                course.setTopics(rset.getString(6));
                course.setOverview(rset.getString(7));
                course.setTimeline(rset.getString(8));
                course.setIndividualAssignments(rset.getString(9));
                course.setGroupAssignments(rset.getString(10));
                course.setOnsiteAssignments(rset.getString(11));
                course.setExams(rset.getString(12));
                course.setQuizzes(rset.getString(13));
                course.setKnowledgeCheckpoints(rset.getString(14));
                course.setGradingRubric(rset.getString(15));
                course.setTechnologyRequirements(rset.getString(16));
                course.setSupportServices(rset.getString(17));
                course.setEvaluationFormat(rset.getString(18));
                course.setProgressMeterBadges(rset.getString(19));
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return course;
    }

    @Override
    public int addCourse(CourseBean course) throws Exception {

        /* TODO: columns not handled in the UI yet
            DocumentTemplates longblob
            EvaluationsAttachments longblob
        */

        String LOG_METHOD_NAME = "add/create course";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // declare result variable
        int courseId = 0;
        try {
            connection = getConnection();

            preparedStatement = connection.prepareStatement(DaoConstants.INSERT_COURSE_SQL, Statement.RETURN_GENERATED_KEYS );
            preparedStatement.setInt(1, course.getOwnerId());
            preparedStatement.setString(2,course.getName() );
            preparedStatement.setString(3, course.getDescription());
            preparedStatement.setString(4, course.getPrerequisites());
            preparedStatement.setString(5, course.getTopics());
            preparedStatement.setString(6, course.getOverview());
            preparedStatement.setString(7, course.getTimeline());
            preparedStatement.setString(8, course.getIndividualAssignments());
            preparedStatement.setString(9, course.getGroupAssignments());
            preparedStatement.setString(10, course.getOnsiteAssignments());
            preparedStatement.setString(11, course.getExams());
            preparedStatement.setString(12, course.getQuizzes());
            preparedStatement.setString(13, course.getKnowledgeCheckpoints());
            preparedStatement.setString(14, course.getGradingRubric());
            preparedStatement.setString(15, course.getTechnologyRequirements());
            preparedStatement.setString(16, course.getSupportServices());
            preparedStatement.setString(17, course.getEvaluationFormat());
            preparedStatement.setString(18, course.getProgressMeterBadges());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            courseId = generatedKeys != null && generatedKeys.next() ? generatedKeys.getInt(1) : 0;
            generatedKeys.close();

            if (courseId == 0) {
                throw new Exception("Course could not be saved");
            }

            course.setId(courseId);

        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            if (connection != null)
                connection.close();
            if (preparedStatement != null)
                preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return courseId;
    }

    @Override
    public void updateCourse(CourseBean course) throws Exception{

        String LOG_METHOD_NAME = "update course info";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            preparedStatement = connection.prepareStatement(DaoConstants.UPDATE_COURSE_SQL);
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.setString(3, course.getPrerequisites());
            preparedStatement.setString(4, course.getTopics());
            preparedStatement.setString(5, course.getOverview());
            preparedStatement.setString(6, course.getTimeline());
            preparedStatement.setString(7, course.getIndividualAssignments());
            preparedStatement.setString(8, course.getGroupAssignments());
            preparedStatement.setString(9, course.getOnsiteAssignments());
            preparedStatement.setString(10, course.getExams());
            preparedStatement.setString(11, course.getQuizzes());
            preparedStatement.setString(12, course.getKnowledgeCheckpoints());
            preparedStatement.setString(13, course.getGradingRubric());
            preparedStatement.setString(14, course.getTechnologyRequirements());
            preparedStatement.setString(15, course.getSupportServices());
            preparedStatement.setString(16, course.getEvaluationFormat());
            preparedStatement.setString(17, course.getProgressMeterBadges());
            preparedStatement.setInt(18, course.getId());

            // execute store procedure
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
	public HashMap<String, LearningObjectDetailsBean> getMapOfLearningObjects(Collection<String> uris) throws Exception {
		
		String LOG_METHOD_NAME = "HashMap<String, LearningObjectDetailsBean> getLearningObjectDetails(String type, Set<String> uri)";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		HashMap<String, LearningObjectDetailsBean> returnMap = new HashMap<String, LearningObjectDetailsBean>();
		LearningObjectDetailsBean learningObjectDetails = null;
		
		try {
			for (String uri : uris) {
				int indexOf = uri.lastIndexOf("#");
				int id = Integer.parseInt(uri.substring(indexOf + 1));
				learningObjectDetails = this.getLearningObject(id);
				if (learningObjectDetails != null) {
				    returnMap.put(uri, learningObjectDetails);
				} else {
				    log.error("Learning Object not found in DB for uri=" + uri);
				}
			}		
		} catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		} finally{
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}
		return returnMap;
	}

    @Override
    public ArrayList<CourseObjectiveBean> getCourseObjectives(int courseId) throws Exception {
        String LOG_METHOD_NAME = "ArrayList<CourseObjectiveBean> getAllObjectiveDescription(int)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        ArrayList<CourseObjectiveBean> objectives = new ArrayList<CourseObjectiveBean>();

        try {
            connection = getConnection();

         
            String sql = DaoConstants.GET_ALL_OBJECTIVE_DESCRIPTION;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
                CourseObjectiveBean courseObjective = new CourseObjectiveBean();
                courseObjective.setId( rset.getInt(1) );
                courseObjective.setDescription( rset.getString(2) );
                objectives.add(courseObjective);
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return objectives;
    }
    
    
    @Override
    public ArrayList<CourseBean> getAllCourses() throws Exception {
        String LOG_METHOD_NAME = "ArrayList<CourseBean> getCoursesForUser()";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        ArrayList<CourseBean> courses = new ArrayList<CourseBean>();

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_ALL_COURSES_SQL;
            preparedStatement = connection.prepareStatement(sql);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
                CourseBean course = new CourseBean();
                int id = rset.getInt(1);
                course.setId(id);

                course.setOwnerId(rset.getInt(2));

                String name = rset.getString(3);
                course.setName(name);

                course.setDescription(rset.getString(4));
                course.setPrerequisites(rset.getString(5));

                courses.add(course);
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return courses;
    }

	@Override
	public ArrayList<CourseBean> getCoursesForUser(UserDetailsBean userDetails) throws Exception {
		String LOG_METHOD_NAME = "ArrayList<CourseBean> getCoursesForUser()";
		log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		// declare a connection by using Connection interface 
		Connection connection = null;

		// Declare prepare statement.
		PreparedStatement preparedStatement = null;
		
		// result set
		ResultSet rset = null;
		
		//return result
		ArrayList<CourseBean> courses = new ArrayList<CourseBean>();
		
		try {
			connection = getConnection();

			String sql = DaoConstants.GET_COURSES_FOR_USER_SQL;
			preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userDetails.getId());
            rset = preparedStatement.executeQuery();
			
			while (rset.next ())
		    {   
				CourseBean course = new CourseBean();
                int id = rset.getInt(1);
                course.setId(id);

                course.setOwnerId(rset.getInt(2));

                String name = rset.getString(3);
                course.setName(name);

                course.setDescription(rset.getString(4));
                course.setPrerequisites(rset.getString(5));
                course.setTopics(rset.getString(6));
                course.setOverview(rset.getString(7));
                course.setTimeline(rset.getString(8));
                course.setIndividualAssignments(rset.getString(9));
                course.setGroupAssignments(rset.getString(10));
                course.setOnsiteAssignments(rset.getString(11));
                course.setExams(rset.getString(12));
                course.setQuizzes(rset.getString(13));
                course.setKnowledgeCheckpoints(rset.getString(14));
                course.setGradingRubric(rset.getString(15));
                course.setTechnologyRequirements(rset.getString(16));
                course.setSupportServices(rset.getString(17));
                course.setEvaluationFormat(rset.getString(18));
                course.setProgressMeterBadges(rset.getString(19));

                courses.add(course);
		     }    
		}catch (Exception ex) {
			// catch if found any exception during rum time.
			log.error(ex);
			throw ex;
		}finally {
			// close all the connections.
			rset.close();
			connection.close();
			preparedStatement.close();
			log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return courses;
	}

    @Override
    public LearningObjectDetailsBean getLearningObject(int id) throws Exception {
        String LOG_METHOD_NAME = "LearningObjectDetailsBean getLearningObject(int id)";;
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        LearningObjectDetailsBean learningObject = null;

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_LEARNING_OBJECT_BY_ID;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
                learningObject = new LearningObjectDetailsBean();
                learningObject.setId( id );
                learningObject.setFileName(rset.getString(1));
                learningObject.setFileType(rset.getString(2));
                learningObject.setContent(rset.getBytes(3));
                learningObject.setContentType(rset.getString(4));
                learningObject.setReferenceId(rset.getString(5));
                if (DaoConstants.ContentTypes.Video.toString().equals(learningObject.getContentType())) {
                    File getFile = new File(DaoConstants.FILES_DIR + learningObject.getFileName());
                    log.debug("Reading Video from file path: " + getFile.toPath());
                    byte[] data = java.nio.file.Files.readAllBytes(getFile.toPath());
                    learningObject.setContent(data);
                }
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return learningObject;
    }
    
    public HashSet<LearningObjectDetailsBean> getLearningObjectsByFullTextSearch(ArrayList<String> keywords) throws Exception {
    	HashSet<LearningObjectDetailsBean> learningObjects = new HashSet<LearningObjectDetailsBean>();
    	for (String keyword : keywords){
    		ArrayList<Integer> learningObjectIds = getLearningObjectIdByFullTextSearch(keyword);
        	for(int id : learningObjectIds){
        		learningObjects.add(getLearningObject(id));
        	}
    	}
    	return learningObjects;
    }
    
    public ArrayList<Integer> getLearningObjectIdByFullTextSearch(String keyword) throws Exception {
        String LOG_METHOD_NAME = "ArrayList<Integer> getLearningObjectIdByFullTextSearch(String keyword)";;
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
    	
    	ArrayList<Integer> LearningObjectIds = new ArrayList<Integer>();

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        try {
//        	If the keyword end with letter or digit, we add asterisk at the end of keyword for wild card search.
        	String search_keyword = keyword;
        	char first_char = keyword.charAt(0);
        	char last_char = keyword.charAt(keyword.length() - 1);
        	if ((Character.isLetter(first_char) || Character.isDigit(first_char)) && (Character.isLetter(last_char) || Character.isDigit(last_char))){
        		search_keyword = keyword + "*";
        	}
        	
            connection = getConnection();
            String sql = DaoConstants.GET_LEARNING_OBJECT_BY_FULL_TEXT_SEARCH;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, search_keyword);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
            	LearningObjectIds.add(rset.getInt(1));
//            	System.out.println("Learning object id: " + rset.getInt(1));
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
//            System.out.println("Connection closed.");
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return LearningObjectIds;
    }

	@Override
    public ArrayList<LearningObjectDetailsBean> getAllLearningObjects() throws Exception {
        String LOG_METHOD_NAME = "getAllLearningObjects()";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        ArrayList<LearningObjectDetailsBean> learningObjects = new ArrayList<LearningObjectDetailsBean>();

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_ALL_CULTURAL_LEARNING_OBJECTS;
            preparedStatement = connection.prepareStatement(sql);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
            	LearningObjectDetailsBean learningObject = new LearningObjectDetailsBean();
            	learningObject.setId( rset.getInt(1) );
            	learningObject.setFileName(rset.getString(2));
            	learningObject.setFileType(rset.getString(3));
                
                learningObject.setContentType(rset.getString(5));
                learningObject.setReferenceId(rset.getString(6));
                if (DaoConstants.ContentTypes.Video.toString().equals(learningObject.getContentType())) {
                    File getFile = new File(DaoConstants.FILES_DIR + learningObject.getFileName());
                    log.debug("Reading Video from file path: " + getFile.toPath());
                    byte[] data = java.nio.file.Files.readAllBytes(getFile.toPath());
                    learningObject.setContent(data);
                }
				else
				{
					learningObject.setContent(rset.getBytes(4));
				}
                learningObjects.add(learningObject);
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return learningObjects;
    }

    @Override
    public ArrayList<CourseLearningObjectLinkBean> getCourseLearningObjectLinks(int courseId) throws Exception {
        String LOG_METHOD_NAME = "getCourseLearningObjectLinks(int courseId)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        ArrayList<CourseLearningObjectLinkBean> links = new ArrayList<CourseLearningObjectLinkBean>();

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_LEARNING_OBJECT_LINKS_FOR_COURSE;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            rset = preparedStatement.executeQuery();

            while (rset.next())
            {
                CourseLearningObjectLinkBean link = new CourseLearningObjectLinkBean();
                link.setId( rset.getInt(1) );
                LearningObjectDetailsBean learningObject = new LearningObjectDetailsBean();
                learningObject.setId(rset.getInt(2));
                link.setLearningObject(learningObject);
                link.setCourseContentName( rset.getString(3) );
                CourseBean course = new CourseBean();
                course.setId(courseId);
                link.setCourse(course);

                links.add(link);
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return links;
    }

    @Override
    public File getCourseContentForExport(int courseId) throws Exception {
       String LOG_METHOD_NAME = "get course content for export";

        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        try {
            connection = getConnection();

            String sql = DaoConstants.GET_COURSE_DATA_FOR_EXPORT;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            rset = preparedStatement.executeQuery();

            PrintWriter writer = null;
            File zip = new File("C:\\exportedCourses\\course_"+courseId+".zip");
            final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zip));

            CourseBean course = getCourse(courseId);
            StringBuilder sb = new StringBuilder();
            File coursefile = new File("C:\\exportedCourses\\course_" + courseId + ".txt");
            if( course != null ){
                sb.append("Name: ")
                        .append(System.lineSeparator())
                        .append(course.getName())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Description: ")
                        .append(System.lineSeparator())
                        .append(course.getDescription())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Prerequisites: ")
                        .append(System.lineSeparator())
                        .append(course.getPrerequisites())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Topics: ")
                        .append(System.lineSeparator())
                        .append(course.getTopics())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Overview: ")
                        .append(System.lineSeparator())
                        .append(course.getOverview())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Timeline: ")
                        .append(System.lineSeparator())
                        .append(course.getTimeline())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Individual Assignments: ")
                        .append(System.lineSeparator())
                        .append(course.getIndividualAssignments())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Group Assignments: ")
                        .append(System.lineSeparator())
                        .append(course.getGroupAssignments())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("On-site Assignments: ")
                        .append(System.lineSeparator())
                        .append(course.getOnsiteAssignments())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Exams: ")
                        .append(System.lineSeparator())
                        .append(course.getExams())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Quizzes: ")
                        .append(System.lineSeparator())
                        .append(course.getQuizzes())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Knowledge Checkpoints: ")
                        .append(System.lineSeparator())
                        .append(course.getKnowledgeCheckpoints())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Grading Rubric: ")
                        .append(System.lineSeparator())
                        .append(course.getGradingRubric())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Technology Requirements: ")
                        .append(System.lineSeparator())
                        .append(course.getTechnologyRequirements())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Support Services: ")
                        .append(System.lineSeparator())
                        .append(course.getSupportServices())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Evaluation Format: ")
                        .append(System.lineSeparator())
                        .append(course.getEvaluationFormat())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                        .append("Progress Meter or Badges: ")
                        .append(System.lineSeparator())
                        .append(course.getProgressMeterBadges())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }

            writer = new PrintWriter(coursefile);
            writer.println(sb.toString());

            ZipEntry courseInfo = new ZipEntry(coursefile.getName());
            zipOutputStream.putNextEntry(courseInfo);
            byte[] bytes = sb.toString().getBytes();
            zipOutputStream.write(bytes);
            zipOutputStream.closeEntry();
            writer.close();


            while (rset.next()) {
                byte[] contentBytes = rset.getBytes(1);
                String contentFileName = rset.getString(2);
                String contentFileType = rset.getString(3);
                if( contentBytes != null ){
                	File content = new File("C:\\exportedCourses\\"+contentFileName);
                    ZipEntry contentEntry = new ZipEntry( content.getName() );
                    zipOutputStream.putNextEntry( contentEntry );
                    zipOutputStream.write( contentBytes );
                    zipOutputStream.closeEntry();                	
                }
                
            }
            zipOutputStream.close();

            return zip;
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            rset.close();
            connection.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void linkLearningObjectToCourse(CourseLearningObjectLinkBean courseLearningObjectLink) throws Exception {
        int id = linkLearningObjectToCourse(
                courseLearningObjectLink.getCourse().getId(),
                courseLearningObjectLink.getLearningObject().getId(),
                courseLearningObjectLink.getCourseContentName());
        courseLearningObjectLink.setId(id);
    }

    @Override
    public int linkLearningObjectToCourse(int courseId, int learningObjectId, String courseFieldName) throws Exception {
        String LOG_METHOD_NAME = "linkLearningObjectToCourse";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            String sql_call = DaoConstants.INSERT_COURSE_CULTURAL_CONTENT_LINK;
            preparedStatement = connection.prepareStatement(sql_call, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, learningObjectId);
            preparedStatement.setString(3, courseFieldName);

            // execute store procedure
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int linkId = generatedKeys != null && generatedKeys.next() ? generatedKeys.getInt(1) : 0;

            if (linkId == 0){
                throw new Exception("Link between Learning Object and Course could not be saved");
            }
            return linkId;
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void linkLearningObjectToCourse(int courseId, String[] culturalContentName) throws Exception {
        String LOG_METHOD_NAME = "linkLearningObjectToCourse";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        CallableStatement callableStatement = null;
        try {
            connection = getConnection();

            for ( String contentName : culturalContentName ) {
                String sql_call = DaoConstants.INSERT_COURSE_CULTURAL_CONTENT_LINK_FROM_CONTENT_NAME;
                callableStatement = connection.prepareCall(sql_call);
                callableStatement.setInt(1, courseId);
                callableStatement.setString(2, contentName);
                callableStatement.execute();
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            callableStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    /**
     * This method is invoked by SignUpAction to store the newly created user account to database.
     * Method returns an String which is the primary key of the user stored in database.
     */
    public void insertUser(
            String userName,
            String firstName,
            String middleName,
            String lastName,
            String emailId,
            String userPassword,
            String role
            ) throws Exception
    {
        String LOG_METHOD_NAME = "String insertUser(String userName, String firstName,String middleName, String lastName, String emailId, String userPassword, String role)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
        
        // declare a connection by using Connection interface 
        Connection connection = null;

        // Declare prepare statement.
        CallableStatement callableStatement = null;
                    
        try {
            connection = getConnection();
            
            String sql_call = DaoConstants.INSERT_USER_PROCEDURE_CALL;
            callableStatement = connection.prepareCall(sql_call);
            callableStatement.setString(1, userName);
            callableStatement.setString(2, firstName);
            callableStatement.setString(3, middleName);
            callableStatement.setString(4, lastName);
            callableStatement.setString(5, emailId);
            callableStatement.setString(6, userPassword);
            callableStatement.setString(7, role);
                        
            // execute store procedure
            callableStatement.executeUpdate();
                    
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally{
            // close all the connections.
            connection.close();
            callableStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }       
    }
    
    @Override
    /**
     * This method is invoked by ManageCourseDetailsAction to delete a course
     */
    public void deleteCourse(
            int courseId
            ) throws Exception
    {
        String LOG_METHOD_NAME = "deleteCourse(int)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
        
        // declare a connection by using Connection interface 
        Connection connection = null;

        // Declare prepare statement.
        CallableStatement callableStatement = null;
                    
        try {
            connection = getConnection();
            
            String sql_call = DaoConstants.DELETE_COURSE_PROCEDURE_CALL;
            callableStatement = connection.prepareCall(sql_call);
            callableStatement.setInt(1, courseId);      
                        
            // execute store procedure
            callableStatement.executeUpdate();
                    
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally{
            // close all the connections.
            connection.close();
            callableStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }       
    }

    @Override
    public void addCourseObjectives(CourseBean course, Collection<CourseObjectiveBean> courseObjectives) throws Exception {
        final String LOG_METHOD_NAME = "addCourseObjectives(Collection<CourseObjectiveBean>)";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
   
        try {
            log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);
            
            if (courseObjectives == null || courseObjectives.isEmpty())
                return;

            StringBuilder sb = new StringBuilder(DaoConstants.INSERT_COURSE_OBJECTIVE);
            // placeholders for course Id and description (?,?)
            final int placeholdersCount = 2;
            final String placeholders = getInsertPlaceholders(placeholdersCount);
            for (int i = 0; i < courseObjectives.size(); i++) {
                if ( i != 0 )
                    sb.append(",");
                sb.append(placeholders);
            }
            final String sqlString = sb.toString();
            
            connection = getConnection();
            statement = connection.prepareStatement(sqlString, PreparedStatement.RETURN_GENERATED_KEYS);

            int placeholder = 1;
            for (CourseObjectiveBean objective : courseObjectives) {
                statement.setInt(placeholder++, course.getId());
                statement.setString(placeholder++, objective.getDescription());
            }

            statement.execute();
            
            rset = statement.getGeneratedKeys();
            int savedObjectivesCount = 0;
            for (CourseObjectiveBean objective : courseObjectives) {
                if (rset.next()) {
                    objective.setId(rset.getInt(1));
                    course.getObjectives().put(rset.getInt(1), objective);
                    savedObjectivesCount++;
                }
            }
            if (savedObjectivesCount != course.getObjectives().size()) {
                throw new Exception(String.format(
                        "Expected to save %d objectives but saved %d objectives",
                        course.getObjectives().size(), savedObjectivesCount));
            }
        } catch (Exception ex) {
            // log and throw exception
            log.error(ex);
            throw ex;
        } finally {
            // close database resources
            if (rset != null)
                rset.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void insertCourseObjective(int courseId, String courseObjectiveDescription) throws Exception {
        String LOG_METHOD_NAME = "insertCourseObjective(int, String[])";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare database resources
        Connection connection = null;
        PreparedStatement preparedStatement = null;
   
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(DaoConstants.INSERT_COURSE_OBJECTIVE);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, courseObjectiveDescription);
            
            // execute store procedure
            preparedStatement.execute();
        } catch (Exception ex) {
            // log and throw exception
            log.error(ex);
            throw ex;
        } finally {
            // close database resources
            if (connection != null)
                connection.close();
            if (preparedStatement != null)
                preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    
    @Override
    public void linkCulturalContentToCourseContent(int courseId, String courseFieldName, int culturalContentId) throws Exception {
        String LOG_METHOD_NAME = "link content to course content";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            String sql_call = DaoConstants.INSERT_COURSE_CONTENT_CULTURAL_CONTENT_LINK;
            preparedStatement = connection.prepareStatement(sql_call, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, courseFieldName);
            preparedStatement.setInt(3, culturalContentId);

            // execute store procedure
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int linkId = generatedKeys != null && generatedKeys.next() ? generatedKeys.getInt(1) : 0;

            if(linkId == 0){
                throw new Exception("Course content to cultural object link could not be saved");
            }
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void linkCulturalContentToCourseContent(int courseId, String courseFieldName, String[] culturalContentName) throws Exception {
        String LOG_METHOD_NAME = "link content to course";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        CallableStatement callableStatement = null;
        try {
            connection = getConnection();

            for( String contentName : culturalContentName){
                String sql_call = DaoConstants.INSERT_COURSE_CONTENT_CULTURAL_CONTENT_LINK_FROM_CONTENT_NAME;
                callableStatement = connection.prepareCall(sql_call);
                callableStatement.setObject(1, courseId);
                callableStatement.setObject( 2, courseFieldName );
                callableStatement.setString( 3, contentName );
                callableStatement.execute();
            }
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally {
            // close all the connections.
            connection.close();
            callableStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public int linkLearningObjectToCourseObjective(int courseObjectiveId, int culturalContentId) throws Exception {
        String LOG_METHOD_NAME = "linkLearningObjectToCourseObjective";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        PreparedStatement preparedStatement = null;

        // result set
        try {
            connection = getConnection();

            String sql_call = DaoConstants.INSERT_COURSE_OBJECTIVE_CULTURAL_CONTENT_LINK;
            preparedStatement = connection.prepareStatement(sql_call, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, courseObjectiveId);
            preparedStatement.setInt(2, culturalContentId);

            // execute store procedure
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int linkId = generatedKeys != null && generatedKeys.next() ? generatedKeys.getInt(1) : 0;

            if (linkId == 0){
                throw new Exception("Course content to cultural object link could not be saved");
            }
            return linkId;
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }
    
    @Override
    public void unlinkLearningObjectToCourse(int courseId, int learningObjectId) throws Exception {

        String LOG_METHOD_NAME = "unlinkLearningObjectToCourse(int courseId, int learningObjectId)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();

            String sql = DaoConstants.DELETE_COURSE_CULTURAL_CONTENT_LINK;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, learningObjectId);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void removeLearningObjectToCourseLink(Collection<Integer> linksToRemove) throws Exception {
        String LOG_METHOD_NAME = "removeLearningObjectToCourseLink(Collection<Integer> linksToRemove)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();

            for ( Integer id : linksToRemove ) {
                String sql = DaoConstants.DELETE_COURSE_CULTURAL_CONTENT_LINK_BY_LINK_ID;
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id );
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public void unlinkLearningObjectToCourseObjective(int courseObjectiveId, int learningObjectId) throws Exception {

        String LOG_METHOD_NAME = "unlinkLearningObjectToCourseObjective(int courseObjectiveId, int learningObjectId)";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();

            String sql = DaoConstants.DELETE_COURSE_OBJECTIVE_CULTURAL_CONTENT_LINK;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseObjectiveId);
            preparedStatement.setInt(2, learningObjectId);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        } finally {
            // close all the connections.
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
    }

    @Override
    public UserDetailsBean getUserDetails( String userName ) throws Exception {

        String LOG_METHOD_NAME = "get user details";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        UserDetailsBean user = null;

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_USER_DETAILS;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userName);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
                user = new UserDetailsBean();
                user.setId( rset.getInt(1));
                user.setUser_name(rset.getString(2));
                user.setFirstName(rset.getString(3));
                user.setLastName(rset.getString(4));
                user.addRole(rset.getString(5));
                user.setRoleId( rset.getInt(6));
            }
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return user;
    }

    @Override
    public List<Role> getRoles() throws Exception {
        String LOG_METHOD_NAME = "get roles()";
        log.debug(this.getClass() + DaoConstants.LOG_BEGIN + LOG_METHOD_NAME);

        // declare a connection by using Connection interface
        Connection connection = null;

        // Declare prepare statement.
        PreparedStatement preparedStatement = null;

        // result set
        ResultSet rset = null;

        //return result
        List<Role> roles = new ArrayList<Role>();

        try {
            connection = getConnection();

            String sql = DaoConstants.GET_ROLES;
            preparedStatement = connection.prepareStatement(sql);
            rset = preparedStatement.executeQuery();

            while (rset.next ())
            {
                Role role = new Role();
                role.setId(rset.getInt(1));
                role.setName(rset.getString(2));
                role.setDescription(rset.getString(3));
                roles.add(role);
            }
        }catch (Exception ex) {
            // catch if found any exception during rum time.
            log.error(ex);
            throw ex;
        }finally {
            // close all the connections.
            rset.close();
            connection.close();
            preparedStatement.close();
            log.debug(this.getClass() + DaoConstants.LOG_END + LOG_METHOD_NAME);
        }
        return roles;
    }

    @Override
    public Set<Permission> getPermissionForRole(int roleId) {
        throw new UnsupportedOperationException( "not yet implemented" );
    }
    
    private String getInsertPlaceholders(int placeholderCount) {
        final StringBuilder builder = new StringBuilder("(");
        for ( int i = 0; i < placeholderCount; i++ ) {
            if ( i != 0 ) {
                builder.append(",");
            }
            builder.append("?");
        }
        return builder.append(")").toString();
    }
}
