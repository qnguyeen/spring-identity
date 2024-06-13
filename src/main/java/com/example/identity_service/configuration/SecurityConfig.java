package com.example.identity_service.configuration;

import com.example.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration//class được init khi chạy, nó sẽ run các method chứa @Bean, đưa vào appContext
@EnableWebSecurity
public class SecurityConfig {

    //các endpoint không cần bảo vệ
    private final String[] PUBLIC_ENDPOINT = {"/users",
            "/auth/token","/auth/introspect"
    };

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    //cấu hình spring security quyết định endpoint nào cần bảo vệ và không cần bảo vệ
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //httpSecurity cung cấp method để cấu hình
        httpSecurity.authorizeHttpRequests(request ->//biểu thức lambda để config request cho authorizeHttp()
                //cấu hình các link public - không cần token
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()//cho phép truy cập k cần xác thực
                        .requestMatchers(HttpMethod.GET,"/users")
                        .hasRole(Role.ADMIN.name())//cũng có thể dùng hasAuthority
                        .anyRequest().authenticated());//các endpoint khác phải cần token

        //method oauth2Res đăng ký, authentication provider
        //khi nhập token, jwt auth provider sẽ inject và bắt đầu thực hiện authentication
        httpSecurity.oauth2ResourceServer(oauth2 ->
                //cấu hình thêm cho jwt 1 cái decoder để giải mã chữ ký
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))//customize scope -> role
                //method decoder chấp nhận 1 đối tượng JwtCoder để thực hiện
        );

        //spSecurity mặc định bật crfs - thứ bảo vệ endpoint, do muôn truy cập nên phải tắt đi
        //httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);//rút ngắn lại = lambda
        return httpSecurity.build();
    }

    @Bean
    //customize converter
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        //method chuyển thông tin thành quyền hạn
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        //method chuyển JWT thành đối tượng Authentication
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        //tạo 1 seckey
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
