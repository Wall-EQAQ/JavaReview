package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysRole;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/3/1 21:33
 * @Modified by:
 */
public interface SysRoleMapper {

    public int insertRole(SysRole role);

    public int deleteRoleByIds(Long[] ids);

    public List<SysRole> selectRolesByUserId(Long userId);

    public List<SysRole> selectRoleList(SysRole role);
    //根据角色id查询角色
    public SysRole selectRoleById(Long roleId);

    //校验角色名称是否唯一
    public SysRole checkRoleNameUnique(String roleName);
    //校验角色权限字符是否唯一
    public SysRole checkRoleKeyUnique(String roleKey);


}
