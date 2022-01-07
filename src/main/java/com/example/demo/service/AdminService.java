package com.example.demo.service;

import com.example.demo.entity.Admin;

import java.util.List;

public interface AdminService {
    boolean checkExist(Long accountId);

    Admin createAdmin(Admin admin);

    void activateAdmin(Admin admin);

    Admin getByEmail(String email);

    List<Admin> getAllAdmin();
}
