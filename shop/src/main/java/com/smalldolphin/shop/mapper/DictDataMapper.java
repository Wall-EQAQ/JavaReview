package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.DictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/12/8 20:33
 * @Modified by:
 */
public interface DictDataMapper {
    //根据字典类型获取字典数据
    List<DictData> selectDictDataByType(String dictData);


    public int countDictDataByType(String dictType);

    public int updateDictDataType(@Param("oldDictType") String oldDictType,@Param("newDictType") String newDictType);
}
