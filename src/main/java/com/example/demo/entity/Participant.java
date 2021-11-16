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

    public Participant(Account account, Classroom classroom, Role role, Boolean hidden){
        this.account = account;
        this.classroom = classroom;
        this.hidden = hidden;
        this.role=role;
    }

    public Participant(Long id, Account account, Classroom classroom, Role role, Boolean hidden){
        this(account, classroom, role, hidden);
        this.id = id;
    }

}
