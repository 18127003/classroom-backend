package com.example.demo.entity;

import com.example.demo.common.enums.GradeCompositionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "GRADE_COMPOSITION")
public class GradeComposition extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Assignment.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    @Column
    @Enumerated(EnumType.STRING)
    private GradeCompositionStatus status;

    @OneToMany(mappedBy = "gradeComposition", cascade = CascadeType.REMOVE)
    private List<Submission> submissions;

    public GradeComposition(Assignment assignment, GradeCompositionStatus status) {
        this.assignment = assignment;
        this.status = status;
    }

    public GradeComposition(Long id, Assignment assignment, GradeCompositionStatus status) {
        this(assignment, status);
        this.id = id;
    }
}
