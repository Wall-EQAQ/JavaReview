package com.smalldolphin.shop.domain;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/4/14 20:25
 * @Modified by:
 */
public class SysUser extends BaseEntity{

    //快速序列化 鼠标停留在类上  Alt+Enter  即可  前面已经配置好了
    private static final long serialVersionUID = 1;
    //用户id
    private Long userId;
    //部门id
    private Long deptId;
    //登录名
    private String loginName;
    //用户名
    private String userName;
    //头像
    private String avatar;
    //年龄
    private Integer age;
    //密码
    private String password;
    //邮箱
    private String email;
    //性别
    private String sex;
    //手机号
    private String phonenumber;
    //盐加密
    private String salt;
    //账号状态 0 正常 1 停用
    private String status;
    //删除标志 0 存在 1代表删除
    private String delFlag;
    //登录的IP
    private String loginIp;
    //登录时间
    private Date loginDate;
    //用户类型
    private String userType;

    private SysDept dept;

    private List<SysRole> roles;
    //角色组
    private Long[] roleIds;
    //岗位组
    private Long[] postIds;

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public SysUser() {
    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    //增加了用户是否为管理员的判断
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }
    public static boolean isAdmin(Long userId) {
        return userId != null && userId == 1L;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }


}
