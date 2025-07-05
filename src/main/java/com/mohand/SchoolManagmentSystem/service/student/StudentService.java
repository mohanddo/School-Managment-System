package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.course.FavoriteCourse;
import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.user.UpdateStudentRequest;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final CartItemRepository cartItemRepository;
    private final FavoriteCourseRepository favoriteCourseRepository;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final TeacherStudentRepository teacherStudentRepository;
    private final ResourceService resourceService;

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

        for(StudentCourse studentCourse: studentResponse.getCourses()) {
            resourceService.addChapterToCourseResponse(studentCourse);
        }

        return studentResponse;
    }



    @Override
    @Transactional
    public void addStudentToCourse(Long courseId, Long studentId) {
        com.mohand.SchoolManagmentSystem.model.course.Course course = courseService.getById(courseId);
        Student student = getById(studentId);

        if (courseService.existsByIdAndStudentId(courseId, studentId)) {
            throw new ConflictException("Student already enrolled in this course");
        }

        if (cartItemRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            cartItemRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        }

        if (!teacherStudentRepository.existsByStudentIdAndTeacherId(studentId, course.getTeacher().getId())) {
            teacherStudentRepository.save(new TeacherStudent(student, course.getTeacher()));
        }

        course.getStudents().add(student);
        student.getCourses().add(course);

        courseService.save(course);
        save(student);
    }

    @Override
    public void addCoursesToStudentResponse(com.mohand.SchoolManagmentSystem.response.authentication.Student studentResponse) {
        List<StudentCourse> courses = new ArrayList<>();


        List<CartItem> cartItems = cartItemRepository.findAllByStudentId(studentResponse.getId());
        for (CartItem cartItem : cartItems) {
            StudentCourse course = modelMapper.map(cartItem.getCourse(), StudentCourse.class);
            course.setInCart(true);
            course.setFavourite(false);
            course.setEnrolled(false);
            course.setAnnouncements(null);
            TeacherPreview teacherPreview = modelMapper.map(cartItem.getCourse().getTeacher(), TeacherPreview.class);
            course.setTeacher(teacherPreview);
            courses.add(course);
        }


        List<FavoriteCourse> favoriteCourses = favoriteCourseRepository.findAllByStudentId(studentResponse.getId());
        outer:
        for (FavoriteCourse favoriteCourse : favoriteCourses) {
            for (StudentCourse course : courses) {
                if (course.getId() == favoriteCourse.getCourse().getId()) {
                    course.setFavourite(true);
                    continue outer;
                }
            }
            StudentCourse course = modelMapper.map(favoriteCourse.getCourse(), StudentCourse.class);
            course.setFavourite(true);
            course.setEnrolled(false);
            course.setAnnouncements(null);
            course.setInCart(false);
            TeacherPreview teacherPreview = modelMapper.map(favoriteCourse.getCourse().getTeacher(), TeacherPreview.class);
            course.setTeacher(teacherPreview);
            courses.add(course);
        }

        List<com.mohand.SchoolManagmentSystem.model.course.Course> enrolledCourses = courseService.getAllCoursesByStudentId(studentResponse.getId());
        outer:
        for (com.mohand.SchoolManagmentSystem.model.course.Course course : enrolledCourses) {
            for (StudentCourse coursePreview : courses) {
                if (coursePreview.getId() == course.getId()) {
                    coursePreview.setEnrolled(true);
                    coursePreview.setProgressPercentage(
                            courseService.countProgressPercentageByCourseIdAndStudentId(coursePreview.getId(), studentResponse.getId())
                    );
                    continue outer;
                }
            }
            StudentCourse coursePreview = modelMapper.map(course, StudentCourse.class);
            coursePreview.setEnrolled(true);
            coursePreview.setFavourite(false);
            coursePreview.setInCart(false);
            coursePreview.setProgressPercentage(
                    courseService.countProgressPercentageByCourseIdAndStudentId(coursePreview.getId(), studentResponse.getId())
            );
            TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
            coursePreview.setTeacher(teacherPreview);
            courses.add(coursePreview);
        }

        studentResponse.setCourses(courses);
    }


    @Override
    public void update(UpdateStudentRequest request, Long id) {
        Student student = getById(id);

        student.setHasProfilePic(request.isHasProfilePic());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());

        save(student);
    }

    @Override
    public List<StudentCourse> getAllCourses(Student student) {
        return courseService.findAll().stream().map(course -> {
            StudentCourse studentCourse = modelMapper.map(course, StudentCourse.class);

            TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
            studentCourse.setTeacher(teacherPreview);

            studentCourse.setInCart(cartItemRepository.existsByStudentIdAndCourseId(student.getId(), course.getId()));
            studentCourse.setFavourite(favoriteCourseRepository.existsByStudentIdAndCourseId(student.getId(), course.getId()));
            if (courseService.existsByIdAndStudentId(course.getId(), student.getId())) {
                studentCourse.setEnrolled(true);
                studentCourse.setProgressPercentage(
                        courseService.countProgressPercentageByCourseIdAndStudentId(course.getId(), student.getId())
                );
            } else {
                studentCourse.setEnrolled(false);
                studentCourse.setAnnouncements(null);
            }
            resourceService.addChapterToCourseResponse(studentCourse);
            return studentCourse;
        }).toList();
    }

    @Override
    public StudentCourse getCourseResponseById(Long courseId, Long studentId) {
        Course course = courseService.getById(courseId);
        StudentCourse studentCourse = modelMapper.map(course, StudentCourse.class);


        TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
        studentCourse.setTeacher(teacherPreview);

        studentCourse.setInCart(cartItemRepository.existsByStudentIdAndCourseId(studentId, course.getId()));
        studentCourse.setFavourite(favoriteCourseRepository.existsByStudentIdAndCourseId(studentId, course.getId()));
        if (courseService.existsByIdAndStudentId(course.getId(), studentId)) {
            studentCourse.setEnrolled(true);
            studentCourse.setProgressPercentage(
                    courseService.countProgressPercentageByCourseIdAndStudentId(course.getId(), studentId)
            );
        } else {
            studentCourse.setEnrolled(false);
            studentCourse.setAnnouncements(null);
        }
        resourceService.addChapterToCourseResponse(studentCourse);
        return studentCourse;
    }

}
