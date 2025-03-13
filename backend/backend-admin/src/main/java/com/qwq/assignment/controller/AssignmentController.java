package com.qwq.assignment.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qwq.assignment.domain.vo.AssignmentVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qwq.common.annotation.Log;
import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.domain.AjaxResult;
import com.qwq.common.enums.BusinessType;
import com.qwq.assignment.domain.Assignment;
import com.qwq.assignment.service.IAssignmentService;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.common.core.page.TableDataInfo;

/**
 * 作业/考试Controller
 *
 * @author william
 * @date 2025-03-12
 */
@RestController
@RequestMapping("/assignment/assignment")
public class AssignmentController extends BaseController {
    @Autowired
    private IAssignmentService assignmentService;

    /**
     * 查询作业/考试列表
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Assignment assignment) {
        startPage();
        List<AssignmentVO> list = assignmentService.selectAssignmentList(assignment);
        return getDataTable(list);
    }

    /**
     * 导出作业/考试列表
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:export')")
    @Log(title = "作业/考试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Assignment assignment) {
        List<AssignmentVO> list = assignmentService.selectAssignmentList(assignment);
        ExcelUtil<AssignmentVO> util = new ExcelUtil<>(AssignmentVO.class);
        util.exportExcel(response, list, "作业/考试数据");
    }

    /**
     * 获取作业/考试详细信息
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:query')")
    @GetMapping(value = "/{assignmentId}")
    public AjaxResult getInfo(@PathVariable("assignmentId") Long assignmentId) {
        return success(assignmentService.selectAssignmentByAssignmentId(assignmentId));
    }

    /**
     * 新增作业/考试
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:add')")
    @Log(title = "作业/考试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Assignment assignment) {
        return toAjax(assignmentService.insertAssignment(assignment));
    }

    /**
     * 修改作业/考试
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:edit')")
    @Log(title = "作业/考试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Assignment assignment) {
        return toAjax(assignmentService.updateAssignment(assignment));
    }

    /**
     * 删除作业/考试
     */
    @PreAuthorize("@ss.hasPermi('assignment:assignment:remove')")
    @Log(title = "作业/考试", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assignmentIds}")
    public AjaxResult remove(@PathVariable Long[] assignmentIds) {
        return toAjax(assignmentService.deleteAssignmentByAssignmentIds(assignmentIds));
    }
}
