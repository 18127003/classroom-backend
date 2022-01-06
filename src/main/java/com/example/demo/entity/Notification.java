package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "NOTIFICATION")
public class Notification extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(columnDefinition = "text")
    private String content;

    public Notification(String content) {
        this.content = content;
    }

    public Notification(Date createdAt, String content) {
        this.createdAt = createdAt;
        this.content = content;
    }

    public Notification(Long id, Date createdAt, String content) {
        this(createdAt, content);
        this.id = id;
    }
}
