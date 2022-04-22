package com.smalldolphin.shop.service;

import com.smalldolphin.shop.annotation.DataScope;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.domain.SysUser;
import com.smalldolphin.shop.domain.UserPost;
import com.smalldolphin.shop.domain.UserRole;
import com.smalldolphin.shop.mapper.SysRoleMapper;
import com.smalldolphin.shop.mapper.SysUserMapper;
import com.smalldolphin.shop.mapper.UserPostMapper;
import com.smalldolphin.shop.mapper.UserRoleMapper;
import com.smalldolphin.shop.utils.Convert;
import com.smalldolphin.shop.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:    用户  业务层处理
 * @Created by Administrator on 2021/5/15 14:45
 * @Modified by:
 */
@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserPostMapper userPostMapper;


    //新增保存用户信息
    @Transactional
    public int insertUser(SysUser user) {
        //新增用户信息
        int rows = userMapper.insertUser(user);
        //新增用户与岗位关联
        insertUserPost(user);
        //新增用户与角色关联
        insertUserRole(user.getUserId(), user.getRoleIds());
        return rows;
    }


    /**
     * 修改用户个人信息
     * @param user
     * @return
     */
    public int updateUser(SysUser user){

        Long userId = user.getUserId();
        //删除用户角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        //新增用户角色关联
        insertUserRole(user.getUserId(), user.getRoleIds());
        //删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        //新增用户与岗位关联
        insertUserPost(user);

        return userMapper.updateUser(user);
    }

    //根据分页条件查询用户列表
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     *  根据登录名去数据库中查询用户信息
     * @param userName
     * @return
     */
    public SysUser selectUserByLoginName(String userName){
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     *  校验用户是否允许操作
     * @param user
     */
    public void checkUserAllowed(SysUser user) {
        if (StringUtil.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new BaseException("不允许操作超级管理员用户");
        }
    }
    //用户状态修改
    public int changeStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     *  批量删除用户信息
     * @param ids   需要删除的数据id
     * @return
     */
    public int deleteUserByIds(String ids) throws BaseException {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        int i = userMapper.deleteUserByIds(userIds);
        System.out.println(i);
        return i;
    }
    //根据用户id查询用户
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     *  用户角色授权
     * @param userId    用户ID
     * @param roleIds   角色组
     */
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    //新增用户角色信息
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtil.isNotNull(roleIds)) {
            List<UserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                list.add(userRole);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    //修改密码
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    //新增用户岗位信息
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtil.isNotNull(posts)) {
            List<UserPost> list = new ArrayList<>();
            for (Long postId : posts) {
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     *  校验登录名称是否唯一
     * @param loginName
     * @return
     */
    public String checkLoginNameUnique(String loginName) {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    //校验手机号码是否唯一
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtil.isNotNull(user) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if(StringUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     *  校验email是否唯一
     * @param user 用户信息
     * @return
     */
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtil.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }else {
            return UserConstants.USER_EMAIL_UNIQUE;
        }

    }




}
