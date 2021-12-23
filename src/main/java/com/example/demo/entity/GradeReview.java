package com.example.demo.entity;

import com.example.demo.common.enums.GradeReviewStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "GRADE_REVIEW")
public class GradeReview extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "request_by", referencedColumnName = "id")
    private Account requestBy;

    @OneToOne(targetEntity = Submission.class)
    @JoinColumn(name = "submission", referencedColumnName = "id")
    private Submission submission;

    @Column(name = "expect_grade")
    private Integer expectGrade;

    @Column(columnDefinition = "text")
    private String explanation;

    @Column
    @Enumerated(EnumType.STRING)
    private GradeReviewStatus status;

    public GradeReview(Account requestBy, Submission submission, Integer expectGrade, String explanation,
                       GradeReviewStatus status) {
        this.requestBy = requestBy;
        this.submission = submission;
        this.expectGrade = expectGrade;
        this.explanation = explanation;
        this.status = status;
    }

    public GradeReview(Long id, Account requestBy, Submission submission, Integer expectGrade,
                       String explanation, GradeReviewStatus status) {
        this(requestBy, submission, expectGrade, explanation, status);
        this.id = id;
    }
}
