package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.AnnouncementComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementCommentRepository extends JpaRepository<AnnouncementComment, Long> {
    Optional<AnnouncementComment> findByIdAndAnnouncementId(Long id, Long announcementId);

    boolean existsByIdAndAnnouncementId(Long id, Long announcementId);
}
