package com.qwq.grade.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 成绩对象 grade
 * 
 * @author william
 * @date 2025-03-07
 */
public class Grade extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联的作业提交记录ID */
    private Long submissionId;

    /** 学生得分（0~满分分值之间） */
    @Excel(name = "学生得分", readConverterExp = "0=~满分分值之间")
    private Long score;

    /** 评分的教师ID */
    @Excel(name = "评分的教师ID")
    private Long graderId;

    /** 成绩评定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成绩评定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gradingTime;

    /** 教师对作业的反馈评语 */
    @Excel(name = "教师对作业的反馈评语")
    private String comments;

    public void setSubmissionId(Long submissionId) 
    {
        this.submissionId = submissionId;
    }

    public Long getSubmissionId() 
    {
        return submissionId;
    }

    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }

    public void setGraderId(Long graderId) 
    {
        this.graderId = graderId;
    }

    public Long getGraderId() 
    {
        return graderId;
    }

    public void setGradingTime(Date gradingTime) 
    {
        this.gradingTime = gradingTime;
    }

    public Date getGradingTime() 
    {
        return gradingTime;
    }

    public void setComments(String comments) 
    {
        this.comments = comments;
    }

    public String getComments() 
    {
        return comments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("submissionId", getSubmissionId())
            .append("score", getScore())
            .append("graderId", getGraderId())
            .append("gradingTime", getGradingTime())
            .append("comments", getComments())
            .toString();
    }
}
