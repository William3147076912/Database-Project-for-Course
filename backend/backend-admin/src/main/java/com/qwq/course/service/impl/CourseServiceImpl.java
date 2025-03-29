package com.qwq.course.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qwq.assignment.mapper.AssignmentMapper;
import com.qwq.course.domain.CourseWithStatistic;
import com.qwq.resource.mapper.LearnTimeMapper;
import com.qwq.resource.mapper.ResourceMapper;
import com.qwq.user.mapper.StudentMapper;
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
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private LearnTimeMapper learnTimeMapper;
    @Autowired
    private StudentMapper studentMapper;

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
        Map<Long, Long> mapList = new HashMap<>();
        List<Map<String,Object>> results=courseMapper.selectCourseEnrollmentCount();
        for (Map<String, Object> row : results) {
            Long courseId = (Long) row.get("courseId");
            Long enrollmentCount = (Long) row.get("enrollmentCount");
            mapList.put(courseId, enrollmentCount);
        }
        List<CourseWithStatistic> list3 = new ArrayList<>();
        Long id;
        for (Course value : list) {
            id=value.getCourseId();
            CourseWithStatistic course1=new CourseWithStatistic(value, mapList.get(id), courseCompleteRate(id));
            course1.setCompletionCount(courseCompleteRate(course1.getCourseId()));
            list3.add(course1);
        }
        return list3;
    }
    //计算课程完成率
    @Override
    public int courseCompleteRate(Long courseId)
    {
        List<Long> students=courseMapper.selectStudentId(courseId);
        List<Long> resources=courseMapper.selectCourseResourceId(courseId);
        int count=0;
        boolean flag;
        for(Long studentId:students)
        {
            if(studentId==null) continue;
            flag=true;
            for (Long resourceId:resources)
            {
                if(!courseMapper.studentFinishResource(studentId,resourceId))
                {
                    flag=false;
                    break;
                }
            }
            if(flag)
            {
                count++;
                System.out.println("学生"+studentId+"完成课程"+courseId);
            }else
            {
                System.out.println("学生"+studentId+"未完成课程"+courseId);
            }
        }
        return count;
    }
}
