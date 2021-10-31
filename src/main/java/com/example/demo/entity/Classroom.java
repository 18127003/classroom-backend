package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CLASSROOM")
public class Classroom extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column
    private String name;

    @Column
    private String part;

    @Column
    private String topic;

    @Column
    private String room;

    public Classroom(String code, String name, String part, String topic, String room) {
        this.code = code;
        this.name = name;
        this.part = part;
        this.topic = topic;
        this.room = room;
    }

    public Classroom(long id, String code, String name, String part, String topic, String room) {
        this(code, name, part, topic, room);
        this.id = id;
    }
}
