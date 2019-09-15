package com.mpp.twitterclone.validators;

/**
 * Created by Jonathan on 9/15/2019.
 *
 * Used to validate the actions an authenticated user performs
 */

public interface UserActionValidator {
	void validateUserAction(String actionPerformingUsername, String resourceUsername);
}
