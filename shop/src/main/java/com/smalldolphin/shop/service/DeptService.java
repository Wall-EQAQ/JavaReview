package com.smalldolphin.shop.service;

import com.smalldolphin.shop.annotation.DataScope;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.domain.SysDept;
import com.smalldolphin.shop.domain.SysRole;
import com.smalldolphin.shop.domain.Ztree;
import com.smalldolphin.shop.mapper.DeptMapper;
import com.smalldolphin.shop.utils.SpringUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/11/30 22:41
 * @Modified by:
 */
@Service
public class DeptService {
    @Autowired
    private DeptMapper deptMapper;

    /**
     *  查询部门信息集合
     * @param dept
     * @return
     */
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     *  新增保存部门信息
     * @param dept
     * @return
     */
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        //如果父部门状态异常，则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new BaseException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    @DataScope(deptAlias = "d")
    public List<Ztree> selectDeptTree(SysDept dept) {
        List<SysDept> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    public List<Ztree> initZtree(List<SysDept> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     * @param deptList  部门列表
     * @param roleDeptList  角色已存在菜单列表
     * @return  树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isChecked = StringUtil.isNotNull(roleDeptList);
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isChecked) {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName() ));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 根据角色ID查询部门（数据权限）
     * @param role
     * @return
     */
    public List<Ztree> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<>();
        List<SysDept> deptList = selectDeptList(new SysDept());
        if (StringUtil.isNotNull(roleId)) {
            List<String> roleDeptList = deptMapper.selectRoleDeptList(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        }else {
            //角色id为空，则展示所有部门树信息
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    /**
     *  查询部门管理树(排除下级)  ???
     * @param dept
     * @return  所有部门信息
     */
    @DataScope(deptAlias = "d")
    public List<Ztree> selectDeptTreeExcludeChild(SysDept dept) {
        Long deptId = dept.getDeptId();
        List<SysDept> deptList = deptMapper.selectDeptList(dept);
        Iterator<SysDept> iterator = deptList.iterator();
        while (iterator.hasNext()) {
            SysDept d = iterator.next();
            if (d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(),","), deptId + "")) {
                iterator.remove();
            }
        }
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    //校验部门名称是否唯一
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtil.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtil.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }
}
