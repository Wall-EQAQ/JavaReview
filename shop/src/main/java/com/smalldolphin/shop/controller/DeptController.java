package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.annotation.Log;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.domain.SysDept;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.Ztree;
import com.smalldolphin.shop.service.DeptService;
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
 * @Created by Administrator on 2021/12/2 22:57
 * @Modified by:
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {
    private String prefix = "system/dept";

    @Autowired
    private DeptService deptService;


    /**
     *  新增部门
     * @param parentId
     * @param modelMap
     * @return 新增部门页面
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap modelMap) {
        modelMap.put("dept", deptService.selectDeptById(parentId));
        return prefix + "/add";
    }



    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDept dept) {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门失败，部门名称已经存在");
        }
        dept.setCreateBy(ShiroUtil.getUser().getLoginName());
        return toAjax(deptService.insertDept(dept));
    }

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept() {
        return prefix + "/dept";
    }

    @RequiresPermissions("system:dept:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept) {
        List<SysDept> deptList = deptService.selectDeptList(dept);
        return deptList;
    }

    //加载部门树列表
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    //加载部门树列表(排除下级)
    @GetMapping("/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) Long excludeId) {
        SysDept dept = new SysDept();
        dept.setDeptId(excludeId);
        List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
        return ztrees;
    }

    //校验部门名称是否唯一
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept) {
        return deptService.checkDeptNameUnique(dept);
    }

    //选择部门树
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") Long deptId, @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap modelMap) {
        modelMap.put("dept", deptService.selectDeptById(deptId));
        modelMap.put("excludeId", excludeId);
        return prefix + "/tree";
    }

    //加载角色部门列表树
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }





}
