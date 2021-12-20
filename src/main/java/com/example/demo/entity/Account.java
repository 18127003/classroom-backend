package com.example.demo.entity;

import com.example.demo.common.enums.AccountRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ACCOUNT")
public class Account extends AbstractEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column(name = "account_role")
    @Enumerated(value = EnumType.STRING)
    private AccountRole accountRole;

    @OneToOne(mappedBy = "classroomAccount")
    private StudentInfo studentInfo;

    @OneToMany(mappedBy = "account")
    private List<Participant> assignedClasses;

    public Account(String firstName, String lastName, String password, String email, AccountRole accountRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName;
        this.password = password;
        this.email = email;
        this.accountRole = accountRole;
    }

    public Account(String firstName, String lastName, String password, String email, StudentInfo studentInfo,
                   AccountRole accountRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName;
        this.password = password;
        this.email = email;
        this.studentInfo = studentInfo;
        this.accountRole = accountRole;
    }

    public Account(long id, String firstName, String lastName, String password, String email, StudentInfo studentInfo,
                   AccountRole accountRole){
        this(firstName, lastName, password, email, studentInfo, accountRole);
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(accountRole.name()));
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
