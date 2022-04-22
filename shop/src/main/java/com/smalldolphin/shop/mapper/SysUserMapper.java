package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysUser;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/5/15 14:47
 * @Modified by:
 */
public interface SysUserMapper {

    //新增保存用户信息
    int insertUser(SysUser user);

    //更新用户方法
    int updateUser(SysUser user);
    //根据用户id查询用户
    SysUser selectUserById(Long userId);
    //根据条件分页查询用户列表
    public List<SysUser> selectUserList(SysUser user);
    //通过登录名查询用户信息(包括角色，部门信息)
    SysUser selectUserByLoginName(String userName);

    //批量删除用户信息
    int deleteUserByIds(Long[] ids);

    //校验登录名是否唯一
    int checkLoginNameUnique(String loginName);

    public SysUser checkPhoneUnique(String phonenumber);
    //校验邮箱是否唯一
    public SysUser checkEmailUnique(String email);


}
