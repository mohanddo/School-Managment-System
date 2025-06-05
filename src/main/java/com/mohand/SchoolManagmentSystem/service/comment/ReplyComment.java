package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.ReplyCommentRepository;
import com.mohand.SchoolManagmentSystem.repository.UpVoteCommentRepository;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyComment implements IReplyComment {

    private final ReplyCommentRepository replyCommentRepository;
    private final UpVoteCommentRepository upVoteCommentRepository;
    private final ICourseService courseService;

    @Override
    public void addOrUpdateReplyComment(AddOrUpdateReplyCommentRequest request, User user) {

//        Resource resource = resourceService.findByIdAndChapterIdAndCourseId(request.getResourceId(), request.getChapterId(), request.getCourseId());
//        Comment comment;
//        if (request.getReplyCommentId() == null) {
//            ReplyComment replyComment = new ReplyComment(request.getText(), user, );
//        } else {
//            comment = findByIdAndUserId(request.getCommentId(), user.getId());
//            comment.setText(request.getText());
//        }
//
//        replyCommentRepository.save(comment);
    }

    @Override
    public void deleteReplyComment(Long replyCommentId, User user) {
        if (replyCommentRepository.deleteByIdAndUserId(replyCommentId, user.getId()) == 0) {
            throw new ResourceNotFoundException("Reply comment not found");
        }
    }

    @Override
    public void upVoteReplyComment(Long replyCommentId, Long userId) {
//        if (upVoteCommentRepository.deleteByCommentIdAndUserId(request.getCommentId(), user.getId()) > 0) {
//            return;
//        }
//
//        if (!courseService.existsByIdAndStudentId(request.getCourseId(), user.getId())
//                && !courseService.existsByIdAndTeacherId(request.getCourseId(), user.getId())) {
//            throw new ResourceNotFoundException("Course not found");
//        }
//
//        Comment comment = findByIdAndResourceIdAndChapterIdAndCourseId(request.getCommentId(), request.getResourceId(), request.getChapterId(), request.getCourseId());
//
//        upVoteCommentRepository.save(new UpVoteComment(comment, user));
    }
}
