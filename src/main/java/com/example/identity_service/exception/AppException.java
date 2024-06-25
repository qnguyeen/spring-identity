package com.example.identity_service.exception;

// đây là 1 Exception tự định nghĩa bởi Dev
public class AppException extends RuntimeException {
    private ErrorCode errorCode; // tạo obj errorCode chưa rõ truyền instance nào

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // gọi constructor của lớp cha
        // lớp cha có constructor nhận một thông điệp lỗi, truyền tham số thông tin lỗi vào super

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
