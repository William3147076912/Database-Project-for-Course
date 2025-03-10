package com.qwq.enrollment.service;

import java.util.List;

import com.qwq.enrollment.domain.CourseEnrollment;
import com.qwq.enrollment.domain.vo.CourseEnrollmentVO;

/**
 * 选课记录Service接口
 *
 * @author william
 * @date 2025-03-09
 */
public interface ICourseEnrollmentService {
    /**
     * 查询选课记录
     *
     * @param enrollmentId 选课记录主键
     * @return 选课记录
     */
    public CourseEnrollment selectCourseEnrollmentByEnrollmentId(Long enrollmentId);

    /**
     * 查询选课记录列表
     *
     * @param courseEnrollment 选课记录
     * @return 选课记录集合
     */
    public List<CourseEnrollmentVO> selectCourseEnrollmentList(CourseEnrollment courseEnrollment);

    /**
     * 新增选课记录
     *
     * @param courseEnrollment 选课记录
     * @return 结果
     */
    public int insertCourseEnrollment(CourseEnrollment courseEnrollment);

    /**
     * 修改选课记录
     *
     * @param courseEnrollment 选课记录
     * @return 结果
     */
    public int updateCourseEnrollment(CourseEnrollment courseEnrollment);

    /**
     * 批量删除选课记录
     *
     * @param enrollmentIds 需要删除的选课记录主键集合
     * @return 结果
     */
    public int deleteCourseEnrollmentByEnrollmentIds(Long[] enrollmentIds);

    /**
     * 删除选课记录信息
     *
     * @param enrollmentId 选课记录主键
     * @return 结果
     */
    public int deleteCourseEnrollmentByEnrollmentId(Long enrollmentId);

    int deleteCourseEnrollmentByCourseId(Long courseId);
}
