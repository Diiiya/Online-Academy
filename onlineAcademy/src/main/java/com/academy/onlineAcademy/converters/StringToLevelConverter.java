package com.academy.onlineAcademy.converters;

import com.academy.onlineAcademy.model.Category;
import com.academy.onlineAcademy.model.Level;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class StringToLevelConverter implements Converter<String, Level> {

	@Override
	public Result<Level> convertToModel(String value, ValueContext context) {
		try {
            return Result.ok(Level.valueOf(value));
        } catch (Exception e) {
            return Result.error("Please enter a category");
        }
	}

	@Override
	public String convertToPresentation(Level value, ValueContext context) {
		return String.valueOf(value);
	}

}
