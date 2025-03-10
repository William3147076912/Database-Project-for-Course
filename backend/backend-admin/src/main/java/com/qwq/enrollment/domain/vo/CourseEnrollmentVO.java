package com.qwq.enrollment.domain.vo;

import com.qwq.common.annotation.Excel;
import com.qwq.enrollment.domain.CourseEnrollment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2025-03-09T22:30:53CST 22:30
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseEnrollmentVO extends CourseEnrollment {
    /**
     * 选修的课程
     */
    @Excel(name = "选修的课程")
    private String name;
}
