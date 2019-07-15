package com.academy.onlineAcademy.converters;

import com.academy.onlineAcademy.model.Category;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class StringToCategoryConverter implements Converter<String, Category>{

	@Override
	public Result<Category> convertToModel(String value, ValueContext context) {
        try {
            return Result.ok(Category.valueOf(value));
        } catch (Exception e) {
            return Result.error("Please enter a category");
        }
	}

	@Override
	public String convertToPresentation(Category value, ValueContext context) {
		return String.valueOf(value);
	}

}
