package com.example.demo.mapper;

import com.example.demo.dto.AdminDto;
import com.example.demo.entity.Admin;
import com.example.demo.mapper.decorator.AdminDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(AdminDecorator.class)
public interface AdminMapper {
    AdminDto toAdminDto(Admin admin);
}
