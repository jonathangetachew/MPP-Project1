package com.mpp.twitterclone.config;

import com.mpp.twitterclone.enums.TokenProvider;
import com.mpp.twitterclone.model.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 9/14/2019.
 */

@Slf4j
@Component
public class JwtTokenProvider {
	private final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;

	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + Long.parseLong(TokenProvider.TOKEN_VALIDITY_DURATION.val()));

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("username", user.getUsername());
		claims.put("roles", user.getRoles());

		return TokenProvider.TOKEN_PREFIX.val() + Jwts.builder()
				.setSubject(user.getId())
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(ALGORITHM, TokenProvider.SECRET_KEY.val())
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(TokenProvider.SECRET_KEY.val()).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT Signature: " + ex.getLocalizedMessage());
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT Token: " + ex.getLocalizedMessage());
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT Token: " + ex.getLocalizedMessage());
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT Token: " + ex.getLocalizedMessage());
		} catch (IllegalArgumentException ex) {
			log.error("JWT Claims String is Empty: " + ex.getLocalizedMessage());
		}

		return false;
	}

	/**
	 *
	 * Used to Extract Username from the given token
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(TokenProvider.SECRET_KEY.val()).parseClaimsJws(token).getBody();

		return (String) claims.get("username");
	}

	public String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(TokenProvider.TOKEN_HEADER.val());

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenProvider.TOKEN_PREFIX.val())) {
			return bearerToken.substring(TokenProvider.TOKEN_PREFIX.val().length());
		}

		return null;
	}
}
