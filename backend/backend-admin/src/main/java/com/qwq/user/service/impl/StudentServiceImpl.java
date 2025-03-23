package com.qwq.user.service.impl;

import com.qwq.common.annotation.Anonymous;
import com.qwq.user.domain.Student;
import com.qwq.user.mapper.StudentMapper;
import com.qwq.user.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    StudentMapper studentMapper;
    @Override
    public List<Student> listStudent(Student student) {
        List<Student> students=studentMapper.selectStudentList(student);
        for (Student student1 : students)
        {
            student1.setLearningTime(studentMapper.studentLearningTime());
        }
        return studentMapper.selectStudentList(student);
    }
    @Override
    public int LearningTime(Long studentId) {

        return 999;
    }
}
