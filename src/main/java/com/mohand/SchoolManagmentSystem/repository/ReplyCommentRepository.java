package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

    Optional<ReplyComment> findByIdAndUserId(Long replyCommentId, Long userId);
    boolean existsByIdAndUserId(Long replyCommentId, Long userId);
    int deleteByIdAndUserId(Long replyCommentId, Long userId);

    @Query("""
    SELECT c FROM ReplyComment c\s
    WHERE c.id = :replyCommentId\s
    AND c.comment.id = :commentId\s
    AND c.comment.resource.id = :resourceId\s
    AND c.comment.resource.chapter.id = :chapterId\s
    AND c.comment.resource.chapter.course.id = :courseId\s
""")
    Optional<ReplyComment> findByIdAndCommentIdAndResourceIdAndChapterIdAndCourseId(@Param("replyCommentId") Long replyCommentId,
                                                                                    @Param("commentId") Long commentId,
                                                                                    @Param("resourceId") Long resourceId,
                                                                                    @Param("chapterId") Long chapterId,
                                                                                    @Param("courseId") Long courseId);
}
