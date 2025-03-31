package com.qwq.user.service;

import com.qwq.user.domain.Student;

import java.util.List;
import java.util.Map;

public interface IStudentService {
    List<Student> listStudent(Student student);
    Long LearningTime(Long studentId);
    List<Map<String,Object>> listStudentScore(Long studentId);
}
