# 作业提交
DROP TABLE IF EXISTS submission;
CREATE TABLE submission
(
    submission_id        INT PRIMARY KEY AUTO_INCREMENT COMMENT '提交记录ID',
    assignment_id        INT                          NOT NULL COMMENT '关联的作业/考试ID',
    student_id           INT                          NOT NULL COMMENT '提交作业的学生ID',
    content_or_file_path TEXT                         NOT NULL COMMENT '提交的文本内容或文件存储路径（如 /submissions/assignment1_student001.txt）',
    submission_time      DATETIME                     NOT NULL COMMENT '作业提交时间',
    status               ENUM ('Submitted', 'Graded') NOT NULL COMMENT '提交状态：已提交、已评分',
    score                INT COMMENT '学生得分（0~满分分值之间）',
    grader_id            INT COMMENT '评分的教师ID',
    grading_time         DATETIME COMMENT '成绩评定时间',
    comments             TEXT COMMENT '教师对作业的反馈评语'
    #     FOREIGN KEY (assignment_id) REFERENCES Assignment (assignment_id) COMMENT '外键：关联作业/考试表的assignment_id',
    #     FOREIGN KEY (student_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
    #     FOREIGN KEY (grader_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '作业提交记录';

-- 插入示例数据
INSERT INTO submission (assignment_id, student_id, content_or_file_path, submission_time, status)
VALUES (1, 3, '/submissions/assignment1_student001.txt', '2023-11-02 12:00:00', 'Graded'),
       (2, 3, '/submissions/exam1_student001.txt', '2023-11-16 12:00:00', 'Graded'),
       (1, 4, '/submissions/assignment1_student002.txt', '2023-11-02 12:00:00', 'Graded'),
       (2, 4, '/submissions/exam1_student002.txt', '2023-11-16 12:00:00', 'Graded');