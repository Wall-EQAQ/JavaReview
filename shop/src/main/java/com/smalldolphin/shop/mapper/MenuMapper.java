package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/15 17:51
 * @Modified by:
 */
public interface MenuMapper {

    Menu selectMenuById(Long menuId);


    /**
     * 查询系统菜单列表
     * @return
     */
    List<Menu> selectMenuList(Menu menu);

    List<Menu> selectMenuListByUserId(Menu menu);

    List<String> selectPermsByUserId(Long userId);

    /**
     * 查询系统所有菜单（含按钮）
     * @return
     */
    List<Menu> selectMenuAll();

    /**
     * 查询系统正常显示菜单（不含按钮）
     * @return
     */
    List<Menu> selectMenuNormalAll();

    /**
     * 根据用户ID查询菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuByUserId(Long userId);

    /**
     *  新增菜单信息
     * @param menu
     * @return
     */
    int insertMenu(Menu menu);

    int updateMenu(Menu menu);

    Menu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

    /**
     *  根据角色id查询菜单
     * @param roleId
     * @return
     */
    public List<String> selectMenuTree(Long roleId);

}
