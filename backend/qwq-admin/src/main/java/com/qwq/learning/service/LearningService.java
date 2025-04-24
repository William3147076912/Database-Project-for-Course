package com.qwq.learning.service;

import com.qwq.learning.domain.Learning;

import java.util.List;

public interface LearningService {
    List<Learning> getLearningProgressAndPlanByStudentId(Integer studentId, String courseName);
    boolean updateLearningPlan(Integer studentId, Integer courseId, String plan);
}