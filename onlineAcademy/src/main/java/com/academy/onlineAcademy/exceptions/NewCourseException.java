package com.academy.onlineAcademy.exceptions;

public class NewCourseException extends Exception {
	
	/**
	 * Enum of error types thrown when a new course is created
	 * @author d.boyadzhieva
	 *
	 */
	public enum NewCourseTypeError {
		VALIDATION_FAILED, EXISTING_COURSE, DURATION_NUMERIC_VALUE, PRICE_NUMERIC_VALUE, CATEGORY_REQUIRED, DATABASE_FAIL
	}
	
	private NewCourseTypeError newCourseTypeError;
	
	/**
	 * Method that gets the type of error for the creation of a new course
	 * @return
	 */
	public NewCourseTypeError getNewCourseTypeError () {
		return newCourseTypeError;
	}
	
	/**
	 * Class constructor
	 * @param newCourseTypeError
	 */
	public NewCourseException(NewCourseTypeError newCourseTypeError) {
		this.newCourseTypeError = newCourseTypeError;
	}

}
