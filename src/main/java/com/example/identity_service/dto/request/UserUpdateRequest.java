package com.example.identity_service.dto.request;

import java.time.LocalDate;

public class UserUpdateRequest {
    //Đây là lớp DTO(Data Transfer Object), sử dụng để nhận dữ liệu từ yêu cầu HTTP
    //Nó chứa các thuộc tính cần thiết để tạo một đối tượng User
    //không cần ID vì ID tự khởi tạo
    //xoá username vì không dc thay đổi
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

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
