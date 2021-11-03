package com.example.demo.controller;

import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AbstractServiceEndpoint.ACCOUNT_PATH)
@RequiredArgsConstructor
public class AccountController extends AbstractServiceEndpoint{
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account account){
        try{
            var createdAccount = accountService.createAccount(account);
            return ResponseEntity.ok(accountMapper.toAccountDto(createdAccount));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
