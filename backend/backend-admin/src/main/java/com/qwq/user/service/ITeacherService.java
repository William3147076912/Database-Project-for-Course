package com.qwq.user.service;

import com.qwq.user.domain.Teacher;

import java.util.List;

public interface ITeacherService {
    List<Teacher> listTeacher(Teacher teacher);
    String teachingQuality(Long teacherId);
}
