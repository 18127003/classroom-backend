package com.example.demo.repository.custom;

import com.example.demo.entity.Admin;

public interface AdminCustomRepository {
    Admin findByEmail(String email);
}
