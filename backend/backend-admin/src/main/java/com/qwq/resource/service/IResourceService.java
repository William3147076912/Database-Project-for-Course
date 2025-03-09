package com.qwq.resource.service;

import java.util.List;
import com.qwq.resource.domain.Resource;

/**
 * 教学资源Service接口
 * 
 * @author william
 * @date 2025-03-09
 */
public interface IResourceService 
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
     * 批量删除教学资源
     * 
     * @param resourceIds 需要删除的教学资源主键集合
     * @return 结果
     */
    public int deleteResourceByResourceIds(Long[] resourceIds);

    /**
     * 删除教学资源信息
     * 
     * @param resourceId 教学资源主键
     * @return 结果
     */
    public int deleteResourceByResourceId(Long resourceId);
}
