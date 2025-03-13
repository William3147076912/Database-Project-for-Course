package com.qwq.assignment.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import com.qwq.assignment.domain.vo.AssignmentVO;
import com.qwq.course.service.ICourseService;
import com.qwq.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.assignment.mapper.AssignmentMapper;
import com.qwq.assignment.domain.Assignment;
import com.qwq.assignment.service.IAssignmentService;

/**
 * 作业/考试Service业务层处理
 *
 * @author william
 * @date 2025-03-12
 */
@Service
public class AssignmentServiceImpl implements IAssignmentService {
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询作业/考试
     *
     * @param assignmentId 作业/考试主键
     * @return 作业/考试
     */
    @Override
    public Assignment selectAssignmentByAssignmentId(Long assignmentId) {
        return assignmentMapper.selectAssignmentByAssignmentId(assignmentId);
    }

    /**
     * 查询作业/考试列表
     *
     * @param assignment 作业/考试
     * @return 作业/考试
     */
    @Override
    public List<AssignmentVO> selectAssignmentList(Assignment assignment) {
        List<Assignment> assignments = assignmentMapper.selectAssignmentList(assignment);
        List<AssignmentVO> assignmentVOS = BeanUtil.copyToList(assignments, AssignmentVO.class);
        assignmentVOS.forEach(assignmentVO -> {
            assignmentVO.setCourseName(courseService.selectCourseByCourseId(assignmentVO.getCourseId()).getName());
            assignmentVO.setCreatorName(userService.selectUserById(assignmentVO.getCreatorId()).getNickName());
        });
        return assignmentVOS;
    }

    /**
     * 新增作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    @Override
    public int insertAssignment(Assignment assignment) {
        assignment.setCreationTime(new Date());
        return assignmentMapper.insertAssignment(assignment);
    }

    /**
     * 修改作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    @Override
    public int updateAssignment(Assignment assignment) {
        return assignmentMapper.updateAssignment(assignment);
    }

    /**
     * 批量删除作业/考试
     *
     * @param assignmentIds 需要删除的作业/考试主键
     * @return 结果
     */
    @Override
    public int deleteAssignmentByAssignmentIds(Long[] assignmentIds) {
        return assignmentMapper.deleteAssignmentByAssignmentIds(assignmentIds);
    }

    /**
     * 删除作业/考试信息
     *
     * @param assignmentId 作业/考试主键
     * @return 结果
     */
    @Override
    public int deleteAssignmentByAssignmentId(Long assignmentId) {
        return assignmentMapper.deleteAssignmentByAssignmentId(assignmentId);
    }
}
