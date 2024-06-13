package com.example.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//lóp tạo ra để lưu giá trị trả về của User
public class UserResponse {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<String> roles;
}
