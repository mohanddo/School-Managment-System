package com.mohand.SchoolManagmentSystem.service.comment;

public interface IReplyComment {

    void addReplyComment(String text, Long commentId, Long userId);

    void deleteReplyComment(Long replyCommentId);

    void updateReplyComment(Long replyCommentId, String text);

    void upVoteReplyComment(Long replyCommentId, Long userId);

    void downVoteReplyComment(Long replyCommentId, Long userId);
}
