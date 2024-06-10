package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor//tạo constructor cho các biến được define = final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    //bỏ @Autowired đi
    UserRepository userRepository;

    UserMapper userMapper;

    //Lưu ý : đôi khi chúng ta chỉ muốn trả về những object nhất định chứ k trả về toán bộ
    //-> tạo 1 DTO khác để nhận dữ liệu trả vể : dto.response.UserResponse
    //public User createUser(UserCreationRequest request){}
    public UserResponse createUser(UserCreationRequest request){
        //kiểm tra nếu trùng với username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        //quăng đối tượng AppExec với tham số từ Enum class ErrorCode

        //map request vào User
        User user = userMapper.toUser(request);
        //method userMapper sẽ map những field cùng tên lại với nhau, xem trong targer
        //tương đương dùng : user.setUsername(request.getUsername());

        //mã hoá pass
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);//10 : độ mạnh
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        //vì ở đây phải tìm userId trước khi map nên có phải khai báo field mapper đích và field mapper
        User user = userRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
        //nếu tìm thấy thì trả về, k thì báo lỗi
        //exc này sẽ được spring tìm và đưa vào GlobalExceptionHandler để xử lý theo lỗi tương ứng
    }
}
