package com.employeedir.demo.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

	private String firstFieldName;
	private String secondFieldName;
	private String message;
	
	
	
	@Override
	public void initialize(FieldMatch constraintAnnotation) {
		
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
		message = constraintAnnotation.message();
		
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		boolean isValid = true;
		
		try {
			
			Object first = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
			Object second = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
			
			isValid = first == null && second == null || first != null && first.equals(second);
			
		} catch (Exception e) {
			
		}
		
		
		if (!isValid) {
			context.buildConstraintViolationWithTemplate(message)
				   .addPropertyNode(firstFieldName)
				   .addConstraintViolation()
				   .disableDefaultConstraintViolation();
		}
		
		return isValid;
	}

}
