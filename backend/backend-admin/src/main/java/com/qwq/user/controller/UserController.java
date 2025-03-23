package com.qwq.user.controller;


import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.page.TableDataInfo;
import com.qwq.user.domain.Student;
import com.qwq.user.domain.Teacher;
import com.qwq.user.service.IStudentService;
import com.qwq.user.service.ITeacherService;
import com.qwq.user.service.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userStatistics")
public class UserController extends BaseController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IStudentService studentService;
    @GetMapping("/listTeacher")
    public TableDataInfo listTeacher(Teacher teacher){
        List<Teacher> teachers = teacherService.listTeacher(teacher);
        return getDataTable(teachers);
    }
    @GetMapping("/listStudent")
    public TableDataInfo student(Student student){
        List<Student> students = studentService.listStudent(student);
        return getDataTable(students);
    }
}
