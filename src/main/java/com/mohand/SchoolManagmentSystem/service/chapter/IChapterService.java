package com.mohand.SchoolManagmentSystem.service.chapter;

import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.chapter.AddOrUpdateChapterRequest;
import com.mohand.SchoolManagmentSystem.request.chapter.ReorderChaptersRequest;

public interface IChapterService {

    void addOrUpdateChapter(AddOrUpdateChapterRequest request, Course course);
    Chapter findByIdAndCourseId(Long id, Long courseId);

    Chapter findByIdAndCourseIdAndTeacherId(Long chapterId,
                                                      Long courseId,
                                                      Long teacherId);

    void  reorderChapters(ReorderChaptersRequest request, Teacher teacher);

    void deleteChapter(Long courseId, Long chapterId, Teacher teacher);
}
