package com.document.backend.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;

/**This class is used for Mongo Audit
 * @author navneet
 *
 */
@Component
@EnableMongoAuditing
public class AuditConfig implements  AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		return "System";
	}
	
}