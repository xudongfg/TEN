package com.ten.triplestore.dao.implementation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import virtuoso.jdbc3.VirtuosoConnectionPoolDataSource;
import virtuoso.jdbc3.VirtuosoDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.ten.beans.CourseAnnotationsBean;
import com.ten.beans.DigitalRightsManagementBean;
import com.ten.beans.LearningObjectBean;
import com.ten.beans.StudentAnnotationsBean;
import com.ten.beans.TenLearningObjectAnnotationsBean;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;
import com.ten.utils.Utils;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * @author Nita Karande
 * This class contains implementation methods for accessing triple store and functionality related to it
 */
public class VirtuosoAccessDaoImpl implements TriplestoreAccessDaoInterface{
	static Logger log = Logger.getLogger(VirtuosoAccessDaoImpl.class);
	
	private static DataSource  m_ds = null;
	//create datasource
    static  
    {  
        try
        {
        	Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			m_ds = (VirtuosoConnectionPoolDataSource)envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
        }
        catch (Exception e)
        {
            log.error(e);
            m_ds = null;
        }
    }
	
    private static String getUriForLearningObjectType(String learningObjectType) throws IllegalArgumentException {
        if (TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO.equalsIgnoreCase(learningObjectType)) {
            return TripleStoreConstants.URI_AUDIO;
        }
        else if (TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE.equalsIgnoreCase(learningObjectType)) {
            return TripleStoreConstants.URI_IMAGE;
        }
        else if (TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT.equalsIgnoreCase(learningObjectType)) {
            return TripleStoreConstants.URI_TEXT;
        }
        else if (TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO.equalsIgnoreCase(learningObjectType)) {
            return TripleStoreConstants.URI_VIDEO;
        }
        throw new IllegalArgumentException("Unknown learning object type: " + learningObjectType);
    }
    
	@Override
	/**
	 * This method is invoked by uploadImageAction to store annotations of image in triple store
	 * imageId passed as parameter forms part of URI to uniquely identify the image in triplestore
	 * imageId is the primary key id created while storing the image in database
	 */
	public boolean insertImageAnnotations(TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean, int imageId) throws Exception
	{		
		String LOG_METHOD_NAME = "insertImageAnnotations(TenLearningObjectAnnotationsBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node image = Node.createURI(TripleStoreConstants.URI_IMAGE + imageId);
						
			//add ten common annotation triples
			tripleList.addAll(createTenCommonAnnotationTriples(image, tenLearningObjectAnnotationsBean));
			
			//add ten annotation triples
			tripleList.addAll(createTenLearningObjectAnnotationTriples(image, tenLearningObjectAnnotationsBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	/**
	 * This method is invoked by uploadVideoAction to store annotations of video in triple store
	 * videoId passed as parameter forms part of URI to uniquely identify the video in triplestore
	 * videoId is the primary key id created while storing the video in database
	 */
	public boolean insertVideoAnnotations(
			TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean,
			int videoId) throws Exception {
		String LOG_METHOD_NAME = "insertVideoAnnotations(TenLearningObjectAnnotationsBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		VirtGraph graph = null;
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			VirtuosoDataSource ds = (VirtuosoConnectionPoolDataSource )envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
			
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node video = Node.createURI(TripleStoreConstants.URI_VIDEO + videoId);
						
			//add ten common annotation triples
			tripleList.addAll(createTenCommonAnnotationTriples(video, tenLearningObjectAnnotationsBean));
			
			//add ten annotation triples
			tripleList.addAll(createTenLearningObjectAnnotationTriples(video, tenLearningObjectAnnotationsBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	/**
	 * This method is invoked by uploadAudioAction to store annotations of audio in triple store
	 * audioId passed as parameter forms part of URI to uniquely identify the audio in triplestore
	 * audioId is the primary key id created while storing the audio in database
	 */
	public boolean insertAudioAnnotations(
			TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean,
			int audioId) throws Exception {
		
		String LOG_METHOD_NAME = "insertAudioAnnotations(TenLearningObjectAnnotationsBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
					
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI,m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node audio = Node.createURI(TripleStoreConstants.URI_AUDIO + audioId);
				
			//add ten common annotation triples
			tripleList.addAll(createTenCommonAnnotationTriples(audio, tenLearningObjectAnnotationsBean));
			
			//add ten annotation triples
			tripleList.addAll(createTenLearningObjectAnnotationTriples(audio, tenLearningObjectAnnotationsBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	/**
	 * This method is invoked by uploadTextAction to store annotations of text/document in triple store
	 * textId passed as parameter forms part of URI to uniquely identify the text/document in triplestore
	 * textId is the primary key id created while storing the text/document in database
	 */
	public boolean insertTextAnnotations(
			TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean,
			int textId) throws Exception {
		String LOG_METHOD_NAME = "insertTextAnnotations(TenLearningObjectAnnotationsBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node text = Node.createURI(TripleStoreConstants.URI_TEXT + textId);
						
			//add ten common annotation triples
			tripleList.addAll(createTenCommonAnnotationTriples(text, tenLearningObjectAnnotationsBean));
			
			//add ten annotation triples
			tripleList.addAll(createTenLearningObjectAnnotationTriples(text, tenLearningObjectAnnotationsBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}
	
public ArrayList<String> queryLearningObject(String learningObjectType, ArrayList<String> orSearchTerms, ArrayList<String> andSearchTerms) throws Exception{
		
		String LOG_METHOD_NAME = "void queryLearningObject(String)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		ArrayList<String> returnValue = new ArrayList<String>();
		
		try{
		
			StringBuffer sparqlQueryString = new StringBuffer();
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_IMAGE);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_AUDIO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_VIDEO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_TEXT);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);
			
			sparqlQueryString.append(" SELECT DISTINCT ?learning_object ");
			sparqlQueryString.append(" WHERE { ");
			sparqlQueryString.append(" ?learning_object a TenOntology:");
			sparqlQueryString.append(learningObjectType);	
			sparqlQueryString.append(" ;?predicate ?object");	
			
			//add predicate object for each and condition
			for(int i=1; i==andSearchTerms.size(); i++){
				sparqlQueryString.append("; ?predicate" + i + " ?object" + i);
			}
			
			//exclude type predicate
			sparqlQueryString.append(" . FILTER (");
	        sparqlQueryString.append(" (?predicate != rdf:type)");
	        
			//add filter for orSearchTerms	
	        int i = 0;
	        if((orSearchTerms != null) && (orSearchTerms.size()>0)){
	        	i=0;
	        	sparqlQueryString.append(" && ");
		        for(String searchTerm:orSearchTerms){
		        	sparqlQueryString.append("(regex(?object, \"");
		        	sparqlQueryString.append(searchTerm);
		        	sparqlQueryString.append("\", \"i\"))");
		        	//if this is not the last element add ||
		        	if(i != (orSearchTerms.size()-1)){
		        		sparqlQueryString.append(" || ");
		        	}
		        	i++;
		        }	
	        }
	        
	        //add filter for and search terms
	        if((andSearchTerms != null) && (andSearchTerms.size()>0)){
	        	i=0;
	        	sparqlQueryString.append(" && ");
	        	for(String searchTerm:andSearchTerms){
	        		sparqlQueryString.append("(regex(?object" + (i+1) + ", \"");
	        		sparqlQueryString.append(searchTerm);
	        		sparqlQueryString.append("\", \"i\"))");
		        	//if this is not the last element add ||
		        	if(i != (andSearchTerms.size()-1)){
		        		sparqlQueryString.append(" && ");
		        	}
		        	i++;
		        }	
	        }
	        
	        sparqlQueryString.append(" ) .}");
			
			log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
			
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//STEP 2 - Create query
			Query sparql = QueryFactory.create(sparqlQueryString.toString());
			
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, graph);
	
			//STEP 3 - Execute
			ResultSet results = vqe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
			    RDFNode rdfNode = result.get("learning_object");
			    returnValue.add(rdfNode.toString());
			    log.debug(graph + " { " + rdfNode + "  }");
			}
		}catch (Exception ex) {
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return returnValue;
	}
	
/**
 * This method will retrieve the learning objects which match user profile
 * If the preferred object type is not set, any learning object which matches the course requirement and student profile is displayed in recommended
 * If the preferred object type is set, only the learning objects of this type which match the course requirement and student preference are displayed
 * 
 */
public HashMap<String, ArrayList<String>> queryRecommendedLearningObjects(StudentAnnotationsBean studentAnnotationsBean, String keywords) throws Exception{
	
	String LOG_METHOD_NAME = "void queryLearningObject(StudentAnnotationsBean studentAnnotationsBean, ArrayList<String> orSearchTerms)";
	log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
	ArrayList<String> imagesList = new ArrayList<String>();
	ArrayList<String> audioList = new ArrayList<String>();
	ArrayList<String> videoList = new ArrayList<String>();
	ArrayList<String> textList = new ArrayList<String>();
	HashMap<String, ArrayList<String>> returnMap = new HashMap<String, ArrayList<String>>();
	try{
		
		StringBuffer sparqlQueryString = new StringBuffer();
		sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_IMAGE);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_AUDIO);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_VIDEO);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_TEXT);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
		sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);		
		sparqlQueryString.append(" SELECT DISTINCT ?learning_object ?learningObjectType ");
		sparqlQueryString.append(" WHERE { ");
			
		//base query with basic keywords conditions
		StringBuffer baseQuery =  new StringBuffer();		
		baseQuery.append(" ?learning_object a ?learningObjectType");
		baseQuery.append(" ;?predicate ?object");	
		
		//exclude type predicate
		baseQuery.append(" . FILTER (");
		baseQuery.append(" (?predicate != rdf:type)");  
		
		//add filter for keywords
        if(!Utils.isEmptyOrNull(keywords)){
        	ArrayList<String> orSearchTerms = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(keywords);
			while (st.hasMoreTokens()) {
				orSearchTerms.add(st.nextToken().trim());
			}
			
        	int i = 0;
        	if((orSearchTerms != null) && (orSearchTerms.size()>0)){
	        	i=0;
	        	baseQuery.append(" && ");
		        for(String searchTerm:orSearchTerms){
		        	baseQuery.append("(regex(?object, \"");
		        	baseQuery.append(searchTerm);
		        	baseQuery.append("\", \"i\"))");
		        	//if this is not the last element add ||
		        	if(i != (orSearchTerms.size()-1)){
		        		baseQuery.append(" || ");
		        	}
		        	i++;
		        }	
        	}
        }
        baseQuery.append(" )");
        
         //add learning object type preference
		if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredLearningObjectType())){
			baseQuery.append("FILTER ( ?learningObjectType = TenOntology:" +  studentAnnotationsBean.getPreferredLearningObjectType() + " )");
		}else{
			baseQuery.append("FILTER ((?learningObjectType = TenOntology:" +  TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE + " )");
			baseQuery.append("|| (?learningObjectType = TenOntology:" +  TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT + " )");
			baseQuery.append("|| (?learningObjectType = TenOntology:" +  TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO + " )");
			baseQuery.append("|| (?learningObjectType = TenOntology:" +  TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO + " )");
			baseQuery.append(" )");
		}
			
		boolean upperCondition = false;
		//Add tribe
		if(!Utils.isEmptyOrNull(studentAnnotationsBean.getTribe())){
			if(upperCondition){
				sparqlQueryString.append( " UNION " );
			}
			sparqlQueryString.append(" { ");
			sparqlQueryString.append(baseQuery.toString());			
			sparqlQueryString.append(" ?learning_object <" + TripleStoreConstants.URI_PREDICATE_TEN_LO_TRIBE + "> ?tribe . ");
			sparqlQueryString.append(" FILTER (regex(?tribe, \"" + studentAnnotationsBean.getTribe() + "\", \"i\" ))" );
			sparqlQueryString.append(" } ");
			upperCondition = true;
		}
		
		//Add language preference
		if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredLanguage())){
			if(upperCondition){
				sparqlQueryString.append( " UNION " );
			}
			sparqlQueryString.append(" { ");
			sparqlQueryString.append(baseQuery.toString());
			sparqlQueryString.append(" ?learning_object <" + TripleStoreConstants.URI_PREDICATE_DC_LANGUAGE + "> ?language . ");
			sparqlQueryString.append(" FILTER (regex(?language, \"" + studentAnnotationsBean.getPreferredLanguage() + "\", \"i\" ))" );
			sparqlQueryString.append(" } ");
			upperCondition = true;
		}
		
		//Add preferred text type
		if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredTextContent())){
			if(upperCondition){
				sparqlQueryString.append( " UNION " );
			}
			sparqlQueryString.append(" { ");
			sparqlQueryString.append(baseQuery.toString());
			sparqlQueryString.append(" ?learning_object <" + TripleStoreConstants.URI_PREDICATE_TEN_TEXT_TYPE + "> ?textType . ");
			sparqlQueryString.append(" FILTER (regex(?textType, \"" + studentAnnotationsBean.getPreferredTextContent() + "\", \"i\" ))" );
			sparqlQueryString.append(" } ");
			upperCondition = true;
		}
		
		//Add preferred image type
		if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredImageContent())){
			if(upperCondition){
				sparqlQueryString.append( " UNION " );
			}
			sparqlQueryString.append(" { ");
			sparqlQueryString.append(baseQuery.toString());
			sparqlQueryString.append(" { ?learning_object <" + TripleStoreConstants.URI_PREDICATE_TEN_IMAGE_TYPE + "> ?imageType . ");
			sparqlQueryString.append(" FILTER (regex(?imageType, \"" + studentAnnotationsBean.getPreferredImageContent() + "\", \"i\" )) " );
			sparqlQueryString.append(" } ");
			upperCondition = true;
		}
		
		
		//display learning objects of type as recommeded
		if((upperCondition == false) && (!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredLearningObjectType()))){
			sparqlQueryString.append(baseQuery.toString());
			upperCondition = true;
		}
		
		sparqlQueryString.append(" }");
		
		log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
		
		if(upperCondition == true){
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			Query sparql = QueryFactory.create(sparqlQueryString.toString());
			
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql.toString(), graph);
	
			//STEP 3 - Execute
			ResultSet results = vqe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
			    RDFNode rdfNodeSubject = result.get("learning_object");
			    RDFNode rdfNodeObject = result.get("learningObjectType");
			    if((TripleStoreConstants.URI_TEN_ONTOLOGY + TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE).equals(rdfNodeObject.toString())){
			    	imagesList.add(rdfNodeSubject.toString());
			    }else if((TripleStoreConstants.URI_TEN_ONTOLOGY + TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO).equals(rdfNodeObject.toString())){
			    	audioList.add(rdfNodeSubject.toString());
			    }else if((TripleStoreConstants.URI_TEN_ONTOLOGY + TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO).equals(rdfNodeObject.toString())){
			    	videoList.add(rdfNodeSubject.toString());
			    }else if((TripleStoreConstants.URI_TEN_ONTOLOGY + TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT).equals(rdfNodeObject.toString())){
			    	textList.add(rdfNodeSubject.toString());
			    }
			 }
		}		
		returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE, imagesList);
		returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO, audioList);
		returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO, videoList);
		returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT, textList);
		
	}catch (Exception ex) {
		log.error(ex);
		throw ex;
	}finally{			
		log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
	}
	return returnMap;
}

	@Override
	public boolean insertImageDigitalRightsManagementData(
			DigitalRightsManagementBean digitalRightsManagementBean, int imageId)
			throws Exception {
		String LOG_METHOD_NAME = "insertImageDigitalRightsManagementData(DigitalRightsManagementBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node image = Node.createURI(TripleStoreConstants.URI_IMAGE + imageId);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_RDF_TYPE);
			Node predicate_value = Node.createURI(TripleStoreConstants.URI_TEN_ONTOLOGY_IMAGE_OBJECT);
			tripleList.add(new Triple(image, predicate, predicate_value));
			
			//add dc annotation tuples
			tripleList.addAll(createDigitalRightsAnnotationTriples(image, digitalRightsManagementBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	public boolean insertVideoDigitalRightsManagementData(
			DigitalRightsManagementBean digitalRightsManagementBean, int videoId)
			throws Exception {
		String LOG_METHOD_NAME = "insertVideoDigitalRightsManagementData(digitalRightsManagementBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		VirtGraph graph = null;
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			VirtuosoDataSource ds = (VirtuosoConnectionPoolDataSource )envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
			
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node video = Node.createURI(TripleStoreConstants.URI_VIDEO + videoId);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_RDF_TYPE);
			Node predicate_value = Node.createURI(TripleStoreConstants.URI_TEN_ONTOLOGY_VIDEO_OBJECT);
			tripleList.add(new Triple(video, predicate, predicate_value));
			
			//add dc annotation tuples
			tripleList.addAll(createDigitalRightsAnnotationTriples(video, digitalRightsManagementBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	public boolean insertAudioDigitalRightsManagementData(
			DigitalRightsManagementBean digitalRightsManagementBean, int audioId)
			throws Exception {
		String LOG_METHOD_NAME = "insertAudioDigitalRightsManagementData(DigitalRightsManagementBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			VirtuosoDataSource ds = (VirtuosoConnectionPoolDataSource )envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
			
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node audio = Node.createURI(TripleStoreConstants.URI_AUDIO + audioId);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_RDF_TYPE);
			Node predicate_value = Node.createURI(TripleStoreConstants.URI_TEN_ONTOLOGY_AUDIO_OBJECT);
			tripleList.add(new Triple(audio, predicate, predicate_value));
			
			//add dc annotation tuples
			tripleList.addAll(createDigitalRightsAnnotationTriples(audio, digitalRightsManagementBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}

	@Override
	public boolean insertTextDigitalRightsManagementData(
			DigitalRightsManagementBean digitalRightsManagementBean, int textId)
			throws Exception {
		String LOG_METHOD_NAME = "insertTextDigitalRightsManagementData(DigitalRightsManagementBean, int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node text = Node.createURI(TripleStoreConstants.URI_TEXT + textId);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_RDF_TYPE);
			Node predicate_value = Node.createURI(TripleStoreConstants.URI_TEN_ONTOLOGY_TEXT_OBJECT);
			tripleList.add(new Triple(text, predicate, predicate_value));
			
			//add dc annotation tuples
			tripleList.addAll(createDigitalRightsAnnotationTriples(text, digitalRightsManagementBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}
	
	
	/**
	 * This method is invoked for creating Dublin core annotation triples of images,audios,videos and text
	 * It creates an arraylist containing triples for the the learning object dublic core annotations, to be inserted in triple store.
	 */
	public ArrayList<Triple> createDigitalRightsAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = "ArrayList createDigitalRightsAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();

		try{					
			tripleList.addAll(addAdministrativeAnnotationTriples(subject, digitalRightsManagementBean));
			
			tripleList.addAll(addCopyRightHolderAnnotationTriples(subject, digitalRightsManagementBean));
			
			tripleList.addAll(addPublisherAnnotationTriples(subject, digitalRightsManagementBean));
			
			tripleList.addAll(addContributorAnnotationTriples(subject, digitalRightsManagementBean));			
						
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * Add descriptive tags
	 */
	public ArrayList<Triple> addDescriptiveAnnotationTriples(Node subject ,TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean){
		
		String LOG_METHOD_NAME = "ArrayList<Triple> addDescriptiveAnnotationTriples(Node, TenLearningObjectAnnotationsBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{
			//DESCRIPTIVE
			//title
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getTitle())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_TITLE);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getTitle());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//Keywords
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getKeywords())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_KEYWORDS);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getKeywords());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//description
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getDescription())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_DESCRIPTION);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getDescription());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//language	
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getLanguage())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_LANGUAGE);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getLanguage());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//tribe
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getTribe())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_LO_TRIBE);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getTribe());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}	
			
		
			//relevant subjects
            if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getRelevantSubjects())){
                predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_RELEVANT_SUBJECTS);
                predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getRelevantSubjects());
                tripleList.add(new Triple(subject, predicate, predicate_value));
            }
			
			
			//image type
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getImageType())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_IMAGE_TYPE);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getImageType());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//text type
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getTextType())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_TEXT_TYPE);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getTextType());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * add structural annotation triples
	 */
	public ArrayList<Triple> addStructuralAnnotationTriples(Node subject ,TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean){
		
		String LOG_METHOD_NAME = "ArrayList<Triple> addStructuralAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{
			//STRUCTURAL	
			//date of annotation
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getDate())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_DATE_ANNOTATION);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getDate());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//format
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getFormat())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_FORMAT);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getFormat());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}			
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * This method is invoked for creating annotations defined by Tribal Education Network Ontology, and those common for learning objects 
	 * as well as other elearning objects like courses, pages etc.
	 * It creates an arraylist containing triples for annotations common to all the objects in ten, to be inserted in triple store
	 */
	public ArrayList<Triple> createTenCommonAnnotationTriples(Node subject ,TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean){
		
		String LOG_METHOD_NAME = "ArrayList createTenCommonAnnotationTriples(Node, TenLearningObjectAnnotationsBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		
		try{
			//modified
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getModified())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_MODIFIED);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getModified());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//rightsHolder	
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getRightsHolder())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_RIGHTS_HOLDER);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getRightsHolder());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//isPartOf	
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getIsPartOf())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_ISPARTOF);
				predicate_value = Node.createURI(tenLearningObjectAnnotationsBean.getIsPartOf());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//hasPart	
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getHasPart())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_HASPART);
				predicate_value = Node.createURI(tenLearningObjectAnnotationsBean.getHasPart());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}			
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * Add administrative annotation triples
	 */
	public ArrayList<Triple> addAdministrativeAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = " ArrayList<Triple> addAdministrativeAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{
			//tribe	
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getTribe())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_TRIBE);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getTribe());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}	
			
			//physicalDescription	
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPhysicalDescription())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_PHYSICAL_DESCRIPTION);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getPhysicalDescription());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//loan period	
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getLoanPeriod())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_LOAN_PERIOD);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getLoanPeriod());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//identifier
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getIdentifier())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getIdentifier());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//identifier description
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getIdentifierDescription())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER_DESCRIPTION);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getIdentifierDescription());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//handling instructions
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getHandlingInstructions())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_HANDLING_INSTRUCTIONS);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getHandlingInstructions());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//rights	
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getRights())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_RIGHTS);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getRights());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//intaker
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getIntaker())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_INTAKER);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getIntaker());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//date of learning object creation
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getDateOfUpload())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_DATE_UPLOAD);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getDateOfUpload());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}			
			
			//Story provided
			boolean storyProvided = (!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProvided()) 
					&& "true".equals(digitalRightsManagementBean.getStoryProvided()))?true:false;
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDED);
			predicate_value = Node.createLiteral(String.valueOf(storyProvided));
			tripleList.add(new Triple(subject, predicate, predicate_value));
			
			//story
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStory())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STORY);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getStory());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}	
			
			//story context
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryContext())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STORY_CONTEXT);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getStoryContext());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}	
			
			if(storyProvided){
				tripleList.addAll(addStoryProviderAnnotationTriples(subject, digitalRightsManagementBean));
			}
					
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * Add copy right holder annotation triples
	 */
	public ArrayList<Triple> addCopyRightHolderAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = " ArrayList<Triple> addCopyRightHolderTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{						
			//copy right holder
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderNotAvailable()) && ("true".equals(digitalRightsManagementBean.getCopyRightHolderNotAvailable()))){
				//copy right holder not available
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_NOT_AVAILABLE);
				predicate_value = Node.createLiteral(digitalRightsManagementBean.getCopyRightHolderNotAvailable());
				tripleList.add(new Triple(subject, predicate, predicate_value));
				
				//copy right holder finder info
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderFinderInfo())){
					predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_FINDER_INFO);
					predicate_value = Node.createLiteral(digitalRightsManagementBean.getCopyRightHolderFinderInfo());
					tripleList.add(new Triple(subject, predicate, predicate_value));
				}
			}else{
				//copy right holder not available
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_NOT_AVAILABLE);
				predicate_value = Node.createLiteral("false");
				tripleList.add(new Triple(subject, predicate, predicate_value));
				
				//copy right holder information
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER);
				StringBuffer copyRightHolderAttributes = new StringBuffer();
				
				//id
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderId())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_ID);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderId());
					copyRightHolderAttributes.append(";");
				}
				
				//email
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderEmail())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_EMAIL);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderEmail());
					copyRightHolderAttributes.append(";");
				}
				
				//copy right holder approved
				boolean approved = (!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderApproved()) 
									&& "true".equals(digitalRightsManagementBean.getCopyRightHolderApproved()))?true:false;
				copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_APPROVED);
				copyRightHolderAttributes.append("=");
				copyRightHolderAttributes.append(approved);
				copyRightHolderAttributes.append(";");
				
				
				//cell phone
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderCellPhone())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_CELL_PHONE);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderCellPhone());
					copyRightHolderAttributes.append(";");
				}
				
				//office phone
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderOfficePhone())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderOfficePhone());
					copyRightHolderAttributes.append(";");
				}
				
				//FAX
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderFax())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_FAX);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderFax());
					copyRightHolderAttributes.append(";");
				}
				
				//street address
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderStreetAddress())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderStreetAddress());
					copyRightHolderAttributes.append(";");
				}
				
				//other address
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderOtherAddress())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderOtherAddress());
					copyRightHolderAttributes.append(";");
				}
				
				//city
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderCity())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_CITY);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderCity());
					copyRightHolderAttributes.append(";");
				}
				
				//state
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderState())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_STATE);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderState());
					copyRightHolderAttributes.append(";");
				}
				
				//state
				if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getCopyRightHolderZipCode())){
					copyRightHolderAttributes.append(TripleStoreConstants.ATTRIBUTE_ZIP_CODE);
					copyRightHolderAttributes.append("=");
					copyRightHolderAttributes.append(digitalRightsManagementBean.getCopyRightHolderZipCode());
					copyRightHolderAttributes.append(";");
				}
				
				predicate_value = Node.createLiteral(copyRightHolderAttributes.toString());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * Add publisher annotation triples
	 */
	public ArrayList<Triple> addPublisherAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = " ArrayList<Triple> addPublisherAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{						
			//PUBLISHER information
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_PUBLISHER);
			StringBuffer publisherAttributes = new StringBuffer();
			
			//id
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisher())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_ID);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisher());
				publisherAttributes.append(";");
			}
			
			//email
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherEmail())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_EMAIL);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherEmail());
				publisherAttributes.append(";");
			}
			
			//approved
			boolean approved = (!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherApproved()) 
					&& "true".equals(digitalRightsManagementBean.getPublisherApproved()))?true:false;
			publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_APPROVED);
			publisherAttributes.append("=");
			publisherAttributes.append(approved);
			publisherAttributes.append(";");
						
			//cell phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherCellPhone())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_CELL_PHONE);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherCellPhone());
				publisherAttributes.append(";");
			}
			
			//office phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherOfficePhone())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherOfficePhone());
				publisherAttributes.append(";");
			}
			
			//FAX
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherFax())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_FAX);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherFax());
				publisherAttributes.append(";");
			}
			
			//street address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherStreetAddress())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherStreetAddress());
				publisherAttributes.append(";");
			}
			
			//other address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherOtherAddress())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherOtherAddress());
				publisherAttributes.append(";");
			}
			
			//city
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherCity())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_CITY);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherCity());
				publisherAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherState())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_STATE);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherState());
				publisherAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getPublisherZipCode())){
				publisherAttributes.append(TripleStoreConstants.ATTRIBUTE_ZIP_CODE);
				publisherAttributes.append("=");
				publisherAttributes.append(digitalRightsManagementBean.getPublisherZipCode());
				publisherAttributes.append(";");
			}
			
			predicate_value = Node.createLiteral(publisherAttributes.toString());
			tripleList.add(new Triple(subject, predicate, predicate_value));
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * Add story provider annotation triples
	 */
	public ArrayList<Triple> addStoryProviderAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = " ArrayList<Triple> addStoryProviderAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{						
			//copy right holder information
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDER);
			StringBuffer storyProviderAttributes = new StringBuffer();
			
			//id
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProvider())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_ID);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProvider());
				storyProviderAttributes.append(";");
			}
			
			//email
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderEmail())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_EMAIL);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderEmail());
				storyProviderAttributes.append(";");
			}
			
			//cell phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderCellPhone())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_CELL_PHONE);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderCellPhone());
				storyProviderAttributes.append(";");
			}
			
			//office phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderOfficePhone())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderOfficePhone());
				storyProviderAttributes.append(";");
			}
			
			//FAX
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderFax())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_FAX);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderFax());
				storyProviderAttributes.append(";");
			}
			
			//street address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderStreetAddress())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderStreetAddress());
				storyProviderAttributes.append(";");
			}
			
			//other address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderOtherAddress())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderOtherAddress());
				storyProviderAttributes.append(";");
			}
			
			//city
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderCity())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_CITY);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderCity());
				storyProviderAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderState())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_STATE);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderState());
				storyProviderAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getStoryProviderZipCode())){
				storyProviderAttributes.append(TripleStoreConstants.ATTRIBUTE_ZIP_CODE);
				storyProviderAttributes.append("=");
				storyProviderAttributes.append(digitalRightsManagementBean.getStoryProviderZipCode());
				storyProviderAttributes.append(";");
			}
			
			predicate_value = Node.createLiteral(storyProviderAttributes.toString());
			tripleList.add(new Triple(subject, predicate, predicate_value));
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	

	/**
	 * Add contributor annotation triples
	 */
	public ArrayList<Triple> addContributorAnnotationTriples(Node subject ,DigitalRightsManagementBean digitalRightsManagementBean){
		
		String LOG_METHOD_NAME = " ArrayList<Triple> addContributorAnnotationTriples(Node, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{						
			//copy right holder information
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_CONTRIBUTOR);
			StringBuffer contributorAttributes = new StringBuffer();
			
			//id
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributor())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_ID);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributor());
				contributorAttributes.append(";");
			}
			
			//email
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorEmail())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_EMAIL);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorEmail());
				contributorAttributes.append(";");
			}
						
			//approved			
			boolean approved = (!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorApproved()) 
					&& "true".equals(digitalRightsManagementBean.getContributorApproved()))?true:false;
			contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_APPROVED);
			contributorAttributes.append("=");
			contributorAttributes.append(approved);
			contributorAttributes.append(";");
						
			//cell phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorCellPhone())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_CELL_PHONE);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorCellPhone());
				contributorAttributes.append(";");
			}
			
			//office phone
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorOfficePhone())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorOfficePhone());
				contributorAttributes.append(";");
			}
			
			//FAX
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorFax())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_FAX);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorFax());
				contributorAttributes.append(";");
			}
			
			//street address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorStreetAddress())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorStreetAddress());
				contributorAttributes.append(";");
			}
			
			//other address
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorOtherAddress())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorOtherAddress());
				contributorAttributes.append(";");
			}
			
			//city
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorCity())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_CITY);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorCity());
				contributorAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorState())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_STATE);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorState());
				contributorAttributes.append(";");
			}
			
			//state
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorZipCode())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_ZIP_CODE);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorZipCode());
				contributorAttributes.append(";");
			}
			
			//tribal affiliation
			if(!Utils.isEmptyOrNull(digitalRightsManagementBean.getContributorTribalAffiliation())){
				contributorAttributes.append(TripleStoreConstants.ATTRIBUTE_TRIBAL_AFFILIATION);
				contributorAttributes.append("=");
				contributorAttributes.append(digitalRightsManagementBean.getContributorTribalAffiliation());
				contributorAttributes.append(";");
			}
			
			predicate_value = Node.createLiteral(contributorAttributes.toString());
			tripleList.add(new Triple(subject, predicate, predicate_value));
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * This method is invoked for creating annotations defined by Tribal Education Network Ontology, and those specific to learning objects. 
	 * Creates an arraylist containing triples specific to ten learning objects, to be inserted in triple store
	 */
	public ArrayList<Triple> createTenLearningObjectAnnotationTriples(Node subject ,TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean){
		
		String LOG_METHOD_NAME = "ArrayList createTenLearningObjectAnnotationTriples(Node, TenLearningObjectAnnotationsBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		try{
			//annotator name
			if(!Utils.isEmptyOrNull(tenLearningObjectAnnotationsBean.getAnnotator())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_ANNOTATOR);
				predicate_value = Node.createLiteral(tenLearningObjectAnnotationsBean.getAnnotator());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			tripleList.add(new Triple(subject, predicate, predicate_value));
			
			tripleList.addAll(addDescriptiveAnnotationTriples(subject, tenLearningObjectAnnotationsBean));
			
			tripleList.addAll(addStructuralAnnotationTriples(subject, tenLearningObjectAnnotationsBean));						
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}
	
	/**
	 * 
	 */
	public boolean insertCourseAnnotations(CourseAnnotationsBean courseAnnotationsBean, int courseId) throws Exception{
		String LOG_METHOD_NAME = "boolean insertCourseAnnotations(CourseAnnotationsBean , int)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		VirtGraph graph = null;
		try{
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
	
			ArrayList<Triple> tripleList = new ArrayList<Triple>();			
			Node course = Node.createURI(TripleStoreConstants.URI_COURSE + courseId);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_RDF_TYPE);
			Node predicate_value = Node.createURI(TripleStoreConstants.URI_TEN_ONTOLOGY_COURSE_OBJECT);
			tripleList.add(new Triple(course, predicate, predicate_value));
			
			//add course annotation tuples
			tripleList.addAll(createCourseAnnotationTriples(course, courseAnnotationsBean));
			
			for(int i=0; i<tripleList.size(); i++){
				graph.add(tripleList.get(i));
			}
		
			//End transaction
			graph.getTransactionHandler().commit();
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
	   	return true;
	}
	
	/**
	 * This method is invoked for creating annotations defined by Tribal Education Network Ontology, and those common for learning objects 
	 * as well as other elearning objects like courses, pages etc.
	 * It creates an arraylist containing triples for annotations common to all the objects in ten, to be inserted in triple store
	 */
	public ArrayList<Triple> createCourseAnnotationTriples(Node subject ,CourseAnnotationsBean courseAnnotationBean){
		
		String LOG_METHOD_NAME = "ArrayList<Triple> createCourseAnnotationTriples(Node subject ,CourseAnnotationsBean courseAnnotationBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		ArrayList<Triple> tripleList = new ArrayList<>();
		Node predicate = null, predicate_value=null;
		
		try{
			
			//modified
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getModified())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_MODIFIED);
				predicate_value = Node.createLiteral(courseAnnotationBean.getModified());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//rightsHolder	
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getRightsHolder())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_RIGHTS_HOLDER);
				predicate_value = Node.createLiteral(courseAnnotationBean.getRightsHolder());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//hasPart	
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getHasPart())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_HASPART);
				predicate_value = Node.createLiteral(courseAnnotationBean.getHasPart());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}	
			
			//creator
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getCreator())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_CREATOR);
				predicate_value = Node.createLiteral(courseAnnotationBean.getCreator());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//description 
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getDescription())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_DC_DESCRIPTION);
				predicate_value = Node.createLiteral(courseAnnotationBean.getDescription());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
			
			//keywords
			if(!Utils.isEmptyOrNull(courseAnnotationBean.getKeywords())){
				predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_KEYWORDS);
				predicate_value = Node.createLiteral(courseAnnotationBean.getKeywords());
				tripleList.add(new Triple(subject, predicate, predicate_value));
			}
		}catch(Exception ex)
		{
			log.error(ex);
			throw ex;
		}finally{
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		
		return tripleList;
	}

	public void testDictionary(String keyword) throws IOException {
    	// construct the URL to the Wordnet dictionary directory
//    	String wnhome = System.getenv (" WNHOME ");
//    	String path = wnhome + File.separator + "dict";
    	String path = "C:\\TEN\\workspace\\TribalEducationNetwork\\WebContent\\WNdb-3.0\\dict";
    	System.out.println ("WNDB path is " + path );
    	URL url = new URL("file", null , path );
    	// construct the dictionary object and open it
    	IDictionary dict = new Dictionary (url);
    	dict.open();

    	// look up first sense of the word "dog "
    	IIndexWord idxWord = dict.getIndexWord(keyword, POS.NOUN );
    	IWordID wordID = idxWord.getWordIDs().get(0) ;
    	IWord word = dict.getWord(wordID);
    	System.out.println("Id = " + wordID );
    	List<IWordID> relatedWords = word.getRelatedWords();
    	for(IWordID wid : relatedWords){
    		IWord eachWord = dict.getWord(wid);
    		System.out.println(eachWord.getLemma());
    	}
    	
    	ISynset synset = word.getSynset();

//    	Get hypernyms from keyword.
    	ArrayList<String> hypernyms = getHypernyms(dict, keyword);
    	
    	for(String hypernym : hypernyms){
    		ArrayList<String> second_layer_results = getHypernyms(dict, hypernym);
    		printWord(second_layer_results, "Second layer hypernym = ");
//    		System.out.println(" hypernym = " + hypernym);
    	}
    	printWord(hypernyms, "First layer hypernym = ");
    	
//    	Get synonyms from keyword.
    	ArrayList<String> synonyms = getSynonyms(dict, keyword);
//    	for(String synonym : synonyms){
//    		System.out.println(" Synonym = " + synonym);
//    	}
    	
//    	for(IWord w : synset.getWords()){
//    		System.out.println(w.getLemma());
//    	}
//    	System.out.println(" Lemma = " + word.getLemma ());
//    	System.out.println(" Gloss = " + word.getSynset().getGloss());
    }
	
	public void printWord(ArrayList<String> wordlist, String line_header){
		for(String word : wordlist){
    		System.out.println(line_header + word);
    	}
	}
	
	public ArrayList<String> getSynonyms(IDictionary dict, String keyword){
		ArrayList<String> synonyms = new ArrayList<String>();
		
		// look up first sense of the keyword
    	IIndexWord idxWord = dict.getIndexWord(keyword, POS.NOUN );
    	IWordID wordID = idxWord.getWordIDs().get(0) ;
    	IWord word = dict.getWord(wordID);
    	ISynset synset = word.getSynset();
		for(IWord w : synset.getWords()){
			synonyms.add(w.getLemma());
//    		System.out.println(w.getLemma());
    	}
		return synonyms;
	}
	
	public ArrayList<String> getHypernyms(IDictionary dict, String keyword){
		ArrayList<String> hypernymResult  = new ArrayList<String>();
		
		// look up first sense of the keyword
    	IIndexWord idxWord = dict.getIndexWord(keyword, POS.NOUN );
    	IWordID wordID = idxWord.getWordIDs().get(0) ;
    	IWord word = dict.getWord(wordID);
    	ISynset synset = word.getSynset();
		
//		get the hypernyms
		List<ISynsetID> hypernyms = synset.getRelatedSynsets();
//		print out each hypernyms id and synonyms
		List <IWord> words;
		for(ISynsetID sid : hypernyms){
			words = dict.getSynset(sid).getWords();
			for(Iterator<IWord> i = words.iterator(); i.hasNext();){
				String hypernym = i.next().getLemma();
				hypernymResult.add(hypernym);
			}
		}
		return hypernymResult;
	}
	
	@Override
	public HashMap<String, TenLearningObjectAnnotationsBean> searchLearningObjects(
			String type, String keywords, String andSearchTerms) throws Exception {
		HashMap<String, TenLearningObjectAnnotationsBean> returnMap = new HashMap<String, TenLearningObjectAnnotationsBean>();
		TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean = null;
		
		String LOG_METHOD_NAME = "HashMap<String, TenLearningObjectAnnotationsBean> searchLearningObjects(String type, String keywords)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		VirtGraph graph = null;
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			VirtuosoDataSource ds = (VirtuosoConnectionPoolDataSource )envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
			
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, ds);
			
			//separate the search terms
			ArrayList<String> orSearchTerms = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(keywords);
			while (st.hasMoreTokens()) {
				String nextKeyword = st.nextToken().trim();
				orSearchTerms.add(nextKeyword);
//				Test JWI search
				testDictionary(nextKeyword);
			}
			
			
			ArrayList<String> andSearchTermsList = new ArrayList<String>();
			StringTokenizer andSt = new StringTokenizer(andSearchTerms);
			while (andSt.hasMoreTokens()) {
				andSearchTermsList.add(andSt.nextToken().trim());
			}
			
			ArrayList<String> uris = new ArrayList<String>();		
			uris = queryLearningObject(type, orSearchTerms, andSearchTermsList);				
						
			for(String uri: uris){
				tenLearningObjectAnnotationsBean = new TenLearningObjectAnnotationsBean();
				returnMap.put(uri, tenLearningObjectAnnotationsBean);
			}
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return returnMap;
	}

	@Override
	public  HashMap<String, ArrayList<String>> queryDefaultLearningObjects(String keywords, String andSearchTerms) throws Exception {
		HashMap<String, ArrayList<String>> returnMap = new HashMap<String, ArrayList<String>>();
		
		String LOG_METHOD_NAME = "HashMap<String, TenLearningObjectAnnotationsBean> searchLearningObjects(String type, String keywords)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		
		VirtGraph graph = null;
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			VirtuosoDataSource ds = (VirtuosoConnectionPoolDataSource )envContext.lookup(TripleStoreConstants.VIRTUOSO_JNDI_LOOKUP_NAME);
			
			graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, ds);
			
			//separate the search terms
			ArrayList<String> orSearchTerms = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(keywords);
			while (st.hasMoreTokens()) {
				orSearchTerms.add(st.nextToken().trim());
			}
			
			ArrayList<String> andSearchTermsList = new ArrayList<String>();
			StringTokenizer andSt = new StringTokenizer(andSearchTerms);
			while (andSt.hasMoreTokens()) {
				andSearchTermsList.add(andSt.nextToken().trim());
			}
			
			//query images
			ArrayList<String> imagesList = new ArrayList<String>();		
			imagesList = queryLearningObject(TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE, orSearchTerms, andSearchTermsList);				
			returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_IMAGE, imagesList);
			
			//query audios
			ArrayList<String> audioList = new ArrayList<String>();		
			audioList = queryLearningObject(TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO, orSearchTerms, andSearchTermsList);				
			returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_AUDIO, audioList);
			
			//query videos
			ArrayList<String> videoList = new ArrayList<String>();		
			videoList = queryLearningObject(TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO, orSearchTerms, andSearchTermsList);				
			returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_VIDEO, videoList);
			
			//query text
			ArrayList<String> textList = new ArrayList<String>();		
			textList = queryLearningObject(TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT, orSearchTerms, andSearchTermsList);				
			returnMap.put(TripleStoreConstants.LEARNING_OBJECT_TYPE_TEXT, textList);
			
		}catch (Exception ex) {
			if(graph!=null){
				graph.getTransactionHandler().abort();
			}
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}		
		return returnMap;
	}
	@Override
	public CourseAnnotationsBean getCourseAnnotations(int courseId)
			throws Exception {
		String LOG_METHOD_NAME = "CourseAnnotationsBean getCourseAnnotations(int courseId)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		CourseAnnotationsBean courseAnnotationsBean = new CourseAnnotationsBean();
		
		try{		
			StringBuffer sparqlQueryString = new StringBuffer();
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_IMAGE);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_AUDIO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_VIDEO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_TEXT);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);
			
			sparqlQueryString.append(" SELECT ?predicate ?object ");
			sparqlQueryString.append(" WHERE { ");
			sparqlQueryString.append("<" + TripleStoreConstants.URI_COURSE + courseId + ">");
			sparqlQueryString.append(" a TenOntology:");
			sparqlQueryString.append(TripleStoreConstants.TYPE_COURSE);	
			sparqlQueryString.append(" ;?predicate ?object .");			
			sparqlQueryString.append(" }");
			
			log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
			
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//STEP 2 - Create query
			Query sparql = QueryFactory.create(sparqlQueryString.toString());
			
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql.toString(), graph);
	
			//STEP 3 - Execute
			ResultSet results = vqe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
			    RDFNode predicateNode = result.get("predicate");
			    RDFNode objectNode = result.get("object");
			    
			    log.debug(graph + " { " + predicateNode + " ; " + objectNode + "  }");
			    
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_MODIFIED)){
			    	//modified
			    	courseAnnotationsBean.setModified(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_RIGHTS_HOLDER)){
			    	//rights holder
			    	courseAnnotationsBean.setRightsHolder(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_ISPARTOF)){
			    	//isPartOf
			    	courseAnnotationsBean.setIsPartOf(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_COMMON_HASPART)){
			    	//hasPart
			    	courseAnnotationsBean.setHasPart(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_DC_CREATOR)){
			    	//creator
			    	courseAnnotationsBean.setCreator(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_DC_DESCRIPTION)){
			    	//description
			    	courseAnnotationsBean.setDescription(objectNode.toString());
			    }else if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_KEYWORDS)){
			    	//keywords
			    	courseAnnotationsBean.setKeywords(objectNode.toString());
			    }
			}
		}catch (Exception ex) {
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return courseAnnotationsBean;
	}

	@Override
	public StudentAnnotationsBean getStudentAnnotations(String user_name)
			throws Exception {
		String LOG_METHOD_NAME = "CourseAnnotationsBean getStudentAnnotations(String user_name)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		StudentAnnotationsBean studentAnnotationsBean = new StudentAnnotationsBean();
		
		try{		
			StringBuffer sparqlQueryString = new StringBuffer();
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_IMAGE);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_AUDIO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_VIDEO);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_TEXT);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);
			sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_STUDENT);
			
			sparqlQueryString.append(" SELECT ?predicate ?object ");
			sparqlQueryString.append(" WHERE { ");
			sparqlQueryString.append("<" + TripleStoreConstants.URI_STUDENT + user_name + ">");
			sparqlQueryString.append(" ?predicate ?object .");			
			sparqlQueryString.append(" }");
			
			log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
			
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//STEP 2 - Create query
			Query sparql = QueryFactory.create(sparqlQueryString.toString());
			
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, graph);
	
			//STEP 3 - Execute
			ResultSet results = vqe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.nextSolution();
			    RDFNode predicateNode = result.get("predicate");
			    RDFNode objectNode = result.get("object");
			    
			    log.debug(graph + " { " + predicateNode + " ; " + objectNode + "  }");
			    
			    //tribe
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_TRIBE)){
			    	studentAnnotationsBean.setTribe(objectNode.toString());
			    }
			    
			    //learning object type preference
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TYPE)){
			    	studentAnnotationsBean.setPreferredLearningObjectType(objectNode.toString());
			    }
			    
			    //learning object text preference
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TEXT_TYPE)){
			    	studentAnnotationsBean.setPreferredTextContent(objectNode.toString());
			    }
			    
			    //learning object image preference
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_IMAGE_TYPE)){
			    	studentAnnotationsBean.setPreferredImageContent(objectNode.toString());
			    }
			    
			   //preferred language
			    if(predicateNode.toString().contains(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LANGUAGE)){
			    	studentAnnotationsBean.setPreferredLanguage(objectNode.toString());
			    }
			}
		}catch (Exception ex) {
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return studentAnnotationsBean;
	}

	@Override
	public void updateStudentAnnotations(String user_name,
			StudentAnnotationsBean studentAnnotationsBean) throws Exception {
		String LOG_METHOD_NAME = "void updateStudentAnnotations(String, StudentAnnotationsBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
	
		try{
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
			
			StringBuffer sparqlDeleteString = new StringBuffer("");
			//delete tribe information
			sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_TRIBE + ">" + " ?tribe . " + "}");
			sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_TRIBE + ">" + " ?tribe . " + "}");
			
			//delete language			
			sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LANGUAGE + ">" + " ?language . " + "}");
			sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LANGUAGE + ">" + " ?language . " + "}");
			
			//delete learning object type
			sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TYPE + ">" + " ?lotype . " + "}");
			sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TYPE + ">" + " ?lotype . " + "}");
			
			//delete text content type
			sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TEXT_TYPE + ">" + " ?textType . " + "}");
			sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TEXT_TYPE + ">" + " ?textType . " + "}");
			
			//delete image content type
			sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_IMAGE_TYPE + ">" + " ?imageType . " + "}");
			sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_STUDENT + user_name + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_IMAGE_TYPE + ">" + " ?imageType . " + "}");
			VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(sparqlDeleteString.toString(), graph);
            vur.exec();           
			
        	List<Triple> addTripleList = new ArrayList<Triple>();
 			Node predicate_value = null;			
			//tribe
			Node student = Node.createURI(TripleStoreConstants.URI_STUDENT + user_name);
			Node predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_TRIBE);
			if(!Utils.isEmptyOrNull(studentAnnotationsBean.getTribe())){
				predicate_value = Node.createLiteral(studentAnnotationsBean.getTribe());	
				addTripleList.add(new Triple(student, predicate, predicate_value));
			}
						
			//language
			student = Node.createURI(TripleStoreConstants.URI_STUDENT + user_name);
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LANGUAGE);
			if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredLanguage())){
				predicate_value = Node.createLiteral(studentAnnotationsBean.getPreferredLanguage());	
				addTripleList.add(new Triple(student, predicate, predicate_value));
			}
			
			//preferred learning object type
			student = Node.createURI(TripleStoreConstants.URI_STUDENT + user_name);
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TYPE);
			if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredLearningObjectType())){
				predicate_value = Node.createLiteral(studentAnnotationsBean.getPreferredLearningObjectType());	
				addTripleList.add(new Triple(student, predicate, predicate_value));
			}
			
			//preferred text content type
			student = Node.createURI(TripleStoreConstants.URI_STUDENT + user_name);
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_TEXT_TYPE);
			if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredTextContent())){
				predicate_value = Node.createLiteral(studentAnnotationsBean.getPreferredTextContent());	
				addTripleList.add(new Triple(student, predicate, predicate_value));
			}
			
			//preferred image content type
			student = Node.createURI(TripleStoreConstants.URI_STUDENT + user_name);
			predicate = Node.createURI(TripleStoreConstants.URI_PREDICATE_TEN_STUDENT_PREFERRED_LO_IMAGE_TYPE);
			if(!Utils.isEmptyOrNull(studentAnnotationsBean.getPreferredImageContent())){
				predicate_value = Node.createLiteral(studentAnnotationsBean.getPreferredImageContent());	
				addTripleList.add(new Triple(student, predicate, predicate_value));
			}
							
			//insert triples
			for(int i=0; i<addTripleList.size(); i++){
				graph.add(addTripleList.get(i));
			}
			
			//End transaction
			graph.getTransactionHandler().commit();
			
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
			
		}catch (Exception ex) {
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return;		
	}

    @Override
    public void deleteCourseAnnotations(int id) throws Exception {
        String LOG_METHOD_NAME = "void deleteCourseAnnotations(int id)";
        log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
    
        try{
            //STEP 1 - Connect to virtuoso database
            VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
            
            //Begin transaction
            log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
            graph.getTransactionHandler().begin();
            
            StringBuffer sparqlDeleteString = new StringBuffer("");
            //delete creator
            sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_CREATOR + ">" + " ?creator . " + "}");
            sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_CREATOR + ">" + " ?creator . " + "}");

            //delete description
            sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_DESCRIPTION + ">" + " ?description . " + "}");
            sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_DESCRIPTION + ">" + " ?description . " + "}");

            //delete keywords
            sparqlDeleteString.append("DELETE FROM GRAPH <http://localhost:8890/DAV/home/ten_user/graph> { " + "<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_KEYWORDS + ">" + " ?keywords . " + "}");
            sparqlDeleteString.append(" WHERE {<" + TripleStoreConstants.URI_COURSE + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_KEYWORDS + ">" + " ?keywords . " + "}");
            
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(sparqlDeleteString.toString(), graph);
            vur.exec();           
            
            //End transaction
            graph.getTransactionHandler().commit();
            
            log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
            
        }catch (Exception ex) {
            log.error(ex);
            throw ex;
        }finally{           
            log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
        }
        return;     
    }

    @Override
    public DigitalRightsManagementBean getDigitalRightsManagement(int learningObjectId, String learningObjectType) throws Exception {
        String LOG_METHOD_NAME = "DigitalRightsManagementBean getDigitalRightsManagement(int learningObjectId)";
        log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
        
        String learningObjectUri = getUriForLearningObjectType(learningObjectType);
        DigitalRightsManagementBean digitalRightsManagementBean = new DigitalRightsManagementBean();
        
        try {        
            StringBuffer sparqlQueryString = new StringBuffer();
            sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
            sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
            sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);
            
            sparqlQueryString.append(" SELECT ?predicate ?object ");
            sparqlQueryString.append(" WHERE { ");
            sparqlQueryString.append("<" + learningObjectUri + learningObjectId + ">");
            //sparqlQueryString.append(" a TenOntology:");
            //sparqlQueryString.append(TripleStoreConstants.TYPE_COURSE); 
            sparqlQueryString.append(" ?predicate ?object .");         
            sparqlQueryString.append(" }");
            
            log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
            
            //STEP 1 - Connect to virtuoso database
            VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
            
            //STEP 2 - Create query
            Query sparql = QueryFactory.create(sparqlQueryString.toString());
            
            VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql.toString(), graph);
    
            //STEP 3 - Execute
            ResultSet results = vqe.execSelect();
            while (results.hasNext()) {
                QuerySolution result = results.nextSolution();
                RDFNode predicateNode = result.get("predicate");
                RDFNode objectNode = result.get("object");
                log.debug(graph + " { " + predicateNode + " ; " + objectNode + "  }");

                //administrative annotations
                if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_TRIBE)) {
                    digitalRightsManagementBean.setTribe(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_PHYSICAL_DESCRIPTION)) {
                    digitalRightsManagementBean.setPhysicalDescription(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_LOAN_PERIOD)) {
                    digitalRightsManagementBean.setLoanPeriod(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER)) {
                    digitalRightsManagementBean.setIdentifier(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER_DESCRIPTION)) {
                    digitalRightsManagementBean.setIdentifierDescription(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_HANDLING_INSTRUCTIONS)) {
                    digitalRightsManagementBean.setHandlingInstructions(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_DC_RIGHTS)) {
                    digitalRightsManagementBean.setRights(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_INTAKER)) {
                    digitalRightsManagementBean.setIntaker(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_DATE_UPLOAD)) {
                    digitalRightsManagementBean.setDateOfUpload(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDED)) {
                    digitalRightsManagementBean.setStoryProvided(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_STORY)) {
                    digitalRightsManagementBean.setStory(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_STORY_CONTEXT)) {
                    digitalRightsManagementBean.setStoryContext(objectNode.toString());
                }

                //contributor annotations
                if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_CONTRIBUTOR)) {
                    getDigitalRightsManagementContributorAttributes(objectNode.toString(), digitalRightsManagementBean);
                }

                //copy right holder annotations
                if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_NOT_AVAILABLE)) {
                    digitalRightsManagementBean.setCopyRightHolderNotAvailable(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_FINDER_INFO)) {
                    digitalRightsManagementBean.setCopyRightHolderFinderInfo(objectNode.toString());
                } else if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER)){
                    getDigitalRightsManagementCopyRightHolderAttributes(objectNode.toString(), digitalRightsManagementBean);
                }

                //publisher annotations
                if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_PUBLISHER)) {
                    getDigitalRightsManagementPublisherAttributes(objectNode.toString(), digitalRightsManagementBean);
                }

                //story provider annotations
                if (predicateNode.toString().equals(TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDER)) {
                    getDigitalRightsManagementStoryProviderAttributes(objectNode.toString(), digitalRightsManagementBean);
                }
            }
        } catch (Exception ex) {
            log.error(ex);
            throw ex;
        } finally {           
            log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
        }
        
        return digitalRightsManagementBean;
    }

    private void getDigitalRightsManagementContributorAttributes(String attributesString, DigitalRightsManagementBean digitalRightsManagementBean) {
        String[] attributes = attributesString.split(";");
        for (String attributePair: attributes) {
            String[] attribute = attributePair.split("=");
            if (TripleStoreConstants.ATTRIBUTE_ID.equals(attribute[0])) {
                digitalRightsManagementBean.setContributor(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_EMAIL.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorEmail(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_APPROVED.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorApproved(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CELL_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorCellPhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorOfficePhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_FAX.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorFax(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorStreetAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorOtherAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CITY.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorCity(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STATE.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorState(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_ZIP_CODE.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorZipCode(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_TRIBAL_AFFILIATION.equals(attribute[0])) {
                digitalRightsManagementBean.setContributorTribalAffiliation(attribute[1]);
            }
        }
    }

    private void getDigitalRightsManagementCopyRightHolderAttributes(String attributesString, DigitalRightsManagementBean digitalRightsManagementBean) {
        String[] attributes = attributesString.split(";");
        for (String attributePair: attributes) {
            String[] attribute = attributePair.split("=");
            if (TripleStoreConstants.ATTRIBUTE_ID.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderId(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_EMAIL.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderEmail(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_APPROVED.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderApproved(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CELL_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderCellPhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderOfficePhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_FAX.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderFax(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderStreetAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderOtherAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CITY.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderCity(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STATE.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderState(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_ZIP_CODE.equals(attribute[0])) {
                digitalRightsManagementBean.setCopyRightHolderZipCode(attribute[1]);
            }
        }
    }

    private void getDigitalRightsManagementPublisherAttributes(String attributesString, DigitalRightsManagementBean digitalRightsManagementBean) {
        String[] attributes = attributesString.split(";");
        for (String attributePair: attributes) {
            String[] attribute = attributePair.split("=");
            if (TripleStoreConstants.ATTRIBUTE_ID.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisher(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_EMAIL.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherEmail(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_APPROVED.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherApproved(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CELL_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherCellPhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherOfficePhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_FAX.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherFax(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherStreetAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherOtherAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CITY.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherCity(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STATE.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherState(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_ZIP_CODE.equals(attribute[0])) {
                digitalRightsManagementBean.setPublisherZipCode(attribute[1]);
            }
        }
    }

    private void getDigitalRightsManagementStoryProviderAttributes(String attributesString, DigitalRightsManagementBean digitalRightsManagementBean) {
        String[] attributes = attributesString.split(";");
        for (String attributePair: attributes) {
            String[] attribute = attributePair.split("=");
            if (TripleStoreConstants.ATTRIBUTE_ID.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProvider(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_EMAIL.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderEmail(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CELL_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderCellPhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OFFICE_PHONE.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderOfficePhone(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_FAX.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderFax(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STREET_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderStreetAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_OTHER_ADDRESS.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderOtherAddress(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_CITY.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderCity(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_STATE.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderState(attribute[1]);
            } else if (TripleStoreConstants.ATTRIBUTE_ZIP_CODE.equals(attribute[0])) {
                digitalRightsManagementBean.setStoryProviderZipCode(attribute[1]);
            }
        }
    }
    
	@Override
	public void updateDigitalRightsManagement(int id, String learningObjectType, DigitalRightsManagementBean digitalRightsManagementBean) throws Exception {
		String LOG_METHOD_NAME = "void updateDigitalRightsManagement(int, DigitalRightsManagementBean)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
	
		try{
	        String learningObjectUri = getUriForLearningObjectType(learningObjectType);

	        //STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			//Begin transaction
			log.debug(TripleStoreConstants.LOG_BEGIN_TRANSACTION);
			graph.getTransactionHandler().begin();
			
			StringBuffer sparqlDeleteString = new StringBuffer("");

   		    //delete copyright holder information
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER + ">" + " ?copyrightHolder . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER + ">" + " ?copyrightHolder . " + "}");

			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_NOT_AVAILABLE + ">" + " ?copyrightHolderNotAvailable . " + "}");
            sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_NOT_AVAILABLE + ">" + " ?copyrightHolderNotAvailable . " + "}");

            sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_FINDER_INFO + ">" + " ?copyrightHolderFinderInfo . " + "}");
            sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_COPY_RIGHT_HOLDER_FINDER_INFO + ">" + " ?copyrightHolderFinderInfo . " + "}");
			
   		    //delete publisher information
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_PUBLISHER + ">" + " ?publisher . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_PUBLISHER + ">" + " ?publisher . " + "}");
			
   		    //delete contributor information
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_CONTRIBUTOR + ">" + " ?contributor . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_CONTRIBUTOR + ">" + " ?contributor . " + "}");

   		    //delete story provider information
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDER + ">" + " ?storyProvider . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDER + ">" + " ?storyProvider . " + "}");

			//delete story provided
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDED + ">" + " ?storyProvided . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDED + ">" + " ?storyProvided . " + "}");

			//delete story
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY + ">" + " ?story . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY + ">" + " ?story . " + "}");

			//delete story context
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_CONTEXT + ">" + " ?storyContext . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_CONTEXT + ">" + " ?storyContext . " + "}");

   		    //delete tribe
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_TRIBE + ">" + " ?tribe . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_TRIBE + ">" + " ?tribe . " + "}");			
			
   		    //delete physical description
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_PHYSICAL_DESCRIPTION + ">" + " ?physicalDescription . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_PHYSICAL_DESCRIPTION + ">" + " ?physicalDescription . " + "}");

   		    //delete loan period end date
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_LOAN_PERIOD + ">" + " ?loanPeriod . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_LOAN_PERIOD + ">" + " ?loanPeriod . " + "}");

   		    //delete source identifier
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER + ">" + " ?sourceIdentifier . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER + ">" + " ?sourceIdentifier . " + "}");

   		    //delete source description
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER_DESCRIPTION + ">" + " ?sourceIdentifierDescription . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_SOURCE_IDENTIFIER_DESCRIPTION + ">" + " ?sourceIdentifierDescription . " + "}");

   		    //delete handling instructions
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_HANDLING_INSTRUCTIONS + ">" + " ?handlingInstructions . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_HANDLING_INSTRUCTIONS + ">" + " ?handlingInstructions . " + "}");

   		    //delete rights
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_RIGHTS + ">" + " ?rights . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_DC_RIGHTS + ">" + " ?rights . " + "}");

   		    //delete intaker
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_INTAKER + ">" + " ?intaker . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_INTAKER + ">" + " ?intaker . " + "}");

   		    //delete date of upload
			sparqlDeleteString.append("DELETE FROM GRAPH <" + TripleStoreConstants.VIRTUOSO_GRAPH_URI + "> { " + "<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_DATE_UPLOAD + ">" + " ?uploadDate . " + "}");
			sparqlDeleteString.append(" WHERE {<" + learningObjectUri + id + "> " + "<" + TripleStoreConstants.URI_PREDICATE_TEN_DATE_UPLOAD + ">" + " ?uploadDate . " + "}");
			
			VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(sparqlDeleteString.toString(), graph);
            vur.exec();           

            //add new DRM
            List<Triple> tripleList = new ArrayList<Triple>();         
            Node learningObject = Node.createURI(learningObjectUri + id);
            tripleList.addAll(createDigitalRightsAnnotationTriples(learningObject, digitalRightsManagementBean));
            
            for(int i=0; i<tripleList.size(); i++){
                graph.add(tripleList.get(i));
            }
			
			//End transaction
			graph.getTransactionHandler().commit();
			
			log.debug(TripleStoreConstants.LOG_END_TRANSACTION);
		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		} finally {			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return;		
	}
	
	@Override
	public ArrayList<LearningObjectBean> removeItemsWithoutStory(
			ArrayList<LearningObjectBean> learningObjects, String learningObjectType) throws Exception {
		String LOG_METHOD_NAME = "ArrayList<LearningObjectBean> removeItemsWithoutStory(ArrayList<LearningObjectBean>)";
		log.debug(this.getClass() + TripleStoreConstants.LOG_BEGIN + LOG_METHOD_NAME);
		ArrayList<LearningObjectBean> returnList = new ArrayList<>();
		
		try{		
			//STEP 1 - Connect to virtuoso database
			VirtGraph graph = new VirtGraph (TripleStoreConstants.VIRTUOSO_GRAPH_URI, m_ds);
			
			for (int i=0; i<learningObjects.size(); i++){
				
				LearningObjectBean learningObject = learningObjects.get(i);
				StringBuffer sparqlQueryString = new StringBuffer();
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_ONTOLOGY);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_IMAGE);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_AUDIO);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_VIDEO);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_TEXT);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_RDF);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_DUBLIN_CORE);
				sparqlQueryString.append(TripleStoreConstants.PREFIX_TEN_STUDENT);
				
				sparqlQueryString.append(" SELECT ?object ");
				sparqlQueryString.append(" WHERE { ");
				sparqlQueryString.append("<" + learningObjectType + learningObject.getId() + ">");
				sparqlQueryString.append(" <" + TripleStoreConstants.URI_PREDICATE_TEN_STORY_PROVIDED  + "> ?object .");			
				sparqlQueryString.append(" }");
				
				log.debug("SEARCH QUERY:  " + sparqlQueryString.toString());
				
				//STEP 2 - Create query
				Query sparql = QueryFactory.create(sparqlQueryString.toString());
				
				VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, graph);
		
				//STEP 3 - Execute
				ResultSet results = vqe.execSelect();
				boolean storyProvided = false;
				while (results.hasNext()) {
					QuerySolution result = results.nextSolution();
				    RDFNode objectNode = result.get("object");
				    log.debug(graph + " { " +  objectNode + "  }");	     
				    if("true".equalsIgnoreCase(objectNode.toString())){
				    	storyProvided = true;
				    }
				}
				
				if(storyProvided){
					returnList.add(learningObject);
				}
			}
		}catch (Exception ex) {
			log.error(ex);
			throw ex;
		}finally{			
			log.debug(this.getClass() + TripleStoreConstants.LOG_END + LOG_METHOD_NAME);
		}
		return returnList;
	}
}
