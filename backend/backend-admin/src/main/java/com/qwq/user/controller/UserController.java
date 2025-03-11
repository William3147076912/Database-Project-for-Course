package com.qwq.user.controller;

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
import com.qwq.user.domain.User;
import com.qwq.user.service.IUserService;
import com.qwq.common.utils.poi.ExcelUtil;
import com.qwq.common.core.page.TableDataInfo;

/**
 * 用户信息Controller
 * 
 * @author william
 * @date 2025-03-10
 */
@RestController
@RequestMapping("/user/user")
public class UserController extends BaseController
{
    @Autowired
    private IUserService userService;

    /**
     * 查询用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(User user)
    {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:user:export')")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, User user)
    {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    /**
     * 获取用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(userService.selectUserByUserId(userId));
    }

    /**
     * 新增用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:user:add')")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody User user)
    {
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:user:edit')")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody User user)
    {
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:user:remove')")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByUserIds(userIds));
    }
}
