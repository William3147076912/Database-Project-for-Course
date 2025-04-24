package com.qwq.learning.service.impl;

import com.qwq.learning.domain.Learning;
import com.qwq.learning.mapper.LearningMapper;
import com.qwq.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningServiceImpl implements LearningService {

    @Autowired
    private LearningMapper learningMapper;

    @Override
    public List<Learning> getLearningProgressAndPlanByStudentId(Integer studentId, String courseName) {
        return learningMapper.getLearningProgressAndPlanByStudentId(studentId, courseName);
    }

    @Override
    public boolean updateLearningPlan(Integer studentId, Integer courseId, String plan) {
        int rowsAffected = learningMapper.updateLearningPlan(studentId, courseId, plan);
        return rowsAffected > 0;
    }
}