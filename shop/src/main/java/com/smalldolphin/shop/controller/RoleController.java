package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.annotation.Log;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.UserRole;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.service.RoleService;
import com.smalldolphin.shop.utils.AjaxResult;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/18 21:49
 * @Modified by:
 */

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    private String prefix = "system/role";

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role() {
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     *  新增保存角色
     * @param role
     * @return
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role) {

        if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色失败，角色名称已存在");
        }else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("新增角色失败,角色权限已存在");
        }
        role.setCreateBy(ShiroUtil.getUser().getLoginName());
        ShiroUtil.clearCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));
    }

    //角色分配数据权限页面
    @GetMapping("/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        SysRole role = roleService.selectRoleById(roleId);
        modelMap.put("role", role);
        return prefix + "/dataScope";
    }

    @RequiresPermissions("system:role:edit")
    @GetMapping("/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        modelMap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }

    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap modelMap) {
        modelMap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/auth/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(UserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/auth/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }


    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(SysRole role) {
        return roleService.checkRoleNameUnique(role);
    }

    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(SysRole role) {
        return roleService.checkRoleKeyUnique(role);
    }


    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            System.out.println(roleService.deleteRoleByIds(ids));//0
            return toAjax(roleService.deleteRoleByIds(ids));
        }catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }












}
