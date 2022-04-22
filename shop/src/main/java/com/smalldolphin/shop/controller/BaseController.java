package com.smalldolphin.shop.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smalldolphin.shop.domain.PageDomain;
import com.smalldolphin.shop.domain.page.TableDataInfo;
import com.smalldolphin.shop.domain.page.TableSupport;
import com.smalldolphin.shop.utils.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * @Description:shop
 * @Created by Administrator on 2021/6/23 22:35
 * @Modified by:
 */
public class BaseController {

    /**
     *  将 前台传递过来的日期格式的字符串，自动转化为Date类型
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text){
                setValue(DateUtil.parseDate(text));
            }
        });
    }

    /**
     *  获取request  response  session
     * @return
     */
    public HttpServletRequest getRequest() {
        return ServletUtil.getRequest();
    }

    public HttpServletResponse getResponse() {
        return ServletUtil.getResponse();
    }

    public HttpSession getSession() {
        return ServletUtil.getSession();
    }

    /**
     *  设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (!StringUtil.isNull(pageNum) && !StringUtil.isNull(pageSize)) {
            String orderBy = SqlUtil.isValidOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     *    响应返回结果
     * @param rows 影响行数
     * @return
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     *  响应请求分页结果
     * @param list
     * @return
     */
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo resData = new TableDataInfo();
        resData.setCode(0);
        resData.setRows(list);
        resData.setTotal(new PageInfo(list).getTotal());
        return resData;
    }



}
