package com.mpp.twitterclone.controllers.v1;

import com.mpp.twitterclone.config.JwtTokenProvider;
import com.mpp.twitterclone.enums.RoleName;
import com.mpp.twitterclone.exceptions.UserValidationException;
import com.mpp.twitterclone.model.Credentials;
import com.mpp.twitterclone.model.Role;
import com.mpp.twitterclone.model.User;
import com.mpp.twitterclone.services.RequestValidationService;
import com.mpp.twitterclone.services.UserService;
import com.mpp.twitterclone.validators.UserValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Jonathan on 9/14/2019.
 *
 * Used for the Authentication of Users
 */

@RestController
@RequestMapping(value = AuthController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	public static final String BASE_URL = "/api/v1";

	private final UserService userService;

	private final RequestValidationService requestValidationService;

	private final UserValidator userValidator;

	private final JwtTokenProvider tokenProvider;

	private final AuthenticationManager authenticationManager;

	public AuthController(UserService userService, RequestValidationService requestValidationService,
	                      UserValidator userValidator, JwtTokenProvider tokenProvider,
	                      AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.requestValidationService = requestValidationService;
		this.userValidator = userValidator;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@ApiOperation(value = "Log into the System")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid Credentials credentials, BindingResult bindingResult) {

		// Check Validity of Request Body
		ResponseEntity<?> validate = requestValidationService.validate(bindingResult);
		if (validate != null) return validate;

		Authentication authentication;

		try {
			// Authenticate user
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

		} catch (AuthenticationException ex) {
			throw new UserValidationException("Invalid Username/Password");
		}

		// Set the Context Holder content to the newly authenticated user for other actions to use
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Generate Token
		String token = tokenProvider.generateToken(authentication);

		// Get user data to return to client
		User user = userService.findUserByUsername(authentication.getName());

		Map<Object, Object> responseMessage = new HashMap<>();
		responseMessage.put("message", "Successfully Logged in as " + credentials.getUsername());
		responseMessage.put("token", token);
		responseMessage.put("user", user);

		return ResponseEntity
				.status(HttpStatus.ACCEPTED)
				.body(responseMessage);
	}

	@ApiOperation(value = "Create a New User")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid User user, BindingResult bindingResult) {

		// Given use the default role of USER
		user.setRoles(new HashSet<>(Arrays.asList(Role.builder().name(RoleName.USER).build())));

		// Validate Roles and Password against custom validation
		userValidator.validate(user, bindingResult);

		// Check Validity of Request Body
		ResponseEntity<?> validate = requestValidationService.validate(bindingResult);
		if (validate != null) return validate;

		// Create User
		User newUser = userService.create(user);

		// After creating user, authenticate then generate token to make the signup process similar to login
		// Authenticate User
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword()));

		// Set the Context Holder content to the newly authenticated user for other actions to use
//		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Generate token
//		String token = tokenProvider.generateToken(authentication);

		Map<Object, Object> responseMessage = new HashMap<>();
		responseMessage.put("message", "Successfully Signed Up as " + newUser.getUsername());
//		responseMessage.put("token", token);
		responseMessage.put("user", newUser);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseMessage);
	}
}
