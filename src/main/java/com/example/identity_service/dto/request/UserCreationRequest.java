package com.example.identity_service.dto.request;

import com.example.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data//tự động tạo method getter setter đồng thời thêm constructor cho class
@NoArgsConstructor//tạo constructor không có tham sô
@AllArgsConstructor//có tham số
@Builder//tạo buidler class cho DTO - giúp tạo các đối tượng một cách rõ ràng, có thể thiết lập giá trị
@FieldDefaults(level = AccessLevel.PRIVATE)//thiết lập mức truy cập cho các field
public class UserCreationRequest {
    //Đây là lớp DTO(Data Transfer Object), sử dụng để nhận dữ liệu từ yêu cầu HTTP
    //Nó chứa các thuộc tính cần thiết để tạo một đối tượng User
    //không cần ID vì ID tự khởi tạo
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    //khi dùng các annotate validation, phải khai báo @Valid trong method ở Controller
    //khai báo size và trả về thông báo nếu sai quy định
    @Size(min = 8, message = "PASSWORD_INVALID")//lấy message Exception here
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;


}
