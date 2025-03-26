package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.exception.course.CourseNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.courseReview.CourseReviewNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import com.mohand.SchoolManagmentSystem.model.course.FavoriteCourse;
import com.mohand.SchoolManagmentSystem.repository.CourseRepository;
import com.mohand.SchoolManagmentSystem.repository.CourseReviewRepository;
import com.mohand.SchoolManagmentSystem.repository.FavoriteCourseRepository;
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
    private final ModelMapper modelMapper;

    @Override
    public void create(CreateCourseRequest request, Long teacherId) {
        Teacher teacher = teacherService.readById(teacherId);
        Course course = Course.builder(request.title(), request.description(), request.price(), request.getPricingModelEnum(), teacher, request.imageUrl(), request.introductionVideoUrl())
                .build();
        courseRepository.save(course);
    }
    @Override
    public void delete(Long courseId, Long teacherId) {

        if (teacherService.checkIfExistById(teacherId)) {
            throw new AccountNotFoundException();
        }

        if (courseRepository.existsByIdAndTeacherId(courseId, teacherId)) {
            courseRepository.deleteById(courseId);
        } else {
            throw new CourseNotFoundException();
        }
    }

    @Override
    public Course updateTitle(Long courseId, String newTitle) {
        Course course = getById(courseId);
        course.setTitle(newTitle);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDescription(Long courseId, String newDescription) {
        Course course = getById(courseId);
        course.setDescription(newDescription);
        return courseRepository.save(course);
    }

    @Override
    public Course updatePrice(Long courseId, double price) {
        Course course = getById(courseId);
        course.setPrice(price);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDiscountPercentage(Long courseId, int discountPercentage) {
        Course course = getById(courseId);
        course.setDiscountPercentage(discountPercentage);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate) {
        Course course = getById(courseId);
        course.setDiscountExpirationDate(discountExpirationDate);
        return courseRepository.save(course);
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

        if (!studentService.checkIfExistById(studentId)) {
            throw new AccountNotFoundException();
        }

        if (courseReviewRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            courseReviewRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        } else {
            throw new CourseReviewNotFoundException(courseId.toString(), studentId.toString());
        }
    }

}
