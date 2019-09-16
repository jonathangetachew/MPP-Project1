package com.mpp.twitterclone.config;

import com.mpp.twitterclone.enums.PasswordEncoderStrength;
import com.mpp.twitterclone.enums.RoleName;
import com.mpp.twitterclone.services.mongo.MongoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by Jonathan on 9/14/2019.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthEntryPoint authEntryPoint;

	private final MongoUserDetailsService userDetailService;

	private final JwtTokenFilter tokenFilter;

	public WebSecurityConfig(@Lazy AuthEntryPoint authEntryPoint,
	                         @Lazy MongoUserDetailsService userDetailService,
	                         @Lazy JwtTokenFilter tokenFilter) {
		this.authEntryPoint = authEntryPoint;
		this.userDetailService = userDetailService;
		this.tokenFilter = tokenFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable().cors() // Disable csrf but enable cors
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(
						"/api/v1/users/{username}",
						"/api/v1/tweets/user/{username}",
						"/api/v1/login",
						"/api/v1/signup").permitAll()
//				.antMatchers(
//						"/api/v1/tweets",
//						"api/v1/users").hasAuthority(RoleName.ADMIN.toString())
				.antMatchers(
						"/api/v1/tweets/**",
						"/api/v1/users/**").hasAnyAuthority(RoleName.USER.toString(), RoleName.ADMIN.toString())
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(authEntryPoint);

		// Apply the token filter to process received tokens
		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow Swagger API access without authentication
		web.ignoring().antMatchers("/v2/api-docs",
									"/configuration/ui",
									"/swagger-resources/**",
									"/configuration/security",
									"/swagger-ui.html",
									"/webjars/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(encoder());
	}

	/**
	 * Define a Bean for the Authentication Manager
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 *
	 * Configure Cors
	 *
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("*"); // Allow all Http Request Methods
		configuration.addAllowedHeader("*");
		configuration.setMaxAge(3600L);

		// Enable cors on current path
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 *
	 * Configure Password Encoder
	 */
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(PasswordEncoderStrength.MEDIUM.val());
	}

}
