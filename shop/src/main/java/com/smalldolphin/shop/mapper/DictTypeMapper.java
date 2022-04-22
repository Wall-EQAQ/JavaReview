package com.smalldolphin.shop.mapper;

import com.smalldolphin.shop.domain.DictType;

import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/12/8 22:07
 * @Modified by:
 */
public interface DictTypeMapper {

    //根据条件分页查询字典类型
    public List<DictType> selectDictTypeList(DictType dictType);

    public DictType selectDictTypeByType(String dictType);

    public List<DictType> selectDictTypeAll();

    public DictType selectDictTypeById(Long dictId);

    public int deleteDictTypeById(Long dictId);

    public int insertDictType(DictType dict);

    public int updateDictType(DictType dict);

    public DictType checkDictTypeUnique(DictType dict);

}
