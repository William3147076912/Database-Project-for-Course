# 教育机构
DROP TABLE IF EXISTS institution;
CREATE TABLE institution
(
    institution_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教育机构ID',
    name           VARCHAR(100) NOT NULL COMMENT '机构名称（如“XX大学”）',
    address        VARCHAR(200) NOT NULL COMMENT '机构详细地址',
    contact_info   VARCHAR(50) COMMENT '联系电话或邮箱'
) COMMENT '教育机构信息表';

-- 插入示例数据
INSERT INTO institution (name, address, contact_info)
VALUES ('XX大学', 'XX市XX区XX路1号', 'contact@xxuniversity.edu'),
       ('YY大学', 'YY市YY区YY路2号', 'contact@yyuniversity.edu');