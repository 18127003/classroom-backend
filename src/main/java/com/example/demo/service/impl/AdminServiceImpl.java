package com.example.demo.service.impl;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import com.example.demo.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordUtil passwordUtil;

    @Override
    public boolean checkExist(UUID adminId) {
        return adminRepository.existsById(adminId);
    }

    @Override
    public Admin createAdmin(Admin admin) {
        var existedAccount = adminRepository.findByEmail(admin.getEmail());
        if(existedAccount!=null){
            throw new RTException(new DuplicateRecordException(existedAccount.getId().toString(), Admin.class.getSimpleName()));
        }
        admin.setPassword(passwordUtil.encodePassword(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public void activateAdmin(Admin admin) {
        admin.setStatus(AccountStatus.ACTIVATED);
        adminRepository.save(admin);
    }

    @Override
    public Admin getByEmail(String email) {
        var admin = adminRepository.findByEmail(email);
        if (admin==null){
            throw new RTException(new RecordNotFoundException(email, Admin.class.getSimpleName()));
        }
        return admin;
    }

    @Override
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
}
