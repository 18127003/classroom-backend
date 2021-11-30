package com.example.demo.entity;

import com.example.demo.common.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "ACCOUNT_CLASSROOM")
public class Participant extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    @Column
    private Boolean hidden;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "classroomAccount")
    private StudentInfo studentInfo;

    public Participant(Account account, Classroom classroom, Role role, Boolean hidden){
        this.account = account;
        this.classroom = classroom;
        this.hidden = hidden;
        this.role = role;
    }

    public Participant(Account account, Classroom classroom, Role role, Boolean hidden, String studentId,
                       StudentInfo studentInfo){
        this.account = account;
        this.classroom = classroom;
        this.hidden = hidden;
        this.role = role;
        this.studentId = studentId;
        this.studentInfo = studentInfo;
    }

    public Participant(Long id, Account account, Classroom classroom, Role role, Boolean hidden, String studentId,
                       StudentInfo studentInfo){
        this(account, classroom, role, hidden, studentId, studentInfo);
        this.id = id;
    }

}
