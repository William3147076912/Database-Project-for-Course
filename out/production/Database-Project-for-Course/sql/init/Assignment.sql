# 作业/考试
DROP TABLE IF EXISTS assignment;
CREATE TABLE assignment
(
    assignment_id   INT PRIMARY KEY AUTO_INCREMENT COMMENT '作业/考试ID',
    course_id       INT                         NOT NULL COMMENT '关联的课程ID',
    creator_id      INT                         NOT NULL COMMENT '创建作业/考试的教师ID',
    assignment_type ENUM ('Assignment', 'Exam') NOT NULL COMMENT '评估类型：作业、考试',
    title           VARCHAR(200)                NOT NULL COMMENT '作业/考试标题（如“期中考试”）',
    description     TEXT COMMENT '作业/考试详细说明',
    deadline        DATETIME COMMENT '作业提交截止时间（仅对作业有效）',
    full_score      INT                         NOT NULL COMMENT '作业/考试的满分分值（如100分）',
    creation_time   DATETIME                    NOT NULL COMMENT '作业/考试创建时间'
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id) COMMENT '外键：关联课程表的course_id',
    #     FOREIGN KEY (creator_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '作业/考试';

-- 插入示例数据
INSERT INTO assignment (course_id, creator_id, assignment_type, title, description, deadline, full_score, creation_time)
VALUES (1, 2, 'Assignment', '作业1', '这是第一份作业', '2023-12-31 23:59:59', 100, '2023-11-01 12:00:00'),
       (1, 2, 'Exam', '期中考试', '这是期中考试', '2023-11-15 17:00:00', 100, '2023-11-01 12:00:00'),
       (2, 2, 'Assignment', '作业2', '这是第二份作业', '2023-12-31 23:59:59', 100, '2023-11-02 12:00:00'),
       (2, 2, 'Exam', '期末考试', '这是期末考试', '2023-12-15 17:00:00', 100, '2023-11-02 12:00:00');