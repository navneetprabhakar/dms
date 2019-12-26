package com.document.backend.config;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.document.backend.constants.ConfigConstants;
import com.document.backend.data.models.Clients;
import com.document.backend.utils.CommonUtils;

import lombok.extern.log4j.Log4j;

/**
 * @author navneet
 *
 */
@Component
@Log4j
public class SystemConfig {
	
	@Autowired private CommonUtils commonUtils;
	@Autowired private ConfigConstants configConstants;
	
	/**
	 * This method is used to create a default system client user from config file
	 */
	@PostConstruct
	private void addSystemUser() {
		Clients client=commonUtils.findOne(new Query(Criteria.where("clientName").is(configConstants.getDefaultUser())), Clients.class);
		if(null==client) {
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			client=new Clients(commonUtils.getNextSequenceId("Clients", Clients.class), configConstants.getDefaultUser(), encoder.encode(configConstants.getDefaultPassword()));
			commonUtils.save(client);
		}
		log.info("Default settings loaded.");
	}
}
