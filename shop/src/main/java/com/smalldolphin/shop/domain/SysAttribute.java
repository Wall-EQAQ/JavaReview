package com.smalldolphin.shop.domain;

import org.apache.shiro.SecurityUtils;

import java.text.SimpleDateFormat;

/**
 * @Description:shop
 * @Created by Administrator on 2021/4/17 9:14
 * @Modified by:
 */
public class SysAttribute {
    //主键id
    private Integer attrId;
    //属性名称
    private String attrName;
    //外键，类型id
    private Integer catId;
    //only:输入框(唯一)  many:后台下拉列表/前台单选框
    private String attrSel;
    //manual:手工录入  list:从列表选择
    private String attrWrite;
    //可选值列表信息,例如颜色：白色,红色,绿色,多个可选值通过逗号分隔
    private String attrVals;
    //删除时间标志
    private Integer deleteTime;

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getAttrSel() {
        return attrSel;
    }

    public void setAttrSel(String attrSel) {
        this.attrSel = attrSel;
    }

    public String getAttrWrite() {
        return attrWrite;
    }

    public void setAttrWrite(String attrWrite) {
        this.attrWrite = attrWrite;
    }

    public String getAttrVals() {
        return attrVals;
    }

    public void setAttrVals(String attrVals) {
        this.attrVals = attrVals;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public SysAttribute() {
    }

    public SysAttribute(Integer attrId, String attrName, Integer catId, String attrSel, String attrWrite, String attrVals, Integer deleteTime) {
        this.attrId = attrId;
        this.attrName = attrName;
        this.catId = catId;
        this.attrSel = attrSel;
        this.attrWrite = attrWrite;
        this.attrVals = attrVals;
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        return "SysAttribute{" +
                "attrId=" + attrId +
                ", attrName='" + attrName + '\'' +
                ", catId=" + catId +
                ", attrSel='" + attrSel + '\'' +
                ", attrWrite='" + attrWrite + '\'' +
                ", attrVals='" + attrVals + '\'' +
                ", deleteTime=" + deleteTime +
                '}';
    }
}
