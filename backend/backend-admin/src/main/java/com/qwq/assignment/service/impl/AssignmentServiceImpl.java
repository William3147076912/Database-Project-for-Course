package com.qwq.assignment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.qwq.assignment.domain.Assignment;
import com.qwq.assignment.domain.vo.AssignmentVO;
import com.qwq.assignment.mapper.AssignmentMapper;
import com.qwq.assignment.service.IAssignmentService;
import com.qwq.common.core.domain.entity.SysUser;
import com.qwq.common.utils.StringUtils;
import com.qwq.course.domain.Course;
import com.qwq.course.service.ICourseService;
import com.qwq.submission.domain.Submission;
import com.qwq.submission.service.ISubmissionService;
import com.qwq.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作业/考试Service业务层处理
 *
 * @author william
 * @date 2025-03-13
 */
@Slf4j
@Service
public class AssignmentServiceImpl implements IAssignmentService {
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISubmissionService submissionService;

    /**
     * 查询作业/考试
     *
     * @param assignmentId 作业/考试主键
     * @return 作业/考试
     */
    @Override
    public AssignmentVO selectAssignmentByAssignmentId(Long assignmentId) {
        Assignment assignment = assignmentMapper.selectAssignmentByAssignmentId(assignmentId);
        AssignmentVO assignmentVO = BeanUtil.copyProperties(assignment, AssignmentVO.class);
        assignmentVO.setSubmissionList(submissionService.selectSubmissionList(new Submission() {{
            setAssignmentId(assignmentId);
        }}));
        // 将用户列表转换为Map，以用户ID为键，用户对象为值
        Map<Long, SysUser> userMap = userService.selectUserList(new SysUser()).stream()
                .collect(Collectors.toMap(SysUser::getUserId, user -> user));

        assignmentVO.setCourseName(courseService.selectCourseByCourseId(assignmentVO.getCourseId()).getName());
        List<Submission> submissionList = assignmentVO.getSubmissionList();
        if (submissionList != null) {
            assignmentVO.getSubmissionList().forEach(submission -> {
                Long studentId = submission.getStudentId();
                if (userMap.containsKey(studentId)) {
                    submission.setStudentName(userMap.get(studentId).getNickName());
                    log.error(userMap.get(studentId).getNickName());
                }
            });
        }

        Long creatorId = assignmentVO.getCreatorId();
        if (userMap.containsKey(creatorId)) {
            assignmentVO.setCreatorName(userMap.get(creatorId).getNickName());
        }
        return assignmentVO;
    }

    /**
     * 查询作业/考试列表
     *
     * @param assignment 作业/考试
     * @return 作业/考试
     */
    @Override
    public List<AssignmentVO> selectAssignmentList(Assignment assignment) {
        List<Assignment> assignments = assignmentMapper.selectAssignmentList(assignment);
        List<AssignmentVO> assignmentVOS = BeanUtil.copyToList(assignments, AssignmentVO.class);


        // 将用户列表转换为Map，以用户ID为键，用户对象为值
        Map<Long, SysUser> userMap = userService.selectUserList(new SysUser()).stream()
                .collect(Collectors.toMap(SysUser::getUserId, user -> user));

        // 将课程信息转换为Map，以课程ID为键，课程名称为值
        Map<Long, String> courseMap = assignmentVOS.stream()
                .map(AssignmentVO::getCourseId)
                .distinct()
                .collect(Collectors.toMap(
                        courseId -> courseId,
                        courseId -> {
                            Course course = courseService.selectCourseByCourseId(courseId);
                            return (course != null) ? course.getName() : "未知课程"; // 处理 null 值
                        }
                ));

        // 遍历 assignmentVOS，填充 creatorName 和 courseName
        assignmentVOS.forEach(assignmentVO -> {
            Long creatorId = assignmentVO.getCreatorId();
            if (userMap.containsKey(creatorId)) {
                assignmentVO.setCreatorName(userMap.get(creatorId).getNickName());
            }
            Long courseId = assignmentVO.getCourseId();
            if (courseMap.containsKey(courseId)) {
                assignmentVO.setCourseName(courseMap.get(courseId));
            } else {
                assignmentVO.setCourseName("未知课程"); // 处理 courseMap 中不存在的情况
            }
        });

        return assignmentVOS;
    }

    /**
     * 新增作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    @Transactional
    @Override
    public int insertAssignment(Assignment assignment) {
        assignment.setCreationTime(new Date());
        int rows = assignmentMapper.insertAssignment(assignment);
        insertSubmission(assignment);
        return rows;
    }

    /**
     * 修改作业/考试
     *
     * @param assignment 作业/考试
     * @return 结果
     */
    @Transactional
    @Override
    public int updateAssignment(Assignment assignment) {
        return assignmentMapper.updateAssignment(assignment);
    }

    /**
     * 批量删除作业/考试
     *
     * @param assignmentIds 需要删除的作业/考试主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAssignmentByAssignmentIds(Long[] assignmentIds) {
        assignmentMapper.deleteSubmissionByAssessmentIds(assignmentIds);
        return assignmentMapper.deleteAssignmentByAssignmentIds(assignmentIds);
    }

    /**
     * 删除作业/考试信息
     *
     * @param assignmentId 作业/考试主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAssignmentByAssignmentId(Long assignmentId) {
        assignmentMapper.deleteSubmissionByAssessmentId(assignmentId);
        return assignmentMapper.deleteAssignmentByAssignmentId(assignmentId);
    }

    /**
     * 新增作业提交记录信息
     *
     * @param assignment 作业/考试对象
     */
    public void insertSubmission(Assignment assignment) {
        List<Submission> submissionList = assignment.getSubmissionList();
        Long assignmentId = assignment.getAssignmentId();
        if (StringUtils.isNotNull(submissionList)) {
            List<Submission> list = new ArrayList<>();
            for (Submission submission : submissionList) {
                submission.setAssignmentId(assignmentId);
                list.add(submission);
            }
            if (!list.isEmpty()) {
                assignmentMapper.batchSubmission(list);
            }
        }
    }
}
