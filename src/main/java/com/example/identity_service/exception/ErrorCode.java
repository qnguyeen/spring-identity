package com.example.identity_service.exception;

//đây là error code tự định nghĩa
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Error"),
    INVALID_KEY(1004,"Invalid Key"),//xử lý lỗi khi UserCrea message không trùng với key ở đây
    USER_EXISTED (1001, "User already existed"),
    USERNAME_INVALID (1002, "Username must have at least 3 characters"),
    PASSWORD_INVALID (1003, "Password must have at least 8 characters"),
    USER_NOT_EXISTED (1005, "User not existed"),
    UNAUTHENTICATED(1006,"Unauthenticated"),
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
