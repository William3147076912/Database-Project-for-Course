package com.qwq.resource.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.resource.mapper.ResourceMapper;
import com.qwq.resource.domain.Resource;
import com.qwq.resource.service.IResourceService;

/**
 * 教学资源Service业务层处理
 * 
 * @author william
 * @date 2025-03-09
 */
@Service
public class ResourceServiceImpl implements IResourceService 
{
    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 查询教学资源
     * 
     * @param resourceId 教学资源主键
     * @return 教学资源
     */
    @Override
    public Resource selectResourceByResourceId(Long resourceId)
    {
        return resourceMapper.selectResourceByResourceId(resourceId);
    }

    /**
     * 查询教学资源列表
     * 
     * @param resource 教学资源
     * @return 教学资源
     */
    @Override
    public List<Resource> selectResourceList(Resource resource)
    {
        return resourceMapper.selectResourceList(resource);
    }

    /**
     * 新增教学资源
     * 
     * @param resource 教学资源
     * @return 结果
     */
    @Override
    public int insertResource(Resource resource)
    {
        return resourceMapper.insertResource(resource);
    }

    /**
     * 修改教学资源
     * 
     * @param resource 教学资源
     * @return 结果
     */
    @Override
    public int updateResource(Resource resource)
    {
        return resourceMapper.updateResource(resource);
    }

    /**
     * 批量删除教学资源
     * 
     * @param resourceIds 需要删除的教学资源主键
     * @return 结果
     */
    @Override
    public int deleteResourceByResourceIds(Long[] resourceIds)
    {
        return resourceMapper.deleteResourceByResourceIds(resourceIds);
    }

    /**
     * 删除教学资源信息
     * 
     * @param resourceId 教学资源主键
     * @return 结果
     */
    @Override
    public int deleteResourceByResourceId(Long resourceId)
    {
        return resourceMapper.deleteResourceByResourceId(resourceId);
    }
}
