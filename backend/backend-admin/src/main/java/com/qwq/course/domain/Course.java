package com.qwq.course.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 课程管理对象 course
 *
 * @author william
 * @date 2025-03-09
 */
@Setter
@Getter
public class Course extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 课程标题（如“Python编程入门”）
     */
    @Excel(name = "课程标题", readConverterExp = "如=“Python编程入门”")
    private String name;

    /**
     * 课程简要描述
     */
    @Excel(name = "课程简要描述")
    private String description;

    /**
     * 课程学习目标
     */
    @Excel(name = "课程学习目标")
    private String objectives;

    /**
     * 详细教学内容
     */
    @Excel(name = "详细教学内容")
    private String content;

    /**
     * 课程大纲（章节结构）
     */
    @Excel(name = "课程大纲", readConverterExp = "章=节结构")
    private String syllabus;

    /**
     * 课程状态：草稿、审核中、已上线、已结束
     */
    @Excel(name = "课程状态：草稿、审核中、已上线、已结束")
    private String status;

    /**
     * 审核课程的管理员ID（仅当状态为“已上线”时有效）
     */
    @Excel(name = "审核课程的管理员ID", readConverterExp = "仅=当状态为“已上线”时有效")
    private Long reviewerId;

    /**
     * 课程创建者ID（教师或管理员）
     */
    @Excel(name = "课程创建者ID", readConverterExp = "教=师或管理员")
    private Long creatorId;

    /**
     * 课程创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "课程创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creationTime;

    /**
     * 课程所属学科（如“计算机科学”）
     */
    @Excel(name = "课程所属学科", readConverterExp = "如=“计算机科学”")
    private String subject;

    /**
     * 课程类型（如“公开课”“必修课”）
     */
    @Excel(name = "课程类型", readConverterExp = "如=“公开课”“必修课”")
    private String courseType;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("courseId", getCourseId())
                .append("name", getName())
                .append("description", getDescription())
                .append("objectives", getObjectives())
                .append("content", getContent())
                .append("syllabus", getSyllabus())
                .append("status", getStatus())
                .append("reviewerId", getReviewerId())
                .append("creatorId", getCreatorId())
                .append("creationTime", getCreationTime())
                .append("subject", getSubject())
                .append("courseType", getCourseType())
                .toString();
    }
}
