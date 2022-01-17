package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.dto.response.UserResponse;
import com.epam.bank.operatorinterface.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccountMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserResponse map(User user);
}
