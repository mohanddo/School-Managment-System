package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.course.FavoriteCourse;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import com.mohand.SchoolManagmentSystem.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final CartItemRepository cartItemRepository;
    private final FavoriteCourseRepository favoriteCourseRepository;
    private final CourseService courseService;
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;

    @Override
    public Student getByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }


    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public boolean checkIfExistByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public boolean checkIfExistById(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Student me(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        com.mohand.SchoolManagmentSystem.response.authentication.Student studentResponse = modelMapper
                .map(student, com.mohand.SchoolManagmentSystem.response.authentication.Student.class);
        addCoursesToStudentResponse(studentResponse);
        return studentResponse;
    }

    @Override
    public void addStudentToCourse(Long courseId, Long studentId) {
        Course course = courseService.getById(courseId);
        Student student = getById(studentId);

        if (courseService.existsByIdAndStudentId(courseId, studentId)) {
            throw new ConflictException("Student already enrolled in this course");
        }

        course.getStudents().add(student);
        course.setNumberOfStudents(course.getNumberOfStudents() + 1);
        student.getCourses().add(course);

        courseService.save(course);
        save(student);
    }

    @Override
    public void addCoursesToStudentResponse(com.mohand.SchoolManagmentSystem.response.authentication.Student studentResponse) {
        List<CoursePreview> courses = new ArrayList<>();


        List<CartItem> cartItems = cartItemRepository.findAllByStudentId(studentResponse.getId());
        for (CartItem cartItem : cartItems) {
            CoursePreview coursePreview = modelMapper.map(cartItem.getCourse(), CoursePreview.class);
            coursePreview.setInCart(true);
            coursePreview.setFavourite(false);
            coursePreview.setEnrolled(false);
            courses.add(coursePreview);
        }


        List<FavoriteCourse> favoriteCourses = favoriteCourseRepository.findAllByStudentId(studentResponse.getId());
        outer:
        for (FavoriteCourse favoriteCourse : favoriteCourses) {
            for (CoursePreview coursePreview : courses) {
                if (coursePreview.getId() == favoriteCourse.getCourse().getId()) {
                    coursePreview.setFavourite(true);
                    continue outer;
                }
            }
            CoursePreview coursePreview = modelMapper.map(favoriteCourse.getCourse(), CoursePreview.class);
            coursePreview.setFavourite(true);
            coursePreview.setEnrolled(false);
            coursePreview.setInCart(false);
            courses.add(coursePreview);
        }

        List<Course> enrolledCourses = courseService.getAllCoursesByStudentId(studentResponse.getId());
        outer:
        for (Course course : enrolledCourses) {
            for (CoursePreview coursePreview : courses) {
                if (coursePreview.getId() == course.getId()) {
                    coursePreview.setEnrolled(true);
                    coursePreview.setProgressPercentage(
                            resourceService.countProgressPercentageByCourseIdAndStudentId(coursePreview.getId(), studentResponse.getId())
                    );
                    continue outer;
                }
            }
            CoursePreview coursePreview = modelMapper.map(course, CoursePreview.class);
            coursePreview.setEnrolled(true);
            coursePreview.setFavourite(false);
            coursePreview.setInCart(false);
            coursePreview.setProgressPercentage(
                    resourceService.countProgressPercentageByCourseIdAndStudentId(coursePreview.getId(), studentResponse.getId())
            );
            courses.add(coursePreview);
        }

        studentResponse.setCourses(courses);
    }

}
