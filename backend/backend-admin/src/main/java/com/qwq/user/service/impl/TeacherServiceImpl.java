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
    public List<Teacher> listTeacher(Teacher teacher) {

        List<Teacher> teachers=teacherMapper.listTeacher(teacher);
        for (Teacher teacher1 : teachers)
        {
            teacher1.setTeachingQuality(teachingQuality(teacher1.getUserId()));
        }
        return teachers;
    }
    @Override
    public String teachingQuality(Long teacherId){

        return "良好";
    }
}
