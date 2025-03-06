# 教育机构
CREATE TABLE Institution
(
    institution_id INT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL COMMENT '机构名称（如“XX大学”）',
    address        VARCHAR(200) NOT NULL COMMENT '机构详细地址',
    contact_info   VARCHAR(50) COMMENT '联系电话或邮箱'
);