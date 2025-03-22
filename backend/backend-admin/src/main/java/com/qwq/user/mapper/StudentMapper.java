package com.qwq.user.mapper;

import com.qwq.user.domain.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    Long studentLearningTime();
    List<Student> selectStudentList(Student student);
}
