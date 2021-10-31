package com.example.demo.mapper;

import com.example.demo.dto.AccountDto;
import com.example.demo.entity.Account;
import com.example.demo.mapper.decorator.AccountDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(AccountDecorator.class)
public interface AccountMapper {
    AccountDto toAccountDto(Account account);
}
