package com.qwq.user.controller;


import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.page.TableDataInfo;
import com.qwq.user.domain.Teacher;
import com.qwq.user.service.ITeacherService;
import com.qwq.user.service.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/userStatistics")
public class UserController extends BaseController {
    @Autowired
    private ITeacherService teacherService;
    @GetMapping("/teacher")
    public Long teacher(){

        Long teacherCount = teacherService.teacherCount();
        return teacherCount;
    }
    @GetMapping("/listTeacher")
    public TableDataInfo listTeacher(){
        List<Teacher> teachers = teacherService.listTeacher();
        return getDataTable(teachers);
    }
    @GetMapping("/student")
    public String student(){
        return "success";
    }
}
