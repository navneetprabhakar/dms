/**
 * 
 */
package com.document.backend.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.document.backend.constants.CommonConstants;
import com.document.backend.data.models.Counters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.WriteConcern;

import freemarker.template.Template;
import lombok.extern.log4j.Log4j;

/**This is util class for the project
 * @author navneet
 *
 */
@Component
@SuppressWarnings("deprecation")
@Log4j
public class CommonUtils {
	
	@Autowired private MongoTemplate mongoTemplate;
	@Autowired private FreeMarkerConfigurer freemarkerConfiguration;
	
	public static final int COLLECTION_COUNTER=1;
	
	/**
	 * This method makes sure that the thread only moves forward if write is successful
	 */
	@PostConstruct
	public void writeConcern() {
		mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
	}
	
	/**This method extract object from JSON Object
	 * @param object
	 * @param classType
	 * @return
	 */
	public static <T> T extractObject(Object object, Class<T> classType) {
		return new ObjectMapper().convertValue(object, classType);
	}
	/**This method extract object from JSON String
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T extractObjectFromString(String json,Class<T> type){
		try {
			return new ObjectMapper().readValue(json, type);
		} catch (Exception e) {
			log.error("An Error Occurred::",e);
		}
		return null;
	}

	/**This method saves object to respective mongo collection
	 * @param entityToSave
	 */
	public <T> void save(T entityToSave) {
		if (entityToSave == null) {
			return;
		} else {
			mongoTemplate.save(entityToSave);
		}
	}
	
	/**This method deletes object from respective mongo collection
	 * @param entityToDelete
	 */
	public <T> void remove(T entityToDelete){
		if(null==entityToDelete){
			return;
		}else{
			mongoTemplate.remove(entityToDelete);
		}
	}

	/**This method saves object to mongo collection name provided
	 * @param entityToSave
	 * @param collectionName
	 */
	public <T> void save(T entityToSave, String collectionName) {
		if (entityToSave == null) {
			return;
		} else {
			mongoTemplate.save(entityToSave, collectionName);
		}
	}

	/**This method saves list of objects to respective mongo collection
	 * @param entityList
	 */
	public <T> void save(List<T> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return;
		} else {
			for (T t : entityList) {
				mongoTemplate.save(t);
			}
		}
	}
	/**This method returns single db object as per the params and entityClass
	 * @param params
	 * @param entityClass
	 * @return
	 */
	public <T> T findOne(Map<String, Object> params, Class<T> entityClass) {
		Query query = new Query();
		if (params == null || params.isEmpty()) {
			return null;
		} else {
			for (Entry<String, Object> entry : params.entrySet()) {
				query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
			}
		}
		return mongoTemplate.findOne(query, entityClass);
	}


	/**This method returns all objects of an entityclass from mongoDb collection
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(Class<T> entityClass) {
		return mongoTemplate.findAll(entityClass);
	}

	/**This method returns all objects satisfying the params criteria from entityClass collections
	 * @param params
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(Map<String, Object> params, Class<T> entityClass) {
		Query query = new Query();
		if (params == null || params.isEmpty()) {
			return null;
		} else {
			for (Entry<String, Object> entry : params.entrySet()) {
				query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
			}
		}
		return mongoTemplate.find(query, entityClass);
	}
	
	/**This method increments the seqId of counter class
	 * @param collectionName
	 * @param entityClass
	 * @param increment
	 * @return
	 */
	public <T> T findAndModify(String collectionName, Class<T> entityClass, Integer increment) {
		Query query = new Query();
		if (collectionName == null || collectionName.isEmpty()) {
			return null;
		} else {
			if (increment == null || increment == 0) {
				increment = 1;
			}
			query.addCriteria(Criteria.where("name").is(collectionName));
			Update update = new Update();
			update.inc("seqId", increment);
			return mongoTemplate.findAndModify(query, update, entityClass);
		}
	}
	/**This method gets the next sequence ID of counter class for the respective entityClass
	 * @param collectionName
	 * @param entityClass
	 * @return
	 */
	public <T> Integer getNextSequenceId(String collectionName, Class<T> entityClass) {
		if (null == collectionName || collectionName.isEmpty()) {
			return null;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("name", collectionName);
		Counters counter = findOne(params, Counters.class);
		if (counter != null) {
			counter.setSeqId(counter.getSeqId()+1);
		}else{
			counter = new Counters();
			counter.setSeqId(COLLECTION_COUNTER);
			counter.setName(collectionName);
		}
		mongoTemplate.save(counter);
		return counter.getSeqId();
	}
	
	/**This method finds single result as per Query param
	 * @param query
	 * @param entityClass
	 * @return
	 */
	public <T> T findOne(Query query,Class<T> entityClass){
		return mongoTemplate.findOne(query, entityClass);
	}
	/**This method reads the cell value from EXCEL spreadsheet
	 * @param cell
	 * @return
	 */
	public String readCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (cell.getCellType() == 1) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCellType() == 0) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				cellValue = sdf.format(cell.getDateCellValue());
			} else {
				cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
		} else if (cell.getCellType() == 3) {
			cellValue = "";
		} else if (cell.getCellType() == 4) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == 2) {
			cellValue = cell.getCellFormula();
		} else if (cell.getCellType() == 5) {
			cellValue = String.valueOf(cell.getErrorCellValue());
		}
		return cellValue;
	}
	/**This method populates the freemarker template as per the input map
	 * @param templateName
	 * @param templateInput
	 * @return
	 */
	public String templateToString(String templateName, Map<String, Object> templateInput) {
		if (templateName == null || templateName.isEmpty()) {
			return "No Template found";
		}
		Template template;
		StringWriter result = new StringWriter();
		try {
			template = freemarkerConfiguration.getConfiguration().getTemplate(templateName);
			template.process(templateInput, result);
		} catch (Exception e) {
			log.error("error occured in templateToString method::", e);
			return CommonConstants.ERROR;
		} finally {
			try {
				result.close();
			} catch (IOException e) {
				log.error("error occured in templateToString method::", e);
				return CommonConstants.ERROR;
			}
		}
		return result.toString();
	}
	/**This method is used to call HTTP POST method as per url, object, header and content type
	 * @param url
	 * @param object
	 * @param header
	 * @param contentType
	 * @return
	 */
	public String postRequestMethod(String url,Object object,Map<String,String> header,String contentType){
		try {
			HttpClient httpClient = HttpClients.createDefault();
			ResponseHandler<String> hanlder = new BasicResponseHandler();
			HttpPost postRequest = new HttpPost(url);
			StringEntity input =null;
			if(contentType.equals(CommonConstants.CONTENT_TYPE_JSON)){
				String json=new Gson().toJson(object);
				input = new StringEntity(json);
			}else{
				input = new StringEntity((String)object);
			}
			input.setContentType(contentType);
			if(null!=header){
				for(Map.Entry<String, String> element:header.entrySet()){
					postRequest.addHeader(element.getKey(),element.getValue());
				}
			}
			postRequest.setEntity(input);
			return httpClient.execute(postRequest, hanlder);
		}catch(HttpResponseException e){
			log.error("POST method call failed for :"+url,e);
			return CommonConstants.HTTP_SERVICE_FAILED;
		}catch(Exception e){
			log.error("An Error Occurred::",e);
		}
		return null;
	}
	
	/**This method validates the input against the EMAIL REGEX
	 * @param word
	 * @return
	 */
	public Boolean isEmail(String word) {
		return word.matches(CommonConstants.EMAIL_REGEX);
	}
	
	/**This method validates the input against the NAME REGEX
	 * @param word
	 * @return
	 */
	public Boolean isName(String word){
		Pattern p = Pattern.compile(CommonConstants.NAME_REGEX);
		return p.matcher(word).find();
	}
	
}
