package com.example.demo.myValidated.myselfValied;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

//@NotNull
public class MustIsExcelAnnoHandler implements ConstraintValidator<MustIsExcel, MultipartFile> {

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
