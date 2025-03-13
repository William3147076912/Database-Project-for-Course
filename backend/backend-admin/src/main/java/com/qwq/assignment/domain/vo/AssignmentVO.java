package com.qwq.assignment.domain.vo;

import com.qwq.assignment.domain.Assignment;
import com.qwq.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2025-03-12T22:34:14CST 22:34
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssignmentVO extends Assignment {
    /**
     * 关联的课程
     */
    @Excel(name = "关联的课程", sort = 2)
    private String courseName;
    /**
     * 创建作业/考试的教师
     */
    @Excel(name = "创建作业/考试的教师", sort = 3)
    private String creatorName;
}
