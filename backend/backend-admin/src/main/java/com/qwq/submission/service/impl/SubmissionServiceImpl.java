package com.qwq.submission.service.impl;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.submission.mapper.SubmissionMapper;
import com.qwq.submission.domain.Submission;
import com.qwq.submission.service.ISubmissionService;

/**
 * 作业提交记录Service业务层处理
 *
 * @author william
 * @date 2025-03-14
 */
@Slf4j
@Service
public class SubmissionServiceImpl implements ISubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    /**
     * 查询作业提交记录
     *
     * @param submissionId 作业提交记录主键
     * @return 作业提交记录
     */
    @Override
    public Submission selectSubmissionBySubmissionId(Long submissionId) {
        return submissionMapper.selectSubmissionBySubmissionId(submissionId);
    }

    /**
     * 查询作业提交记录列表
     *
     * @param submission 作业提交记录
     * @return 作业提交记录
     */
    @Override
    public List<Submission> selectSubmissionList(Submission submission) {
        return submissionMapper.selectSubmissionList(submission);
    }

    /**
     * 新增作业提交记录
     *
     * @param submission 作业提交记录
     * @return 结果
     */
    @Override
    public int insertSubmission(Submission submission) {
        submission.setSubmissionTime(new Date());
        submission.setStatus("Submitted");
        return submissionMapper.insertSubmission(submission);
    }

    /**
     * 修改作业提交记录
     *
     * @param submission 作业提交记录
     * @return 结果
     */
    @Override
    public int updateSubmission(Submission submission) {
        submission.setGradingTime(new Date());
        submission.setStatus("Graded");
        return submissionMapper.updateSubmission(submission);
    }

    /**
     * 批量删除作业提交记录
     *
     * @param submissionIds 需要删除的作业提交记录主键
     * @return 结果
     */
    @Override
    public int deleteSubmissionBySubmissionIds(Long[] submissionIds) {
        return submissionMapper.deleteSubmissionBySubmissionIds(submissionIds);
    }

    /**
     * 删除作业提交记录信息
     *
     * @param submissionId 作业提交记录主键
     * @return 结果
     */
    @Override
    public int deleteSubmissionBySubmissionId(Long submissionId) {
        return submissionMapper.deleteSubmissionBySubmissionId(submissionId);
    }
}
