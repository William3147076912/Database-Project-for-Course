package com.qwq.user.service;

import com.qwq.user.domain.Teacher;

import java.util.List;

public interface ITeacherService {
    Long courseCount(Long teacherId);
    Long teacherCount();
    List<Teacher> listTeacher();
}
