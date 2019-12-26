package com.document.backend.filters;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**Cross Origin Filter
 * @author navneet
 *
 */
@Component
@Order(1)
public class CORSFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "2592000");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, sessionId, x-token,Set-Cookie,Access-Control-Allow-Credentials,ClientId,Authorization");
		//response.setHeader("Set-Cookie", "sessionId=" +request.getSession().getId() +"; path=/; secure; HttpOnly");
		//response.setHeader("Access-Control-Expose-Headers", "sessionId, x-token");
		response.addHeader("Cache-Control", "no-cache, no-store");
       // response.setDateHeader("Expires", 0); 
        response.setDateHeader("Last-Modified", new Date().getTime());
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Allow", "GET, POST, OPTIONS");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Strict-Transport-Security", "max-age=31536000");
		filterChain.doFilter(request, response);

	}
}
