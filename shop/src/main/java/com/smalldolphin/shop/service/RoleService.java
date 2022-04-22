package com.smalldolphin.shop.service;

import com.smalldolphin.shop.annotation.DataScope;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.SysRoleMenu;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.domain.UserRole;
import com.smalldolphin.shop.mapper.SysRoleMapper;
import com.smalldolphin.shop.mapper.SysRoleMenuMapper;
import com.smalldolphin.shop.mapper.UserRoleMapper;
import com.smalldolphin.shop.utils.Convert;
import com.smalldolphin.shop.utils.SpringUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/16 22:46
 * @Modified by:
 */
@Service
public class RoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     *  新增保存角色信息
     * @param role
     * @return
     */
    public int insertRole(SysRole role) {
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     *  新增角色菜单信息
     * @param role
     * @return
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        List<SysRoleMenu> list = new ArrayList<>();

        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }

        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }


    /**
     *  根据条件查询角色数据
     * @param role
     * @return
     */
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     *  查询所有角色
     * @return
     */
    public List<SysRole> selectRoleAll() {
        return SpringUtil.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     *  根据用户id查询用户所属角色列表
     * @param userId
     * @return
     */
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolesByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }
    //根据角色id查询角色
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     *  取消用户角色授权
     * @param userRole  用户喝角色关联信息
     * @return  结果
     */
    public int deleteAuthUser(UserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     *  批量取消授权用户角色
     * @param roleId
     * @param userIds   需要删除的用户数据ID
     * @return  结果
     */
    public int deleteAuthUsers(Long roleId, String userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    //校验角色名称是否唯一
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    //校验角色权限字符是否唯一
    public String checkRoleKeyUnique(SysRole role) {
        Long userId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtil.isNotNull(info) && info.getRoleId() != role.getRoleId()) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     *  批量删除角色信息
     * @param ids  需要删除的数据Id
     * @return
     * @throws Exception
     */
    public int deleteRoleByIds(String ids) throws Exception {
        Long[] roleIds = Convert.toLongArray(ids);
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole sysRole = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new BaseException("角色已分配，不能删除");
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     *  校验角色是否允许操作
     * @param role
     */
    public void checkRoleAllowed(SysRole role) {
        if (StringUtil.isNotNull(role) && role.isAdmin()) {
            throw new BaseException("不允许操作超级管理员角色");
        }
    }


    /**
     *  通过角色id查询角色使用数量
     * @param roleId
     * @return
     */
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }


}
