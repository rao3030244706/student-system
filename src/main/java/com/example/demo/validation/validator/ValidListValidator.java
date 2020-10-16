package com.example.demo.validation.validator;


import com.example.demo.exception.ListValidException;
import com.example.demo.util.ValidatorUtils;
import com.example.demo.validation.ValidList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 负责校验逻辑的类，一般都是多例的
 */
public class ValidListValidator implements ConstraintValidator<ValidList, List> {

    //分组
    Class<?>[] groupings;
    //如果出现了第一个校验失败的，是否还往下去校验list下面面的元素，true代表不往下继续校验，false代表继续往下校验
    boolean quickFail;

    @Override
    public void initialize(ValidList validList) {
        groupings = validList.groupings();
        quickFail = validList.quickFail();
    }

    @Override
    public boolean isValid(List list, ConstraintValidatorContext context) {
        Map<Integer, Set<ConstraintViolation<Object>>> errors = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            Set<ConstraintViolation<Object>> error = ValidatorUtils.validator.validate(object, groupings);
            if (error.size() > 0) {
                errors.put(i, error);
                if (quickFail) {
                    //这里抛出的ListValidException会包装成ValidationException
                    throw new ListValidException(errors);
                }
            }
        }

        if (errors.size() > 0) {
            throw new ListValidException(errors);
        }

        return true;
    }
}
