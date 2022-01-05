package com.example.demo.repository.impl;

import com.example.demo.entity.Admin;
import com.example.demo.entity.QAdmin;
import com.example.demo.repository.custom.AdminCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl extends AbstractRepositoryImpl<Admin> implements AdminCustomRepository {
    @Override
    public Admin findByEmail(String email) {
        return selectFrom(QAdmin.admin).where(QAdmin.admin.email.eq(email)).fetchOne();
    }
}
