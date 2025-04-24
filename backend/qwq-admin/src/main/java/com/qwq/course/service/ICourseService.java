package com.qwq.course.service;

import java.util.List;
import java.util.Map;

import com.qwq.course.domain.Course;
import com.qwq.course.domain.CourseWithStatistic;

/**
 * 课程管理Service接口
 *
 * @author william
 * @date 2025-03-09
 */
 public interface ICourseService {
    /**
     * 查询课程管理
     *
     * @param courseId 课程管理主键
     * @return 课程管理
     */
     Course selectCourseByCourseId(Long courseId);

    /**
     * 查询课程管理列表
     *
     * @param course 课程管理
     * @return 课程管理集合
     */
     List<Course> selectCourseList(Course course);

    /**
     * 新增课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
     int insertCourse(Course course);

    /**
     * 修改课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
     int updateCourse(Course course);

    /**
     * 批量删除课程管理
     *
     * @param courseIds 需要删除的课程管理主键集合
     * @return 结果
     */
     int deleteCourseByCourseIds(Long[] courseIds);

    /**
     * 删除课程管理信息
     *
     * @param courseId 课程管理主键
     * @return 结果
     */
     int deleteCourseByCourseId(Long courseId);


    /**
     * 统计所有课程各自的选课人数
     *
     * @return 课程ID和选课人数的映射集合
     */
     List<CourseWithStatistic> selectCourseEnrollmentCount(Course course);

     int courseCompleteRate(Long courseId);
}
