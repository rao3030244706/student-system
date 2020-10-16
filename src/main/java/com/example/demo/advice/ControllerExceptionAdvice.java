package com.example.demo.advice;


import com.example.demo.exception.ListValidException;
import com.example.demo.exception.ManageException;
import com.example.demo.pojo.CommonResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {


    @ExceptionHandler(ManageException.class)
    public CommonResponseVO tesseractExceptionExceptionHandler(ManageException e) {
        e.printStackTrace();
        log.error(e.getMsg());
        return CommonResponseVO.fail(e.getStatus(), e.getMsg(), null);
    }

    /**
     * 系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public CommonResponseVO commonExceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponseVO.FAIL;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResponseVO constraintViolationExceptionHandler(
            ConstraintViolationException e) {
        Map<Path, String> collect = e.getConstraintViolations().stream().
                collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage));
        return CommonResponseVO.fail(collect);
    }

    @ExceptionHandler
    public CommonResponseVO exceptionHandler(ValidationException e) {
        Map<Integer, Map<Path, String>> map = new HashMap<>();

        ((ListValidException) e.getCause()).getErrors().forEach((integer, constraintViolations) -> {
            map.put(integer, constraintViolations.stream()
                    .collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage)));
        });
        return CommonResponseVO.fail(map);
    }


    /**
     * 拦截的是@RequestBody传递的参数
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponseVO methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().
                getFieldErrors().
                stream().
                collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return CommonResponseVO.fail(collect);
    }

    /**
     * 拦截的地址栏的get，put
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public CommonResponseVO bindExceptionHandler(BindException e) {
        Map<String, String> collect = e.getBindingResult().
                getFieldErrors().
                stream().
                collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return CommonResponseVO.fail(collect);
    }


}
