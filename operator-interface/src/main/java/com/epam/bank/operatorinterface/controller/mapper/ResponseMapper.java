package com.epam.bank.operatorinterface.controller.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper extends AccountMapper, CardMapper {
}
