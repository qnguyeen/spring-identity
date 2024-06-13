package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor//tạo constructor cho các biến được define = final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")//lấy dữ liệu từ file yaml
    protected String SIGN_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // tìm thông tin user theo username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());
        //method matches trong interface kiểu boolean
        //nó sẽ mã hoá password từ request và so sánh với pass đã mã hoá từ db
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    private String generateToken(User user){//build JWT
        //header của JWT chứa thuật toán và kiểu
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        //data trong body gọi là claim, khởi tạo claim sau đó truyền vào Payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())//khai báo thông tin trong payload
                .issuer("http://localhost:8080")
                .issueTime(new Date())//ngày khởi tạo
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))//thời hạn
                //cấu hình chung role trong auth2
                .claim("scope", buildScope(user))//'scope' : "LIST ROLE"
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);//truyen vao header va payload

        //ký JWS với một khóa bí mật tuỳ thuộc vào thuật toán được sử dụng
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));//chữ ký này sẽ được thêm vào trong JWT
            return jwsObject.serialize();//chuyển đối tượng JWS thành JSON
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    //verify token
    public IntrospectResponse introspectResponse(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());//xác minh tính hợp lệ của chữ ký
        SignedJWT signedJWT = SignedJWT.parse(token);//lấy chữ ký đã lưu
        var verified = signedJWT.verify(verifier);// -> lấy chữ ký và xác minh
        //hàm verify trả về true nếu token k thay đổi, false nếu ngược lại

        //chữ ký rất quan trọng, hacker có chữ ký có thể varify được nên phải báo mật sign chặt chẽ(yaml)

        //kiểm tra xem token hết hạn chưa
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();//lấy thời gian hết hạn

        return  IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    //dùng trên generationToken để build scope
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);//dùng foreach vì có thể nhiều role

        return stringJoiner.toString();

    }
}
