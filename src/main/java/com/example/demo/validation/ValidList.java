package com.example.demo.validation;

import com.example.demo.validation.validator.ValidListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 支持 list 中的分组校验
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValidListValidator.class})
public @interface ValidList {
    /**
     * 要验证的分组
     */
    Class<?>[] groupings() default {Default.class};

    /**
     * 是否继续往下去校验list里面的元素 true不继续，false继续
     *
     * @return
     */
    boolean quickFail() default false;


    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
