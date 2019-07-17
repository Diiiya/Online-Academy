package com.academy.onlineAcademy.exceptions;

public class CourseException extends Exception {

	/**
	 * Enum of error types thrown when updating a course
	 * @author d.boyadzhieva
	 *
	 */
	public enum CourseErrorType {
		EXISTING_COURSE, FIELDS_VALIDATION, DATABASE_UPDATE_FAILED
	}
	
	private CourseErrorType courseErrorType;
	
	/**
	 * Method that gets the type of course error
	 * @return
	 */
	public CourseErrorType getCourseErrorType() {
		return courseErrorType;
	}
	
	/**
	 * Class constructor
	 * @param courseErrorType
	 */
	public CourseException(CourseErrorType courseErrorType) {
		this.courseErrorType = courseErrorType;
	}
	
}
