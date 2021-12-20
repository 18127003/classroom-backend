package com.example.demo.mapper.decorator;

import com.example.demo.dto.AdminDto;
import com.example.demo.entity.Admin;
import com.example.demo.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AdminDecorator implements AdminMapper {

    @Autowired
    @Qualifier("delegate")
    private AdminMapper delegate;

    @Override
    public AdminDto toAdminDto(Admin admin) {
        var result = delegate.toAdminDto(admin);
        result.setRole("ADMIN");
        return result;
    }
}
