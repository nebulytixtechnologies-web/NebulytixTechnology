package com.neb.exception;

/**
 * ---------------------------------------------------------------
 * File Name   : CustomeException.java
 * Package     : com.neb.exception
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to handle custom application-specific
 *   exceptions in a clean and uniform way.
 *
 * Description :
 *   - It extends RuntimeException, allowing unchecked exceptions
 *     to be thrown during runtime.
 *   - It is mainly used to throw meaningful error messages from
 *     the service or controller layer to the frontend.
 *
 * Constructor :
 *   CustomeException(String message)
 *       → Accepts a custom error message to describe the issue.
 *
 * Result :
 *   This exception class helps in improving error handling by
 *   providing descriptive and consistent messages for runtime errors.
 * ---------------------------------------------------------------
 */

@SuppressWarnings("serial")
public class CustomeException extends RuntimeException {
	public CustomeException(String message) {
		super(message);
	}
}
