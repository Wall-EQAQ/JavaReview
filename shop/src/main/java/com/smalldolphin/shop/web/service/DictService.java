package com.smalldolphin.shop.web.service;

import com.smalldolphin.shop.domain.DictData;
import com.smalldolphin.shop.service.DictDataService;
import com.smalldolphin.shop.service.DictTypeService;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:    html调用 thymeleaf 实现字典读取
 * @Created by Administrator on 2021/12/7 22:27
 * @Modified by:
 */

@Service("dict")
public class DictService {

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private DictTypeService dictTypeService;

    /**
     *  根据字典类型查询字典数据信息
     * @param dictType
     * @return
     */
    public List<DictData> getType(String dictType) {
        return dictTypeService.selectDictDataByType(dictType);
    }



}
