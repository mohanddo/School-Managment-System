package com.mohand.SchoolManagmentSystem.model.course;


import com.mohand.SchoolManagmentSystem.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class AnnouncementComment {

    public AnnouncementComment(User user, Announcement announcement, String text) {
        this.user = user;
        this.announcement = announcement;
        this.text = text;
        this.dateOfCreation = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Column(nullable = false)
    private LocalDateTime dateOfCreation;

    @Column(nullable = false)
    private String text;
}
