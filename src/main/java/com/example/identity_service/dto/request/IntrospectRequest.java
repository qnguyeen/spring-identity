package com.example.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// lớp gửi token để xác thực
public class IntrospectRequest {
    String token;
}
