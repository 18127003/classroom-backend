package com.example.demo.mapper.decorator;

import com.example.demo.dto.AccountDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Participant;
import com.example.demo.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AccountDecorator implements AccountMapper {
    @Autowired
    @Qualifier("delegate")
    private AccountMapper delegate;

    @Override
    public AccountDto toAccountDto(Account account) {
        return delegate.toAccountDto(account);
    }

    @Override
    public AccountDto toParticipantDto(Participant participant) {
        var user = delegate.toAccountDto(participant.getAccount());
        user.setRole(participant.getRole().name());
        user.setHidden(participant.getHidden());
        user.setStudentId(participant.getStudentId());
        return user;
    }
}
