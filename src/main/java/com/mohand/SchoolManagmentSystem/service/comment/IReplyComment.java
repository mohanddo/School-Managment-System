package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateReplyCommentRequest;

public interface IReplyComment {

    void addOrUpdateReplyComment(AddOrUpdateReplyCommentRequest request, User user);

    void deleteReplyComment(Long replyCommentId, User user);

    void upVoteReplyComment(Long replyCommentId, Long userId);
}
