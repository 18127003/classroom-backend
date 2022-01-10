package com.example.demo.repository.custom;

import com.example.demo.entity.Classroom;

import java.util.List;

public interface ClassroomCustomRepository {
    Classroom findByCode(String code);

    List<Classroom> findAllSort(boolean isDesc);

    List<Classroom> findAllSortSearch(boolean isDesc, String q);
}
