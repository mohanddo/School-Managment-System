package com.mohand.SchoolManagmentSystem.service.resource;

import com.mohand.SchoolManagmentSystem.model.chapter.Document;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.AddDocumentRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.AddVideoRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.UpdateDocumentRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.UpdateVideoRequest;
import com.mohand.SchoolManagmentSystem.response.chapter.Chapter;
import org.springframework.data.repository.query.Param;

import java.io.DataInput;
import java.util.List;
import java.util.Optional;

public interface IResourceService {
    void addVideo(AddVideoRequest request, Teacher teacher);
    void addDocument(AddDocumentRequest request, Teacher teacher);

    void updateVideo(UpdateVideoRequest request, Teacher teacher);
    void updateDocument(UpdateDocumentRequest request, Teacher teacher);

    void addOrDeleteFinishedResource(Long resourceId, Long chapterId, Long courseId , Student student);

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

    int countByCourseId(Long courseId);

    int addVideosToChapterResponse(Chapter chapter, Long studentId, boolean hasAccessToResource);
    int addDocumentsToChapterResponse(Chapter chapter, Long studentId, boolean hasAccessToResource);

    void addChapterToCourseResponse(com.mohand.SchoolManagmentSystem.response.course.Course courseResponse, Long studentId);
}
