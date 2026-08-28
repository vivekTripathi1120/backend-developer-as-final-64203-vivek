package com.project.resource_booking_system.exception;

import org.springframework.util.StringUtils;

public class CustomValidationException extends IllegalArgumentException{

    private final ErrorCode errorCode;

    public CustomValidationException(ErrorCode errorCode) {
        super(getMessage(errorCode));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }

    private static String getMessage(ErrorCode errorCode){
        if(StringUtils.hasLength(errorCode.getMessage())){
            return errorCode.getMessage();
        }
        return null;
    }
}