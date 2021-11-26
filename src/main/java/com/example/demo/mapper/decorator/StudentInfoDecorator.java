package com.example.demo.mapper.decorator;

import com.example.demo.dto.StudentInfoDto;
import com.example.demo.entity.StudentInfo;
import com.example.demo.mapper.StudentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class StudentInfoDecorator implements StudentInfoMapper {
    @Autowired
    @Qualifier("delegate")
    private StudentInfoMapper delegate;

    @Override
    public StudentInfoDto toStudentInfoDto(StudentInfo studentInfo) {
        return delegate.toStudentInfoDto(studentInfo);
    }
}
