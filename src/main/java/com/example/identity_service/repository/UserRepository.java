package com.example.identity_service.repository;

import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    //JpaRepo là Interface cung cấp method CRUD
    //JPA tự động generate code
    //=> tạo 1 repo cho User, khoá chính kiểu String như file UserEntiry
}
