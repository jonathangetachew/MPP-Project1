package com.mpp.twitterclone.controllers.v1.resourceassemblers;

import com.mpp.twitterclone.controllers.v1.UserController;
import com.mpp.twitterclone.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Resource;

import static org.junit.jupiter.api.Assertions.*;

class UserResourceAssemblerTest {

	public static final String ID = "user1";
	public static final String USERNAME = "john";

	UserResourceAssembler userResourceAssembler;

	@BeforeEach
	void setUp() {

		userResourceAssembler = new UserResourceAssemblerImpl();
	}

	@Test
	void userToUserResource_ValidateUser_ResourceAssembled() {
		//given
		User user = User.builder().id(ID).username(USERNAME).build();

		//when
		Resource<User> userResource = userResourceAssembler.toResource(user);

		//then
		assertEquals(ID, userResource.getContent().getId());
		assertEquals(USERNAME, userResource.getContent().getUsername());
		assertEquals(UserController.BASE_URL + "/" + USERNAME, userResource.getLink("self").getHref());
		assertEquals(UserController.BASE_URL, userResource.getLink("users").getHref());
	}
}