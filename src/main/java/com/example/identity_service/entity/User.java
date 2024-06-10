package com.example.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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

}
