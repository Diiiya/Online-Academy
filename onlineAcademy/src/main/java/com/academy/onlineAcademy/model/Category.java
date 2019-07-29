package com.academy.onlineAcademy.model;

/**
 * Set of category values determining the theme of a course.
 * 
 * @author d.boyadzhieva
 *
 */
public enum Category {

	BUSINESS, ARTS, HEALTH, HISTORY, IT, LANGUAGES, SCIENCE;

	@Override
	public String toString() {
		switch (this) {
		case BUSINESS:
			return "Business";
		case ARTS:
			return "Arts";
		case HEALTH:
			return "Health";
		case HISTORY:
			return "History";
		case IT:
			return "IT";
		case LANGUAGES:
			return "Languages";
		case SCIENCE:
			return "Science";
		}
		return "";
	}

}
