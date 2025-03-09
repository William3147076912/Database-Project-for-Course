# 选课记录表
DROP TABLE IF EXISTS course_enrollment;
CREATE TABLE course_enrollment
(
    enrollment_id   INT PRIMARY KEY AUTO_INCREMENT COMMENT '选课记录ID',
    student_id      INT                              NOT NULL COMMENT '选课学生ID',
    course_id       INT                              NOT NULL COMMENT '选修的课程ID',
    enrollment_time DATETIME                         NOT NULL COMMENT '学生选课时间',
    status          ENUM ('InProgress', 'Completed') NOT NULL COMMENT '选课状态：进行中、完成',
    completion_time DATETIME COMMENT '课程完成时间（仅当状态为“完成”时有效）'
    #     FOREIGN KEY (student_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id',
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id) COMMENT '外键：关联课程表的course_id'
) COMMENT '选课记录表';

-- 插入示例数据
INSERT INTO course_enrollment (student_id, course_id, enrollment_time, status, completion_time)
VALUES (3, 1, '2023-10-05 10:00:00', 'Completed', '2023-12-31 23:59:59'),
       (4, 1, '2023-10-06 10:00:00', 'Completed', '2023-12-31 23:59:59'),
       (3, 2, '2023-10-07 10:00:00', 'InProgress', NULL);