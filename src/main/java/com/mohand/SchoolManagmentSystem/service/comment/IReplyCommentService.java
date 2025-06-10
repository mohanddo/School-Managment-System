package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.model.comment.ReplyComment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteReplyCommentRequest;

import java.util.Optional;

public interface IReplyCommentService {

    void addOrUpdateReplyComment(AddOrUpdateReplyCommentRequest request, User user);

    void deleteReplyComment(Long replyCommentId, User user);

    void upVoteReplyComment(UpVoteReplyCommentRequest request, User user);

    ReplyComment findByIdAndUserId(Long replyCommentId, Long userId);

    void removeUpVoteReplyComment(Long replyCommentId, User user);

    ReplyComment findByIdAndCommentIdAndResourceIdAndChapterIdAndCourseId(Long replyCommentId, Long commentId,
                                                                                    Long resourceId, Long chapterId, Long courseId);
}
