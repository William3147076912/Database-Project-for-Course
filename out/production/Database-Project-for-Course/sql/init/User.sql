# TODO 已经废弃，使用框架自带的表即可实现
# 用户
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    user_id           INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username          VARCHAR(50)                          NOT NULL UNIQUE COMMENT '用户登录名（如 teacher_001）',
    password          VARCHAR(100)                         NOT NULL COMMENT '加密后的密码（如 SHA-256 哈希值）',
    email             VARCHAR(100)                         NOT NULL UNIQUE COMMENT '用户邮箱地址',
    role              ENUM ('Admin', 'Teacher', 'Student') NOT NULL COMMENT '用户角色：Admin（管理员）、Teacher（教师）、Student（学生）',
    institution_id    INT                                  NOT NULL COMMENT '用户所属的教育机构',
    registration_time DATETIME                             NOT NULL COMMENT '用户账号创建时间'
    #     FOREIGN KEY (institution_id) REFERENCES Institution (institution_id) COMMENT '外键：关联教育机构表的institution_id'
) COMMENT '用户信息表';

-- 插入示例数据
INSERT INTO user (username, password, email, role, institution_id, registration_time)
VALUES ('admin_001', 'hashed_password_001', 'admin_001@example.com', 'Admin', 1, '2023-09-01 10:00:00'),
       ('teacher_001', 'hashed_password_001', 'teacher_001@example.com', 'Teacher', 1, '2023-09-02 10:00:00'),
       ('student_001', 'hashed_password_001', 'student_001@example.com', 'Student', 1, '2023-09-03 10:00:00'),
       ('student_002', 'hashed_password_002', 'student_002@example.com', 'Student', 1, '2023-09-04 10:00:00');