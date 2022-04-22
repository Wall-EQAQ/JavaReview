package com.smalldolphin.shop.domain.page;

import com.smalldolphin.shop.common.constant.Constants;
import com.smalldolphin.shop.domain.PageDomain;
import com.smalldolphin.shop.utils.ServletUtil;

/**
 * @Description:    表格数据处理(分页信息)
 * @Created by Administrator on 2021/8/22 22:29
 * @Modified by:
 */
public class TableSupport {
    //封装分页对象
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtil.getParameterToInt(Constants.PAGE_NUM));
        pageDomain.setPageSize(ServletUtil.getParameterToInt(Constants.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtil.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtil.getParameter(Constants.IS_ASC));
        return pageDomain;
    }
    //构建分页请求
    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
