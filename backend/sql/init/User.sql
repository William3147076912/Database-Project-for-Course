# 用户
CREATE TABLE User
(
    user_id           INT PRIMARY KEY AUTO_INCREMENT,
    username          VARCHAR(50)                          NOT NULL UNIQUE COMMENT '用户登录名（如 teacher_001）',
    password          VARCHAR(100)                         NOT NULL COMMENT '加密后的密码（如 SHA-256 哈希值）',
    email             VARCHAR(100)                         NOT NULL UNIQUE COMMENT '用户邮箱地址',
    role              ENUM ('Admin', 'Teacher', 'Student') NOT NULL COMMENT '用户角色：Admin（管理员）、Teacher（教师）、Student（学生）',
    institution_id    INT                                  NOT NULL COMMENT '用户所属的教育机构',
    registration_time DATETIME                             NOT NULL COMMENT '用户账号创建时间'
    #     FOREIGN KEY (institution_id) REFERENCES Institution (institution_id)
);