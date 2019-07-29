package com.academy.onlineAcademy.model;

/**
 * Set of levels determining the difficulty of a course.
 * 
 * @author d.boyadzhieva
 *
 */
public enum Level {

	BEGINNER, INTERMEDIATE, ADVANCED;

	@Override
	public String toString() {
		switch (this) {
		case BEGINNER:
			return "Beginner";
		case INTERMEDIATE:
			return "Intermediate";
		case ADVANCED:
			return "Advanced";
		}
		return "";
	}

}
