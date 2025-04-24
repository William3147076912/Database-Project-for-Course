package com.qwq.submission.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 作业提交记录对象 submission
 *
 * @author william
 * @date 2025-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Submission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 提交记录ID
     */
    private Long submissionId;

    /**
     * 关联的作业/考试ID
     */
    @Excel(name = "关联的作业/考试ID")
    private Long assignmentId;
    /**
     * 提交作业的学生ID
     */

    private Long studentId;
    @Excel(name = "提交的学生")
    private String studentName;

    /**
     * 提交的文本内容或文件存储路径（如 /submissions/assignment1_student001.txt）
     */
    @Excel(name = "提交的文本内容或文件存储路径", readConverterExp = "如=,/=submissions/assignment1_student001.txt")
    private String contentOrFilePath;

    /**
     * 作业提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "作业提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submissionTime;

    /**
     * 提交状态：已提交、已评分
     */
    @Excel(name = "提交状态：已提交、已评分")
    private String status;

    /**
     * 学生得分（0~满分分值之间）
     */
    @Excel(name = "学生得分", readConverterExp = "0=~满分分值之间")
    private Long score;

    /**
     * 评分的教师ID
     */
    private Long graderId;
    @Excel(name = "评分的教师")
    private String graderName;
    /**
     * 成绩评定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成绩评定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gradingTime;

    /**
     * 教师对作业的反馈评语
     */
    @Excel(name = "教师对作业的反馈评语")
    private String comments;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("submissionId", getSubmissionId())
                .append("assignmentId", getAssignmentId())
                .append("studentId", getStudentId())
                .append("contentOrFilePath", getContentOrFilePath())
                .append("submissionTime", getSubmissionTime())
                .append("status", getStatus())
                .append("score", getScore())
                .append("graderId", getGraderId())
                .append("gradingTime", getGradingTime())
                .append("comments", getComments())
                .toString();
    }
}
