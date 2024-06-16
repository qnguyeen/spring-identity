package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity//đánh dấu class như 1 Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id //tự khởi tạo id cho User
    @GeneratedValue(strategy = GenerationType.UUID)//UUID cho phép khởi tạo ID 1 cách ngẫu nhiên
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    List<String> roles;
    //Set sẽ unique các item bên trong nó -> != List


}
