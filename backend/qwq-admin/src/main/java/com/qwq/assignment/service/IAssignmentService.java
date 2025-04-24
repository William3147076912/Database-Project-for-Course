package com.qwq.assignment.service;

import java.util.List;

import com.qwq.assignment.domain.Assignment;
import com.qwq.assignment.domain.vo.AssignmentVO;

/**
 * 作业/考试Service接口
 *
 * @author william
 * @date 2025-03-13
 */
public interface IAssignmentService {
    /**
     * 查询作业/考试
     *
     * @param assignmentId 作业/考试主键
     * @return 作业/考试
     */
    public AssignmentVO selectAssignmentByAssignmentId(Long assignmentId);

    /**
     * 查询作业/考试列表
     *
     * @param assignment 作业/考试
     * @return 作业/考试集合
     */
    public List<AssignmentVO> selectAssignmentList(Assignment assignment);

    /**
     * 新增作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    public int insertAssignment(Assignment assignment);

    /**
     * 修改作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    public int updateAssignment(Assignment assignment);

    /**
     * 批量删除作业/考试
     *
     * @param assignmentIds 需要删除的作业/考试主键集合
     * @return 结果
     */
    public int deleteAssignmentByAssignmentIds(Long[] assignmentIds);

    /**
     * 删除作业/考试信息
     *
     * @param assignmentId 作业/考试主键
     * @return 结果
     */
    public int deleteAssignmentByAssignmentId(Long assignmentId);
}
