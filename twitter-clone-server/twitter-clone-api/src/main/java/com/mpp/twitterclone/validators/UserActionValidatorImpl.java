package com.mpp.twitterclone.validators;

import com.mpp.twitterclone.enums.RoleName;
import com.mpp.twitterclone.exceptions.UnauthorizedUserException;
import com.mpp.twitterclone.services.UserService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jonathan on 9/15/2019.
 */

@Service
public class UserActionValidatorImpl implements UserActionValidator {

	private final UserService userService;

	public UserActionValidatorImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void validateUserAction(String actionPerformingUsername, String resourceUsername) {
		AtomicBoolean isAdmin = new AtomicBoolean(false);

		userService.findUserByUsername(actionPerformingUsername).getRoles()
				.forEach(role -> {
					if (role.getName().equals(RoleName.ADMIN)) isAdmin.set(true);
				});

		// If the action performing user is an ADMIN then allow it
		if (isAdmin.get()) return;

		// If the resource owner's username and the action performing username are different reject
		if (!resourceUsername.equals(actionPerformingUsername))
			throw new UnauthorizedUserException("Unauthorized Action");
	}
}
