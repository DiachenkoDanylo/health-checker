package com.diachenko.checker.model.entity;


/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authority",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "authority")
        }
)
@AllArgsConstructor
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    public Authority() {

    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authority='" + authority + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority1 = (Authority) o;
        return authority.equals(authority1.authority);
    }

    @Override
    public int hashCode() {
        return authority.hashCode();
    }
}
