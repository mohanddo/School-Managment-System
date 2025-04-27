package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("""
    SELECT r FROM Resource r\s
    WHERE r.id = :resourceId\s
    AND r.chapter.id = :chapterId\s
    AND r.chapter.course.id = :courseId\s
""")
    Optional<Resource> findByIdAndChapterIdAndCourseId(@Param("resourceId") Long resourceId,
                                                                @Param("chapterId") Long chapterId,
                                                                @Param("courseId") Long courseId);

    @Query("select count(r) from Resource r where r.chapter.course.id = :courseId")
    int countByCourseId(Long courseId);
}
