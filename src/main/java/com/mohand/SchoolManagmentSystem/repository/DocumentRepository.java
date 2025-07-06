package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Document;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("""
    SELECT d FROM Document d\s
    WHERE d.id = :documentId\s
    AND d.chapter.id = :chapterId\s
    AND d.chapter.course.id = :courseId\s
    AND d.chapter.course.teacher.id = :teacherId
""")
    Optional<Document> findByIdAndChapterIdAndCourseIdAndTeacherId(@Param("documentId") Long documentId,
                                                                @Param("chapterId") Long chapterId,
                                                                @Param("courseId") Long courseId,
                                                                @Param("teacherId") Long teacherId);

    List<Document> findAllByChapterId(Long chapterId);
    int countAllByChapterId(Long chapterId);

    @Query("""
    SELECT count(d) FROM Document d\s
    WHERE d.chapter.course.id = :courseId\s
""")
    int countAllByCourseId(@Param("courseId") Long courseId);
}
