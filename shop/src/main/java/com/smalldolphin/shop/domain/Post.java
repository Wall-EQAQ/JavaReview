package com.smalldolphin.shop.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/24 6:34
 * @Modified by:
 */
public class Post extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long postId;

    private String postCode;

    private String postName;

    private String postSort;
    //状态    0正常 1停用
    private String status;
    //用户是否存在此岗位标识   默认不存在
    private boolean flag = false;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostSort() {
        return postSort;
    }

    public void setPostSort(String postSort) {
        this.postSort = postSort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("postId", getPostId())
                .append("postCode", getPostCode())
                .append("postName", getPostName())
                .append("postSort", getPostSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
