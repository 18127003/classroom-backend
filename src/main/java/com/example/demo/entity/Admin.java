package com.example.demo.entity;

import com.example.demo.common.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ADMIN")
public class Admin extends AbstractEntity implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Admin(String name, String password, String email, Date createdAt, AccountStatus status) {
        this.name =name;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Admin(UUID id, String name, String password, String email, Date createdAt, AccountStatus status){
        this(name, password, email, createdAt, status);
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
