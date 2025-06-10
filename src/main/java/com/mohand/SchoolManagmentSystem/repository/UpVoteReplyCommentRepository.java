package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpVoteReplyCommentRepository extends JpaRepository<UpVoteReplyComment, Long> {
    boolean existsByReplyCommentIdAndUserId(Long commentId, Long userId);
    int deleteByReplyCommentIdAndUserId(Long commentId, Long userId);
    Boolean existsByUserIdAndReplyCommentId(Long userId, Long commentId);
    Long countByReplyCommentId(Long replyCommentId);
}
