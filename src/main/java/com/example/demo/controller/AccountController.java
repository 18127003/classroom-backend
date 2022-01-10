package com.example.demo.controller;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.NotificationDto;
import com.example.demo.dto.StudentInfoDto;
import com.example.demo.dto.request.PasswordRequestDto;
import com.example.demo.dto.request.ResetPasswordRequest;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.VerifyTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AbstractServiceEndpoint.ACCOUNT_PATH)
@RequiredArgsConstructor
public class AccountController extends AbstractServiceEndpoint{
    private final AccountService accountService;
    private final VerifyTokenService verifyTokenService;
    private final NotificationService notificationService;
    private final AccountMapper accountMapper;
    private final NotificationMapper notificationMapper;

    @PostMapping("create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account account){
        var createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(accountMapper.toAccountDto(createdAccount));
    }

    @PatchMapping("activate")
    public ResponseEntity<Void> activateAccount(@RequestBody String tokenString){
        var token = verifyTokenService.getByTokenString(tokenString);
        accountService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("activate/request")
    public ResponseEntity<Void> requestActivateAccount(@RequestParam String email, HttpServletRequest request){
        try {
            String frontHost = request.getHeader("origin");
            var account = accountService.getAccountByEmail(email);
            var token = verifyTokenService.createVerifyToken(account, VerifyTokenType.ACCOUNT_ACTIVATE, 60);
            accountService.sendAccountActivateEmail(frontHost + "/#/activateAccount", account, token);
            return ResponseEntity.ok().build();
        } catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("resetPassword/request")
    public ResponseEntity<Void> requestResetPassword(@RequestParam String email, HttpServletRequest request){
        try {
            String frontHost = request.getHeader("origin");
            var account = accountService.getAccountByEmail(email);
            var token = verifyTokenService.createVerifyToken(account, VerifyTokenType.PASSWORD_RESET, 15);
            accountService.sendResetPasswordEmail(frontHost + "/#/resetPassword", account, token);
            return ResponseEntity.ok().build();
        } catch (IOException exception){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request){
        var token = verifyTokenService.verifyToken(request.getToken());
        accountService.resetPassword(token, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody Account account, @AuthenticationPrincipal UUID accountId){
        var updated = accountService.updateAccount(accountId, account);
        return ResponseEntity.ok(accountMapper.toAccountDto(updated));
    }

    @PatchMapping("change_password")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordRequestDto request, @AuthenticationPrincipal UUID accountId){
        if(accountService.changePassword(accountId, request.getOldPassword(), request.getNewPassword())){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("studentId/update")
    public ResponseEntity<Void> updateStudentId(@RequestBody StudentInfoDto studentInfo, @AuthenticationPrincipal UUID accountId){
        accountService.updateStudentId(accountId, studentInfo.getStudentId(), studentInfo.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("notification/all")
    public ResponseEntity<List<NotificationDto>> getAllNotification(@AuthenticationPrincipal UUID accountId){
        return ResponseEntity.ok(accountService.getAllNotification(accountId)
                .stream().map(notificationMapper::toNotificationDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("notification/test")
    public ResponseEntity<Void> test(@AuthenticationPrincipal UUID accountId){
        notificationService.test(accountId);
        return ResponseEntity.ok().build();
    }
}
