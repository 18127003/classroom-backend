package com.example.demo.repository.custom;

import com.example.demo.entity.Admin;

import java.util.List;

public interface AdminCustomRepository {
    Admin findByEmail(String email);

    List<Admin> findAllSort(boolean isDesc);

    List<Admin> findAllSOrtSearch(boolean isDesc, String q);
}
