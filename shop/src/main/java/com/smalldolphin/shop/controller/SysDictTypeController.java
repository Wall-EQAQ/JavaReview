package com.smalldolphin.shop.controller;

import com.smalldolphin.shop.domain.DictType;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.service.DictTypeService;
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
 * @Created by Administrator on 2022/3/17 21:34
 * @Modified by:
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {

    private String prefix = "system/dict/type";

    @Autowired
    private DictTypeService dictTypeService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictType() {
        return prefix + "/type";
    }


    @RequiresPermissions("system:dict:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DictType dictType) {
        startPage();
        List<DictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }
}
