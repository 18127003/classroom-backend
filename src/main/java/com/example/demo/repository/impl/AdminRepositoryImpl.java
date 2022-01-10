package com.example.demo.repository.impl;

import com.example.demo.entity.Admin;
import com.example.demo.entity.QAdmin;
import com.example.demo.repository.custom.AdminCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepositoryImpl extends AbstractRepositoryImpl<Admin> implements AdminCustomRepository {
    @Override
    public Admin findByEmail(String email) {
        return selectFrom(QAdmin.admin).where(QAdmin.admin.email.eq(email)).fetchOne();
    }

    @Override
    public List<Admin> findAllSort(boolean isDesc) {
        var res = selectFrom(QAdmin.admin);
        if (isDesc){
            return res.orderBy(QAdmin.admin.createdAt.desc()).fetch();
        }
        return res.orderBy(QAdmin.admin.createdAt.asc()).fetch();
    }

    @Override
    public List<Admin> findAllSOrtSearch(boolean isDesc, String q) {
        var res = selectFrom(QAdmin.admin)
                .where(QAdmin.admin.name.contains(q)
                        .or(QAdmin.admin.email.contains(q)));
        if (isDesc){
            return res.orderBy(QAdmin.admin.createdAt.desc()).fetch();
        }
        return res.orderBy(QAdmin.admin.createdAt.asc()).fetch();
    }
}
