package com.zemiak.movies.admin.jsf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author vasko
 */
public class BeanValidator<T> {
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();
    
    public List<String> validate(final T bean) {
        Set<ConstraintViolation<T>> errors = validator.validate(bean);
        if (null == errors || errors.isEmpty()) {
            return null;
        }
        
        List<String> ret = new ArrayList<>();
        for (ConstraintViolation<T> violation: errors) {
            ret.add(violation.getMessage());
        }
        
        return ret;
    }
}
