package com.example.demo.validation;

import com.example.demo.validation.validator.MustIsExcelValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 要求上传的文件必须是Excel这种文件
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {MustIsExcelValidator.class})
public @interface MustIsExcel {
    String message() default "{上传的文件类型必须是Excel}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
