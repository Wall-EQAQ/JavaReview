package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:    用户角色关联表
 * @Created by Administrator on 2022/1/18 22:00
 * @Modified by:
 */
public interface UserRoleMapper {

    //通过用户id删除用户和角色关联
    public int deleteUserRoleByUserId(Long userID);
    //批量新增用户角色关联
    public int batchUserRole(List<UserRole> userRoleList);
    //删除用户和角色关联信息
    public int deleteUserRoleInfo(UserRole userRole);
    //批量取消授权用户角色
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
    //通过角色ID查询角色使用数量
    public int countUserRoleByRoleId(Long roleId);


}
