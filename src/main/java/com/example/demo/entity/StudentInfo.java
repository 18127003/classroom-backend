package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * moi student info la 1 dong cua file excel ma giao vien upload
 * moi dong bao gom mssv va ho ten sinh vien giong nhu danh sach
 * sinh vien thuc te cua lop hoc
 **/
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "STUDENT_INFO")
public class StudentInfo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    @Column
    private String name;

    @ManyToOne(targetEntity = Classroom.class)
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    @OneToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account classroomAccount;

    @OneToMany(mappedBy = "studentInfo", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    public StudentInfo(String studentId, String name, Classroom classroom) {
        this.studentId = studentId;
        this.name = name;
        this.classroom = classroom;
    }

    public StudentInfo(String studentId, String name, Account classroomAccount, Classroom classroom) {
        this.studentId = studentId;
        this.name = name;
        this.classroomAccount = classroomAccount;
        this.classroom = classroom;
    }

    public StudentInfo(Long id, String studentId, String name, Account classroomAccount, Classroom classroom) {
        this(studentId, name, classroomAccount, classroom);
        this.id = id;
    }
}
