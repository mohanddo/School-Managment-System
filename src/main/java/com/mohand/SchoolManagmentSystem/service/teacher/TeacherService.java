package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.TeacherRepository;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.chapter.ChapterService;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.payment.ChargilyPayService;
import com.mohand.SchoolManagmentSystem.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final ChargilyPayService chargilyPayService;
    private final ResourceService resourceService;

    @Override
    public Teacher readById(Long id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getByEmail(String email) {
        return teacherRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public boolean checkIfExistById(Long id) {
        return teacherRepository.existsById(id);
    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Teacher me(Authentication authentication) {
        Teacher teacher = (Teacher) authentication.getPrincipal();
        com.mohand.SchoolManagmentSystem.response.authentication.Teacher teacherResponse = modelMapper
                .map(teacher, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class);

        List<Course> courses = courseService.getAllCoursesByTeacherId(teacher.getId());
        List<TeacherCourse> teacherCourseList = courses.stream().map(course -> {
            TeacherCourse teacherCourse = modelMapper.map(course, TeacherCourse.class);
            resourceService.addChapterToCourseResponse(teacherCourse, null);
            if (!courseService.existsByIdAndTeacherId(course.getId(), teacher.getId())) {
                teacherCourse.setAnnouncements(null);
            }
            resourceService.addChapterToCourseResponse(teacherCourse, null);
            return teacherCourse;
        }).toList();

        teacherResponse.setCourses(teacherCourseList);
        return teacherResponse;
    }

    @Override
    public List<TeacherCourse> getAllCourses(Teacher teacher) {
        return courseService.findAll().stream().map(course -> {
            TeacherCourse teacherCourse = modelMapper.map(course, TeacherCourse.class);
            teacherCourse.setOwnsCourse(courseService.existsByIdAndTeacherId(course.getId(), teacher.getId()));
            if (!courseService.existsByIdAndTeacherId(course.getId(), teacher.getId())) {
                teacherCourse.setAnnouncements(null);
            }
            resourceService.addChapterToCourseResponse(teacherCourse, null);
            return teacherCourse;
        }).toList();
    }

    @Override
    public TeacherCourse getCourseResponseById(Long courseId, Long teacherId) {
        Course course = courseService.getById(courseId);
        TeacherCourse teacherResponse = modelMapper.map(course, TeacherCourse.class);
        teacherResponse.setOwnsCourse(courseService.existsByIdAndTeacherId(course.getId(),teacherId));
        if (!courseService.existsByIdAndTeacherId(course.getId(), teacherId)) {
            teacherResponse.setAnnouncements(null);
        }
        resourceService.addChapterToCourseResponse(teacherResponse, null);
        return teacherResponse;
    }

    @Override
    public boolean checkIfExistByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }

    @Transactional
    public void create(CreateCourseRequest request, Teacher teacher) {

        com.mohand.SchoolManagmentSystem.model.course.Course course = com.mohand.SchoolManagmentSystem.model.course.Course.builder(request.getTitle(), request.getDescription(), request.getPrice(), request.getPricingModelEnum(), request.getCategoryEnum(), teacher, request.getImageUrl(), request.getIntroductionVideoUrl())
                .build();

        String priceId = chargilyPayService.createProduct(course);
        course.setPriceId(priceId);

        teacher.setNumberOfCourses(teacher.getNumberOfCourses() + 1);
        courseService.save(course);
        teacherRepository.save(teacher);
    }
}
