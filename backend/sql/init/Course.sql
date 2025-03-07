# 课程
DROP TABLE IF EXISTS Course;
CREATE TABLE Course
(
    course_id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程id',
    name          VARCHAR(200)                                            NOT NULL COMMENT '课程标题（如“Python编程入门”）',
    description   TEXT                                                    NOT NULL COMMENT '课程简要描述',
    objectives    TEXT COMMENT '课程学习目标',
    content       TEXT COMMENT '详细教学内容',
    syllabus      TEXT COMMENT '课程大纲（章节结构）',
    status        ENUM ('Draft', 'UnderReview', 'Published', 'Completed') NOT NULL COMMENT '课程状态：草稿、审核中、已上线、已结束',
    reviewer_id   INT COMMENT '审核课程的管理员ID（仅当状态为“已上线”时有效）',
    creator_id    INT                                                     NOT NULL COMMENT '课程创建者ID（教师或管理员）',
    creation_time DATETIME                                                NOT NULL COMMENT '课程创建时间',
    subject       VARCHAR(50)                                             NOT NULL COMMENT '课程所属学科（如“计算机科学”）',
    course_type   VARCHAR(50)                                             NOT NULL COMMENT '课程类型（如“公开课”“必修课”）',
    FOREIGN KEY (creator_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id',
    FOREIGN KEY (reviewer_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '课程管理';

-- 插入示例数据
INSERT INTO Course (name, description, objectives, content, syllabus, status, reviewer_id, creator_id, creation_time, subject, course_type)
VALUES
('Python编程入门', 'Python基础课程', '掌握Python基础语法', '详细教学内容...', '课程大纲...', 'Published', 1, 2, '2023-10-01 12:00:00', '计算机科学', '必修课'),
('Java编程', 'Java基础课程', '掌握Java基础语法', '详细教学内容...', '课程大纲...', 'Published', 1, 2, '2023-10-01 12:00:00', '计算机科学', '必修课'),
('数据结构与算法', '数据结构与算法基础', '掌握常见数据结构与算法', '详细教学内容...', '课程大纲...', 'Published', 1, 2, '2023-10-02 12:00:00', '计算机科学', '必修课');