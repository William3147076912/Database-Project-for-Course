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