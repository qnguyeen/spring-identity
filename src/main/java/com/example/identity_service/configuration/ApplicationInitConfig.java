package com.example.identity_service.configuration;

import com.example.identity_service.entity.User;
import com.example.identity_service.enums.Role;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j//dùng log
public class ApplicationInitConfig {
    //khi start, hệ thống sẽ tự động tạo 1 role admin vào user

     PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            //vì app sẽ restart nhiều lần -> phải kiểm tra role admin đã tồn tại chưa
            if(userRepository.findByUsername("admin").isEmpty()){
                var roles =  new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                      //  .roles(roles)//truyen role vao
                        .build();

                userRepository.save(user);
                log.warn("default admin user has been created with default password: admin, please change id");
            }
        };
    }
}
