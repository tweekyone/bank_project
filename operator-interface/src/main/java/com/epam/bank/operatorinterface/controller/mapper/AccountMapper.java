package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.entity.Account;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CardMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {
    @Mapping(target = "userId", source = "account.user.id")
    AccountResponse map(Account account);

    List<AccountResponse> map(List<Account> accounts);
}
