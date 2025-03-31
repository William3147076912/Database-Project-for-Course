package com.qwq.learning.controller;

import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.page.TableDataInfo;
import com.qwq.learning.domain.Learning;
import com.qwq.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class LearningController extends BaseController {

    @Autowired
    private LearningService learningService;

    @GetMapping("/listProgress/{studentId}")
    public TableDataInfo getLearningProgressAndPlanByStudentId(@PathVariable Integer studentId,
                                                               @RequestParam(required = false) String courseName) {
        startPage();
        List<Learning> list = learningService.getLearningProgressAndPlanByStudentId(studentId, courseName);
        return getDataTable(list);
    }

    @PostMapping("/updatePlan")
    public boolean updateLearningPlan(@RequestParam Integer studentId, @RequestParam Integer courseId, @RequestParam String plan) {
        System.out.println("will" + studentId + ", courseId: " + courseId + ", plan: " + plan);
        return learningService.updateLearningPlan(studentId, courseId, plan);
    }
}