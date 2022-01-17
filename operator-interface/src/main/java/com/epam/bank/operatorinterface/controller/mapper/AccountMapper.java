package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {
    @Mapping(target = "userId", source = "account.user.id")
    AccountResponse map(Account account);
}
