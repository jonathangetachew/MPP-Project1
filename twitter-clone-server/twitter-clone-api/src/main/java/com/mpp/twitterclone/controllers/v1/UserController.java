package com.mpp.twitterclone.controllers.v1;

import com.mpp.twitterclone.controllers.v1.resourceassemblers.UserResourceAssembler;
import com.mpp.twitterclone.model.User;
import com.mpp.twitterclone.services.UserService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jonathan on 9/8/2019.
 */

@RestController
@RequestMapping(value = UserController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE,
				consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	public static final String BASE_URL = "/api/v1/users";

	private final UserService userService;

	private final UserResourceAssembler userResourceAssembler;

	public UserController(UserService userService, UserResourceAssembler userResourceAssembler) {
		this.userService = userService;
		this.userResourceAssembler = userResourceAssembler;
	}

	///> Get Mappings
	@GetMapping
	public Resources<Resource<User>> getAllUsers() {
		List<Resource<User>> users = userService.findAll().stream()
				.map(userResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(users,
				linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}

	@GetMapping("/{username}")
	public Resource<User> getUserByUsername(@PathVariable String username) {
		return userResourceAssembler.toResource(userService.findUserByUsername(username));
	}

	@GetMapping("/{id}/followers")
	public Resources<Resource<User>> getAllFollowersById(@PathVariable String id) {
		List<Resource<User>> users = userService.findAllFollowers(id).stream()
				.map(userResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(users,
				linkTo(methodOn(UserController.class).getAllFollowersById(id)).withSelfRel());
	}

	@GetMapping("/{id}/following")
	public Resources<Resource<User>> getAllFollowingById(@PathVariable String id) {
		List<Resource<User>> users = userService.findAllFollowing(id).stream()
				.map(userResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(users,
				linkTo(methodOn(UserController.class).getAllFollowingById(id)).withSelfRel());
	}

	///> Post Mappings
	@PostMapping("/create")
	public ResponseEntity<Resource<User>> createUser(@RequestBody User user) throws URISyntaxException {
		Resource<User> userResource = userResourceAssembler.toResource(userService.create(user));

		return ResponseEntity
				.created(new URI(userResource.getId().expand().getHref()))
				.body(userResource);
	}

	@PostMapping("/{id}/follow")
	public ResponseEntity<Resource<User>> followUser(@PathVariable String id) throws URISyntaxException {
		// Todo: change user id to id from principal object
		Resource<User> userResource = userResourceAssembler.toResource(userService.followUser(id, "test"));

		return ResponseEntity
				.created(new URI(userResource.getId().expand().getHref()))
				.body(userResource);
	}

	///> Put Mappings
	@PutMapping("/{id}/update")
	public ResponseEntity<Resource<User>> updateUser(@RequestBody User user,
	                                                 @PathVariable String id) throws URISyntaxException {
		Resource<User> userResource = userResourceAssembler.toResource(userService.update(user, id));

		return ResponseEntity
				.created(new URI(userResource.getId().expand().getHref()))
				.body(userResource);
	}

	///> Delete Mappings
	@DeleteMapping("/{id}/remove")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		userService.deleteById(id);

		Map<String, String> responseMessage = new HashMap<>();
		responseMessage.put("message", "User Removed Successfully");

		return ResponseEntity
				.accepted()
				.contentType(MediaType.APPLICATION_JSON)
				.body(responseMessage);
	}
}
