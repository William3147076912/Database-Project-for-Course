package com.qwq.resource.domain.vo;

import com.qwq.common.annotation.Excel;
import com.qwq.resource.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2025-03-10T17:45:39CST 17:45
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceVO extends Resource {
    // 课程名称
    @Excel(name = "所属课程名称", sort = 2)
    private String name;
    // 创建人
    @Excel(name = "创建人")
    private String createdBy;
}