package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.UserPost;

import java.util.List;

/**
 * @Description:    用户与岗位关联表
 * @Created by Administrator on 2022/1/24 16:23
 * @Modified by:
 */
public interface UserPostMapper {

    //通过用户id删除用户和岗位关联
    public int deleteUserPostByUserId(Long userId);

    public int batchUserPost(List<UserPost> userPostList);
}
