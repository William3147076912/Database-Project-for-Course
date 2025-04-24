package com.qwq.resource.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 教学资源对象 resource
 *
 * @author william
 * @date 2025-03-09
 */
public class Resource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源所属课程
     */
    private Long courseId;

    /**
     * 上传资源的教师
     */
    private Long uploaderId;

    /**
     * 文件名称
     */
    @Excel(name = "资源名称", sort = 1)
    private String resourceName;

    /**
     * 资源在存储系统中的路径
     */
    private String filePath;

    /**
     * 资源类型
     */
    @Excel(name = "资源类型", sort = 3)
    private String resourceType;

    /**
     * 上传时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH-mm-SS")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH-mm-SS", sort = 4)
    private Date uploadTime;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("resourceId", getResourceId())
                .append("courseId", getCourseId())
                .append("uploaderId", getUploaderId())
                .append("resourceName", getResourceName())
                .append("filePath", getFilePath())
                .append("resourceType", getResourceType())
                .append("uploadTime", getUploadTime())
                .toString();
    }
}
