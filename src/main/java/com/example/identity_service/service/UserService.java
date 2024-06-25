package com.example.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor // tạo constructor cho các biến được define = final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    // bỏ @Autowired đi
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // khi tạo 1 user mới, set thêm role mặc định cho user đó
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name()); // gán "USER" vào chuỗi JSON roles
        // user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // tạo endpoint cho phép lấy dữ liệu user mà không cần nhập para
    public UserResponse getMyInfo() {
        // trong spring secu, khi request được xác thực thành công, thông tin user đăng nhập sẽ được lưu trong
        // secuContextHolder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        // từ username vừa lấy được từ request đăng nhập, kiểm tra với repo
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // SprAuth sẽ tạo procedure kiểm tra có role ADMIN mới gọi được method này
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream() // stream : chuyển list thành 1 luồng các đối tượng user
                .map(userMapper::toUserResponse)
                .toList(); // map và chuyển thành danh sách
    }

    @PostAuthorize("returnObject.username == authentication.name")
    // inject sau khi method thực hiện xong - ngược lại với PreAutho
    // kiểm tra nếu đúng id của chính người đăng nhập mới truy cập vào được
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }
}
