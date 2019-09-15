package com.mpp.twitterclone.config;

import com.mpp.twitterclone.model.User;
import com.mpp.twitterclone.services.mongo.MongoUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jonathan on 9/14/2019.
 *
 * Used to handle user token authorization and authentication
 */

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;

	private final MongoUserDetailsService mongoUserDetailsService;

	public JwtTokenFilter(@Lazy JwtTokenProvider tokenProvider,@Lazy MongoUserDetailsService mongoUserDetailsService) {
		this.tokenProvider = tokenProvider;
		this.mongoUserDetailsService = mongoUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest,
	                                HttpServletResponse httpServletResponse,
	                                FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = tokenProvider.getTokenFromRequest(httpServletRequest);

			if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
				User user = (User) mongoUserDetailsService.loadUserByUsername(tokenProvider.getUsernameFromToken(token));

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception ex) {
			log.error("Could Not Set User Authentication Token in Security Context", ex);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);

	}
}
