package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import com.sun.jdi.LongValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpVoteCommentRepository extends JpaRepository<UpVoteComment, Long> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    int deleteByCommentIdAndUserId(Long commentId, Long userId);
}
