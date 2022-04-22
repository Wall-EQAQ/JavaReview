package com.smalldolphin.shop.domain;

import com.smalldolphin.shop.utils.StringUtil;

/**
 * @Description:    分页数据
 * @Created by Administrator on 2021/6/25 15:23
 * @Modified by:
 */
public class PageDomain extends BaseEntity{

    /** 当前记录起始索引 */
    private Integer pageNum;
    /** 每页显示记录数 */
    private Integer pageSize;
    /** 排序列  */
    private String orderByColumn;
    /** 排序的方向desc或者asc  */
    private String isAsc = "asc";

    public String getOrderBy() {
        if (StringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtil.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }
}
