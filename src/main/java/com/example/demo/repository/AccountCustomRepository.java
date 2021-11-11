package com.example.demo.repository;

import com.example.demo.entity.Account;

public interface AccountCustomRepository {
    // deprecate
    Account findByName(String name);

    Account findByEmail(String email);
}
