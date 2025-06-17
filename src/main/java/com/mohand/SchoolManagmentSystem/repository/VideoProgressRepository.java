package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.chapter.VideoProgress;
import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoProgressRepository extends JpaRepository<VideoProgress, Long> {

    Optional<VideoProgress> findByStudentAndVideo(Student student, Video video);
    boolean existsByStudentAndVideo(Student student, Video video);
}
