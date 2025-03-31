package com.qwq.learning.domain;

import lombok.Data;

@Data
public class Learning {
    private Integer studentId;
    private Integer courseId;
    private String courseName;
    private Integer totalAssignments;
    private Integer submittedAssignments;
    private String plan;

    public Learning(Integer studentId, Integer courseId, String courseName, Integer totalAssignments, Integer submittedAssignments, String plan) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalAssignments = totalAssignments;
        this.submittedAssignments = submittedAssignments;
        this.plan = plan;
    }

    // 无参构造函数
    public Learning() {
    }
}    