package com.mohand.SchoolManagmentSystem.service.resource;

import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.*;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.chapter.AddDocumentRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.AddVideoRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.UpdateDocumentRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.UpdateVideoRequest;
import com.mohand.SchoolManagmentSystem.service.chapter.ChapterService;
import com.mohand.SchoolManagmentSystem.service.course.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService implements IResourceService {

    private final VideoRepository videoRepository;
    private final ChapterService chapterService;
    private final DocumentRepository documentRepository;
    private final ResourceRepository resourceRepository;
    private final CourseRepository courseRepository;
    private final FinishedResourceRepository finishedResourceRepository;

    @Override
    public void addVideo(AddVideoRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Video video = new Video(request.getTitle(), request.getDownloadUrl(), chapter, request.getDuration());
        videoRepository.save(video);
    }

    @Override
    public void addDocument(AddDocumentRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Document document = new Document(request.getTitle(), request.getDownloadUrl(), chapter);
        documentRepository.save(document);
    }

    @Override
    public void updateVideo(UpdateVideoRequest request, Teacher teacher) {
        Video video = findVideoByIdAndChapterIdAndCourseIdAndTeacherId(request.getVideoId(), request.getChapterId(), request.getCourseId(), teacher.getId());
        video.setDuration(request.getDuration());
        video.setTitle(request.getTitle());
        videoRepository.save(video);
    }

    @Override
    public void updateDocument(UpdateDocumentRequest request, Teacher teacher) {
        Document document = findDocumentByIdAndChapterIdAndCourseIdAndTeacherId(request.getDocumentId(), request.getChapterId(), request.getCourseId(), teacher.getId());
        document.setTitle(request.getTitle());
        documentRepository.save(document);
    }

    @Override
    @Transactional
    public void addOrDeleteFinishedResource(Long resourceId, Long chapterId, Long courseId, Student student) {
        if (!courseRepository.isStudentEnrolledInCourse(courseId, student.getId()))
            throw new ResourceNotFoundException("Course not found");

        Resource resource = findByIdAndChapterIdAndCourseId(resourceId, chapterId, courseId);

        if (finishedResourceRepository.existsByResourceIdAndStudentId(resourceId, student.getId())) {
            finishedResourceRepository.deleteByResourceIdAndStudentId(resourceId, student.getId());
        } else {
            FinishedResource finishedResource = new FinishedResource(student, resource);
            finishedResourceRepository.save(finishedResource);
        }
    }


    @Override
    public Video findVideoByIdAndChapterIdAndCourseIdAndTeacherId(Long videoId, Long chapterId, Long courseId, Long teacherId) {
        return videoRepository
                .findByIdAndChapterIdAndCourseIdAndTeacherId(videoId, chapterId, courseId, teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    @Override
    public Document findDocumentByIdAndChapterIdAndCourseIdAndTeacherId(Long documentId, Long chapterId, Long courseId, Long teacherId) {
        return documentRepository
                .findByIdAndChapterIdAndCourseIdAndTeacherId(documentId, chapterId, courseId, teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public Resource findByIdAndChapterIdAndCourseId(Long resourceId, Long chapterId, Long courseId) {
        return resourceRepository.findByIdAndChapterIdAndCourseId(resourceId, chapterId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @Override
    public List<Video> getAllVideosByChapterId(Long chapterId) {
        return videoRepository.findAllByChapterId(chapterId);
    }

    @Override
    public List<Document> getAllDocumentsByChapterId(Long chapterId) {
        return documentRepository.findAllByChapterId(chapterId);
    }

    @Override
    public int countByCourseId(Long courseId) {
        return resourceRepository.countByCourseId(courseId);
    }
}
