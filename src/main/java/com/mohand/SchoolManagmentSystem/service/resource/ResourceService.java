package com.mohand.SchoolManagmentSystem.service.resource;

import com.mohand.SchoolManagmentSystem.exception.ConflictException;
import com.mohand.SchoolManagmentSystem.exception.NotFoundException;
import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.*;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.course.CurrentResource;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.*;
import com.mohand.SchoolManagmentSystem.request.chapter.*;
import com.mohand.SchoolManagmentSystem.request.course.UpdateActiveResourceRequest;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.chapter.ChapterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CurrentResourceRepository currentResourceRepository;

    @Override
    public void addVideo(AddVideoRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Integer maxPosition = resourceRepository.findMaxPositionByChapter(request.getCourseId(), request.getChapterId());
        int newPosition = (maxPosition != null ? maxPosition : 0) + 1;
        Video video = new Video(request.getTitle(), request.getDownloadUrl(), chapter, request.getDuration(), request.isFree(), newPosition);
        videoRepository.save(video);
    }

    @Override
    public void addDocument(AddDocumentRequest request, Teacher teacher) {
        Chapter chapter = chapterService.findByIdAndCourseIdAndTeacherId(request.getChapterId(), request.getCourseId(), teacher.getId());
        Integer maxPosition = resourceRepository.findMaxPositionByChapter(request.getCourseId(), request.getChapterId());
        int newPosition = (maxPosition != null ? maxPosition : 0) + 1;
        Document document = new Document(request.getTitle(), request.getDownloadUrl(), chapter, request.isFree(), newPosition);
        documentRepository.save(document);
    }

    @Override
    public void updateResource(UpdateResourceRequest request, Teacher teacher) {
        Resource resource = findResourceByIdAndChapterIdAndCourseIdAndTeacherId(request.getResourceId(), request.getChapterId(), request.getCourseId(), teacher.getId());
        resource.setTitle(request.getTitle());
        resource.setFree(request.isFree());
        if(resource instanceof Video video && request.getDuration() != null) {
            video.setDuration(request.getDuration());
        }
        resourceRepository.save(resource);
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


    public List<Resource> getAllResourcesByChapterId(Long chapterId) {
        return resourceRepository.findAllByChapterId(chapterId);
    }

    @Override
    public Video findVideoByIdAndChapterIdAndCourseId(Long resourceId, Long chapterId, Long courseId) {
        return videoRepository.findByIdAndChapterIdAndCourseId(resourceId, chapterId, courseId).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    @Override
    public Resource findResourceByIdAndChapterIdAndCourseIdAndTeacherId(Long resourceId, Long chapterId, Long courseId, Long teacherId) {
        return resourceRepository.findByIdAndChapterIdAndCourseIdAndTeacherId(resourceId, chapterId, courseId, teacherId).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    @Override
    public int countByCourseId(Long courseId) {
        return resourceRepository.countByCourseId(courseId);
    }

    @Override
    public void addResourcesToChapterResponse(com.mohand.SchoolManagmentSystem.response.chapter.Chapter chapter, Long courseId) {
        List<com.mohand.SchoolManagmentSystem.response.chapter.Resource> resources = getAllResourcesByChapterId(chapter.getId())
                .stream()
                .map(resource -> {
                    com.mohand.SchoolManagmentSystem.response.chapter.Resource resourceResponse = modelMapper.map(resource, com.mohand.SchoolManagmentSystem.response.chapter.Resource.class);

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = auth.getPrincipal();

                    boolean hasAccessToResource = resource.isFree();
                    hasAccessToResource = hasAccessToResource || (principal instanceof Student student && courseRepository.isStudentEnrolledInCourse(student.getId(), courseId));
                    hasAccessToResource = hasAccessToResource || (principal instanceof Teacher teacher && courseRepository.existsByIdAndTeacherId(courseId, teacher.getId()));

                    if (hasAccessToResource) {
                        resourceResponse.setDownloadUrl(azureBlobService.signBlobUrl(resource.getDownloadUrl()));
                        if (principal instanceof Student student) {
                            resourceResponse.setIsFinished(finishedResourceRepository.existsByResourceIdAndStudentId(resourceResponse.getId(), student.getId()));
                            if(resource instanceof Video video) {
                                videoProgressRepository.findByStudentIdAndVideoId(student.getId(), video.getId()).ifPresentOrElse((videoProgress) -> resourceResponse.setVideoProgress(videoProgress.getProgress()), () -> resourceResponse.setVideoProgress(0));
                            }
                        }
                    }
                    return resourceResponse;
                }).toList();
        chapter.setResources(resources);
    }

    @Override
    public void addChapterToCourseResponse(com.mohand.SchoolManagmentSystem.response.course.Course courseResponse) {

        List<com.mohand.SchoolManagmentSystem.response.chapter.Chapter> chapters = courseResponse.getChapters().stream().map((chapter -> {
            com.mohand.SchoolManagmentSystem.response.chapter.Chapter chapterResponse = modelMapper.map(chapter, com.mohand.SchoolManagmentSystem.response.chapter.Chapter.class);

            addResourcesToChapterResponse(chapter, courseResponse.getId());

            courseResponse.setNumberOfDocuments(courseResponse.getNumberOfDocuments() + documentRepository.countAllByChapterId(chapter.getId()));
            courseResponse.setNumberOfVideos(courseResponse.getNumberOfVideos() + videoRepository.countAllByChapterId(chapter.getId()));

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

        Optional<VideoProgress> optionalProgress = videoProgressRepository.findByStudentIdAndVideoId(student.getId(), video.getId());

        VideoProgress progress = optionalProgress.orElse(new VideoProgress(student, video));
        progress.setProgress(request.getProgress());

        videoProgressRepository.save(progress);

    }

    public void updateActiveResource(UpdateActiveResourceRequest request, Student student) {
        if (!courseRepository.isStudentEnrolledInCourse(student.getId(), request.getCourseId())) {
            throw new ResourceNotFoundException("Course not found");
        }

        Resource resource = findByIdAndChapterIdAndCourseId(request.getResourceId(), request.getChapterId(), request.getCourseId());

        Optional<CurrentResource> optionalCurrentResource = currentResourceRepository.findByStudentIdAndResourceIdAndCourseId(student.getId(), request.getResourceId(), request.getCourseId());

        CurrentResource currentResource = optionalCurrentResource.orElse(new CurrentResource(student, courseRepository.findById(request.getCourseId()).get(), resource));


        currentResourceRepository.save(currentResource);
    }

    @Override
    @Transactional
    public void reorderResources(ReorderResourcesRequest request, Teacher teacher) {
        for (int i = 0; i < request.getOrderedResourceIds().size(); i++) {
            Resource resource = resourceRepository.
                    findByIdAndChapterIdAndCourseIdAndTeacherId(request.getOrderedResourceIds().get(i), request.getChapterId() ,request.getCourseId(), teacher.getId())
                    .orElseThrow(() -> new NotFoundException("resource not found"));
            resource.setPosition(i);
            resourceRepository.save(resource);
        }
    }

    @Override
    public void deleteResource(Long courseId, Long chapterId, Long resourceId, Teacher teacher) {

        Resource resourceToDelete = resourceRepository.
                findByIdAndChapterIdAndCourseIdAndTeacherId(resourceId, chapterId, courseId, teacher.getId())
                .orElseThrow(() -> new NotFoundException("resource not found"));

        int deletedPosition = resourceToDelete.getPosition();
        Course course = resourceToDelete.getChapter().getCourse();
        Chapter chapter = resourceToDelete.getChapter();


        resourceRepository.delete(resourceToDelete);

        List<Resource> resourcesToShift = resourceRepository
                .findByCourseAndChapterAndPositionGreaterThanOrderByPositionAsc(course, chapter, deletedPosition);

        for (Resource r : resourcesToShift) {
            r.setPosition(r.getPosition() - 1);
        }

        resourceRepository.saveAll(resourcesToShift);
    }

}
