package com.smalldolphin.shop.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/15 19:02
 * @Modified by:    菜单权限
 */

public class Menu extends BaseEntity {

    /** 菜单ID */
    private Long menuId;

    /** 菜单名称 */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 1,max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /** 父菜单名称 */
    private String parentName;

    /** 父菜单ID */
    private Long parentId;

    /** 显示顺序 */
    @NotBlank(message = "显示顺序不能为空")
    private String orderNum;

    /** 菜单URL */
    @Size(min = 0, max = 200, message = "请求地址不能超过200个字符")
    private String url;

    /** 打开方式：menuItem页签 menuBlank新窗口 */
    private String target;

    /** 类型:0目录,1菜单,2按钮 */
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    /** 菜单状态:0显示,1隐藏 */
    private String visible;

    /** 权限字符串 */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 子菜单 */
    private List<Menu> children = new ArrayList<>();

    public Menu() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    /**
     * 百度说这样可以防止内存溢出
     * 大部分实体的toString()方法都用的是简单的"+"，而每"＋"一个就会new一个String对象，这样如果系统内存小的话会暴内存
     * 使用ToStringBuilder可以避免
     * @return
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("menuId",getMenuId())
                .append("menuName",getMenuName())
                .append("parentId", getParentId())
                .append("orderNum", getOrderNum())
                .append("url", getUrl())
                .append("target", getTarget())
                .append("menuType", getMenuType())
                .append("visible", getVisible())
                .append("perms", getPerms())
                .append("icon", getIcon())
                .append("createBy",getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }

}
