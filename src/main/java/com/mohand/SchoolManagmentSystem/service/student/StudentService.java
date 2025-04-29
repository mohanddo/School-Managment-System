package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import com.mohand.SchoolManagmentSystem.model.course.FavoriteCourse;
import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.response.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.response.chapter.Document;
import com.mohand.SchoolManagmentSystem.response.chapter.Video;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
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
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;
    private final TeacherStudentRepository teacherStudentRepository;
    private final AzureBlobService azureBlobService;
    private final FinishedResourceRepository finishedResourceRepository;

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
            addChaptersToStudentCourse(studentCourse, student.getId());
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
            course.setInCart(false);
            courses.add(course);
        }

        List<com.mohand.SchoolManagmentSystem.model.course.Course> enrolledCourses = courseService.getAllCoursesByStudentId(studentResponse.getId());
        outer:
        for (com.mohand.SchoolManagmentSystem.model.course.Course course : enrolledCourses) {
            for (StudentCourse coursePreview : courses) {
                if (coursePreview.getId() == course.getId()) {
                    coursePreview.setEnrolled(true);
                    coursePreview.setProgressPercentage(
                            resourceService.countProgressPercentageByCourseIdAndStudentId(coursePreview.getId(), studentResponse.getId())
                    );
                    continue outer;
                }
            }
            StudentCourse coursePreview = modelMapper.map(course, StudentCourse.class);
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

    @Override
    public void addChaptersToStudentCourse(StudentCourse studentCourse, Long studentId) {
        List<Chapter> chapters = studentCourse.getChapters().stream().map((chapter -> {
            Chapter chapterResponse = modelMapper.map(chapter, Chapter.class);

            List<Video> videos = resourceService.getAllVideosByChapterId(chapter.getId())
                    .stream()
                    .map(video -> {
                        Video videoResponse = modelMapper.map(video, Video.class);
                        videoResponse.setDownloadUrl(azureBlobService.signBlobUrl(video.getDownloadUrl(), true));
                        if (finishedResourceRepository.existsByResourceIdAndStudentId(videoResponse.getId(), studentId)) {
                            videoResponse.setIsFinished(true);
                        } else {
                            videoResponse.setIsFinished(false);
                        }
                        return videoResponse;
                    }).toList();
            chapter.setVideos(videos);

            List<Document> documents = resourceService.getAllDocumentsByChapterId(chapter.getId())
                    .stream()
                    .map(document -> {
                        Document documentResponse = modelMapper.map(document, Document.class);
                        documentResponse.setDownloadUrl(azureBlobService.signBlobUrl(document.getDownloadUrl(), true));
                        if (finishedResourceRepository.existsByResourceIdAndStudentId(documentResponse.getId(), studentId)) {
                            documentResponse.setIsFinished(true);
                        } else {
                            documentResponse.setIsFinished(false);
                        }
                        return documentResponse;
                    }).toList();
            chapter.setDocuments(documents);

            return chapterResponse;
        })).toList();

        studentCourse.setChapters(chapters);
    }

}
