package com.qwq.enrollment.controller;

import com.qwq.common.annotation.Log;
import com.qwq.common.core.controller.BaseController;
import com.qwq.common.core.domain.AjaxResult;
import com.qwq.common.core.page.TableDataInfo;
import com.qwq.common.enums.BusinessType;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.enrollment.domain.CourseEnrollment;
import com.qwq.enrollment.domain.vo.CourseEnrollmentVO;
import com.qwq.enrollment.service.ICourseEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 选课记录Controller
 *
 * @author william
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/enrollment/enrollment")
public class CourseEnrollmentController extends BaseController {
    @Autowired
    private ICourseEnrollmentService courseEnrollmentService;

    /**
     * 查询选课记录列表
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseEnrollment courseEnrollment) {
        startPage();
        List<CourseEnrollmentVO> list = courseEnrollmentService.selectCourseEnrollmentList(courseEnrollment);
        return getDataTable(list);
    }

    /**
     * 导出选课记录列表
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:export')")
    @Log(title = "选课记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseEnrollment courseEnrollment) {
        List<CourseEnrollmentVO> list = courseEnrollmentService.selectCourseEnrollmentList(courseEnrollment);
        ExcelUtil<CourseEnrollmentVO> util = new ExcelUtil<>(CourseEnrollmentVO.class);
        util.exportExcel(response, list, "选课记录数据");
    }

    /**
     * 获取选课记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:query')")
    @GetMapping(value = "/{enrollmentId}")
    public AjaxResult getInfo(@PathVariable("enrollmentId") Long enrollmentId) {
        return success(courseEnrollmentService.selectCourseEnrollmentByEnrollmentId(enrollmentId));
    }

    /**
     * 新增选课记录
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:add')")
    @Log(title = "选课记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseEnrollment courseEnrollment) {
        return toAjax(courseEnrollmentService.insertCourseEnrollment(courseEnrollment));
    }

    /**
     * 修改选课记录
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:edit')")
    @Log(title = "选课记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseEnrollment courseEnrollment) {
        return toAjax(courseEnrollmentService.updateCourseEnrollment(courseEnrollment));
    }

    /**
     * 删除选课记录
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:remove')")
    @Log(title = "选课记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{enrollmentIds}")
    public AjaxResult remove(@PathVariable Long[] enrollmentIds) {
        return toAjax(courseEnrollmentService.deleteCourseEnrollmentByEnrollmentIds(enrollmentIds));
    }

    /**
     * 根据courseId删除选课记录
     */
    @PreAuthorize("@ss.hasPermi('enrollment:enrollment:remove')")
    @Log(title = "选课记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/course/{courseId}")
    public AjaxResult removeByCourseId(@PathVariable Long courseId) {
        return toAjax(courseEnrollmentService.deleteCourseEnrollmentByCourseId(courseId));
    }
}
