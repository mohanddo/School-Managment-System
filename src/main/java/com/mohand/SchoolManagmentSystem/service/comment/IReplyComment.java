package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.model.user.User;

public interface IReplyComment {

    void addOrUpdateReplyComment(String text, Long commentId, Long userId);

    void deleteReplyComment(Long replyCommentId, User user);

    void upVoteReplyComment(Long replyCommentId, Long userId);
}
