package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "NOTIFICATION_RECEIVER")
public class Receiver extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Notification.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Notification notification;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Account account;

    public Receiver(Notification notification, Account account) {
        this.notification = notification;
        this.account = account;
    }
}
