package com.example.demo.controller;

import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.AdminDto;
import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Admin;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AbstractServiceEndpoint.ADMIN_PATH)
@RequiredArgsConstructor
public class AdminController extends AbstractServiceEndpoint{
    private final AdminService adminService;
    private final AccountService accountService;
    private final AdminMapper adminMapper;
    private final AccountMapper accountMapper;

    @PostMapping("create")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody Admin admin){
        var created = adminService.createAdmin(admin);
        return ResponseEntity.ok(adminMapper.toAdminDto(created));
    }

    @PatchMapping("activate")
    public ResponseEntity<Void> activateAdmin(@RequestParam String email){
        var admin = adminService.getByEmail(email);
        adminService.activateAdmin(admin);
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    public ResponseEntity<List<AdminDto>> getAllAdmin(){
        return ResponseEntity.ok(adminService.getAllAdmin().stream().map(adminMapper::toAdminDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("account/all")
    public ResponseEntity<List<AccountDto>> getAllAccount(){
        return ResponseEntity.ok(accountService.getAllAccount().stream().map(accountMapper::toAccountDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("account/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id){
        var account = accountService.getAccountById(id);
        return ResponseEntity.ok(accountMapper.toAccountDto(account));
    }

    @PostMapping("account/{id}/lock")
    public ResponseEntity<Void> lockAccount(@PathVariable Long id){
        accountService.lockAccount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("account/{id}/unlock")
    public ResponseEntity<Void> unlockAccount(@PathVariable Long id){
        accountService.unlockAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("account/{id}/studentId/map")
    public ResponseEntity<Void> mapStudentIdToAccount(@PathVariable Long id, @RequestParam String studentId){
        return ResponseEntity.ok().build();
    }

    @PutMapping("account/{id}/studentId/remove")
    public ResponseEntity<Void> removeAccountStudentId(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @GetMapping("classroom/all")
    public ResponseEntity<List<ClassroomDto>> getAllClassroom(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("classroom/{id}")
    public ResponseEntity<ClassroomDto> getClassroom(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
