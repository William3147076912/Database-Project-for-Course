package com.qwq.assignment.mapper;

import java.util.List;

import com.qwq.assignment.domain.Assignment;
import com.qwq.submission.domain.Submission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作业/考试Mapper接口
 *
 * @author william
 * @date 2025-03-13
 */
public interface AssignmentMapper {
    /**
     * 查询作业/考试
     *
     * @param assignmentId 作业/考试主键
     * @return 作业/考试
     */
    public Assignment selectAssignmentByAssignmentId(Long assignmentId);

    /**
     * 查询作业/考试列表
     *
     * @param assignment 作业/考试
     * @return 作业/考试集合
     */
    public List<Assignment> selectAssignmentList(Assignment assignment);

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
     * 删除作业/考试
     *
     * @param assignmentId 作业/考试主键
     * @return 结果
     */
    public int deleteAssignmentByAssignmentId(Long assignmentId);

    /**
     * 批量删除作业/考试
     *
     * @param assignmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAssignmentByAssignmentIds(Long[] assignmentIds);

    /**
     * 批量删除作业提交记录
     *
     * @param assignmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSubmissionByAssessmentIds(Long[] assignmentIds);

    /**
     * 批量新增作业提交记录
     *
     * @param submissionList 作业提交记录列表
     * @return 结果
     */
    public int batchSubmission(List<Submission> submissionList);


    /**
     * 通过作业/考试主键删除作业提交记录信息
     *
     * @param assignmentId 作业/考试ID
     * @return 结果
     */
    public int deleteSubmissionByAssessmentId(Long assignmentId);
}
