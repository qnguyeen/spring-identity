package com.example.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// Nơi chứa tất cả các field cần cho việc chuẩn hoá
@JsonInclude(JsonInclude.Include.NON_NULL) // nếu field = null thì k đưa vào JSON trả về
public class ApiResponse<T> {
    int code = 1000;
    String message;
    T result; // do thông tin trả vể có nhiều kiểu nên đế = T
}
