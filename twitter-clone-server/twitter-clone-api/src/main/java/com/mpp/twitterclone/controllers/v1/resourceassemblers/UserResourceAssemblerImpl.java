package com.mpp.twitterclone.controllers.v1.resourceassemblers;

import com.mpp.twitterclone.controllers.v1.UserController;
import com.mpp.twitterclone.model.User;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jonathan on 9/8/2019.
 */

@Component
public class UserResourceAssemblerImpl implements UserResourceAssembler {
	@Override
	public Resource<User> toResource(User user) {
		return new Resource<>(user,
				linkTo(methodOn(UserController.class).getUserByUsername(user.getUsername())).withSelfRel(),
				linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
	}
}
