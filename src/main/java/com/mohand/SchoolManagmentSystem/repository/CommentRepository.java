package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
    int deleteByIdAndUserId(Long commentId, Long userId);

    @Query("""
    SELECT COUNT(c) > 0 FROM Comment c\s
    WHERE c.id = :commentId\s
    AND c.resource.id = :resourceId\s
    AND c.resource.chapter.id = :chapterId\s
    AND c.resource.chapter.course.id = :courseId\s
""")
    boolean existsByIdAndResourceIdAndChapterIdAndCourseId(@Param("commentId") Long commentId,
                                                                @Param("resourceId") Long resourceId,
                                                                @Param("chapterId") Long chapterId,
                                                                @Param("courseId") Long courseId);

    @Query("""
    SELECT c FROM Comment c\s
    WHERE c.id = :commentId\s
    AND c.resource.id = :resourceId\s
    AND c.resource.chapter.id = :chapterId\s
    AND c.resource.chapter.course.id = :courseId\s
""")
    Optional<Comment> findByIdAndResourceIdAndChapterIdAndCourseId(@Param("commentId") Long commentId,
                                                           @Param("resourceId") Long resourceId,
                                                           @Param("chapterId") Long chapterId,
                                                           @Param("courseId") Long courseId);

    @Modifying
    @Query("""
    DELETE FROM Comment c\s
    WHERE c.id = :commentId\s
    AND c.resource.id = :resourceId\s
    AND c.resource.chapter.id = :chapterId\s
    AND c.resource.chapter.course.id = :courseId\s
""")
    int deleteByIdAndResourceIdAndChapterIdAndCourseId(@Param("commentId") Long commentId,
                                                           @Param("resourceId") Long resourceId,
                                                           @Param("chapterId") Long chapterId,
                                                           @Param("courseId") Long courseId);

}
