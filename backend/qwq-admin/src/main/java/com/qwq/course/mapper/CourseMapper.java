package com.qwq.course.mapper;

import java.util.List;
import java.util.Map;

import com.qwq.course.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 课程管理Mapper接口
 *
 * @author william
 * @date 2025-03-09
 */
@Mapper
public interface CourseMapper {
    /**
     * 查询课程管理
     *
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    public Course selectCourseByCourseId(Long courseId);

    /**
     * 查询课程管理列表
     *
     * @param course 课程管理
     * @return 课程管理集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 新增课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 修改课程管理
     *
     * @param course 课程管理
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 删除课程管理
     *
     * @param courseId 课程管理主键
     * @return 结果
     */
    public int deleteCourseByCourseId(Long courseId);

    /**
     * 批量删除课程管理
     *
     * @param courseIds 需要的数据主键集合
     * @return 结果
     */
    public int deleteCourseByCourseIds(Long[] courseIds);

    /**
     * 统计所有课程各自的选课人数
     *
     * @return 课程ID和选课人数的映射集合
     */
    public List<Map<String, Object>> selectCourseEnrollmentCount();
    List<Long> selectCourseResourceId(Long courseId);
    List<Long> selectStudentId(Long courseId);

    Boolean studentFinishResource(@Param("studentId")Long studentId, @Param("resourceId")Long resourceId);
}
