package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public Teacher readTeacherById(Long id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("No teacher with this id: " + id));

    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
}
