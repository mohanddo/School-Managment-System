package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.NotFoundException;
import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.*;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementCommentRequest;
import com.mohand.SchoolManagmentSystem.request.announcement.CreateOrUpdateAnnouncementRequest;
import com.mohand.SchoolManagmentSystem.request.course.CreateOrUpdateCourseReviewRequest;
import com.mohand.SchoolManagmentSystem.request.course.UpdateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.service.chapter.ChapterService;
import com.mohand.SchoolManagmentSystem.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Lazy
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final FavoriteCourseRepository favoriteCourseRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCommentRepository announcementCommentRepository;
    private final CartItemRepository cartItemRepository;
    private final FinishedResourceRepository finishedResourceRepository;
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;


    @Override
    public void update(UpdateCourseRequest request, Teacher teacher) {


        courseRepository.findByIdAndTeacherId(request.getCourseId(), teacher.getId()).ifPresentOrElse((course) -> {

            course.setTitle(request.getTitle());
            course.setDescription(request.getDescription());
            course.setImageUrl(request.getImageUrl());
            course.setIntroductionVideoUrl(request.getIntroductionVideoUrl());

            course.setPricingModel(request.getPricingModelEnum());
            course.setCategory(request.getCategoryEnum());
            course.setPrice(request.getPrice());

            course.setDiscountPercentage(request.getDiscountPercentage());
            course.setDiscountExpirationDate(request.getDiscountExpirationDate());

                    courseRepository.save(course);
                }, () -> {
                    throw new ResourceNotFoundException("Course not found");
                }
        );
    }


    @Override
    public List<Course> getAll() {
        List<com.mohand.SchoolManagmentSystem.model.course.Course> allCourses = courseRepository.findAll();
        return allCourses.stream().map(course ->  {
            Course courseResponse = modelMapper.map(course, Course.class);
            resourceService.addChapterToCourseResponse(courseResponse, null);
            return courseResponse;
        }).toList();
    }

    @Override
    public Course getCourseResponseById(Long id) {
        com.mohand.SchoolManagmentSystem.model.course.Course courses = getById(id);
        Course courseResponse = modelMapper.map(courses, Course.class);
        resourceService.addChapterToCourseResponse(courseResponse, null);
        return courseResponse;
    }

    @Override
    public List<com.mohand.SchoolManagmentSystem.model.course.Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public com.mohand.SchoolManagmentSystem.model.course.Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public void addCourseToFavourite(Student student, Long courseId) {
        com.mohand.SchoolManagmentSystem.model.course.Course course = getById(courseId);

        if(favoriteCourseRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new ConflictException("Course already added to favorites");
        } else {
            favoriteCourseRepository.save(new FavoriteCourse(student, course));
        }
    }

    @Override
    @Transactional
    public void removeCourseFromFavourite(Student student, Long courseId) {
        if(!favoriteCourseRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new NotFoundException("Course not found in favorites");
        } else {
            favoriteCourseRepository.deleteByStudentIdAndCourseId(student.getId(), courseId);
        }
    }



    @Override
    @Transactional
    public void addCourseToCart(Student student, Long courseId) {
        com.mohand.SchoolManagmentSystem.model.course.Course course = getById(courseId);

        if (cartItemRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new ConflictException("Student already enrolled in this course");
        } else {
            cartItemRepository.save(new CartItem(student, course));
        }
    }

    @Override
    @Transactional
    public void removeCourseFromCart(Student student, Long courseId) {
        if (!cartItemRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new NotFoundException("Course not found in cart");
        } else {
            cartItemRepository.deleteByStudentIdAndCourseId(student.getId(), courseId);
        }
    }


    @Override
    @Transactional
    public void createOrUpdateCourseReview(CreateOrUpdateCourseReviewRequest request, Student student) {
        com.mohand.SchoolManagmentSystem.model.course.Course course = getById(request.courseId());

        if (!existsByIdAndStudentId(request.courseId(), student.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        courseReviewRepository.findByStudentIdAndCourseId(student.getId(), request.courseId()).ifPresentOrElse((courseReview) -> {
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
        com.mohand.SchoolManagmentSystem.model.course.Course course = getById(courseId);
        if (courseReviewRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            courseReviewRepository.deleteByStudentIdAndCourseId(studentId, courseId);
            course.setNumberOfReviews(course.getNumberOfReviews() - 1);
        } else {
            throw new ResourceNotFoundException("Course review not found");
        }
    }

    @Override
    public void createOrUpdateAnnouncement(CreateOrUpdateAnnouncementRequest request, Long teacherId) {

        com.mohand.SchoolManagmentSystem.model.course.Course course = findByIdAndTeacherId(request.courseId(), teacherId);

            if (request.announcementId() == null) {
                Announcement announcement = new Announcement(request.text(), course);
                announcementRepository.save(announcement);
            } else {
                announcementRepository.findByIdAndCourseId(request.announcementId(), request.courseId()).ifPresentOrElse((announcement) -> {
                    announcement.setText(request.text());
                    announcementRepository.save(announcement);
                }, () -> {
                    throw new ResourceNotFoundException("Announcement not found");
                });
            }

    }


    @Override
    public void deleteAnnouncement(Long announcementId, Long courseId, Long teacherId) {

        if (!courseRepository.existsByIdAndTeacherId(courseId, teacherId)) {
            throw new ResourceNotFoundException("Course not found");
        }

        if(!announcementRepository.existsByIdAndCourseId(announcementId, courseId)) {
            throw new ResourceNotFoundException("Announcement not found");
        }

        announcementRepository.deleteById(announcementId);
    }

    @Override
    public void createOrUpdateAnnouncementComment(CreateOrUpdateAnnouncementCommentRequest request, User user) {

        if (!courseRepository.existsByIdAndTeacherId(request.courseId(), user.getId()) && !existsByIdAndStudentId(request.courseId(), user.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        Announcement announcement = announcementRepository
                .findByIdAndCourseId(request.announcementId(), request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found"));

        if (request.commentId() == null) {
            AnnouncementComment announcementComment = new AnnouncementComment(user, announcement, request.comment());
            announcementCommentRepository.save(announcementComment);
            return;
        }

        announcementCommentRepository.findByIdAndAnnouncementId(request.commentId(), request.announcementId()).ifPresentOrElse((announcementComment) -> {
            announcementComment.setText(request.comment());
            announcementCommentRepository.save(announcementComment);
        }, () -> {
            throw new ResourceNotFoundException("Announcement comment not found");
        });
    }

    @Override
    public void deleteAnnouncementComment(Long announcementId, Long courseId, Long commentId, User user) {
        if (!courseRepository.existsByIdAndTeacherId(courseId, user.getId()) && !existsByIdAndStudentId(courseId, user.getId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        if(!announcementRepository.existsByIdAndCourseId(announcementId, courseId)) {
            throw new ResourceNotFoundException("Announcement not found");
        }

        if(!announcementCommentRepository.existsByIdAndAnnouncementId(commentId, announcementId)) {
            throw new ResourceNotFoundException("Announcement comment not found");
        }

        announcementCommentRepository.deleteById(commentId);
    }


    @Override
    public boolean existsByIdAndStudentId(Long id, Long studentId) {
        return courseRepository.isStudentEnrolledInCourse(studentId, id);
    }

    @Override
    public boolean existsByIdAndTeacherId(Long id, Long teacherId) {
        return courseRepository.existsByIdAndTeacherId(id, teacherId);
    }

    @Override
    public com.mohand.SchoolManagmentSystem.model.course.Course findByIdAndTeacherId(Long id, Long teacherId) {
        return courseRepository.findByIdAndTeacherId(id, teacherId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public List<com.mohand.SchoolManagmentSystem.model.course.Course> getAllCoursesByStudentId(Long studentId) {
        return courseRepository.findAllCoursesByStudentId(studentId);
    }

    @Override
    public List<com.mohand.SchoolManagmentSystem.model.course.Course> getAllCoursesByTeacherId(Long teacherId) {
        return courseRepository.findAllByTeacherId(teacherId);
    }

    @Override
    public void save(com.mohand.SchoolManagmentSystem.model.course.Course course) {
        courseRepository.save(course);
    }

    @Override
    public int countProgressPercentageByCourseIdAndStudentId(Long courseId, Long studentId) {
        if (!existsByIdAndStudentId(courseId, studentId))
            throw new ResourceNotFoundException("Course not found");

        if (resourceService.countByCourseId(courseId) == 0) {
            return 0;
        }
        return (finishedResourceRepository.countFinishedResourceByCourseIdAndStudentId(courseId, studentId) / resourceService.countByCourseId(courseId)) * 100;
    }

}
