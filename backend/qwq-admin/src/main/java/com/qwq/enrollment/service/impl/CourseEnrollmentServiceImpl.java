package com.qwq.enrollment.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import com.qwq.course.domain.Course;
import com.qwq.course.service.ICourseService;
import com.qwq.enrollment.domain.vo.CourseEnrollmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.enrollment.mapper.CourseEnrollmentMapper;
import com.qwq.enrollment.domain.CourseEnrollment;
import com.qwq.enrollment.service.ICourseEnrollmentService;

/**
 * 选课记录Service业务层处理
 *
 * @author william
 * @date 2025-03-09
 */
@Service
public class CourseEnrollmentServiceImpl implements ICourseEnrollmentService {
    @Autowired
    private CourseEnrollmentMapper courseEnrollmentMapper;
    @Autowired
    private ICourseService courseService;

    /**
     * 查询选课记录
     *
     * @param enrollmentId 选课记录主键
     * @return 选课记录
     */
    @Override
    public CourseEnrollment selectCourseEnrollmentByEnrollmentId(Long enrollmentId) {
        return courseEnrollmentMapper.selectCourseEnrollmentByEnrollmentId(enrollmentId);
    }

    /**
     * 查询选课记录列表
     *
     * @param courseEnrollment 选课记录
     * @return 选课记录
     */
    @Override
    public List<CourseEnrollmentVO> selectCourseEnrollmentList(CourseEnrollment courseEnrollment) {
        List<CourseEnrollment> courseEnrollments = courseEnrollmentMapper.selectCourseEnrollmentList(courseEnrollment);
        List<CourseEnrollmentVO> courseEnrollmentVOS = BeanUtil.copyToList(courseEnrollments, CourseEnrollmentVO.class);
        courseEnrollmentVOS.forEach(courseEnrollmentVO -> {
            // 根据id查找name
            Course course = courseService.selectCourseByCourseId(courseEnrollmentVO.getCourseId());
            courseEnrollmentVO.setName(course.getName());
        });
        return courseEnrollmentVOS;
    }

    /**
     * 新增选课记录
     *
     * @param courseEnrollment 选课记录
     * @return 结果
     */
    @Override
    public int insertCourseEnrollment(CourseEnrollment courseEnrollment) {
        courseEnrollment.setEnrollmentTime(new Date());
        return courseEnrollmentMapper.insertCourseEnrollment(courseEnrollment);
    }

    /**
     * 修改选课记录
     *
     * @param courseEnrollment 选课记录
     * @return 结果
     */
    @Override
    public int updateCourseEnrollment(CourseEnrollment courseEnrollment) {
        return courseEnrollmentMapper.updateCourseEnrollment(courseEnrollment);
    }

    /**
     * 批量删除选课记录
     *
     * @param enrollmentIds 需要删除的选课记录主键
     * @return 结果
     */
    @Override
    public int deleteCourseEnrollmentByEnrollmentIds(Long[] enrollmentIds) {
        return courseEnrollmentMapper.deleteCourseEnrollmentByEnrollmentIds(enrollmentIds);
    }

    /**
     * 删除选课记录信息
     *
     * @param enrollmentId 选课记录主键
     * @return 结果
     */
    @Override
    public int deleteCourseEnrollmentByEnrollmentId(Long enrollmentId) {
        return courseEnrollmentMapper.deleteCourseEnrollmentByEnrollmentId(enrollmentId);
    }

    /**
     * 根据课程id删除选课记录
     *
     * @param courseId 课程id
     * @return 结果
     */
    @Override
    public int deleteCourseEnrollmentByCourseId(Long studentId,Long courseId) {
        return courseEnrollmentMapper.deleteCourseEnrollmentByCourseId(studentId,courseId);
    }
}
