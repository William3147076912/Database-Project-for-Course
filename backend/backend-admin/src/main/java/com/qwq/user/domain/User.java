package com.qwq.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qwq.common.annotation.Excel;
import com.qwq.common.core.domain.BaseEntity;

/**
 * 用户信息对象 user
 *
 * @author william
 * @date 2025-03-10
 */
        public class User extends BaseEntity
        {
        private static final long serialVersionUID = 1L;

                /** 用户ID */
        private Long userId;

                /** 用户登录名（如 teacher_001） */
                @Excel(name = "用户登录名" , readConverterExp = "如=,t=eacher_001" )
        private String username;

                /** 加密后的密码（如 SHA-256 哈希值） */
                @Excel(name = "加密后的密码" , readConverterExp = "如=,S=HA-256,哈=希值" )
        private String password;

                /** 文件地址 */
                @Excel(name = "文件地址" )
        private String email;

                /** 用户角色：Admin（管理员）、Teacher（教师）、Student（学生） */
                @Excel(name = "用户角色：Admin" , readConverterExp = "管=理员" )
        private String role;

                /** 用户所属的教育机构 */
                @Excel(name = "用户所属的教育机构" )
        private Long institutionId;

                /** 用户账号创建时间 */
                @JsonFormat(pattern = "yyyy-MM-dd" )
                @Excel(name = "用户账号创建时间" , width = 30, dateFormat = "yyyy-MM-dd" )
        private Date registrationTime;

                public void setUserId(Long userId) {
                    this.userId = userId;
                }

                public Long getUserId() {
                    return userId;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getUsername() {
                    return username;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getPassword() {
                    return password;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getEmail() {
                    return email;
                }

                public void setRole(String role) {
                    this.role = role;
                }

                public String getRole() {
                    return role;
                }

                public void setInstitutionId(Long institutionId) {
                    this.institutionId = institutionId;
                }

                public Long getInstitutionId() {
                    return institutionId;
                }

                public void setRegistrationTime(Date registrationTime) {
                    this.registrationTime = registrationTime;
                }

                public Date getRegistrationTime() {
                    return registrationTime;
                }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId" , getUserId())
                .append("username" , getUsername())
                .append("password" , getPassword())
                .append("email" , getEmail())
                .append("role" , getRole())
                .append("institutionId" , getInstitutionId())
                .append("registrationTime" , getRegistrationTime())
                    .toString();
        }
}
