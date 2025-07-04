package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("""
    SELECT v FROM Video v\s
    WHERE v.id = :videoId\s
    AND v.chapter.id = :chapterId\s
    AND v.chapter.course.id = :courseId\s
    AND v.chapter.course.teacher.id = :teacherId
""")
    Optional<Video> findByIdAndChapterIdAndCourseIdAndTeacherId(@Param("videoId") Long videoId,
                              @Param("chapterId") Long chapterId,
                              @Param("courseId") Long courseId,
                              @Param("teacherId") Long teacherId);

    @Query("""
    SELECT v FROM Video v\s
    WHERE v.id = :videoId\s
    AND v.chapter.id = :chapterId\s
    AND v.chapter.course.id = :courseId\s
""")
    Optional<Video> findByIdAndChapterIdAndCourseId(@Param("videoId") Long videoId,
                                                                @Param("chapterId") Long chapterId,
                                                                @Param("courseId") Long courseId);

    List<Video> findAllByChapterId(Long chapterId);
    int countAllByChapterId(Long chapterId);

}
