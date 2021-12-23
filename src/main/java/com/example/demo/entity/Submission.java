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
@Table(name = "SUBMISSION")
public class Submission extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer grade;

    @ManyToOne(targetEntity = StudentInfoClassroom.class)
    @JoinColumns({
        @JoinColumn(name = "student_info", referencedColumnName = "student_info"),
        @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    })
    private StudentInfoClassroom studentInfoClassroom;

    @ManyToOne(targetEntity = GradeComposition.class)
    @JoinColumn(name = "grade_composition", referencedColumnName = "id")
    private GradeComposition gradeComposition;

    public Submission(Integer grade, StudentInfoClassroom studentInfo, GradeComposition gradeComposition) {
        this.grade = grade;
        this.studentInfoClassroom = studentInfo;
        this.gradeComposition = gradeComposition;
    }

    public Submission(Long id, Integer grade, StudentInfoClassroom studentInfo, GradeComposition gradeComposition) {
        this(grade, studentInfo, gradeComposition);
        this.id = id;
    }
}
