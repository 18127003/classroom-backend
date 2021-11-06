package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @Column(nullable = false)
    private String name;

    @Column
    private String part;

    @Column
    private String topic;

    @Column
    private String room;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private Account creator;

    @OneToMany(mappedBy = "classroom")
    private List<Participant> participants;

    public Classroom(String code, String name, String part, String topic, String room,
                     List<Participant> participants, Account creator) {
        this.code = code;
        this.name = name;
        this.part = part;
        this.topic = topic;
        this.room = room;
        this.participants=participants;
        this.creator = creator;
    }

    public Classroom(long id, String code, String name, String part, String topic, String room,
                     List<Participant> participants, Account creator) {
        this(code, name, part, topic, room, participants, creator);
        this.id = id;
    }
}
