package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/11/30 22:36
 * @Modified by:
 */
public interface DeptMapper {

    SysDept selectDeptById(Long deptId);

    List<SysDept> selectDeptList(SysDept dept);

    /**
     *  新增部门信息
     * @param dept
     * @return
     */
    public int insertDept(SysDept dept);

    /**
     * 根据角色id查询部门列表(数据权限)
     * @param roleId
     * @return
     */
    List<String> selectRoleDeptList(Long roleId);

    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

}
