package com.example.demo.mapper.decorator;

import com.example.demo.dto.StudentInfoClassroomDto;
import com.example.demo.entity.StudentInfoClassroom;
import com.example.demo.mapper.StudentInfoClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class StudentInfoClassroomDecorator implements StudentInfoClassroomMapper {
    @Autowired
    @Qualifier("delegate")
    private StudentInfoClassroomMapper delegate;

    @Override
    public StudentInfoClassroomDto toStudentInfoClassroomDto(StudentInfoClassroom studentInfo) {
        var result = delegate.toStudentInfoClassroomDto(studentInfo);
        result.setStudentId(studentInfo.getStudentInfo().getStudentId());
        result.setName(studentInfo.getStudentInfo().getName());
        var mappedAccount = studentInfo.getStudentInfo().getClassroomAccount();
        if(mappedAccount!=null){
            result.setAccountMail(mappedAccount.getEmail());
        }
        return result;
    }
}
