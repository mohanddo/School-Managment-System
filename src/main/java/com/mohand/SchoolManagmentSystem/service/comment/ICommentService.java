package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;

import java.util.Optional;

public interface ICommentService {

    void addOrUpdateComment(AddOrUpdateCommentRequest request, User user);
    void deleteComment(Long commentId, User user);
    void upVoteComment(UpVoteCommentRequest request, User user);
    void removeUpVoteComment(Long commentId, User user);

    Comment findByIdAndUserId(Long commentId, Long userId);
    Comment findByIdAndResourceIdAndChapterIdAndCourseId(Long commentId, Long resourceId, Long chapterId, Long courseId);
}
