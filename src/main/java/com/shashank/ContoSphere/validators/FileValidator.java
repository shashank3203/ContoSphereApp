package com.shashank.ContoSphere.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    public static final long   MAX_FILE_SIZE = 1024 * 1024 * 5; // 5MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {


        // Should not be Empty
        if(file == null || file.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should not be empty").addConstraintViolation();
            return false;
        }

        // Should be less than or equal to 5MB
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should not exceed 5MB").addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
