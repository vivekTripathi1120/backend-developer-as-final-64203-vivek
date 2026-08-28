package com.project.resource_booking_system.exception;

public enum ErrorCode implements ErrorHandle{


    CODE_2001(2001,"Something went wrong..."),
    CODE_2002(2002,"No User Found..."),
    CODE_2003(2003, "Not Authorized to Access this resource!..."),
    CODE_2004(2004, "Resource Not Found..."),
    CODE_2005(2005, "Reservation Not Found..."),
    CODE_2006(2006,"User Already Exists..."),
    CODE_2007(2007,"Invalid Credentials..."),
    CODE_2008(2008,"Invalid Request..."),
    CODE_2009(2009,"Resource Not Found..."),
    CODE_2010(2010, "Start time must be before end time"), CODE_2011(2011, "Resource is not available");


    private final int errorCode;

    private final String message;


    ErrorCode(int errorCode,String message){
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}