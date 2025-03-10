package com.qwq.enrollment.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 选课记录对象 course_enrollment
 *
 * @author william
 * @date 2025-03-09
 */
@Setter
@Getter
public class CourseEnrollment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 选课记录ID
     */
    private Long enrollmentId;

    /**
     * 选课学生
     */
    @Excel(name = "选课学生")
    private Long studentId;

    /**
     * 选修的课程ID
     */
    @Excel(name = "选修的课程ID")
    private Long courseId;

    /**
     * 学生选课时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "学生选课时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date enrollmentTime;

    /**
     * 选课状态
     */
    @Excel(name = "选课状态")
    private String status;

    /**
     * 课程完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "课程完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("enrollmentId", getEnrollmentId())
                .append("studentId", getStudentId())
                .append("courseId", getCourseId())
                .append("enrollmentTime", getEnrollmentTime())
                .append("status", getStatus())
                .append("completionTime", getCompletionTime())
                .toString();
    }
}
