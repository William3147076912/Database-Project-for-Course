# 选课关系表
DROP TABLE IF EXISTS CourseRelation;
CREATE TABLE CourseRelation
(
    course_id  INT   NOT NULL COMMENT '关联的课程ID',
    student_id INT   NOT NULL COMMENT '关联的学生ID',
    progress   FLOAT NOT NULL COMMENT '课程学习进度百分比（0~100）',
    PRIMARY KEY (course_id, student_id)
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id),
    #     FOREIGN KEY (student_id) REFERENCES User (user_id)
) COMMENT '课程学习进度表';