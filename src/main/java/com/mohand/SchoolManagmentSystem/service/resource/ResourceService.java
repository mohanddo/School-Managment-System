package com.mohand.SchoolManagmentSystem.service.resource;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.NotFoundException;
import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.*;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.chapter.*;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.chapter.ChapterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceService implements IResourceService {

    private final VideoRepository videoRepository;
    private final ChapterService chapterService;
    private final DocumentRepository documentRepository;
    private final ResourceRepository resourceRepository;
    private final CourseRepository courseRepository;
    private final FinishedResourceRepository finishedResourceRepository;
    private final AzureBlobService azureBlobService;
    private final ModelMapper modelMapper;
    private final VideoProgressRepository videoProgressRepository;

    @Override
    public void addVideo(AddVideoRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Video video = new Video(request.getTitle(), request.getDownloadUrl(), chapter, request.getDuration(), request.isFree());
        videoRepository.save(video);
    }

    @Override
    public void addDocument(AddDocumentRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Document document = new Document(request.getTitle(), request.getDownloadUrl(), chapter, request.isFree());
        documentRepository.save(document);
    }

    @Override
    public void updateVideo(UpdateVideoRequest request, Teacher teacher) {
        Video video = findVideoByIdAndChapterIdAndCourseIdAndTeacherId(request.getVideoId(), request.getChapterId(), request.getCourseId(), teacher.getId());
        video.setDuration(request.getDuration());
        video.setTitle(request.getTitle());
        video.setFree(request.isFree());
        videoRepository.save(video);
    }

    @Override
    public void updateDocument(UpdateDocumentRequest request, Teacher teacher) {
        Document document = findDocumentByIdAndChapterIdAndCourseIdAndTeacherId(request.getDocumentId(), request.getChapterId(), request.getCourseId(), teacher.getId());
        document.setTitle(request.getTitle());
        document.setFree(request.isFree());
        documentRepository.save(document);
    }

    @Override
    @Transactional
    public void addFinishedResource(Long resourceId, Long chapterId, Long courseId, Student student) {
        if (!courseRepository.isStudentEnrolledInCourse(student.getId(), courseId))
            throw new ResourceNotFoundException("Course not found");

        Resource resource = findByIdAndChapterIdAndCourseId(resourceId, chapterId, courseId);

        if (finishedResourceRepository.existsByResourceIdAndStudentId(resourceId, student.getId())) {
            throw new ConflictException("Resource already finished");
        } else {
            FinishedResource finishedResource = new FinishedResource(student, resource);
            finishedResourceRepository.save(finishedResource);
        }
    }

    @Override
    @Transactional
    public void deleteFinishedResource(Long resourceId, Student student) {
        if (finishedResourceRepository.existsByResourceIdAndStudentId(resourceId, student.getId())) {
            finishedResourceRepository.deleteByResourceIdAndStudentId(resourceId, student.getId());
        } else {
            throw new NotFoundException("Resource not finished");
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
    public Video findVideoByIdAndChapterIdAndCourseId(Long resourceId, Long chapterId, Long courseId) {
        return videoRepository.findByIdAndChapterIdAndCourseId(resourceId, chapterId, courseId).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    @Override
    public int countByCourseId(Long courseId) {
        return resourceRepository.countByCourseId(courseId);
    }

    @Override
    public int  addVideosToChapterResponse(com.mohand.SchoolManagmentSystem.response.chapter.Chapter chapter, Long studentId, Long courseId) {
        List<com.mohand.SchoolManagmentSystem.response.chapter.Video> videos = getAllVideosByChapterId(chapter.getId())
                .stream()
                .map(video -> {
                    com.mohand.SchoolManagmentSystem.response.chapter.Video videoResponse = modelMapper.map(video, com.mohand.SchoolManagmentSystem.response.chapter.Video.class);

                    boolean hasAccessToResource = (studentId != null && courseRepository.isStudentEnrolledInCourse(studentId, courseId))
                            || video.isFree();

                    if (hasAccessToResource) {
                        videoResponse.setDownloadUrl(azureBlobService.signBlobUrl(video.getDownloadUrl()));
                        if (studentId != null) {
                            videoResponse.setIsFinished(finishedResourceRepository.existsByResourceIdAndStudentId(videoResponse.getId(), studentId));
                        }
                    }
                    return videoResponse;
                }).toList();
        chapter.setVideos(videos);
        return videos.size();
    }

    @Override
    public int addDocumentsToChapterResponse(com.mohand.SchoolManagmentSystem.response.chapter.Chapter chapter, Long studentId, Long courseId) {
        List<com.mohand.SchoolManagmentSystem.response.chapter.Document> documents = getAllDocumentsByChapterId(chapter.getId())
                .stream()
                .map(document -> {
                    com.mohand.SchoolManagmentSystem.response.chapter.Document documentResponse = modelMapper.map(document, com.mohand.SchoolManagmentSystem.response.chapter.Document.class);

                    boolean hasAccessToResource = (studentId != null && courseRepository.isStudentEnrolledInCourse(studentId, courseId))
                            || document.isFree();

                    if (hasAccessToResource) {
                        documentResponse.setDownloadUrl(azureBlobService.signBlobUrl(document.getDownloadUrl()));
                        if (studentId != null) {
                            documentResponse.setIsFinished(finishedResourceRepository.existsByResourceIdAndStudentId(documentResponse.getId(), studentId));
                        }
                    }
                    return documentResponse;
                }).toList();
        chapter.setDocuments(documents);
        return documents.size();
    }

    @Override
    public void addChapterToCourseResponse(com.mohand.SchoolManagmentSystem.response.course.Course courseResponse, Long studentId) {

        List<com.mohand.SchoolManagmentSystem.response.chapter.Chapter> chapters = courseResponse.getChapters().stream().map((chapter -> {
            com.mohand.SchoolManagmentSystem.response.chapter.Chapter chapterResponse = modelMapper.map(chapter, com.mohand.SchoolManagmentSystem.response.chapter.Chapter.class);

            int numberOfVideosInChapter = addVideosToChapterResponse(chapter, studentId, courseResponse.getId());

            int numberOfDocumentsInChapter = addDocumentsToChapterResponse(chapter, studentId, courseResponse.getId());

            courseResponse.setNumberOfDocuments(courseResponse.getNumberOfDocuments() + numberOfDocumentsInChapter);
            courseResponse.setNumberOfVideos(courseResponse.getNumberOfVideos() + numberOfVideosInChapter);

            return chapterResponse;
        })).toList();

        courseResponse.setChapters(chapters);
    }

    public void updateVideoProgress(Student student, UpdateVideoProgressRequest request) {
        if (!courseRepository.isStudentEnrolledInCourse(student.getId(), request.getCourseId()))
            throw new ResourceNotFoundException("Course not found");

        Video video = findVideoByIdAndChapterIdAndCourseId(request.getVideoId(), request.getChapterId(), request.getCourseId());


        if (request.getProgress() > video.getDuration()) {
            throw new IllegalArgumentException("Progress cannot exceed video duration");
        }

        Optional<VideoProgress> optionalProgress = videoProgressRepository.findByStudentAndVideo(student, video);

        VideoProgress progress = optionalProgress.orElse(new VideoProgress(student, video));
        progress.setProgress(request.getProgress());

        videoProgressRepository.save(progress);

    }
}
