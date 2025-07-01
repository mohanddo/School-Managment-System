package com.mohand.SchoolManagmentSystem.service.chapter;


import com.mohand.SchoolManagmentSystem.exception.NotFoundException;
import com.mohand.SchoolManagmentSystem.exception.ResourceNotFoundException;
import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.repository.ChapterRepository;
import com.mohand.SchoolManagmentSystem.request.chapter.AddOrUpdateChapterRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.ReorderChaptersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChapterService implements IChapterService {

    private final ChapterRepository chapterRepository;

    @Override
    public void addOrUpdateChapter(AddOrUpdateChapterRequest request, Course course) {

        Chapter chapter;

        if (request.getChapterId() == null) {
            Integer maxPosition = chapterRepository.findMaxPositionByCourse(course);
            int newPosition = (maxPosition != null ? maxPosition : 0) + 1;
            chapter = new Chapter(request.getTitle(), course, newPosition);
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

    @Override
    @Transactional
    public void reorderChapters(ReorderChaptersRequest request, Teacher teacher) {
        for (int i = 0; i < request.getOrderedChapterIds().size(); i++) {
            Chapter chapter = chapterRepository.
                    findByIdAndCourseIdAndTeacherId(request.getOrderedChapterIds().get(i), request.getCourseId(), teacher.getId())
                    .orElseThrow(() -> new NotFoundException("Chapter not found"));
            chapter.setPosition(i);
            chapterRepository.save(chapter);
        }
    }

    @Override
    public void deleteChapter(Long courseId, Long chapterId, Teacher teacher) {

            Chapter chapterToDelete = chapterRepository.
                    findByIdAndCourseIdAndTeacherId(chapterId, courseId, teacher.getId())
                    .orElseThrow(() -> new NotFoundException("Chapter not found"));

            int deletedPosition = chapterToDelete.getPosition();
            Course course = chapterToDelete.getCourse();


            chapterRepository.delete(chapterToDelete);

            List<Chapter> chaptersToShift = chapterRepository
                    .findByCourseAndPositionGreaterThanOrderByPositionAsc(course, deletedPosition);

            for (Chapter c : chaptersToShift) {
                c.setPosition(c.getPosition() - 1);
            }

            chapterRepository.saveAll(chaptersToShift);
    }


}
