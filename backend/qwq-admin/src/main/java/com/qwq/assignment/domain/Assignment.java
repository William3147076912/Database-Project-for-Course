package com.qwq.assignment.domain;

import java.util.List;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qwq.submission.domain.Submission;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 任务对象 assignment
 *
 * @author william
 * @date 2025-03-13
 */
@Setter
@Getter
public class Assignment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long assignmentId;

    /**
     * 关联的课程
     */
    private Long courseId;

    /**
     * 创建任务的教师
     */
    private Long creatorId;

    /**
     * 评估类型
     */
    @Excel(name = "评估类型")
    private String assignmentType;

    /**
     * 任务标题
     */
    @Excel(name = "任务标题")
    private String title;

    /**
     * 任务详细说明
     */
    @Excel(name = "任务详细说明")
    private String description;

    /**
     * 作业提交截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "作业提交截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deadline;

    /**
     * 任务的满分分值
     */
    @Excel(name = "任务的满分分值")
    private Long fullScore;

    /**
     * 任务创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creationTime;

    /**
     * 作业提交记录信息
     */
    private List<Submission> submissionList;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("assignmentId", getAssignmentId())
                .append("courseId", getCourseId())
                .append("creatorId", getCreatorId())
                .append("assignmentType", getAssignmentType())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("deadline", getDeadline())
                .append("fullScore", getFullScore())
                .append("creationTime", getCreationTime())
                .append("submissionList", getSubmissionList())
                .toString();
    }
}
