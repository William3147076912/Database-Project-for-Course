package com.qwq.user.mapper;

import com.qwq.user.domain.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    List<Student> selectStudentList(Student student);
    Long learningTime(Long studentId);
    List<Map<String,Object>> listStudentScore(Long studentId);
}
