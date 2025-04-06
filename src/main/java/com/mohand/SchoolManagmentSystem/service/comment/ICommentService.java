package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.DeleteCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;

import java.util.Optional;

public interface ICommentService {

    void addOrUpdateComment(AddOrUpdateCommentRequest request, User user);
    void deleteComment(DeleteCommentRequest request, User user);
    void upVoteComment(UpVoteCommentRequest request, User user);

    Comment findByIdAndResourceId(Long commentId, Long resourceId);
    Comment findByIdAndResourceIdAndChapterIdAndCourseId(Long commentId, Long resourceId, Long chapterId, Long courseId);
}
