package com.smalldolphin.shop.service;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.common.constant.UserConstants;
import com.smalldolphin.shop.common.exception.BaseException;
import com.smalldolphin.shop.domain.DictData;
import com.smalldolphin.shop.domain.DictType;
import com.smalldolphin.shop.domain.Ztree;
import com.smalldolphin.shop.mapper.DictDataMapper;
import com.smalldolphin.shop.mapper.DictTypeMapper;
import com.smalldolphin.shop.utils.Convert;
import com.smalldolphin.shop.utils.DictUtil;
import com.smalldolphin.shop.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.smalldolphin.shop.utils.StringUtil.isNull;

/**
 * @Description:shop
 * @Created by Administrator on 2021/12/7 23:30
 * @Modified by:
 */
@Service
public class DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     *  项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     *  加载字典缓存数据
     */
    public void loadingDictCache() {
        List<DictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for(DictType dictType : dictTypeList) {
            List<DictData> dictData = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtil.setDictCache(dictType.getDictType(), dictData);
        }
    }



    /**
     *  根据字典类型查询字典数据
     * @param dictType
     * @return
     */
    public List<DictData> selectDictDataByType(String dictType) {
        //缓存中有，从缓存中取
        List<DictData> dictData = DictUtil.getDictCache(dictType);
        if (StringUtil.isNotEmpty(dictData)) {
            return dictData;
        }
        //缓存中没有，从数据库查询
        dictData = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtil.isNotEmpty(dictData)) {
            DictUtil.setDictCache(dictType, dictData);
            return dictData;
        }
        return null;
    }

    /**
     *  根据分页条件查询查询字典类型
     * @param dictType
     * @return
     */
    public List<DictType> selectDictTypeList(DictType dictType) {
        return dictTypeMapper.selectDictTypeList(dictType);
    }


    /**
     *  查询所有字典类型
     * @return
     */
    public List<DictType> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     *  根据字典类型ID查询信息
     * @param dictId
     * @return
     */
    public DictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    public DictType selectDictTypeByType(String dictType) {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     *  批量删除字典类型
     * @param ids
     */
    public void deleteDictTypeByIds(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            DictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new BaseException("已分配，不能删除");
            }
            dictTypeMapper.deleteDictTypeById(dictId);
            DictUtil.removeDictCache(dictType.getDictType());
        }
    }

    /**
     *  清空字典缓存
     */
    public void clearDictCache() {
        DictUtil.clearDictCache();
    }

    /**
     *  新增保存字典类型信息
     * @param dict
     * @return
     */
    public int insertDictType(DictType dict) {
        int row = dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtil.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     *  修改保存字典类型信息
     * @param dict
     * @return
     */
    public int updateDictType(DictType dict) {
        DictType oldDict = dictTypeMapper.selectDictTypeById(dict.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<DictData> dictData = dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtil.setDictCache(dict.getDictType(), dictData);
        }
        return row;
    }

    /**
     *  校验字典类型是否唯一
     * @param dict
     * @return
     */
    public String checkDictTypeUnique(DictType dict) {
       Long dictId =  StringUtil.isNull(dict.getDictId()) ? -1 : dict.getDictId();
        DictType info = dictTypeMapper.checkDictTypeUnique(dict);
        if (StringUtil.isNotNull(info) && info.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    /**
     *  查询字典类型树
     * @param dict
     * @return
     */
    public List<Ztree> selectDictTree(DictType dict) {
        List<Ztree> ztrees = new ArrayList<>();
        List<DictType> dictTypeList = dictTypeMapper.selectDictTypeList(dict);
        for (DictType dictType : dictTypeList) {
            Ztree ztree = new Ztree();
            ztree.setId(dictType.getDictId());
            ztree.setName(transDictName(dictType));
            ztree.setTitle(dictType.getDictType());
            ztrees.add(ztree);
        }

        return ztrees;
    }


    public String transDictName(DictType dictType) {
        StringBuffer sb = new StringBuffer();
        sb.append("(" + dictType.getDictName() + ")");
        sb.append("&nbsp;&nbsp;&nbsp;" + dictType.getDictType());
        return sb.toString();
    }
















}
