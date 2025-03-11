package com.qwq.resource.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qwq.resource.domain.vo.ResourceVO;
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
import com.qwq.resource.domain.Resource;
import com.qwq.resource.service.IResourceService;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.common.core.page.TableDataInfo;

/**
 * 教学资源Controller
 *
 * @author william
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/resource/resource")
public class ResourceController extends BaseController {
    @Autowired
    private IResourceService resourceService;

    /**
     * 查询教学资源列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(Resource resource) {
        startPage();
        List<ResourceVO> list = resourceService.selectResourceList(resource);
        return getDataTable(list);
    }

    /**
     * 导出教学资源列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:export')")
    @Log(title = "教学资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Resource resource) {
        List<ResourceVO> list = resourceService.selectResourceList(resource);
        ExcelUtil<ResourceVO> util = new ExcelUtil<>(ResourceVO.class);
        util.exportExcel(response, list, "教学资源数据");
    }

    /**
     * 获取教学资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:query')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId) {
        return success(resourceService.selectResourceByResourceId(resourceId));
    }

    /**
     * 新增教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:add')")
    @Log(title = "教学资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Resource resource) {
        return toAjax(resourceService.insertResource(resource));
    }

    /**
     * 修改教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:edit')")
    @Log(title = "教学资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Resource resource) {
        return toAjax(resourceService.updateResource(resource));
    }

    /**
     * 删除教学资源
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:remove')")
    @Log(title = "教学资源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds) {
        return toAjax(resourceService.deleteResourceByResourceIds(resourceIds));
    }
}
