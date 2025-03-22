package com.qwq.user.service.impl;

import com.qwq.user.domain.Teacher;
import com.qwq.user.mapper.TeacherMapper;
import com.qwq.user.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements ITeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Long courseCount(Long teacherId) {
        Long courseCount = teacherMapper.courseCount(teacherId);
        return 0L;
    }
    @Override
    public Long teacherCount() {
        return teacherMapper.teacherCount();
    }
    @Override
    public List<Teacher> listTeacher() {

        List<Teacher> teachers=teacherMapper.listTeacher();
        return teachers;
    }
}
