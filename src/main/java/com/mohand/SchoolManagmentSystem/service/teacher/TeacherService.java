package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.TeacherRepository;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.request.user.UpdateTeacherRequest;
import com.mohand.SchoolManagmentSystem.response.course.Announcement;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.response.user.TeacherPreview;
import com.mohand.SchoolManagmentSystem.service.course.CourseService;
import com.mohand.SchoolManagmentSystem.service.payment.ChargilyPayService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final ChargilyPayService chargilyPayService;


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
            TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
            teacherCourse.setTeacher(teacherPreview);
            if (courseService.existsByIdAndTeacherId(course.getId(), teacher.getId())) {
                List<Announcement> announcements = course.getAnnouncements().stream()
                        .map(a -> modelMapper.map(a, Announcement.class))
                        .collect(Collectors.toList());
                teacherCourse.setAnnouncements(announcements);
            }

            return teacherCourse;
        }).toList();

        teacherResponse.setCourses(teacherCourseList);
        return teacherResponse;
    }

    @Override
    public List<TeacherCourse> getAllCourses(Teacher teacher) {
        return courseService.findAll().stream().map(course -> {
            TeacherCourse teacherCourse = modelMapper.map(course, TeacherCourse.class);

            TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
            teacherCourse.setTeacher(teacherPreview);

            if (courseService.existsByIdAndTeacherId(course.getId(), teacher.getId())) {
                List<Announcement> announcements = course.getAnnouncements().stream()
                        .map(a -> modelMapper.map(a, Announcement.class))
                        .collect(Collectors.toList());
                teacherCourse.setAnnouncements(announcements);
            }


            return teacherCourse;
        }).toList();
    }

    @Override
    public TeacherCourse getCourseResponseById(Long courseId, Long teacherId) {
        Course course = courseService.getById(courseId);
        TeacherCourse teacherResponse = modelMapper.map(course, TeacherCourse.class);

        TeacherPreview teacherPreview = modelMapper.map(course.getTeacher(), TeacherPreview.class);
        teacherResponse.setTeacher(teacherPreview);


        if (courseService.existsByIdAndTeacherId(course.getId(), teacherId)) {
            List<Announcement> announcements = course.getAnnouncements().stream()
                    .map(a -> modelMapper.map(a, Announcement.class))
                    .collect(Collectors.toList());
            teacherResponse.setAnnouncements(announcements);
        }

        return teacherResponse;
    }

    @Override
    public void update(UpdateTeacherRequest request, Long id) {

            Teacher teacher = readById(id);

            teacher.setHasProfilePic(request.isHasProfilePic());
            teacher.setFirstName(request.getFirstName());
            teacher.setLastName(request.getLastName());
            teacher.setDescription(request.getDescription());
            teacher.setFacebookLink(request.getFacebookLink());
            teacher.setYoutubeLink(request.getYoutubeLink());
            teacher.setInstagramLink(request.getInstagramLink());

            save(teacher);

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
