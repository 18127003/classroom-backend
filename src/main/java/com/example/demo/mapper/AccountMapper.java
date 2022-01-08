package com.example.demo.mapper;

import com.example.demo.dto.AccountDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Participant;
import com.example.demo.mapper.decorator.AccountDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
@DecoratedWith(AccountDecorator.class)
public interface AccountMapper {
    AccountDto toAccountDto(Account account);
}
