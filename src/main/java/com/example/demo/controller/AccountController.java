package com.example.demo.controller;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.StudentInfoDto;
import com.example.demo.dto.request.PasswordRequestDto;
import com.example.demo.dto.request.ResetPasswordRequest;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(AbstractServiceEndpoint.ACCOUNT_PATH)
@RequiredArgsConstructor
public class AccountController extends AbstractServiceEndpoint{
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account account){
        account.setStatus(AccountStatus.CREATED);
        var createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(accountMapper.toAccountDto(createdAccount));
    }

    @PatchMapping("activate")
    public ResponseEntity<Void> activateAccount(@RequestBody String token){
        accountService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("activate/request")
    public ResponseEntity<Void> requestActivateAccount(@RequestParam String email){
        try {
            var account = accountService.getAccountByEmail(email);
            accountService.sendAccountActivateEmail("http://localhost:8085/#/activateAcocunt", account);
            return ResponseEntity.ok().build();
        } catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("resetPassword/request")
    public ResponseEntity<Void> requestResetPassword(@RequestParam String email){
        System.out.println(email);
        try {
            var account = accountService.getAccountByEmail(email);
            accountService.sendResetPasswordEmail("http://localhost:8085/#/resetPassword", account);
            return ResponseEntity.ok().build();
        } catch (IOException exception){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request){
        accountService.resetPassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody Account account, @AuthenticationPrincipal UUID accountId){
        var updated = accountService.updateAccount(accountId, account);
        return ResponseEntity.ok(accountMapper.toAccountDto(updated));
    }

    @PatchMapping("/change_password")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordRequestDto request, @AuthenticationPrincipal UUID accountId){
        if(accountService.changePassword(accountId, request.getOldPassword(), request.getNewPassword())){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/studentId/update")
    public ResponseEntity<Void> updateStudentId(@RequestBody StudentInfoDto studentInfo, @AuthenticationPrincipal UUID accountId){
        accountService.updateStudentId(accountId, studentInfo.getStudentId(), studentInfo.getName());
        return ResponseEntity.ok().build();
    }
}
