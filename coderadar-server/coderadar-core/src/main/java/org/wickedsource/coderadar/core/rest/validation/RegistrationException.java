package org.wickedsource.coderadar.core.rest.validation;

public class RegistrationException extends UserException {

	public RegistrationException(String username) {
		super(String.format("User %s is already registered", username));
	}
}
