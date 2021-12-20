package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "COMMENT")
public class Comment extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private Account author;

    @ManyToOne(targetEntity = GradeReview.class)
    @JoinColumn(name = "grade_review", referencedColumnName = "id")
    private GradeReview gradeReview;

    @Column(columnDefinition = "text")
    private String content;

    public Comment(Account author, GradeReview gradeReview, String content) {
        this.author = author;
        this.gradeReview = gradeReview;
        this.content = content;
    }

    public Comment(Long id, Account author, GradeReview gradeReview, String content) {
        this(author, gradeReview, content);
        this.id = id;
    }
}
