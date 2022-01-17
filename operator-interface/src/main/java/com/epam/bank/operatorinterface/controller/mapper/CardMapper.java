package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.dto.response.CardResponse;
import com.epam.bank.operatorinterface.entity.Card;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    List<CardResponse> map(List<Card> cards);

    @Mapping(target = "accountId", source = "card.account.id")
    CardResponse map(Card card);
}
