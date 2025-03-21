package com.qwq.course.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qwq.course.domain.CourseWithStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qwq.course.mapper.CourseMapper;
import com.qwq.course.domain.Course;
import com.qwq.course.service.ICourseService;

/**
 * 课程管理Service业务层处理
 *
 * @author william
 * @date 2025-03-09
 */
@Service
public class CourseServiceImpl implements ICourseService {
    @Autowired
    private CourseMapper courseMapper;

    /**
     * 查询课程管理
     *
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    @Override
    public Course selectCourseByCourseId(Long courseId) {
        return courseMapper.selectCourseByCourseId(courseId);
    }

    /**
     * 查询课程管理列表
     *
     * @param course 课程管理
     * @return 课程管理
     */
    @Override
    public List<Course> selectCourseList(Course course) {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int insertCourse(Course course) {
        return courseMapper.insertCourse(course);
    }

    /**
     * 修改课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int updateCourse(Course course) {
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程管理
     *
     * @param courseIds 需要删除的课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseIds(Long[] courseIds) {
        return courseMapper.deleteCourseByCourseIds(courseIds);
    }

    /**
     * 删除课程管理信息
     *
     * @param courseId 课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseId(Long courseId) {
        return courseMapper.deleteCourseByCourseId(courseId);
    }
    /**
     * 统计所有课程各自的选课人数
     *
     * @return 课程ID和选课人数的映射集合
     */
    @Override
    public List<CourseWithStatistic> selectCourseEnrollmentCount(Course course)
    {
        List<Course>  list=this.selectCourseList(course);
        Map<Long, Long> list2 = new HashMap<>();
        List<Map<String,Object>> results=courseMapper.selectCourseEnrollmentCount();
        for (Map<String, Object> row : results) {
            Long courseId = (Long) row.get("courseId");
            Long enrollmentCount = (Long) row.get("enrollmentCount");
            list2.put(courseId, enrollmentCount);
        }
        List<CourseWithStatistic> list3 = new ArrayList<>();
        for (Course value : list) {
            list3.add(new CourseWithStatistic(value, list2.get(value.getCourseId()), 0));
        }
        return list3;
    }
}
