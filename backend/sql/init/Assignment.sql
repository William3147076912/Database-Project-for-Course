# 作业/考试
CREATE TABLE Assignment
(
    assignment_id   INT PRIMARY KEY AUTO_INCREMENT,
    course_id       INT                         NOT NULL COMMENT '关联的课程ID',
    creator_id      INT                         NOT NULL COMMENT '创建作业/考试的教师ID',
    assignment_type ENUM ('Assignment', 'Exam') NOT NULL COMMENT '评估类型：作业、考试',
    title           VARCHAR(200)                NOT NULL COMMENT '作业/考试标题（如“期中考试”）',
    description     TEXT COMMENT '作业/考试详细说明',
    deadline        DATETIME COMMENT '作业提交截止时间（仅对作业有效）',
    full_score      INT                         NOT NULL COMMENT '作业/考试的满分分值（如100分）',
    creation_time   DATETIME                    NOT NULL COMMENT '作业/考试创建时间'
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id),
    #     FOREIGN KEY (creator_id) REFERENCES User (user_id)
);