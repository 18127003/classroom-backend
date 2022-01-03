package com.example.demo.entity;

import com.example.demo.common.enums.VerifyTokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "VERIFY_TOKEN")
public class VerifyToken extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column
    private Date expiry;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private VerifyTokenType tokenType;

    @OneToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public VerifyToken(String token, Date expiry, VerifyTokenType tokenType, Account account) {
        this.token = token;
        this.expiry = expiry;
        this.tokenType = tokenType;
        this.account = account;
    }

    public VerifyToken(Long id, String token, Date expiry, VerifyTokenType tokenType, Account account) {
        this(token, expiry, tokenType, account);
        this.id = id;
    }
}
