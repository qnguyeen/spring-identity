package com.example.identity_service.repository;

import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    //JpaRepo là Interface cung cấp method CRUD
    //JPA tự động generate code
    //=> tạo 1 repo cho User, khoá chính kiểu String như file UserEntiry

    //do JPA chỉ tự tạo được những method CRUD nên các method phức tạp hơn phải tự tạo
    //tạo 1 method kiểm tra xem username đã tồn tại chưa khi tạo 1 user mới
    boolean existsByUsername(String username);//đây là sự kỳ diệu của jpa, tự khởi tạo sql query
    Optional<User> findByUsername(String username);
}

