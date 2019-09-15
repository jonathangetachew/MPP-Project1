package com.mpp.twitterclone.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jonathan on 9/14/2019.
 *
 * Used to override the default unauthorized access not allowed message
 */

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest httpServletRequest,
	                     HttpServletResponse httpServletResponse,
	                     AuthenticationException e) throws IOException, ServletException {
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.getWriter().print(
				"{" +
					"\"status\":" + HttpStatus.UNAUTHORIZED.value() + "," +
					"\"error\":\"" + HttpStatus.UNAUTHORIZED.getReasonPhrase() + "\"," +
					"\"message\":\"Unauthorized Access is Not Allowed\"" +
					"}");
	}
}
