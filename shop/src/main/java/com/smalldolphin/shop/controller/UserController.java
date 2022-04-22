package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.annotation.Log;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.service.PostService;
import com.smalldolphin.shop.service.RoleService;
import com.smalldolphin.shop.service.SysUserService;
import com.smalldolphin.shop.shiro.service.SysPasswordService;
import com.smalldolphin.shop.utils.AjaxResult;
import com.smalldolphin.shop.utils.ShiroUtil;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Created by Administrator on 2021/9/14 22:03
 * @Modified by:
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController  {
    private String prefix = "system/user";

    @Autowired
    private SysUserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private PostService postService;

    @GetMapping
    public String user() {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    //新增用户
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        modelMap.put("roles", roleService.selectRoleAll().stream().filter( role -> !role.isAdmin()).collect(Collectors.toList()));
        modelMap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysUser user) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            return AjaxResult.error("新增用户失败，登录账号已存在");
        }else if(UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户失败,手机号码已存在");
        }else if(UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户失败,邮箱已存在");
        }
        user.setSalt(ShiroUtil.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(),user.getSalt()));
        user.setCreateBy(ShiroUtil.getUser().getLoginName());
        return toAjax(userService.insertUser(user));
    }


    /**
     *  用户状态变更
     * @param user
     * @return
     */
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user) {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:user:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try{
            return toAjax(userService.deleteUserByIds(ids));
        }catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequiresPermissions("system:user:resetPwd")
    @GetMapping("/resetPwd")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap modelMap) {
        modelMap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @PostMapping("/resetPwd")
    @ResponseBody
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    public AjaxResult resetPwdSave(SysUser user) {
        userService.checkUserAllowed(user);
        user.setSalt(ShiroUtil.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(),user.getPassword(),user.getSalt()));
        if(userService.resetPwd(user) > 0) {
            if (ShiroUtil.getUser().getUserId().longValue() == user.getUserId().longValue()) {
                ShiroUtil.setUser(userService.selectUserById(user.getUserId()));
            }
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }



    //进入授权角色页
    @GetMapping("/authRole/{userId}")
    public String authRole(@PathVariable Long userId, ModelMap modelMap) {

        SysUser user = userService.selectUserById(userId);
        //获取用户所属的角色列表
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        modelMap.put("user", user);
        modelMap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(role -> !role.isAdmin()).collect(Collectors.toList()));
        return prefix + "/authRole";

    }
    //用户角色授权
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole/insertAuthRole")
    @ResponseBody
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.insertUserAuth(userId, roleIds);
        return AjaxResult.success();
    }

    //校验邮箱是否唯一
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return userService.checkEmailUnique(user);
    }

    //校验用户名
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }


    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return userService.checkPhoneUnique(user);
    }



}
