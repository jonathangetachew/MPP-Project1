package com.mpp.twitterclone.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpp.twitterclone.config.AuthEntryPoint;
import com.mpp.twitterclone.config.JwtTokenProvider;
import com.mpp.twitterclone.controllers.v1.resourceassemblers.UserResourceAssembler;
import com.mpp.twitterclone.controllers.v1.resourceassemblers.UserResourceAssemblerImpl;
import com.mpp.twitterclone.enums.RoleName;
import com.mpp.twitterclone.model.Credentials;
import com.mpp.twitterclone.model.Role;
import com.mpp.twitterclone.model.User;
import com.mpp.twitterclone.services.RequestValidationService;
import com.mpp.twitterclone.services.UserService;
import com.mpp.twitterclone.validators.UserValidator;
import com.mpp.twitterclone.validators.UserValidatorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@WithMockUser(value = UserControllerTest.USERNAME, authorities = "USER")
class AuthControllerTest {

	public static final String USERNAME = "john";
	public static final String PASSWORD = "john";

	@TestConfiguration
	static class AuthControllerTestContextConfiguration {
		@Bean
		public JwtTokenProvider jwtTokenProvider() {
			return new JwtTokenProvider();
		}

		@Bean
		public AuthEntryPoint authEntryPoint() {
			return new AuthEntryPoint();
		}

		@Bean
		public RequestValidationService requestValidationService() {
			return new RequestValidationService();
		}

		@Bean
		public UserValidator userValidator() {
			return new UserValidatorImpl();
		}
	}

	@MockBean
	UserService userService;

	@Autowired
	RequestValidationService requestValidationService;

	@Autowired
	UserValidator userValidator;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	ObjectMapper objectToJsonMapper;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
					.webAppContextSetup(webApplicationContext)
					.apply(springSecurity())
					.build();
	}

	@AfterEach
	void tearDown() {
		reset(userService);
	}

	@Test
	void login_InvalidCredentials_FailedLogin() throws Exception {
		//given
		Credentials credentials = Credentials.builder().username(USERNAME).password(PASSWORD).build();

		when(userService.findUserByUsername(anyString())).thenReturn(User.builder().username(USERNAME).build());

		//when
		mockMvc.perform(post(AuthController.BASE_URL + "/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectToJsonMapper.writeValueAsBytes(credentials)))
				//then
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				// Test JSON
				.andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
				.andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())));
	}

	@Test
	void signup_ValidUser_SignedUp() throws Exception {
		//given
		User user = User.builder().username(USERNAME).password(PASSWORD)
						.roles(new HashSet<>(Arrays.asList(Role.builder().name(RoleName.USER).build()))).build();

		when(userService.create(any(User.class))).thenReturn(user);

		//when
		mockMvc.perform(post(AuthController.BASE_URL + "/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectToJsonMapper.writeValueAsBytes(user)))
				.andDo(print())
				//then
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				// Test JSON
				.andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
				.andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())));
	}
}