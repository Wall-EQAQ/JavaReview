package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysRoleMenu;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/3/21 22:24
 * @Modified by:
 */
public interface SysRoleMenuMapper {

    /**
     *  批量新增角色菜单信息
     * @param roleMenuList
     * @return
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
