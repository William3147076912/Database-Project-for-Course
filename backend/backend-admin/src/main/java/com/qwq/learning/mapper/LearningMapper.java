package com.qwq.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qwq.learning.domain.Learning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LearningMapper extends BaseMapper<Learning> {

    @Select("<script>" +
            "SELECT e.student_id AS student_id, " +
            "c.course_id AS course_id, " +
            "c.name AS course_name, " +
            "(SELECT COUNT(*) FROM assignment a1 WHERE a1.course_id = c.course_id) AS total_assignments, " +
            "(SELECT COUNT(*) FROM submission s JOIN assignment a2 ON a2.assignment_id = s.assignment_id AND a2.course_id = c.course_id WHERE s.student_id = #{studentId}) AS submitted_assignments, " +
            "e.plan AS plan " +
            "FROM course_enrollment e JOIN course c ON c.course_id = e.course_id " +
            "WHERE e.student_id = #{studentId} " +
            "<if test='courseName!= null and courseName!= \"\"'>" +
            "AND c.name LIKE CONCAT('%', #{courseName}, '%') " +
            "</if>" +
            "</script>")
    List<Learning> getLearningProgressAndPlanByStudentId(@Param("studentId") Integer studentId, @Param("courseName") String courseName);

    @Update("UPDATE course_enrollment " +
            "SET plan = #{plan} " +
            "WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int updateLearningPlan(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId, @Param("plan") String plan);
}