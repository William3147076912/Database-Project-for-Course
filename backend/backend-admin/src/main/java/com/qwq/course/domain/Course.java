package com.qwq.course.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 课程管理对象 course
 *
 * @author william
 * @date 2025-03-08
 */
        public class Course extends BaseEntity
        {
        private static final long serialVersionUID = 1L;

                /** 课程id */
        private Long courseId;

                /** 课程名称 */
                @Excel(name = "课程名称" )
        private String name;

                /** 课程简介 */
                @Excel(name = "课程简介" )
        private String description;

                /** 课程目标 */
                @Excel(name = "课程目标" )
        private String objectives;

                /** 课程内容 */
                @Excel(name = "课程内容" )
        private String content;

                /** 课程大纲 */
                @Excel(name = "课程大纲" )
        private String syllabus;

                /** 课程状态 */
                @Excel(name = "课程状态" )
        private String status;

                /** 审核课程的管理员ID（仅当状态为“已上线”时有效） */
        private Long reviewerId;

                /** 课程创建者ID（教师或管理员） */
        private Long creatorId;

                /** 课程创建时间 */
        private Date creationTime;

                /** 所属学科（如“计算机科学”） */
                @Excel(name = "所属学科" , readConverterExp = "如=“计算机科学”" )
        private String subject;

                /** 课程类型（如“公开课”“必修课”） */
        private String courseType;

                public void setCourseId(Long courseId) {
                    this.courseId = courseId;
                }

                public Long getCourseId() {
                    return courseId;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return name;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getDescription() {
                    return description;
                }

                public void setObjectives(String objectives) {
                    this.objectives = objectives;
                }

                public String getObjectives() {
                    return objectives;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContent() {
                    return content;
                }

                public void setSyllabus(String syllabus) {
                    this.syllabus = syllabus;
                }

                public String getSyllabus() {
                    return syllabus;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getStatus() {
                    return status;
                }

                public void setReviewerId(Long reviewerId) {
                    this.reviewerId = reviewerId;
                }

                public Long getReviewerId() {
                    return reviewerId;
                }

                public void setCreatorId(Long creatorId) {
                    this.creatorId = creatorId;
                }

                public Long getCreatorId() {
                    return creatorId;
                }

                public void setCreationTime(Date creationTime) {
                    this.creationTime = creationTime;
                }

                public Date getCreationTime() {
                    return creationTime;
                }

                public void setSubject(String subject) {
                    this.subject = subject;
                }

                public String getSubject() {
                    return subject;
                }

                public void setCourseType(String courseType) {
                    this.courseType = courseType;
                }

                public String getCourseType() {
                    return courseType;
                }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("courseId" , getCourseId())
                .append("name" , getName())
                .append("description" , getDescription())
                .append("objectives" , getObjectives())
                .append("content" , getContent())
                .append("syllabus" , getSyllabus())
                .append("status" , getStatus())
                .append("reviewerId" , getReviewerId())
                .append("creatorId" , getCreatorId())
                .append("creationTime" , getCreationTime())
                .append("subject" , getSubject())
                .append("courseType" , getCourseType())
                    .toString();
        }
}
