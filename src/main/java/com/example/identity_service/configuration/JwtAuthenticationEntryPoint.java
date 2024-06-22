package com.example.identity_service.configuration;

import com.example.identity_service.dto.request.ApiResponse;
import com.example.identity_service.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

//sẽ được gọi để xử lý các lỗi xác thực khi truy cập tài nguyên đang được bảo vệ mà k có JWT hợp lệ
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //ctrl + i để implement các method
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//kiểu nội dung res là JSON

        ApiResponse<?> apiResponse = ApiResponse.builder()//tạo phản hồi JSON
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMappermapper = new ObjectMapper();
        //write trả về 1 string, nhưng apiResponse đang ở dạng Object -> dùng objectMapper
        response.getWriter().write(objectMappermapper.writeValueAsString(apiResponse));
        response.flushBuffer();//dữ liệu hiện tại trong bộ đệm được gửi tới client ngay lập tức

    }
}
