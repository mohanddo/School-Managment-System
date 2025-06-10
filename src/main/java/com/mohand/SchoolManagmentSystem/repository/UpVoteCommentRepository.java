package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpVoteCommentRepository extends JpaRepository<UpVoteComment, Long> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    int deleteByCommentIdAndUserId(Long commentId, Long userId);
    long countByCommentId(Long commentId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
