package com.devteria.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1000, "Invalid message key!", HttpStatus.BAD_REQUEST),
    USER_EXISTS(1001,"User is already existed", HttpStatus.BAD_REQUEST ),
    USER_NOT_EXISTS(1002,"User is not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003,"Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Password must have at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005,"Authentication failed", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006,"You do not have permisson", HttpStatus.FORBIDDEN),
    INVALID_DOB(1007,"Your age must be {min}", HttpStatus.BAD_REQUEST),
    ;

    final private int code;
    final private  String message;
    final private HttpStatus status;
    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
