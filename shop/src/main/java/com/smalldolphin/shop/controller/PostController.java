package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.domain.Post;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.service.PostService;
import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2022/2/28 22:05
 * @Modified by:
 */
@Controller
@RequestMapping("/system/post")
public class PostController extends BaseController {

    private String prefix = "system/post";

    @Autowired
    private PostService postService;

    @RequiresPermissions("system:post:view")
    @GetMapping()
    public String post() {
        return prefix + "/post";
    }

    @RequiresPermissions("system:post:list")
    @ResponseBody
    @PostMapping("/list")
    public TableDataInfo list(Post post) {
        startPage();
        List<Post> postList = postService.selectPostList(post);
        return getDataTable(postList);
    }


    @PostMapping("/checkPostNameUnique")
    public String checkPostNameUnique(Post post) {
        return postService.checkPostNameUnique(post);
    }


}
