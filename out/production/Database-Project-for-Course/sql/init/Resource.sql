# 教学资源
DROP TABLE IF EXISTS resource;
CREATE TABLE resource
(
    resource_id   INT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    course_id     INT                                                 NOT NULL COMMENT '资源所属课程ID',
    uploader_id   INT                                                 NOT NULL COMMENT '上传资源的教师ID',
    resource_name VARCHAR(200)                                        NOT NULL COMMENT '资源文件名称（如“第1章课件.pdf”）',
    file_path     VARCHAR(500)                                        NOT NULL COMMENT '资源在存储系统中的路径（如 /resources/course101/chapter1.pdf）',
    resource_type ENUM ('Video', 'Presentation', 'ReferenceMaterial') NOT NULL COMMENT '资源类型：视频、课件、参考资料',
    upload_time   DATETIME                                            NOT NULL COMMENT '资源上传时间',
    INDEX idx_course_id (course_id),
    INDEX idx_uploader_id (uploader_id)
) COMMENT '教学资源';

-- 插入示例数据
INSERT INTO resource (course_id, uploader_id, resource_name, file_path, resource_type, upload_time)
VALUES (1, 2, '第1章课件.pdf', '/resources/course1/chapter1.pdf', 'Presentation', '2023-10-01 12:00:00'),
       (1, 2, '第1章视频.mp4', '/resources/course1/chapter1_video.mp4', 'Video', '2023-10-01 12:00:00'),
       (2, 2, '第1章课件.pdf', '/resources/course2/chapter1.pdf', 'Presentation', '2023-10-02 12:00:00');