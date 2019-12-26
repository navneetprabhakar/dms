package com.document.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**This class is used to config Mongo DB for GridFsHandling
 * @author navneet
 *
 */
@Configuration
public class MongoConfig {
	
	@Autowired MongoDbFactory mongoDbFactory;
	@Autowired MongoConverter mongoConverter;
	
	/**This method creates gridfsTemplateBean
	 * @return
	 */
	@Bean
	public GridFsTemplate gridfsTemplate(){
		return new GridFsTemplate(mongoDbFactory, mongoConverter);
	}


}
