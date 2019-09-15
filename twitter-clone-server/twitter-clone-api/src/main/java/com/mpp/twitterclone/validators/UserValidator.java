package com.mpp.twitterclone.validators;


import org.springframework.validation.Validator;

/**
 * Created by Jonathan on 9/14/2019.
 *
 * Used to verify data sent from client - eg: Roles, Password length, strength, ...
 */

public interface UserValidator extends Validator {
}
