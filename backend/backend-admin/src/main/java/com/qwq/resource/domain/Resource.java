package com.qwq.resource.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 教学资源对象 resource
 *
 * @author william
 * @date 2025-03-09
 */
        public class Resource extends BaseEntity
        {
        private static final long serialVersionUID = 1L;

                /** 资源ID */
        private Long resourceId;

                /** 资源所属课程 */
                @Excel(name = "资源所属课程" )
        private Long courseId;

                /** 上传资源的教师 */
                @Excel(name = "上传资源的教师" )
        private Long uploaderId;

                /** 文件名称 */
                @Excel(name = "文件名称" )
        private String resourceName;

                /** 资源在存储系统中的路径 */
        private String filePath;

                /** 资源类型 */
                @Excel(name = "资源类型" )
        private String resourceType;

                /** 上传时间 */
                @JsonFormat(pattern = "yyyy-MM-dd" )
                @Excel(name = "上传时间" , width = 30, dateFormat = "yyyy-MM-dd" )
        private Date uploadTime;

                public void setResourceId(Long resourceId) {
                    this.resourceId = resourceId;
                }

                public Long getResourceId() {
                    return resourceId;
                }

                public void setCourseId(Long courseId) {
                    this.courseId = courseId;
                }

                public Long getCourseId() {
                    return courseId;
                }

                public void setUploaderId(Long uploaderId) {
                    this.uploaderId = uploaderId;
                }

                public Long getUploaderId() {
                    return uploaderId;
                }

                public void setResourceName(String resourceName) {
                    this.resourceName = resourceName;
                }

                public String getResourceName() {
                    return resourceName;
                }

                public void setFilePath(String filePath) {
                    this.filePath = filePath;
                }

                public String getFilePath() {
                    return filePath;
                }

                public void setResourceType(String resourceType) {
                    this.resourceType = resourceType;
                }

                public String getResourceType() {
                    return resourceType;
                }

                public void setUploadTime(Date uploadTime) {
                    this.uploadTime = uploadTime;
                }

                public Date getUploadTime() {
                    return uploadTime;
                }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("resourceId" , getResourceId())
                .append("courseId" , getCourseId())
                .append("uploaderId" , getUploaderId())
                .append("resourceName" , getResourceName())
                .append("filePath" , getFilePath())
                .append("resourceType" , getResourceType())
                .append("uploadTime" , getUploadTime())
                    .toString();
        }
}
