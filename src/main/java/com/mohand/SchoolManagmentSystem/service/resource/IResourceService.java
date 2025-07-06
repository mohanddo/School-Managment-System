package com.mohand.SchoolManagmentSystem.service.resource;

import com.mohand.SchoolManagmentSystem.model.chapter.Document;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.*;
import com.mohand.SchoolManagmentSystem.request.course.UpdateActiveResourceRequest;
import com.mohand.SchoolManagmentSystem.response.chapter.Chapter;
import org.springframework.data.repository.query.Param;

import java.io.DataInput;
import java.util.List;
import java.util.Optional;

public interface IResourceService {
    void addVideo(AddVideoRequest request, Teacher teacher);
    void addDocument(AddDocumentRequest request, Teacher teacher);

    void updateResource(UpdateResourceRequest request, Teacher teacher);
    Resource findResourceByIdAndChapterIdAndCourseIdAndTeacherId(Long resourceId, Long chapterId, Long courseId, Long teacherId);


    void addFinishedResource(Long resourceId, Long chapterId, Long courseId , Student student);
    void deleteFinishedResource(Long resourceId, Student student);

    Video findVideoByIdAndChapterIdAndCourseIdAndTeacherId(Long videoId,
                                                           Long chapterId,
                                                           Long courseId,
                                                           Long teacherId);

    Document findDocumentByIdAndChapterIdAndCourseIdAndTeacherId(Long documentId,
                                                         Long chapterId,
                                                         Long courseId,
                                                         Long teacherId);

    Resource findByIdAndChapterIdAndCourseId(Long resourceId, Long chapterId, Long courseId);

    List<Video> getAllVideosByChapterId(Long chapterId);
    List<Document> getAllDocumentsByChapterId(Long chapterId);

    Video findVideoByIdAndChapterIdAndCourseId(Long resourceId, Long chapterId, Long courseId);

    int countByCourseId(Long courseId);

    void updateVideoProgress(Student student, UpdateVideoProgressRequest request);

    void updateActiveResource(UpdateActiveResourceRequest request, Student student);

    void reorderResources(ReorderResourcesRequest request, Teacher teacher);

    void deleteResource(Long courseId, Long chapterId, Long resourceId, Teacher teacher);
}
