package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private Date deadline;

    @Column(name = "created_at")
    private Date createdAt;

    public Assignment(String name, String description, Integer points, Date deadline, Date createdAt) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public Assignment(Long id, String name, String description, Integer points, Date deadline, Date createdAt,
                      Classroom classroom, Account creator) {
        this(name, description, points, deadline, createdAt);
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
