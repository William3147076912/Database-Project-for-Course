package com.qwq.submission.service;

import java.util.List;
import com.qwq.submission.domain.Submission;

/**
 * 作业提交记录Service接口
 * 
 * @author william
 * @date 2025-03-14
 */
public interface ISubmissionService 
{
    /**
     * 查询作业提交记录
     * 
     * @param submissionId 作业提交记录主键
     * @return 作业提交记录
     */
    public Submission selectSubmissionBySubmissionId(Long submissionId);

    /**
     * 查询作业提交记录列表
     * 
     * @param submission 作业提交记录
     * @return 作业提交记录集合
     */
    public List<Submission> selectSubmissionList(Submission submission);

    /**
     * 新增作业提交记录
     * 
     * @param submission 作业提交记录
     * @return 结果
     */
    public int insertSubmission(Submission submission);

    /**
     * 修改作业提交记录
     * 
     * @param submission 作业提交记录
     * @return 结果
     */
    public int updateSubmission(Submission submission);

    /**
     * 批量删除作业提交记录
     * 
     * @param submissionIds 需要删除的作业提交记录主键集合
     * @return 结果
     */
    public int deleteSubmissionBySubmissionIds(Long[] submissionIds);

    /**
     * 删除作业提交记录信息
     * 
     * @param submissionId 作业提交记录主键
     * @return 结果
     */
    public int deleteSubmissionBySubmissionId(Long submissionId);
}
