package com.example.demo.entity;

import com.example.demo.common.enums.AssignmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ASSIGNMENT")
public class Assignment extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private Integer points;

    @Column
    private LocalDateTime deadline;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column
    private Integer position;

    @Column
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.REMOVE)
    private List<Submission> submissions;

    public Assignment(String name, String description, Integer points, LocalDateTime deadline, Date createdAt,
                      Integer position, AssignmentStatus status) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.position = position;
        this.status = status;
    }

    public Assignment(Long id, String name, String description, Integer points, LocalDateTime deadline, Date createdAt,
                      Classroom classroom, Account creator, Integer position, AssignmentStatus status) {
        this(name, description, points, deadline, createdAt, position, status);
        this.id = id;
        this.classroom = classroom;
        this.creator = creator;
    }

    @ManyToOne(targetEntity = Classroom.class)
    @JoinColumn(name = "classroom", referencedColumnName = "id")
    private Classroom classroom;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private Account creator;
}
