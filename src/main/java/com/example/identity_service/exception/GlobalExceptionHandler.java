package com.example.identity_service.exception;

import com.example.identity_service.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
//khi có exc xảy ra, class này sẽ chịu trách nhiệm handle cái exc đó
//@ControllerAdvice can thiệp vào việc xử lý của các Controller thông thường
//@RestControllerAdvice khác ở chỗ nó can thiệp vào việc xử lý của các @RestController
public class GlobalExceptionHandler {
    //define Exception
    //Ví dụ : ở Hàm getUserByID ở UserService đang trả về 1 exception
    // .orElseThrow(() -> new RuntimeException("User not found"))
    @ExceptionHandler(value = RuntimeException.class)
    //khai báo lỗi muốn bắt
    //khi có RunTimeException xảy ra ở bất kỳ đâu trong hệ thống sẽ được tập trung ở đây để xử lý
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {
        //khi khai báo parameter exception vào method -> spring inject Exception vào para
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(1001);
        //lưu ý, mỗi ExcHandler sẽ xử lý những Exc khác nhau => code trả về sẽ khác nhau
        //-> tạo ra lớp enum chứa Error Code, định nghĩa Exception mới (AppException) và bắt phía dưới
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }//chưa được sử dụng

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();//gán errorCode = errorCode lấy từ AppException
        //nếu ở UserService ErrorCode.USER_EXISTED thì errorCode ở đây sẽ nhận 2 tham số tương ứng
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();//lấy ra message "USERNAME_INVALID" hoặc "PASSWORD_INVALID"
        //ErrorCode errorCode = ErrorCode.valueOf(enumKey); => ErrorCode.USERNAME_INVALID
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try{
            errorCode = ErrorCode.valueOf(enumKey);
        }catch (IllegalArgumentException e){
        // là ngoại lệ sẽ được ném ra nếu enumKey không tương ứng với bất kỳ giá trị nào của enum ErrorCode
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    //Nếu không muốn dùng ResponseEntity có thể dùng @ResponseStatus thay thế
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)

    //Đối với những Exception không được khai báo - dùng lớp này để bắt
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
