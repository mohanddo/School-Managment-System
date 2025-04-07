package com.mohand.SchoolManagmentSystem.service.comment;

import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.comment.Comment;
import com.mohand.SchoolManagmentSystem.model.comment.UpVoteComment;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.CommentRepository;
import com.mohand.SchoolManagmentSystem.repository.UpVoteCommentRepository;
import com.mohand.SchoolManagmentSystem.request.comment.AddOrUpdateCommentRequest;
import com.mohand.SchoolManagmentSystem.request.comment.UpVoteCommentRequest;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import com.mohand.SchoolManagmentSystem.service.resource.IResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final ICourseService courseService;
    private final CommentRepository commentRepository;
    private final UpVoteCommentRepository upVoteCommentRepository;
    private final IResourceService resourceService;

    @Override
    public void addOrUpdateComment(AddOrUpdateCommentRequest request, User user) {
        if (!courseService.existsByIdAndStudentId(request.getCourseId(), user.getId())
                && !courseService.existsByIdAndTeacherId(request.getCourseId(), user.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        Resource resource = resourceService.findByIdAndChapterIdAndCourseId(request.getResourceId(), request.getChapterId(), request.getCourseId());
        Comment comment;
        if (request.getCommentId() == null) {
            comment = new Comment(request.getText(), user, resource);
        } else {
            comment = findByIdAndUserId(request.getCommentId(), user.getId());
            comment.setText(request.getText());
        }

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        if(commentRepository.deleteByIdAndUserId(commentId, user.getId()) == 0) {
            throw new ResourceNotFoundException("Comment not found");
        }
    }

    @Override
    @Transactional
    public void upVoteComment(UpVoteCommentRequest request, User user) {

        if (upVoteCommentRepository.deleteByCommentIdAndUserId(request.getCommentId(), user.getId()) > 0) {
            return;
        }

        if (!courseService.existsByIdAndStudentId(request.getCourseId(), user.getId())
                && !courseService.existsByIdAndTeacherId(request.getCourseId(), user.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        Comment comment = findByIdAndResourceIdAndChapterIdAndCourseId(request.getCommentId(), request.getResourceId(), request.getChapterId(), request.getCourseId());

        upVoteCommentRepository.save(new UpVoteComment(comment, user));
    }

    @Override
    public Comment findByIdAndUserId(Long commentId, Long userId) {
        return commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    @Override
    public Comment findByIdAndResourceIdAndChapterIdAndCourseId(Long commentId, Long resourceId, Long chapterId, Long courseId) {
        return commentRepository.findByIdAndResourceIdAndChapterIdAndCourseId(commentId, resourceId, chapterId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }
}
