package com.document.backend.filters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.document.backend.constants.CommonConstants;
import com.document.backend.constants.ErrorCodes;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;
import com.document.backend.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

/**This filter performs authorization check i.e. user name and password check
 * @author navneet
 *
 */
@Component
@Order(2)
@Log4j
public class AuthFilter extends OncePerRequestFilter {
	
	@Autowired private AuthenticationService authenticationService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		ClientResponse<GenericResponse> clientResponse = new ClientResponse<>();
		try {
			String authorization = request.getHeader("Authorization");
			if (null != authorization && !authorization.isEmpty() && authorization.startsWith("Basic")) {
				String base64Credentials = authorization.substring("Basic".length()).trim();
				if (null != base64Credentials && !base64Credentials.isEmpty()) {
					String credentials = new String(Base64.getDecoder().decode(base64Credentials),Charset.forName("UTF-8"));
					final String[] values = credentials.split(":");
					if(values.length>=2){
						clientResponse=authenticationService.authenticateClient(values[0], values[1]);
						if(clientResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
							chain.doFilter(request, response);
						}else{
							response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
							response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
						}
					}else{
						log.info("Base 64 encryption not in proper format.");
						clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Base 64 encryption not in proper format.", ErrorCodes.INVALID_HEADER.getCode(), null);
						response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
						response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
					}
				} else {
					log.info("Authorization header not valid.");
					clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Authorization header not valid.", ErrorCodes.INVALID_HEADER.getCode(), null);
					response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
					response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
				}
			} else {
				log.info("Authorization header not valid.");
				clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Authorization header not valid.", ErrorCodes.INVALID_HEADER.getCode(), null);
				response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
				response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
			}
		} catch (Exception e) {
			log.error("An Error occurred::", e);
			clientResponse = new ClientResponse<>(CommonConstants.ERROR,"An Error Occurred.", ErrorCodes.GENERIC.getCode(), null);
			response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
			response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
		}

	}

}
