package com.smalldolphin.shop.service;

import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.domain.Post;
import com.smalldolphin.shop.mapper.PostMapper;
import com.smalldolphin.shop.utils.StringUtil;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/24 16:20
 * @Modified by:
 */

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    //查询岗位相关信息集合
    public List<Post> selectPostList(Post post) {
        return postMapper.selectPostList(post);
    }

    //查询所有岗位
    public List<Post> selectPostAll(){
        return postMapper.selectPostAll();
    }

    /**
     *  校验部门名称是否唯一
     * @param post  岗位信息
     * @return  结果
     */
    public String checkPostNameUnique(Post post) {
        Long postId = StringUtil.isNull(post.getPostId()) ? -1L : post.getPostId();
        Post info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    /**
     *  校验岗位编码是否唯一
     * @param post
     * @return
     */
    public String checkPostCodeUnique(Post post) {
        Long postId = StringUtil.isNull(post.getPostCode()) ? -1L : post.getPostId();
        Post info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }



}
