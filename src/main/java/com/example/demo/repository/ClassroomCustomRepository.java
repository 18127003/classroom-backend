package com.example.demo.repository;

import com.example.demo.entity.Classroom;

import java.util.List;

public interface ClassroomCustomRepository {
    Classroom findByCode(String code);
}
