package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.ReplyComment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteReplyComment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.ReplyCommentRepository;
import com.mohand.SchoolManagmentSystem.repository.UpVoteReplyCommentRepository;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteReplyCommentRequest;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyCommentService implements IReplyCommentService {

    private final ReplyCommentRepository replyCommentRepository;
    private final UpVoteReplyCommentRepository upVoteReplyCommentRepository;
    private final CommentService commentService;
    private final ICourseService courseService;

    @Override
    public void addOrUpdateReplyComment(AddOrUpdateReplyCommentRequest request, User user) {
        ReplyComment replyComment;
        if (request.getReplyCommentId() == null) {
            Comment comment = commentService.findByIdAndUserId(request.getCommentId(), user.getId());
            replyComment = new ReplyComment(request.getText(), user, comment);
        } else {
            replyComment = findByIdAndUserId(request.getReplyCommentId(), user.getId());
            replyComment.setText(request.getText());
        }

        replyCommentRepository.save(replyComment);
    }

    @Override
    @Transactional
    public void deleteReplyComment(Long replyCommentId, User user) {
        if (replyCommentRepository.deleteByIdAndUserId(replyCommentId, user.getId()) == 0) {
            throw new ResourceNotFoundException("Reply comment not found");
        }
    }

    @Override
    @Transactional
    public void upVoteReplyComment(UpVoteReplyCommentRequest request, User user) {

        if (!courseService.existsByIdAndStudentId(request.getCourseId(), user.getId())
                && !courseService.existsByIdAndTeacherId(request.getCourseId(), user.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        ReplyComment replyComment = findByIdAndCommentIdAndResourceIdAndChapterIdAndCourseId(request.getReplyCommentId(), request.getCommentId(), request.getResourceId(), request.getChapterId(), request.getCourseId());

        if (upVoteReplyCommentRepository.existsByReplyCommentIdAndUserId(replyComment.getId(), user.getId())) {
            throw new ConflictException("Reply Comment already upVoted");
        }

        upVoteReplyCommentRepository.save(new UpVoteReplyComment(user, replyComment));
    }

    @Override
    @Transactional
    public void removeUpVoteReplyComment(Long replyCommentId, User user) {
        if (upVoteReplyCommentRepository.deleteByReplyCommentIdAndUserId(replyCommentId, user.getId()) < 1) {
            throw new ResourceNotFoundException("up vote not found");
        }
    }

    @Override
    public ReplyComment findByIdAndUserId(Long replyCommentId, Long userId) {
        return replyCommentRepository.findByIdAndUserId(replyCommentId, userId).orElseThrow(() -> new ResourceNotFoundException("Reply comment not found"));
    }

    @Override
    public ReplyComment findByIdAndCommentIdAndResourceIdAndChapterIdAndCourseId(Long replyCommentId, Long commentId,
                                                                                    Long resourceId, Long chapterId, Long courseId) {
        return replyCommentRepository.findByIdAndCommentIdAndResourceIdAndChapterIdAndCourseId(replyCommentId, commentId, resourceId, chapterId, courseId).orElseThrow(() -> new ResourceNotFoundException("Reply comment not found"));
    }
}
