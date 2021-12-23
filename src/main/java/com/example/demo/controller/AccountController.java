package com.example.demo.controller;

import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.StudentInfoDto;
import com.example.demo.dto.request.PasswordRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("{id}/update")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody Account account, @PathVariable Long id){
        try{
            var updated = accountService.updateAccount(id, account);
            return ResponseEntity.ok(accountMapper.toAccountDto(updated));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}/change_password")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordRequestDto request, @PathVariable Long id){
        if(accountService.changePassword(id, request.getOldPassword(), request.getNewPassword())){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/studentId/update")
    public ResponseEntity<Void> updateStudentId(@RequestBody StudentInfoDto studentInfo, @AuthenticationPrincipal Long accountId){
        try {
            accountService.updateStudentId(accountId, studentInfo.getStudentId(), studentInfo.getName());
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
