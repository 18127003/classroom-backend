package com.example.demo.repository;

import com.example.demo.entity.Account;

public interface AccountCustomRepository {
    Account findByName(String name);
}
