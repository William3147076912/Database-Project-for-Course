package com.qwq.assignment.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 作业/考试对象 assignment
 *
 * @author william
 * @date 2025-03-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Assignment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 作业/考试ID
     */
    private Long assignmentId;


    private Long courseId;


    private Long creatorId;

    /**
     * 评估类型
     */
    @Excel(name = "评估类型", sort = 1)
    private String assignmentType;

    /**
     * 作业/考试标题
     */
    @Excel(name = "作业/考试标题", sort = 0)
    private String title;

    /**
     * 作业/考试详细说明
     */
    @Excel(name = "作业/考试详细说明", sort = 4)
    private String description;

    /**
     * 作业提交截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "作业提交截止时间", width = 30, dateFormat = "yyyy-MM-dd", sort = 6)
    private Date deadline;

    /**
     * 作业/考试的满分分值
     */
    @Excel(name = "作业/考试的满分分值", sort = 5)
    private Long fullScore;

    /**
     * 作业/考试创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "作业/考试创建时间", width = 30, dateFormat = "yyyy-MM-dd", sort = 7)
    private Date creationTime;

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
                .toString();
    }
}
