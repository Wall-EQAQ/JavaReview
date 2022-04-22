package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.annotation.Log;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.enums.BusinessType;
import com.smalldolphin.shop.domain.Menu;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.Ztree;
import com.smalldolphin.shop.service.MenuService;
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
 * @Created by Administrator on 2021/6/22 22:37
 * @Modified by:
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends BaseController {

    private String prefix = "system/menu";

    @Autowired
    MenuService menuService;

    @RequiresPermissions("system:menu:view")
    @GetMapping
    public String menu() {
        return prefix + "/menu";
    }

    @RequiresPermissions("system:menu:list")
    @PostMapping("/list")
    @ResponseBody
    public List<Menu> selectMenuList(Menu menu) {
        Long userId = ShiroUtil.getUser().getUserId();
        List<Menu> menuList = menuService.selectMenuList(menu, userId);
        return  menuList;
    }
    //新增
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap modelMap) {
        Menu menu = null;
        if (0L != parentId) {
            System.out.println("1");
            menu = menuService.selectMenuById(parentId);
            System.out.println("2");
        }else {
            menu = new Menu();
            menu.setMenuId(0L);
            menu.setMenuName("主目录");
            System.out.println("3");
        }
        modelMap.put("menu", menu);
        return prefix + "/add";
    }

    //新增保存菜单
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @RequiresPermissions("system:menu:add")
    @ResponseBody
    public AjaxResult addSave(@Validated Menu menu) {
        if (UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单" + menu.getMenuName() +"失败，菜单名称已存在");
        }
        menu.setCreateBy(ShiroUtil.getUser().getLoginName());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     *  修改菜单
     * @param menuId
     * @param modelMap
     * @return
     */
    @GetMapping("/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, ModelMap modelMap) {
        modelMap.put("menu", menuService.selectMenuById(menuId));
        return prefix + "/edit";
    }

    /**
     *  修改菜单保存
     * @param menu
     * @return
     */
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:menu:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Menu menu) {
        if (UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单失败，" + menu.getMenuName() + "菜单名称已存在");
        }
        menu.setUpdateBy(ShiroUtil.getUser().getLoginName());
        ShiroUtil.clearCachedAuthorizationInfo();
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     *  加载所有菜单列表树
     * @return
     */
    @GetMapping("/menuTreeData")
    @ResponseBody
    public List<Ztree> menuTreeData() {
        Long userId = ShiroUtil.getUser().getUserId();
        List<Ztree> ztrees = menuService.menuTreeData(userId);
        return ztrees;
    }

    @GetMapping("/roleMenuTreeData")
    @ResponseBody
    public List<Ztree> roleMenuTreeData(SysRole role) {
        Long userId = ShiroUtil.getUser().getUserId();
        List<Ztree> ztrees = menuService.roleMenuTreeData(role, userId);
        return ztrees;
    }

    //选择菜单树
    @GetMapping("/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap modelMap) {
        modelMap.put("menu", menuService.selectMenuById(menuId));
        return prefix + "/tree";
    }

}
