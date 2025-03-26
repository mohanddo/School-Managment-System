package com.mohand.SchoolManagmentSystem.model.user;

import com.mohand.SchoolManagmentSystem.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Entity
public class Admin extends User {

    public Admin(String firstName, String lastName, String email, String password, String verificationCode, LocalDateTime verificationCodeExpiresAt) {
        super(firstName, lastName, email, password, verificationCode, verificationCodeExpiresAt, Role.ROLE_ADMIN);
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(Role.ROLE_ADMIN.getValue())
        );
    }

}
