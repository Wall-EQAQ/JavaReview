var isMobile = false;
var sideBarHeight = isMobile ? '100%' : '96%';

//最小化菜单
function navToggle() {
    $(".navbar-minimalize").trigger("click");
}


$(function () {
    <!-- MetsiMenu-->
    $("#side-menu").metisMenu();
    //固定菜单栏
    $(".sidebar-collapse").slimScroll({
        height: sideBarHeight,
        alwaysVisible: false,
        railOpacity: 0.2
    });
});

//暂时不知道作用
function syncMenuTab(dataId) {
    if (isLinkage) {
        var obj = $('a[href$="' + decodeURI(dataId) + '"]');
        if (obj.attr("class") != null && !obj.hasClass("noactive")) {
            $(".nav li").removeClass("in");
            obj.parents("ul").addClass("in");
            obj.parents("li").addClass("active").siblings().removeClass("active").find("li").removeClass("active");
            obj.parents("ul").css("height","auto").height();
            obj.click();
            var tabStr = obj.parents(".tab-pane").attr("id");
            if ($.common.isNotEmpty(tabStr)) {
                var sepIndex = tabStr.lastIndexOf("_");
                var menuId = tabStr.substring(sepIndex + 1, tabStr.length);
                $("#tab_" + menuId + " a").click();
            }
        }
    }
}

//iframe处理
$(function () {
    //计算元素的宽度
    function calSumWidth(elements) {
        var width = 0;
        $(elements).each(function () {
            width += $(this).outerWidth(true);
        });
        return width;
    }
    //滚动到指定选项卡
    function scrollToTab(element) {
        var marginLeftVal = calSumWidth($(element).prevAll());
        var marginRightVal = calSumWidth($(element).nextAll());
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true);
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").outerWidth() < visibleWidth) {
            scrollVal = 0;
        } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
            if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                scrollVal = marginLeftVal;
                var tabElement = element;
                while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                    scrollVal -= $(tabElement).prev().outerWidth();
                    tabElement = $(tabElement).prev();
                }
            }
        } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
            scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
        }
        $('.page-tabs-content').animate({
                marginLeft: 0 - scrollVal + 'px'
            }, "fast");
    }
    //激活指定选项卡
    function setTabActive(element) {
        if (!$(element).hasClass("active")) {
            var currentId = $(element).data("id");
            syncMenuTab(currentId);
            $(".shop_iframe").each(function () {
                if ($(this).data("id") == currentId) {
                    $(this).show().siblings(".shop_iframe").hide();
                }
            });
            $(element).addClass("active").siblings(".menuTab").removeClass("active");

        }
    }
    //点击选项卡菜单
    function activeTab() {
        if (!$(this).hasClass("active")) {
            var currentId = $(this).data("id");
            syncMenuTab(currentId);
            $(".mainContent .shop_iframe").each(function () {
                if ($(this).data("id") == currentId) {
                    $(this).siblings(".shop_iframe").hide();
                    return false;
                }
            });
            $(this).addClass("active").siblings(".menuTab").removeClass("active");
            scrollToTab(this);
        }
    }

    $(".menuTabs").on("click", ".menuTab", activeTab);


    //通过遍历给菜单项加上data-index属性,暂时不知道index用途
    $(".menuItem").each(function(index) {
        if(!$(this).attr("data-index")) {
            $(this).attr("data-index", index)
        }
    });

    //新建选项卡菜单
    function menuItem() {
        //获取标识数据
        var dataUrl = $(this).attr("href");
        var dataIndex = $(this).data("index");
        console.log(dataIndex);
        var menuName = $.trim($(this).text());
        var flag = true;
        //decodeURI的原因是 URL中包含中文或者特殊字符会被转码
        var obj = $('a[href$="' + decodeURI(dataUrl) + '"]');
        if (!obj.hasClass("noactive")) {
            $(".nav ul").removeClass("in");
            obj.parents("ul").addClass("in");
            obj.parents("li").addClass("active").siblings().removeClass("active").find("li").removeClass("active");
            obj.parents("ul").css("height", "auto").height();
            $(".nav ul li, .nav li").removeClass("selected");
            $(this).parent("li").addClass("selected");
        }
        //设置锚点
        setIframeUrl(dataUrl);
        if (dataUrl == undefined || $.trim(dataUrl).length == 0)    return false;
        //选项卡菜单已存在
        $(".menuTab").each(function () {
            if ($(this).data("id") == dataUrl) {
                if (!$(this).hasClass("active")) {
                    $(this).addClass("active").siblings(".menuTab").removeClass("active");
                    scrollToTab(this);
                    $(".mainContent .shop_iframe").each(function () {
                        if ($(this).data("id") == dataUrl) {
                            $(this).show().siblings(".shop_iframe").hide();
                            return false;
                        }
                    });
                }
                flag = false;
                return false;
            }
        });
        //选项卡菜单不存在
        if (flag) {
            var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl +'">' + menuName + '<i class="fa fa-times-circle"></i></a>';
            $(".menuTab").removeClass("active");

            //添加选项卡对应的iframe
            var str1 = '<iframe class="shop_iframe" name="iframe' + dataIndex +'" width="100%" height="100%" src="' + dataUrl +'" frameborder="0" data-id="' + dataUrl +'" seamless ></iframe>';
            $(".mainContent").find("iframe.shop_iframe").hide().parents(".mainContent").append(str1);
            $(".menuTabs .page-tabs-content").append(str);

            scrollToTab($(".menuTab.active"));
        }
        return false;
    }

    $(".menuItem").on("click",menuItem);

    //关闭选项卡菜单
    function closeTab() {
        var closeTabId = $(this).parents(".menuTab").data("id");
        var currentWidth = $(this).parents(".menuTab").width();
        //当前元素处于活动状态
        if ($(this).parents(".menuTab").hasClass("active")) {
            console.log("当前元素处于活动状态");
            //当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents(".menuTab").next(".menuTab").size()) {
                //后一个元素的id(即URL)
                var activeId = $(this).parents(".menuTab").next(".menuTab:eq(0)").data("id");
                $(this).parents(".menuTab").next(".menuTab:eq(0)").addClass("active");

                $(".mainContent .shop_iframe").each(function () {
                    if ($(this).data("id") == activeId){
                        $(this).show().siblings(".shop_iframe").hide();
                        return false;
                    }
                });
                //移除当前选项卡
                $(this).parents(".menuTab").remove();
                //移除tab对应的内容区域
                $(".mainContent .shop_iframe").each(function () {
                    if ($(this).data("id") == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
            //当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if($(this).parents(".menuTab").prev(".menuTab").size()) {
                console.log("当前元素后面没有同辈元素");
                var activeId = $(this).parents(".menuTab").prev(".menuTab:last").data("id");
                $(this).parents(".menuTab").prev(".menuTab:last").addClass("active");
                $(".mainContent .shop_iframe").each(function() {
                    if($(this).data("id") == activeId) {
                        $(this).show().siblings(".shop_iframe").hide();
                        return false;
                    }
                });
                //移除当前选项卡
                $(this).parents(".menuTab").remove();
                //移除tab对应的内容区域
                $(".mainContent .shop_iframe").each(function() {
                    if($(this).data("id") == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
        } else {
            //移除当前选项卡
            $(this).parents(".menuTab").remove();
            //移除tab对应的内容区域
            $(".mainContent .shop_iframe").each(function () {
                if ($(this).data("id") == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
        }
        scrollToTab($(".menuTab.active"));
        syncMenuTab($(".page-tabs-content").find(".active").attr("data-id"));
        return false;
    }

    $(".menuTabs").on("click", ".menuTab i", closeTab);

    //设置锚点
    function setIframeUrl(href) {
        if ("history" == mode) {
            storage.set("publicPath", href);
        }else {
            var nowUrl = window.location.href;
            var newUrl = nowUrl.substring(0, nowUrl.indexOf("#"));
            window.location.href = newUrl + "#" + href;
        }
    }
    //hash值发生变化时
    window.onhashchange = function () {
        var hash = location.hash;
        var url = hash.substring(1, hash.length);
        $('a[href$="' + url +'"]').click();
    }





});