package com.academy.onlineAcademy.exceptions;

public class CourseException extends Exception {

	public enum CourseErrorType {
		EXISTING_COURSE, FIELDS_VALIDATION, DATABASE_UPDATE_FAILED
	}
	
	private CourseErrorType courseErrorType;
	
	public CourseErrorType getCourseErrorType() {
		return courseErrorType;
	}
	
	public CourseException(CourseErrorType courseErrorType) {
		this.courseErrorType = courseErrorType;
	}
	
}
