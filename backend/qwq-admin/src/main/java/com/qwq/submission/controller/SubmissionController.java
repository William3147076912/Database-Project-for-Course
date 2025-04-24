package com.qwq.submission.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.qwq.submission.domain.Submission;
import com.qwq.submission.service.ISubmissionService;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.common.core.page.TableDataInfo;

/**
 * 作业提交记录Controller
 *
 * @author william
 * @date 2025-03-14
 */
@RestController
@RequestMapping("/submission/submission")
public class SubmissionController extends BaseController {
    @Autowired
    private ISubmissionService submissionService;

    /**
     * 查询作业提交记录列表
     */
    @PreAuthorize("@ss.hasPermi('submission:submission:list')")
    @GetMapping("/list")
    public TableDataInfo list(Submission submission) {
        startPage();
        List<Submission> list = submissionService.selectSubmissionList(submission);
        return getDataTable(list);
    }

    /**
     * 导出作业提交记录列表
     */
    @PreAuthorize("@ss.hasPermi('submission:submission:export')")
    @Log(title = "作业提交记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Submission submission) {
        List<Submission> list = submissionService.selectSubmissionList(submission);
        ExcelUtil<Submission> util = new ExcelUtil<Submission>(Submission.class);
        util.exportExcel(response, list, "作业提交记录数据");
    }

    /**
     * 获取作业提交记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('submission:submission:query')")
    @GetMapping(value = "/{submissionId}")
    public AjaxResult getInfo(@PathVariable("submissionId") Long submissionId) {
        return success(submissionService.selectSubmissionBySubmissionId(submissionId));
    }

    /**
     * 新增作业提交记录
     */
    @Log(title = "作业提交记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Submission submission) {
        return toAjax(submissionService.insertSubmission(submission));
    }

    /**
     * 修改作业提交记录
     */
//    @PreAuthorize("@ss.hasPermi('submission:submission:edit')")
    @Log(title = "作业提交记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Submission submission) {
        return toAjax(submissionService.updateSubmission(submission));
    }

    /**
     * 删除作业提交记录
     */
    @PreAuthorize("@ss.hasPermi('submission:submission:remove')")
    @Log(title = "作业提交记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{submissionIds}")
    public AjaxResult remove(@PathVariable Long[] submissionIds) {
        return toAjax(submissionService.deleteSubmissionBySubmissionIds(submissionIds));
    }
}
