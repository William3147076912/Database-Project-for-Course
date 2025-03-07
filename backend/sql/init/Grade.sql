# 成绩
DROP TABLE IF EXISTS Grade;
CREATE TABLE Grade
(
    submission_id INT PRIMARY KEY COMMENT '关联的作业提交记录ID',
    score         INT      NOT NULL COMMENT '学生得分（0~满分分值之间）',
    grader_id     INT      NOT NULL COMMENT '评分的教师ID',
    grading_time  DATETIME NOT NULL COMMENT '成绩评定时间',
    comments      TEXT COMMENT '教师对作业的反馈评语'
    #     FOREIGN KEY (submission_id) REFERENCES Submission (submission_id) COMMENT '外键：关联作业提交表的submission_id',
    #     FOREIGN KEY (grader_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '成绩';

-- 插入示例数据
INSERT INTO Grade (submission_id, score, grader_id, grading_time, comments)
VALUES (1, 85, 2, '2023-12-31 23:59:59', 'Good job!'),
       (2, 90, 2, '2023-11-15 17:00:00', 'Excellent!'),
       (3, 78, 2, '2023-12-31 23:59:59', 'Needs improvement.'),
       (4, 88, 2, '2023-12-15 17:00:00', 'Very good!');