package com.qwq.grade.service;

import java.util.List;
import com.qwq.grade.domain.Grade;

/**
 * 成绩Service接口
 * 
 * @author william
 * @date 2025-03-07
 */
public interface IGradeService 
{
    /**
     * 查询成绩
     * 
     * @param submissionId 成绩主键
     * @return 成绩
     */
    public Grade selectGradeBySubmissionId(Long submissionId);

    /**
     * 查询成绩列表
     * 
     * @param grade 成绩
     * @return 成绩集合
     */
    public List<Grade> selectGradeList(Grade grade);

    /**
     * 新增成绩
     * 
     * @param grade 成绩
     * @return 结果
     */
    public int insertGrade(Grade grade);

    /**
     * 修改成绩
     * 
     * @param grade 成绩
     * @return 结果
     */
    public int updateGrade(Grade grade);

    /**
     * 批量删除成绩
     * 
     * @param submissionIds 需要删除的成绩主键集合
     * @return 结果
     */
    public int deleteGradeBySubmissionIds(Long[] submissionIds);

    /**
     * 删除成绩信息
     * 
     * @param submissionId 成绩主键
     * @return 结果
     */
    public int deleteGradeBySubmissionId(Long submissionId);
}
