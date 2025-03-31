# 作业/考试
DROP TABLE IF EXISTS Assignment;
CREATE TABLE Assignment
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
INSERT INTO Assignment (course_id, creator_id, assignment_type, title, description, deadline, full_score, creation_time)
VALUES (1, 2, 'Assignment', '作业1', '这是第一份作业', '2023-12-31 23:59:59', 100, '2023-11-01 12:00:00'),
       (1, 2, 'Exam', '期中考试', '这是期中考试', '2023-11-15 17:00:00', 100, '2023-11-01 12:00:00'),
       (2, 2, 'Assignment', '作业2', '这是第二份作业', '2023-12-31 23:59:59', 100, '2023-11-02 12:00:00'),
       (2, 2, 'Exam', '期末考试', '这是期末考试', '2023-12-15 17:00:00', 100, '2023-11-02 12:00:00');


# 课程
DROP TABLE IF EXISTS Course;
CREATE TABLE Course
(
    course_id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程id',
    name          VARCHAR(200)                                            NOT NULL COMMENT '课程标题（如“Python编程入门”）',
    description   TEXT                                                    NOT NULL COMMENT '课程简要描述',
    objectives    TEXT COMMENT '课程学习目标',
    content       TEXT COMMENT '详细教学内容',
    syllabus      TEXT COMMENT '课程大纲（章节结构）',
    status        ENUM ('Draft', 'UnderReview', 'Published', 'Completed') NOT NULL COMMENT '课程状态：草稿、审核中、已上线、已结束',
    reviewer_id   INT COMMENT '审核课程的管理员ID（仅当状态为“已上线”时有效）',
    creator_id    INT                                                     NOT NULL COMMENT '课程创建者ID（教师或管理员）',
    creation_time DATETIME                                                NOT NULL COMMENT '课程创建时间',
    subject       VARCHAR(50)                                             NOT NULL COMMENT '课程所属学科（如“计算机科学”）',
    course_type   VARCHAR(50)                                             NOT NULL COMMENT '课程类型（如“公开课”“必修课”）'
    #     FOREIGN KEY (creator_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id',
    #     FOREIGN KEY (reviewer_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '课程管理';

-- 插入示例数据
INSERT INTO Course (name, description, objectives, content, syllabus, status, reviewer_id, creator_id, creation_time,
                    subject, course_type)
VALUES ('Python编程入门', 'Python基础课程', '掌握Python基础语法', '详细教学内容...', '课程大纲...', 'Published', 1, 2,
        '2023-10-01 12:00:00', '计算机科学', '必修课'),
       ('Java编程', 'Java基础课程', '掌握Java基础语法', '详细教学内容...', '课程大纲...', 'Published', 1, 2,
        '2023-10-01 12:00:00', '计算机科学', '必修课'),
       ('数据结构与算法', '数据结构与算法基础', '掌握常见数据结构与算法', '详细教学内容...', '课程大纲...', 'Published',
        1, 2, '2023-10-02 12:00:00', '计算机科学', '必修课');


# 选课关系表
DROP TABLE IF EXISTS CourseRelation;
CREATE TABLE CourseRelation
(
    course_id  INT   NOT NULL COMMENT '关联的课程ID',
    student_id INT   NOT NULL COMMENT '关联的学生ID',
    progress   FLOAT NOT NULL COMMENT '课程学习进度百分比（0~100）',
    PRIMARY KEY (course_id, student_id)
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id) COMMENT '外键：关联课程表的course_id',
    #     FOREIGN KEY (student_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '课程学习进度表';

-- 插入示例数据
INSERT INTO CourseRelation (course_id, student_id, progress)
VALUES (1, 3, 100.0),
       (1, 4, 100.0),
       (2, 3, 85.0);


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


# 教育机构
DROP TABLE IF EXISTS Institution;
CREATE TABLE Institution
(
    institution_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教育机构ID',
    name           VARCHAR(100) NOT NULL COMMENT '机构名称（如“XX大学”）',
    address        VARCHAR(200) NOT NULL COMMENT '机构详细地址',
    contact_info   VARCHAR(50) COMMENT '联系电话或邮箱'
) COMMENT = '教育机构信息表';


# 教学资源
DROP TABLE IF EXISTS Resource;
CREATE TABLE Resource
(
    resource_id   INT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    course_id     INT                                                 NOT NULL COMMENT '资源所属课程ID',
    uploader_id   INT                                                 NOT NULL COMMENT '上传资源的教师ID',
    resource_name VARCHAR(200)                                        NOT NULL COMMENT '资源文件名称（如“第1章课件.pdf”）',
    file_path     VARCHAR(500)                                        NOT NULL COMMENT '资源在存储系统中的路径（如 /resources/course101/chapter1.pdf）',
    resource_type ENUM ('Video', 'Presentation', 'ReferenceMaterial') NOT NULL COMMENT '资源类型：视频、课件、参考资料',
    upload_time   DATETIME                                            NOT NULL COMMENT '资源上传时间'
    #     FOREIGN KEY (course_id) REFERENCES Course (course_id),
    #     FOREIGN KEY (uploader_id) REFERENCES User (user_id)
) COMMENT = '教学资源';


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
    #     FOREIGN KEY (assessment_id) REFERENCES Assignment (assignment_id) COMMENT '外键：关联作业/考试表的assignment_id',
    #     FOREIGN KEY (student_id) REFERENCES User (user_id) COMMENT '外键：关联用户表的user_id'
) COMMENT '作业提交记录';

-- 插入示例数据
INSERT INTO Submission (assessment_id, student_id, content_or_file_path, submission_time, status)
VALUES (1, 3, '/submissions/assignment1_student001.txt', '2023-11-02 12:00:00', 'Graded'),
       (2, 3, '/submissions/exam1_student001.txt', '2023-11-16 12:00:00', 'Graded'),
       (1, 4, '/submissions/assignment1_student002.txt', '2023-11-02 12:00:00', 'Graded'),
       (2, 4, '/submissions/exam1_student002.txt', '2023-11-16 12:00:00', 'Graded');


# 用户
DROP TABLE IF EXISTS User;
CREATE TABLE User
(
    user_id           INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username          VARCHAR(50)                          NOT NULL UNIQUE COMMENT '用户登录名（如 teacher_001）',
    password          VARCHAR(100)                         NOT NULL COMMENT '加密后的密码（如 SHA-256 哈希值）',
    email             VARCHAR(100)                         NOT NULL UNIQUE COMMENT '用户邮箱地址',
    role              ENUM ('Admin', 'Teacher', 'Student') NOT NULL COMMENT '用户角色：Admin（管理员）、Teacher（教师）、Student（学生）',
    institution_id    INT                                  NOT NULL COMMENT '用户所属的教育机构',
    registration_time DATETIME                             NOT NULL COMMENT '用户账号创建时间'
    #     FOREIGN KEY (institution_id) REFERENCES Institution (institution_id) COMMENT '外键：关联教育机构表的institution_id'
) COMMENT '用户信息表';

-- 插入示例数据
INSERT INTO User (username, password, email, role, institution_id, registration_time)
VALUES ('admin_001', 'hashed_password_001', 'admin_001@example.com', 'Admin', 1, '2023-09-01 10:00:00'),
       ('teacher_001', 'hashed_password_001', 'teacher_001@example.com', 'Teacher', 1, '2023-09-02 10:00:00'),
       ('student_001', 'hashed_password_001', 'student_001@example.com', 'Student', 1, '2023-09-03 10:00:00'),
       ('student_002', 'hashed_password_002', 'student_002@example.com', 'Student', 1, '2023-09-04 10:00:00');
