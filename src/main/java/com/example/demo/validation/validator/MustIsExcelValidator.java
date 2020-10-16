package com.example.demo.validation.validator;

import com.example.demo.validation.MustIsExcel;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//@NotNull
public class MustIsExcelValidator implements ConstraintValidator<MustIsExcel, MultipartFile> {

    @Override
    public void initialize(MustIsExcel constraintAnnotation) {
        String message=constraintAnnotation.message();

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            String filename = value.getOriginalFilename();
            return filename.lastIndexOf(".xls") != -1 ||
                    filename.lastIndexOf(".xlsx") != -1;
        }
    }
}
