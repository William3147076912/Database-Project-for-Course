package com.qwq.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.qwq.common.config.RuoYiConfig;
import com.qwq.common.core.domain.entity.SysUser;
import com.qwq.common.utils.file.FileUtils;
import com.qwq.course.domain.Course;
import com.qwq.resource.domain.LearnTime;
import com.qwq.course.service.ICourseService;
import com.qwq.resource.domain.Resource;
import com.qwq.resource.domain.vo.ResourceVO;
import com.qwq.resource.mapper.LearnTimeMapper;
import com.qwq.resource.mapper.ResourceMapper;
import com.qwq.resource.service.IResourceService;
import com.qwq.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 教学资源Service业务层处理
 *
 * @author william
 * @date 2025-03-09
 */
@Slf4j
@Service
public class ResourceServiceImpl implements IResourceService {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    LearnTimeMapper learnTimeMapper;
    /**
     * 查询教学资源
     *
     * @param resourceId 教学资源主键
     * @return 教学资源
     */
    @Override
    public Resource selectResourceByResourceId(Long resourceId) {
        return resourceMapper.selectResourceByResourceId(resourceId);
    }

    /**
     * 查询教学资源列表
     *
     * @param resource 教学资源
     * @return 教学资源
     */
    @Override
    public List<ResourceVO> selectResourceList(Resource resource) {
        List<Resource> resources = resourceMapper.selectResourceList(resource);
        List<ResourceVO> resourceVOS = BeanUtil.copyToList(resources, ResourceVO.class);
        resourceVOS.forEach(resourceVO -> {
            Course course = courseService.selectCourseByCourseId(resourceVO.getCourseId());
            if (course != null) {
                resourceVO.setName(course.getName());
            } else {
                resourceVO.setName("课程名称未找到");
            }

            SysUser user = userService.selectUserById(resourceVO.getUploaderId());
            if (user != null) {
                resourceVO.setCreatedBy(user.getNickName());
            } else {
                resourceVO.setCreatedBy("上传者未找到");
            }
        });
        return resourceVOS;
    }

    /**
     * 新增教学资源
     *
     * @param resource 教学资源
     * @return 结果
     */
    @Override
    public int insertResource(Resource resource) {
        resource.setUploadTime(new Date());
        return resourceMapper.insertResource(resource);
    }

    /**
     * 修改教学资源
     *
     * @param resource 教学资源
     * @return 结果
     */
    @Override
    public int updateResource(Resource resource) {
        Resource resourceByResourceId = selectResourceByResourceId(resource.getResourceId());
        if (!resourceByResourceId.getFilePath().equals(resource.getFilePath()) && !resourceByResourceId.getFilePath().isEmpty()) {
            // 如果文件路径改变，则删除旧文件
            String filePath = RuoYiConfig.getProfile() + resourceByResourceId.getFilePath().substring("/profile".length());
            FileUtils.deleteFile(filePath);
        }
        return resourceMapper.updateResource(resource);
    }

    /**
     * 批量删除教学资源
     *
     * @param resourceIds 需要删除的教学资源主键
     * @return 结果
     */
    @Override
    public int deleteResourceByResourceIds(Long[] resourceIds) {
        return resourceMapper.deleteResourceByResourceIds(resourceIds);
    }

    /**
     * 删除教学资源信息
     *
     * @param resourceId 教学资源主键
     * @return 结果
     */
    @Override
    public int deleteResourceByResourceId(Long resourceId) {
        return resourceMapper.deleteResourceByResourceId(resourceId);
    }

    @Override
    public int updateLearnTime(LearnTime learnTime){
        LearnTime existingLearnTime = learnTimeMapper.selectLearnTime(learnTime);
        if (existingLearnTime == null) {
            // 如果不存在，则插入新的学习记录
            return learnTimeMapper.insertLearnTime(learnTime);
        } else {
            // 如果存在，则更新现有学习记录
            if(existingLearnTime.isFinished() && !learnTime.isFinished())
                learnTime.setFinished(true);
            learnTime.setLearnTime(existingLearnTime.getLearnTime() + learnTime.getLearnTime());
            return learnTimeMapper.updateLearnTime(learnTime);
        }
    }

}
