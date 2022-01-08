package com.example.demo.service;

import com.example.demo.entity.Admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    boolean checkExist(UUID adminId);

    Admin createAdmin(Admin admin);

    void activateAdmin(Admin admin);

    Admin getByEmail(String email);

    List<Admin> getAllAdmin();
}
