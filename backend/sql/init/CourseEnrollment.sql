# 选课记录表
CREATE TABLE CourseEnrollment
(
    enrollment_id   INT PRIMARY KEY AUTO_INCREMENT,
    student_id      INT                              NOT NULL COMMENT '选课学生ID',
    course_id       INT                              NOT NULL COMMENT '选修的课程ID',
    enrollment_time DATETIME                         NOT NULL COMMENT '学生选课时间',
    status          ENUM ('InProgress', 'Completed') NOT NULL COMMENT '选课状态：进行中、完成',
    completion_time DATETIME COMMENT '课程完成时间（仅当状态为“完成”时有效）'
    #     FOREIGN KEY (student_id) REFERENCES User (user_id),
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id)
);