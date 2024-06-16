package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.PermissionResponse;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Permission;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);

}
