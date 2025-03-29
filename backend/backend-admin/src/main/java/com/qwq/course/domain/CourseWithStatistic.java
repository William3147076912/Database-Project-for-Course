package com.qwq.course.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseWithStatistic extends Course{
    public Long enrollmentCount;
    public int completionCount = 0;
    public CourseWithStatistic(Course course,Long enrollmentCount,int completionCount){
        this.setCourseId(course.getCourseId());
        this.setName(course.getName());
        this.setDescription(course.getDescription());
        this.setContent(course.getContent());
        this.setSyllabus(course.getSyllabus());
        this.setStatus(course.getStatus());
        this.setSubject(course.getSubject());
        this.setReviewerId(course.getReviewerId());
        this.setCreatorId(course.getCreatorId());
        this.setEnrollmentCount(enrollmentCount);
        this.setCompletionCount(completionCount);
    }
}
