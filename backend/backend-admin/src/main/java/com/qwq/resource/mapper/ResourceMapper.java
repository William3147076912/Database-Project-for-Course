package com.qwq.resource.mapper;

import java.util.List;
import com.qwq.resource.domain.Resource;

/**
 * 教学资源Mapper接口
 * 
 * @author william
 * @date 2025-03-09
 */
public interface ResourceMapper 
{
    /**
     * 查询教学资源
     * 
     * @param resourceId 教学资源主键
     * @return 教学资源
     */
    public Resource selectResourceByResourceId(Long resourceId);

    /**
     * 查询教学资源列表
     * 
     * @param resource 教学资源
     * @return 教学资源集合
     */
    public List<Resource> selectResourceList(Resource resource);

    /**
     * 新增教学资源
     * 
     * @param resource 教学资源
     * @return 结果
     */
    public int insertResource(Resource resource);

    /**
     * 修改教学资源
     * 
     * @param resource 教学资源
     * @return 结果
     */
    public int updateResource(Resource resource);

    /**
     * 删除教学资源
     * 
     * @param resourceId 教学资源主键
     * @return 结果
     */
    public int deleteResourceByResourceId(Long resourceId);

    /**
     * 批量删除教学资源
     * 
     * @param resourceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResourceByResourceIds(Long[] resourceIds);
}
