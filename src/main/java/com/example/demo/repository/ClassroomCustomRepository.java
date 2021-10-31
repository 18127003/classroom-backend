package com.example.demo.repository;

import com.example.demo.entity.Classroom;

import java.util.List;

public interface ClassroomCustomRepository {
    List<Classroom> getClasses();

    Classroom addClass();
}
