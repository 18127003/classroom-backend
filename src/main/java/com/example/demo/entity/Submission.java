package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * diem so cua 1 student info trong 1 assignment
 * la 1 submission
 * => giao cua dong student info va cot assignment
 **/
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "ASSIGNMENT_SUBMISSION")
public class Submission extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer grade;

    @ManyToOne(targetEntity = StudentInfo.class)
    @JoinColumns({
        @JoinColumn(name = "student_id", referencedColumnName = "student_id"),
        @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    })
    private StudentInfo studentInfo;

    @ManyToOne(targetEntity = Assignment.class)
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    public Submission(Integer grade, StudentInfo studentInfo, Assignment assignment) {
        this.grade = grade;
        this.studentInfo = studentInfo;
        this.assignment = assignment;
    }

    public Submission(Long id, Integer grade, StudentInfo studentInfo, Assignment assignment) {
        this(grade, studentInfo, assignment);
        this.id = id;
    }
}
