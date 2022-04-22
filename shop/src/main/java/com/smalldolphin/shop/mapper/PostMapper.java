package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.Post;
import javafx.geometry.Pos;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/1/24 16:21
 * @Modified by:
 */
public interface PostMapper {

    public List<Post> selectPostList(Post post);

    //查询所有岗位
    public List<Post> selectPostAll();

    //校验岗位名称是否唯一
    public Post checkPostNameUnique(String postName);

    //校验岗位编码是否唯一
    public Post checkPostCodeUnique(String postCode);

}
