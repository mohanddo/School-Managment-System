package com.mohand.SchoolManagmentSystem.service.chapter;


import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.repository.ChapterRepository;
import com.mohand.SchoolManagmentSystem.repository.CourseRepository;
import com.mohand.SchoolManagmentSystem.request.chapter.AddOrUpdateChapterRequest;
import com.mohand.SchoolManagmentSystem.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChapterService implements IChapterService {

    private final ChapterRepository chapterRepository;

    @Override
    public void addOrUpdateChapter(AddOrUpdateChapterRequest request, Course course) {

        Chapter chapter;

        if (request.getChapterId() == null) {
            chapter = new Chapter(request.getTitle(), course);
        } else {
            chapter = findByIdAndCourseId(request.getChapterId(), request.getCourseId());
            chapter.setTitle(request.getTitle());
        }

        chapterRepository.save(chapter);
    }

    @Override
    public Chapter findByIdAndCourseId(Long id, Long courseId) {
        return chapterRepository
                .findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
    }

    @Override
    public Chapter findByIdAndCourseIdAndTeacherId(Long chapterId, Long courseId, Long teacherId) {
        return chapterRepository
                .findByIdAndCourseIdAndTeacherId(chapterId, courseId, teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
    }


}
