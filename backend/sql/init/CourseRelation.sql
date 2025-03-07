# 选课关系表
DROP TABLE IF EXISTS CourseRelation;
CREATE TABLE CourseRelation
(
    course_id  INT   NOT NULL COMMENT '关联的课程ID',
    student_id INT   NOT NULL COMMENT '关联的学生ID',
    progress   FLOAT NOT NULL COMMENT '课程学习进度百分比（0~100）',
    PRIMARY KEY (course_id, student_id),
    FOREIGN KEY (course_id) REFERENCES Course (course_id) COMMENT '外键：关联课程表的course_id',
    FOREIGN KEY (student_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '课程学习进度表';

-- 插入示例数据
INSERT INTO CourseRelation (course_id, student_id, progress)
VALUES
(1, 3, 100.0),
(1, 4, 100.0),
(2, 3, 85.0);