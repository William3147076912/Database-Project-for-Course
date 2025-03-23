package com.qwq.user.mapper;

import com.qwq.user.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    Long teacherCount();

    Long courseCount(Long teacherId);

    List<Teacher> listTeacher(Teacher teacher);
}
