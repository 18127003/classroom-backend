package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "STUDENT_INFO_CLASSROOM")
public class StudentInfoClassroom extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Classroom.class)
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    @ManyToOne(targetEntity = StudentInfo.class)
    @JoinColumn(name = "student_info", referencedColumnName = "id")
    private StudentInfo studentInfo;

    @OneToMany(mappedBy = "studentInfoClassroom", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    public StudentInfoClassroom(Classroom classroom, StudentInfo studentInfo) {
        this.classroom = classroom;
        this.studentInfo = studentInfo;
    }

    public StudentInfoClassroom(Long id, Classroom classroom, StudentInfo studentInfo) {
        this(classroom, studentInfo);
        this.id = id;
    }
}
