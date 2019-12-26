package com.document.backend.constants;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
/** This class reads configurations from config property files
 * @author navneet
 *
 */
@Getter
@Component
public class ConfigConstants {

	@Value("${file.path}") private String filePath;
	
	/*
	 * Default settings
	 */
	@Value("${default.user}") private String defaultUser;
	@Value("${default.password}") private String defaultPassword;
	@Value("${byPass.services}") private String byPassServices;
	@Value("${default.reset.password}") private String resetPassword;
	private List<String> allowedServices;
	
	@PostConstruct
	private void setAllowedServices(){
		allowedServices=new ArrayList<>();
		if(null!=byPassServices && !byPassServices.isEmpty()){
			String[] split=byPassServices.split(",");
			for(String ele:split){
				allowedServices.add(ele);
			}
		}
	}
	
}
