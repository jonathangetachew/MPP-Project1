package com.mpp.twitterclone.validators;

import com.mpp.twitterclone.enums.RoleName;
import com.mpp.twitterclone.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Created by Jonathan on 9/14/2019.
 */

@Component
public class UserValidatorImpl implements UserValidator {
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {

		User user = (User) o;

		if (user.getRoles() == null) {
			errors.rejectValue("roles", "Not Found", "Roles are Required");
		} else {
			user.getRoles().forEach(role -> {
				String currentRole = role.getName().toString();
				if (currentRole.equals("") ||
						!(currentRole.equals(RoleName.USER.toString()) || currentRole.equals(RoleName.ADMIN.toString())))
					errors.rejectValue("roles", "Found", "Invalid Roles");
			});
		}

	}
}
