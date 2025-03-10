package com.qwq.resource.domain.vo;

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
    private String name;
    private String createBy;
}