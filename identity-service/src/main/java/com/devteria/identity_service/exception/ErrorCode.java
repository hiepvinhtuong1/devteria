package com.devteria.identity_service.exception;

public enum ErrorCode {
    USER_EXISTS(1001,"User is already existed"),
    USER_NOT_EXISTS(1002,"User is not existed"),
    USERNAME_INVALID(1003,"Username must be at least 8 characters"),
    PASSWORD_INVALID(1004,"Password must have at least 8 characters"),
    AUTHENTICATION_FAILED(1005,"Authentication failed"),
    ;

    final private int code;
    final private  String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
