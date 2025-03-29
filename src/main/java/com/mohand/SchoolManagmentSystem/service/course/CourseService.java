package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.exception.announcement.AnnouncementCommentNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.announcement.AnnouncementNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.course.CourseNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.courseReview.CourseReviewNotFoundException;
import com.mohand.SchoolManagmentSystem.model.Cart;
import com.mohand.SchoolManagmentSystem.model.course.*;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import com.mohand.SchoolManagmentSystem.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final FavoriteCourseRepository favoriteCourseRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCommentRepository announcementCommentRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(CreateCourseRequest request, Teacher teacher) {
        Course course = Course.builder(request.title(), request.description(), request.price(), request.getPricingModelEnum(), teacher, request.imageUrl(), request.introductionVideoUrl())
                .build();
        courseRepository.save(course);
    }

    @Override
    public void delete(Long courseId, Long teacherId) {

        if (courseRepository.existsByIdAndTeacherId(courseId, teacherId)) {
            courseRepository.deleteById(courseId);
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public List<CoursePreview> getAll(Long studentId) {
        List<Course> allCourses = courseRepository.findAll();

        List<CoursePreview> allCoursesPreviews = allCourses.stream().map(course -> modelMapper.map(course, CoursePreview.class)).toList();

        if (studentId == null) {
            return allCoursesPreviews;
        }

        allCoursesPreviews = allCoursesPreviews.stream().peek(coursePreview -> coursePreview.setFavourite(favoriteCourseRepository.existsByStudentIdAndCourseId(studentId, coursePreview.getId()))).toList();

        return allCoursesPreviews;
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    @Override
    @Transactional
    public void addOrRemoveCourseFromFavourite(Long studentId, Long courseId) {
        Student student = studentService.getById(studentId);
        Course course = getById(courseId);

        if(favoriteCourseRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            favoriteCourseRepository.deleteByStudentIdAndCourseId(studentId, courseId);
            return;
        }

        favoriteCourseRepository.save(new FavoriteCourse(student, course));
    }

    @Override
    @Transactional
    public void addOrRemoveCourseFromCart(Long studentId, Long courseId) {
        Student student = studentService.getById(studentId);
        Course course = getById(courseId);

        if(cartRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            cartRepository.deleteByStudentIdAndCourseId(studentId, courseId);
            return;
        }

        cartRepository.save(new Cart(student, course));
    }

    @Override
    @Transactional
    public void createOrUpdateCourseReview(CreateOrUpdateCourseReviewRequest request, Long studentId) {
        Student student = studentService.getById(studentId);
        Course course = getById(request.courseId());


        courseReviewRepository.findByStudentIdAndCourseId(studentId, request.courseId()).ifPresentOrElse((courseReview) -> {
            courseReview.setComment(request.comment());
            courseReview.setReview(request.getReviewEnum());
            courseReviewRepository.save(courseReview);
        }, () -> {
            CourseReview courseReview = CourseReview.builder(request.comment(), request.getReviewEnum(), student, course).build();
            courseReviewRepository.save(courseReview);

            course.setNumberOfReviews(course.getNumberOfReviews() + 1);
            courseRepository.save(course);
        });
    }


    @Override
    @Transactional
    public void deleteCourseReview(Long courseId, Long studentId) {

        if (courseReviewRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            courseReviewRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        } else {
            throw new CourseReviewNotFoundException(courseId.toString(), studentId.toString());
        }
    }

    @Override
    public void createOrUpdateAnnouncement(CreateOrUpdateAnnouncementRequest request, Long teacherId) {

        if (courseRepository.existsByIdAndTeacherId(request.courseId(), teacherId)) {
            if (request.announcementId() == null) {
                Course course = getById(request.courseId());
                Announcement announcement = new Announcement(request.text(), course);
                announcementRepository.save(announcement);
            } else {
                announcementRepository.findByIdAndCourseId(request.announcementId(), request.courseId()).ifPresentOrElse((announcement) -> {
                    announcement.setText(request.text());
                    announcementRepository.save(announcement);
                }, () -> {
                    throw new AnnouncementNotFoundException(request.announcementId().toString(), request.courseId().toString());
                });
            }
        } else {
            throw new CourseNotFoundException();
        }
    }


    @Override
    public void deleteAnnouncement(Long announcementId, Long courseId, Long teacherId) {

        if (courseRepository.existsByIdAndTeacherId(courseId, teacherId)) {
            if(announcementRepository.existsByIdAndCourseId(announcementId, courseId)) {
                announcementRepository.deleteById(announcementId);
            } else {
                throw new AnnouncementNotFoundException(announcementId.toString(), courseId.toString());
            }
        } else {
            throw new CourseNotFoundException();
        }

    }

    @Override
    public void createOrUpdateAnnouncementComment(CreateOrUpdateAnnouncementCommentRequest request, User user) {

        if (!courseRepository.existsByIdAndTeacherId(request.courseId(), user.getId()) && !existsByIdAndStudentId(request.courseId(), user.getId())) {
            throw new CourseNotFoundException();
        }

        Announcement announcement = announcementRepository
                .findByIdAndCourseId(request.announcementId(), request.courseId())
                .orElseThrow(() -> new AnnouncementNotFoundException(request.announcementId().toString(), request.courseId().toString()));

        if (request.commentId() == null) {
            AnnouncementComment announcementComment = new AnnouncementComment(user, announcement, request.comment());
            announcementCommentRepository.save(announcementComment);
            return;
        }

        announcementCommentRepository.findByIdAndAnnouncementId(request.commentId(), request.announcementId()).ifPresentOrElse((announcementComment) -> {
            announcementComment.setText(request.comment());
            announcementCommentRepository.save(announcementComment);
        }, () -> {
            throw new AnnouncementCommentNotFoundException(request.announcementId().toString(), request.commentId().toString());
        });
    }

    @Override
    public void deleteAnnouncementComment(Long announcementId, Long courseId, Long commentId, User user) {
        if (!courseRepository.existsByIdAndTeacherId(courseId, user.getId()) && !existsByIdAndStudentId(courseId, user.getId())) {
            throw new CourseNotFoundException();
        }

        if(!announcementRepository.existsByIdAndCourseId(announcementId, courseId)) {
            throw new AnnouncementNotFoundException(announcementId.toString(), courseId.toString());
        }

        if(!announcementCommentRepository.existsByIdAndAnnouncementId(commentId, announcementId)) {
            throw new AnnouncementCommentNotFoundException(announcementId.toString(), commentId.toString());
        }

        announcementCommentRepository.deleteById(commentId);
    }


    @Override
    public boolean existsByIdAndStudentId(Long id, Long studentId) {
        Student student = studentService.getById(studentId);
        Course course = getById(id);
        return student.getCourses().contains(course) && course.getStudents().contains(student);
    }

}
