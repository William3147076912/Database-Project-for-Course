# 教育机构
DROP TABLE IF EXISTS Institution;
CREATE TABLE Institution
(
    institution_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教育机构ID',
    name           VARCHAR(100) NOT NULL COMMENT '机构名称（如“XX大学”）',
    address        VARCHAR(200) NOT NULL COMMENT '机构详细地址',
    contact_info   VARCHAR(50) COMMENT '联系电话或邮箱'
) COMMENT = '教育机构信息表';