package com.example.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    // RoleRequest nhận vào 1 list String -> Mà trong Role lại là 1 list Permission
    // => phải tự map, ignore permisssions
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
