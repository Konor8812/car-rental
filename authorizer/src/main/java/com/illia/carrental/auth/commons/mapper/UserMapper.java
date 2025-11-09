package com.illia.carrental.auth.commons.mapper;


import com.illia.carrental.auth.data.entity.User;
import com.illia.carrental.auth.dto.UserDTO;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterUserRequest registerUserRequest);

    UserDTO toDTO(User user);
}