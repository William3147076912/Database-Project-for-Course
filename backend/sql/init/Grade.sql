# 成绩
CREATE TABLE Grade
(
    submission_id INT PRIMARY KEY COMMENT '关联的作业提交记录ID',
    score         INT      NOT NULL COMMENT '学生得分（0~满分分值之间）',
    grader_id     INT      NOT NULL COMMENT '评分的教师ID',
    grading_time  DATETIME NOT NULL COMMENT '成绩评定时间',
    comments      TEXT COMMENT '教师对作业的反馈评语'
    #     FOREIGN KEY (submission_id) REFERENCES Submission (submission_id),
    #     FOREIGN KEY (grader_id) REFERENCES User (user_id)
);