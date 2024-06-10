package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")//gán component để autowired
//chỉ định đây là 1 mapper
//MapStruct sẽ tạo ra một implementation của interface này vào thời điểm biên dịch
//Các phương thức trong interface này sẽ ánh xạ các thuộc tính từ một đối tượng này sang một đối tượng khác
public interface UserMapper {
    User toUser(UserCreationRequest request);
    //khi gọi userMapper.toUser(requet) ở Service, nó sẽ tự động map các giá trị UserCrea vào User

    @Mapping(source = "",target = "")//tự chỉ định field muốn map
    UserResponse toUserResponse(User user);
    //map giá trị từ User -> UserResponse

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    //@MappingTarget chỉ định field đươc map và field map
    //vì ở đây phải tìm userId trước khi map nên có phải khai báo field mapper đích và field mapper

}
