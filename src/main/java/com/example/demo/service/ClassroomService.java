package com.example.demo.service;

import com.example.demo.entity.Classroom;

import java.util.List;

public interface ClassroomService {
    List<Classroom> getClassrooms();

    Classroom createClassroom(Classroom classroom);
}
