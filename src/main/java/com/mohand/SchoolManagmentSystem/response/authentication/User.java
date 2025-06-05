package com.mohand.SchoolManagmentSystem.response.authentication;

import com.mohand.SchoolManagmentSystem.enums.Role;
import com.mohand.SchoolManagmentSystem.model.PasswordResetToken;
import com.mohand.SchoolManagmentSystem.model.course.AnnouncementComment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    protected long id;

    protected String firstName;

    protected String lastName;

    protected String email;

    protected boolean hasProfilePic;

    protected String sasTokenForReadingProfilePic;

    protected String sasTokenForWritingProfilePic;
}
