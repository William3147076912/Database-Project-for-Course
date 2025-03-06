# 作业提交
DROP TABLE IF EXISTS Submission;
CREATE TABLE Submission
(
    submission_id        INT PRIMARY KEY AUTO_INCREMENT COMMENT '提交记录ID',
    assessment_id        INT                          NOT NULL COMMENT '关联的作业/考试ID',
    student_id           INT                          NOT NULL COMMENT '提交作业的学生ID',
    content_or_file_path TEXT                         NOT NULL COMMENT '提交的文本内容或文件存储路径（如 /submissions/assignment1_student001.txt）',
    submission_time      DATETIME                     NOT NULL COMMENT '作业提交时间',
    status               ENUM ('Submitted', 'Graded') NOT NULL COMMENT '提交状态：已提交、已评分'
    #     FOREIGN KEY (assessment_id) REFERENCES Assessment (assessment_id),
    #     FOREIGN KEY (student_id) REFERENCES User (user_id)
) COMMENT '作业提交记录';