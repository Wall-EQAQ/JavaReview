package com.smalldolphin.shop.service;

import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.domain.Menu;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.domain.Ztree;
import com.smalldolphin.shop.mapper.MenuMapper;
import com.smalldolphin.shop.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/15 18:48
 * @Modified by:
 */
@Service
public class MenuService {
    @Autowired
    private MenuMapper menuMapper;

    /**
     *  根据菜单ID查询信息
     * @param menuId
     * @return
     */
    public Menu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     *  校验菜单名称是否唯一
     * @param menu
     * @return
     */
    public String checkMenuNameUnique(Menu menu) {
       Long menuId =  StringUtil.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        Menu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        //当根据前台传入的新增menu信息。如果在数据库中能查到，表示菜单名称不唯一
        if (!StringUtil.isNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.MENU_NAME_NOT_UNIQUE;
        }
        return UserConstants.MENU_NAME_UNIQUE;
    }


    public List<Menu> selectMenuList(Menu menu, Long userId) {
        List<Menu> menuList = null;
        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuList(menu);
        }else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     *  查询所有菜单
     * @param userId
     * @return
     */
    public List<Menu> selectMenuAll(Long userId) {
        List<Menu> menuList = null;
        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuAll();
        }else{
            menuList = menuMapper.selectMenuByUserId(userId);
        }
        return menuList;
    }

    /**
     * 根据用户id查询对应的菜单
     * @param user
     * @return
     */
    public List<Menu> selectMenuByUser(SysUser user) {

        List<Menu> menus = new LinkedList<>();
        if (user.isAdmin()) {
            menus = menuMapper.selectMenuNormalAll();
        }else {
            menus = menuMapper.selectMenuByUserId(user.getUserId());
        }
        //为什么直接传 0
        return getChildPerms(menus, 0);
    }

    /**
     *  根据用户id查询对应的权限
     * @param userId
     * @return
     */
    public Set<String> selectPermsByUserId(Long userId){
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     *  得到子节点列表
     * @param list
     * @param menu
     * @return
     */
    private List<Menu> getChildList(List<Menu> list, Menu menu) {
        List<Menu> mlist = new ArrayList<>();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext()) {
            Menu m = it.next();
            if (m.getParentId().longValue() == menu.getMenuId().longValue()) {
                mlist.add(m);
            }
        }
        return mlist;
    }

    /**
     *  判断当前节点是否有子节点
     * @param list
     * @param m
     * @return
     */
    private boolean hasChild(List<Menu> list, Menu m) {
        return getChildList(list,m).size() > 0 ? true : false;
    }

    /**
     * 根据父节点的ID获取所有子节点
     * @param list
     * @param parentId
     * @return
     */
    public List<Menu> getChildPerms(List<Menu> list, int parentId) {

        List<Menu> returnList = new ArrayList<>();
        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
            Menu m = iterator.next();
            //根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (m.getParentId() == parentId) {
                recursionFn(list, m);
                returnList.add(m);
            }
        }
        return returnList;
    }

    /**
     *  递归列表
     * @param list
     * @param menu
     */
    private void recursionFn(List<Menu> list, Menu menu) {
        List<Menu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (Menu tchild : childList) {
            if (hasChild(list, tchild)) {
                recursionFn(list, tchild);
            }
        }
    }

    public int insertMenu(Menu menu) {
        return menuMapper.insertMenu(menu);
    }

    public int updateMenu(Menu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     *  对象转菜单树
     * @param menuList  菜单列表
     * @return  树结构列表
     */
    public List<Ztree> initZtree(List<Menu> menuList) {
        return initZtree(menuList, null, false);
    }

    /**
     *  对象转菜单树
     * @param menuList  菜单列表
     * @param roleMenuLists 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识符
     * @return  树结构列表
     */
    public List<Ztree> initZtree(List<Menu> menuList, List<String> roleMenuLists, boolean permsFlag) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtil.isNotNull(roleMenuLists);
        for (Menu menu : menuList) {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setTitle(menu.getMenuName());
            ztree.setName(transMenuName(menu, permsFlag));
            if (isCheck){
                //???
                ztree.setChecked(roleMenuLists.contains(menu.getMenuId() + menu.getPerms()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transMenuName(Menu menu, boolean permsFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getMenuName());
        if (permsFlag) {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
        }
        return sb.toString();
    }

    /**
     *  查询所有菜单
     * @param userId
     * @return  菜单列表
     */
    public List<Ztree> menuTreeData(Long userId) {
        List<Menu> menuList = selectMenuAll(userId);
        List<Ztree> ztrees = initZtree(menuList);
        return ztrees;
    }


    /**
     *  根据角色ID查询菜单
     * @param role
     * @param userId
     * @return
     */
    public List<Ztree> roleMenuTreeData(SysRole role, Long userId) {
        Long roleId = role.getRoleId();

        List<Ztree> ztrees = new ArrayList<>();
        List<Menu> menuList = selectMenuAll(userId);
        if (StringUtil.isNotNull(roleId)) {
            List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
        }else {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }



}
