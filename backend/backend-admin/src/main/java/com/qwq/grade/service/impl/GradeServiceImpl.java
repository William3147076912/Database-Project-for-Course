package com.qwq.grade.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.grade.mapper.GradeMapper;
import com.qwq.grade.domain.Grade;
import com.qwq.grade.service.IGradeService;

/**
 * 成绩Service业务层处理
 * 
 * @author william
 * @date 2025-03-07
 */
@Service
public class GradeServiceImpl implements IGradeService 
{
    @Autowired
    private GradeMapper gradeMapper;

    /**
     * 查询成绩
     * 
     * @param submissionId 成绩主键
     * @return 成绩
     */
    @Override
    public Grade selectGradeBySubmissionId(Long submissionId)
    {
        return gradeMapper.selectGradeBySubmissionId(submissionId);
    }

    /**
     * 查询成绩列表
     * 
     * @param grade 成绩
     * @return 成绩
     */
    @Override
    public List<Grade> selectGradeList(Grade grade)
    {
        return gradeMapper.selectGradeList(grade);
    }

    /**
     * 新增成绩
     * 
     * @param grade 成绩
     * @return 结果
     */
    @Override
    public int insertGrade(Grade grade)
    {
        return gradeMapper.insertGrade(grade);
    }

    /**
     * 修改成绩
     * 
     * @param grade 成绩
     * @return 结果
     */
    @Override
    public int updateGrade(Grade grade)
    {
        return gradeMapper.updateGrade(grade);
    }

    /**
     * 批量删除成绩
     * 
     * @param submissionIds 需要删除的成绩主键
     * @return 结果
     */
    @Override
    public int deleteGradeBySubmissionIds(Long[] submissionIds)
    {
        return gradeMapper.deleteGradeBySubmissionIds(submissionIds);
    }

    /**
     * 删除成绩信息
     * 
     * @param submissionId 成绩主键
     * @return 结果
     */
    @Override
    public int deleteGradeBySubmissionId(Long submissionId)
    {
        return gradeMapper.deleteGradeBySubmissionId(submissionId);
    }
}
