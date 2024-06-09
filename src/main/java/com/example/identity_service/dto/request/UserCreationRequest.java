package com.example.identity_service.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserCreationRequest {
    //Đây là lớp DTO(Data Transfer Object), sử dụng để nhận dữ liệu từ yêu cầu HTTP
    //Nó chứa các thuộc tính cần thiết để tạo một đối tượng User
    //không cần ID vì ID tự khởi tạo
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    //khi dùng các annotate validation, phải khai báo @Valid trong method ở Controller
    //khai báo size và trả về thông báo nếu sai quy định
    @Size(min = 8, message = "PASSWORD_INVALID")//lấy message Exception here
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
