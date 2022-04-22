//当前table相关信息
var table = {
    config: {},
    //当前配置实例
    options: {},
    //设置实例配置
    set: function (id) {
        if ($.common.getLength(table.config) > 1) {
            var tableId = $.commons.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find("table.table").attr("id") : id;
            if ($.commons.isNotEmpty(tableId)) {
                table.options = table.get(tableId);
            }
        }
    },
    //获取实例配置
    get: function (id) {
        return table.config[id];
    },
    //记住选择实例组
    rememberSelecteds: {},
    //记住选择ID组
    rememberSelectedIds: {}
};
(function ($){
    $.extend({
        _tree: {},
        bttTable: {},
        //表格封装处理
        table: {
            //初始化表格参数
            init: function (options) {
                console.log("初始化表格参数");
                var defaults = {
                    id: "bootstrap-table",
                    type: 0,//0代表bootstrapTable,1代表bootstrapTreeTable
                    method: "post",
                    height: undefined,
                    sidePagination: "server",
                    sortName: "",
                    sortOrder: "asc",
                    pagination: true,
                    paginationLoop: false,
                    pageSize: 10,
                    pageNumber: 1,
                    pageList: [10, 20, 30],
                    toolbar: "toolbar",
                    loadingFontSize: 13,
                    striped: false,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    clickToSelect: false,
                    singleSelect: false,
                    mobileResponsive: true,
                    maintainSelected: false,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    fixedRightNumber: 0,
                    queryParams: $.table.queryParams,
                    rowStyle: {},
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $("#" + options.id).bootstrapTable({
                    url: options.url,                                   // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",   // 编码类型
                    method: options.method,                             // 请求方式（*）
                    cache: false,                                       // 是否使用缓存
                    height: options.height,                             // 表格的高度
                    striped: options.striped,                           // 是否显示行间隔色
                    sortable: true,                                     // 是否启用排序
                    sortStable: true,                                   // 设置为 true 将获得稳定的排序
                    sortName: options.sortName,                         // 排序列名称
                    sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
                    pagination: options.pagination,                     // 是否显示分页（*）
                    paginationLoop: options.paginationLoop,             // 是否启用分页条无限循环的功能
                    pageNumber: 1,                                      // 初始化加载第一页，默认第一页
                    pageSize: options.pageSize,                         // 每页的记录行数（*）
                    pageList: options.pageList,                         // 可供选择的每页的行数（*）
                    firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
                    escape: options.escape,                             // 转义HTML字符串
                    showFooter: options.showFooter,                     // 是否显示表尾
                    iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    loadingFontSize: options.loadingFontSize,           // 自定义加载文本的字体大小
                    sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
                    search: options.search,                             // 是否显示搜索框功能
                    searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showPageGo: options.showPageGo,               		// 是否显示跳转页
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
                    showExport: options.showExport,                     // 是否支持导出文件
                    showHeader: options.showHeader,                     // 是否显示表头
                    showFullscreen: options.showFullscreen,             // 是否显示全屏按钮
                    uniqueId: options.uniqueId,                         // 唯 一的标识符
                    clickToSelect: options.clickToSelect,				// 是否启用点击选中行
                    singleSelect: options.singleSelect,                 // 是否单选checkbox
                    mobileResponsive: options.mobileResponsive,         // 是否支持移动端适配
                    cardView: options.cardView,                         // 是否启用显示卡片视图
                    detailView: options.detailView,                     // 是否启用显示细节视图
                    onClickRow: options.onClickRow,                     // 点击某行触发的事件
                    onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
                    onClickCell: options.onClickCell,                   // 单击某格触发的事件
                    onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
                    onEditableSave: options.onEditableSave,             // 行内编辑保存的事件
                    onExpandRow: options.onExpandRow,                   // 点击详细视图的事件
                    maintainSelected: options.maintainSelected,         // 前端翻页时保留所选行
                    rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
                    fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
                    fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
                    fixedRightNumber: options.fixedRightNumber,         // 列冻结的个数（右侧）
                    onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
                    queryParams: options.queryParams,                   // 传递参数（*）
                    rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
                    footerStyle: options.footerStyle,                   // 通过自定义函数设置页脚样式
                    headerStyle: options.headerStyle,                   // 通过自定义函数设置标题样式
                    columns: options.columns,                           // 显示列信息（*）
                    data: options.data,                                 // 被加载的数据
                    responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
                    exportOptions: options.exportOptions,               // 前端导出忽略列索引
                    detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
                });
            },
            //初始化事件
            initEvent: function() {
                //实例ID信息
                var optionsIds = $.table.getOptionsIds();
                $(optionsIds).on(TABLE_EVENTS, function() {
                    table.set($(this).attr("id"));
                });
            },
            //获取实例ID，如存在多个返回#id1,#id2
            getOptionsIds: function (separator) {
                var _separator = $.common.isEmpty(separator) ? "," : separator;
                var optionsIds = "";
                $.each(table.config, function (key, value) {
                    optionsIds += "#" + key + _separator;
                });
                return optionsIds.substr(0, optionsIds.length - 1);
            },
            //搜索，默认第一个form
            search: function (formId, tableId) {
                table.set(tableId);
                table.options.formId = $.common.isEmpty(formId) ? $("form").attr("id") : formId;
                var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable("getOptions") : $("#" + tableId).bootstrapTable("getOptions");
                if ($.common.isNotEmpty(tableId)) {
                    $("#" + tableId).bootstrapTable("refresh", params); // ???
                }else {
                    $("#" + table.options.id).bootstrapTable("refresh", params);
                }
            },
            //刷新表格
            refresh: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                });
            },
            //查询表格首列值   ???
            selectFirstColumns: function() {
                var rows = $.map($("#" + table.options.id).bootstrapTable("getSelections"), function (row) {
                    return $.common.getItemField(row, table.options.columns[1].field);
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(selectedRows, function (row) {
                            return $.common.getItemField(row, table.options.columns[1].field);
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            //查询表格指定列
            selectColumns: function(column) {
                var rows = $.map($("#" + table.options.id).bootstrapTable("getSelections"), function (row) {
                   return $.common.getItemField(row, column);
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
                            return $.common.getItemField(row, column);
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            //回显字典数据
            selectDictLabel: function(datas, value) {
                var actions = [];
                $.each(datas, function(index, dict){
                    if (dict.dictValue == ("" + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
            },
            // 列超出指定长度浮动提示 target（copy单击复制文本 open弹窗打开文本）
            tooltip: function (value, length, target) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                var _target = $.common.isEmpty(target) ? 'copy' : target;
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g,"&apos;");
                    _value = _value.replace(/\"/g,"&quot;");
                    var actions = [];
                    actions.push($.common.sprintf('<input style="opacity: 0;position: absolute;width:5px;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf('<a href="###" class="tooltip-show" data-toggle="tooltip" data-target="%s" title="%s">%s</a>', _target, _value, _text));
                    return actions.join('');
                } else {
                    _text = _value;
                    return _text;
                }
            },
            //查询条件
            queryParams: function(params) {
                var curParams = {
                    pageSize: params.limit,
                    pageNumber: params.offset / params.limit + 1,
                    searchValue: params.search,
                    orderByColumn: params.sort,
                    isAsc: params.order
                };
                var currentId = $.common.isEmpty(table.options.formId) ? $("form").attr("id") : table.options.formId;
                return $.extend(curParams, $.common.formToJSON(currentId));
            },
            //当所有数据被加载时触发
            onLoadSuccess: function(data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data);
                }
            },
            //序列号生成
            serialNumber: function(index, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                //bootstrapTable('getOptions')获取表格的参数
                var tableParams = $("#" + currentId).bootstrapTable('getOptions');
                var pageSize = $.common.isEmpty(tableParams.pageSize) ? tableParams.pageSize : table.options.pageSize;
                var pageNumber = $.common.isEmpty(tableParams.pageNumber) ? tableParams.pageNumber : table.options.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            }
        },
        treeTable: {
            // 初始化表格
            init: function(options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 1,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,                                 // 用于设置父子关系
                    parentCode: options.parentCode,                     // 用于设置父子关系
                    type: 'post',                                       // 请求方式（*）
                    url: options.url,                                   // 请求后台的URL（*）
                    data: options.data,                                 // 无url时用于渲染的数据
                    ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
                    rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
                    height: options.height,                             // 表格树的高度
                    expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
                    striped: options.striped,                           // 是否显示行间隔色
                    bordered: false,                                    // 是否显示边框
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    expandAll: options.expandAll,                       // 是否全部展开
                    expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.treeTable.responseHandler,       // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess                // 当所有数据被加载时触发处理函数
                });
            },
            // 刷新
            refresh: function () {
                $.bttTable.bootstrapTreeTable('refresh');
            },
            search: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $("form").attr("id") : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable("refresh", params);
            },
            //请求获取数据后处理回调函数，校验异常状态提醒
            responseHandler: function(res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }
                if (res.code != undefined && res.code != 0) {
                    $.modal.alertWarning(res.msg);
                    return [];
                }else {
                    return res;
                }
            }
        },
        common: {
            equals: function (str, that) {
                return str == that;
            },
            isEmpty: function (value) {
                if (value == null || this.trim(value) == ""){
                    return true;
                }
                return false;
            },
            isNotEmpty: function (value) {
                return !$.common.isEmpty;
            },
            getLength: function (obj) {
                var count = 0;
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++;
                    }
                }
                return count;
            },
            //获取form表单下的所有字段并转换为json对象
            formToJSON: function (formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function (i, field){
                    if (json[field.name]) {
                        json[field.name] += ("," + field.value);
                    }else {
                        json[field.name] = field.value;
                    }
                });
                return json;
            },
            //指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            //数组去重
            uniqueFn: function (array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i<array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            //数字正则表达式，只能为0-9数字
            numValid: function (text) {
                var pattern = new RegExp(/^[0-9]+$/);
                return pattern.test(text);
            },
            // 英文正则表达式，只能为a-z和A-Z字母
            enValid : function(text){
                var pattern = new RegExp(/^[a-zA-Z]+$/);
                return pattern.test(text);
            },
            // 英文、数字正则表达式，必须包含（字母，数字）
            enNumValid : function(text){
                var pattern = new RegExp(/^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$/);
                return pattern.test(text);
            },
            // 英文、数字、特殊字符正则表达式，必须包含（字母，数字，特殊字符-_）
            charValid : function(text){
                var pattern = new RegExp(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[-_])[A-Za-z\d-_]{6,}$/);
                return pattern.test(text);
            },
            //获取节点数据，支持多层级访问 ？？？
            getItemField: function(item, field) {
                var value = item;
                if (typeof field !== "string" || item.hasOwnProperty(field)) {
                    return item[field];
                }
                var props = field.split(".");
                for (var p in props) {
                    value = value && value[props[p]];
                }
                return value;
            },
            //空对象转字符串
            nullToStr: function(value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg == null ? '' : arg;
                });
                return flag ? str : '';
            },
        },
        tree: {
            _options: {},
            _lastValue: {},
            //初始化树结构
            init: function (options) {
                var defaults = {
                    id: "tree",                 // 属性ID
                    expandLevel: 0,             // 展开等级节点
                    view: {
                        selectedMulti: false,   // 设置是否允许同时选中多个节点
                        nameIsHTML: true        // 设置 name 属性是否支持 HTML 脚本
                    },
                    check: {
                        enable: false,          // 置 zTree 的节点上是否显示 checkbox / radio
                        nocheckInherit: true,   // 设置子节点是否自动继承
                        checkBoxType: {"Y": "ps", "N": "ps"}    // 父子节点的关联关系
                    },
                    data: {
                        key: {
                            title: "title"      //保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true        // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    },
                };
                var options = $.extend(defaults, options);
                $.tree._options = options;
                var setting = {
                    callback: {
                        onClick: options.onClick,           // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,           // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick      // 用于捕获鼠标双击之后的事件回调函数
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                $.get(options.url, function (data) {
                    var treeId = $("#treeId").val();
                    //初始化部门树
                    tree = $.fn.zTree.init($("#" + options.id), setting, data);
                    $._tree = tree;
                    var node = tree.getNodeByParam("id", treeId, null);
                    $.tree.selectByIdName(treeId, node);
                });
            },
            //根据id和name选中指定节点
            selectByIdName: function (treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.selectNode(node, true);
                }
            },
            //折叠
            collapse: function () {
                $._tree.expandAll(false);
            },
            //展开
            expand: function () {
                $._tree.expandAll(true);
            },
            notAllowLastLevel: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                for (let i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.errorMsg("不能选择最后层级节点" + nodes[i].name);
                        return false;
                    }
                }
                return true;
            },
            //获取当前被勾选集合   ？？？
            getCheckedNodes: function(column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function(row) {
                   return row[_column];
                }).join();
            }
            },
        modal: {
            //显示图标
            icon: function (type) {
                var icon = "";
                if (type == modal_staus.WARNING) {
                    icon = 0;
                }else if (type == modal_staus.SUCCESS) {
                    icon = 1;
                }else if (type == modal_staus.FAIL) {
                    icon = 2;
                }else {
                    icon = 3;
                }
                return icon;
            },
            msg: function (content, type) {
                if (type != undefined) {
                    layer.msg(content, { icon: $.modal.icon(type), time: 1000, shift: 5});
                }else {
                    layer.msg(content);
                }
            },
            successMsg: function (content) {
                $.modal.msg(content, modal_staus.SUCCESS);
            },
            errorMsg: function (content) {
                $.modal.msg(content, modal_staus.FAIL)
            },
            //弹出层指定宽度
            open: function (title, url, width, height, callback) {
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    //$(window).height()窗口可视区域高度    $(document).height()网页内容高度
                    height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function (index,layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    shadeClose: true,
                    //确定按钮的回调方法
                    yes: callback,
                    //右上角关闭按钮触发的回调
                    cancel: function(index) {
                        return true;
                    }
                });
            },
            confirm: function (content, callback) {
                layer.confirm(content, {
                    icon: 3,
                    title: "系统提示",
                    btn: ['确认', '取消']
                }, function (index) {
                    layer.close(index);
                    callback(true);
                });
            },
            alert: function (content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "系统提示",
                    btn: ['确认'],
                    btnclass: ['btn btn-primary'],
                });
            },
            //警告提示
            alertWarning: function (content) {
                $.modal.alert(content, modal_staus.WARNING);
            },
            //错误提示
            alertError: function (content) {
                $.modal.alert(content, modal_staus.FAIL);
            },
            //选项卡打开方式
            openTab: function (title, url) {
                createMenuItem(url, title);
            },
            //关闭窗体
            close: function (index) {
                layer.close(index);
                // if ($.common.isEmpty(index)) {
                //     //在这是获取不到指定iframe层的index.  只能写在下面
                //     //var index = parent.layer.getFrameIndex(window.name);
                //     parent.layer.close(index);
                // }else {
                //
                // }
            },
            //关闭选项卡
            closeTab: function (dataId) {
                closeItem(dataId);
            },
            //消息提示并刷新父窗体
            msgReload: function (msg, type) {
                layer.msg(msg, {
                    icon: $.modal.icon(type),
                    time: 500,
                    shade: [0.1, '#8F8F8F']
                },
                    function () {
                    $.modal.reload();
                });
            },
            //重新加载
            reload: function () {
                parent.location.reload();
            },
            //打开遮罩层
            loading: function(message) {
                $.blockUI({message: '<div class="loaderbox"><div class="loading-activity"></div>' + message + '</div>'});
            },
            //关闭遮罩层
            closeLoading: function () {
                setTimeout(function() {
                    $.unblockUI();
                },50);
            },
            //启用按钮
            enable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            //弹出层指定参数选项
            openOptions: function (options) {
                console.log("执行$.modal.openOptions方法");
                var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
                var _width = $.common.isEmpty(options.width) ? "800" : options.width;
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                var _btn = ['<i class="fa fa-check"></i>确认', '<i class="fa fa-close"></i>关闭'];
                console.log("局部变量初始化完成");
                if ($.common.isEmpty(options.yes)) {
                    options.yes = function (index, layero) {
                        options.callBack(index, layero);
                    }
                }
                var btnCallback = {};
                if (options.btn instanceof Array) {
                    for (var i = 1, len = options.btn.length; i < len; i++) {
                        var btn = options["btn" + (i + 1)];
                        if (btn) {
                            btnCallback["btn" + (i + 1)] = btn;
                        }
                    }
                }
                console.log("展示弹框");
                var index = layer.open({
                    type: 2,
                    maxmin: $.common.isEmpty(options.maxmin) ? true : options.maxmin,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width + 'px', _height + 'px'],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
                    yes: options.yes,
                    cancel: function () {
                        return true;
                    }});
                console.log("展示弹框完成");
                if ($.common.isNotEmpty(options.full) && options.full === true) {
                    layer.full(index);
                }
            }
        },
        operate: {
            //提交数据
            submit: function (url, type, dataType, data, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    }
                };
                $.ajax(config);
            },
            get: function (url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            },
            post: function (url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            //保存结果弹出msg刷新table表格
            ajaxSuccess: function (result) {
                if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                    $.modal.successMsg(result.msg);
                    $.table.refresh();
                }else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                    $.modal.successMsg(result.msg);
                    $.treeTable.refresh();
                }else if (result.code == web_status.SUCCESS && $.common.isEmpty(table.options.type)) {
                    $.modal.successMsg(result.msg);
                }else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg);
                }else {
                    $.modal.alertError(result.msg);
                }
            },
            addTab: function (id) {
                table.set();
                $.modal.openTab("添加" + table.options.modalName, $.operate.addUrl(id));
            },
            //修改信息。以tab页展现
            editTab: function(id) {
                table.set(id);
                $.modal.openTab("修改" + table.options.modalName, $.operate.editUrl(id));
            },
            //保存选项卡信息
            saveTab: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successTabCallback(result);
                    }
                };
                $.ajax(config);
            },
            //修改访问地址
            editUrl: function (id) {
                var url = "/404.html";
                if($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                }else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.updateUrl.replace("{id}", id);
                }
                return url;
            },
            //添加访问地址
            addUrl: function (id) {
                var url =$.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url;
            },
            //删除信息
            remove: function (id) {
                table.set();
                $.modal.confirm("确定删除该条" + table.options.modalName + "信息吗？", function () {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if (table.options.type == table_type.bootstrapTreeTable) {
                        $.operate.get(url);
                    }else {
                        var data = {"ids": id};
                        $.operate.submit(url, "post", "json", data);
                    }
                });
            },
            //批量删除信息
            removeAll: function() {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function() {
                    var url = table.options.removeUrl;
                    //这里的join ???
                    var data = { "ids" : rows.join() };
                    $.operate.submit(url, "post", "json", data);
                });
            },
            //保存信息，刷新表格
            save: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                $.ajax(config);
            },
            //成功回调执行事件(父窗体静默更新)
            successCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msg(result.msg, modal_staus.SUCCESS);
                        parent.$.table.refresh();
                    }else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msg(result.msg, modal_staus.SUCCESS);
                        parent.$.table.refresh();
                    }else {
                        $.modal.msgReload("保存成功，真正刷新数据请稍后...", modal_staus.SUCCESS);
                    }
                }else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg);
                }else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
                $.modal.enable();
            },
            //选项卡成功回调执行事件(父窗体静默更新)
            successTabCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var topWindow = $(window.parent.document);
                    console.log(topWindow);
                    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
                    console.log(currentId);
                    var $contentWindow = $('.shop_iframe[data-id="'+ currentId +'"]',topWindow)[0].contentWindow;
                    var index = parent.layer.getFrameIndex(window.name);
                    $.modal.close(index);
                    console.log("关闭选项卡");
                    $contentWindow.$.modal.msg(result.msg, modal_staus.SUCCESS);
                    $contentWindow.$(".layui-layer-padding").removeAttr("style");
                    if ($contentWindow.table.options.type == table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh();
                    }else if ($contentWindow.table.options.type = table_type.bootstrapTreeTable) {
                        $contentWindow.$.treeTable.refresh();
                    }
                    $.modal.closeTab();
                    console.log("关闭选项卡结束");
                }else if(result.code ==web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                }else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            //添加信息
            add: function (id) {
                table.set();
                $.modal.open("添加" + table.options.modalName, $.operate.addUrl(id))
            },
            //修改信息
            edit: function (id) {
                table.set();
                if ($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
                    var row = $("#" + table.options.id).bootstrapTreeTable("getSelections")[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("修改" + table.options.modalName, url);
                }else {
                    $.modal.open("修改" + table.options.modalName, $.operate.editUrl(id));
                }
            }
        },
        form: {
            //表单重置
            reset: function (formId, tableId) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $("form").attr("id") : formId;
                $("#" + currentId)[0].reset();
                if (table.options.type == table_type.bootstrapTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTable("refresh");
                    }else {
                        $("#" + tableId).bootstrapTable("refresh");
                    }
                }else if (table.options.type == table_type.bootstrapTreeTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTreeTable("refresh", []);
                    }else {
                        $("#" + tableId).bootstrapTreeTable("refresh", []);
                    }
                }
            },
            //获取选中复选框
            selectCheckeds: function (name) {
                var checkeds = "";
                $("input:checkbox[name='"+ name +"']:checked").each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    }else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            //获取选中下拉框
            selectSelects: function (name) {
                var selects = "";
                $("#" + name + "option:selected").each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    }else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }

        },
        validate: {
            //判断返回标识是否唯一
            unique: function (value) {
                if (value == "0") {
                    return true;
                }else {
                    return false;
                }
            },
            //表单验证
            form: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $("form").attr("id") : formId;
                return $("#" + currentId).validate().form();
            },
            //重置表单验证(清除提示信息)
            reset: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $("form").attr("id") : formId;
                return $("#" + currentId).validate().resetForm();
            }
        }
    });
})(jQuery);

//关闭选项卡(测试正常)
var closeItem = function (dataId) {
    console.log(dataId);
    var topWindow = $(window.parent.document);
    if ($.common.isNotEmpty(dataId)) {
        //根据dataId关闭指定选项卡
        $(".menuTab[data-id='" + dataId + "']", topWindow).remove();
        //移除相应的内容区域
        $(".mainContent .shop_iframe[data-id='" + dataId + "']", topWindow).remove();
        return;
    }
    var panelUrl = window.frameElement.getAttribute("data-panel");
    console.log(panelUrl)
    $(".page-tabs-content .active i", topWindow).click();
    if ($.common.isNotEmpty(panelUrl)) {
        $(".menuTab[data-id'" + panelUrl +"']", topWindow).addClass("active").siblings(".menuTab").removeClass("active");
        $(".mainContent .shop_iframe", topWindow).each(function() {
            if ($(this).data("id") == panelUrl) {
                $(this).show().siblings(".shop_iframe").hide();
                return false;
            }
        });
    }
}
//创建选项卡
function createMenuItem(dataUrl, menuName) {
    var paneUrl = window.frameElement.getAttribute("data-id");
    var dataIndex = $.common.random(1, 100);
    var flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
    var topwindow = $(window.parent.document);
    //选项卡已存在
    $(".menuTab", topwindow).each(function () {
        console.log("选项卡菜单已存在");
        if ($(this).data("id") == dataUrl) {
            if (!$(this).hasClass("active")) {
                $(this).addClass("active").siblings(".menuTab").removeClass("active");
                scrollToTab(this);
                $(".page-tabs-content").animate({marginLeft: ""}, "fast");
                $(".mainContent .shop_iframe", topwindow).each(function () {
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
    //选项卡不存在
    if (flag) {
        console.log("选项卡菜单不存在");
        var str = '<a href="javascript:;" class="active menuTab noactive" data-id="' + dataUrl +'" data-panel="' + paneUrl +'">' + menuName + '<i class="fa fa-times-circle"></i></a>';
        $(".menuTab", topwindow).removeClass("active");
        //添加选项卡对应的iframe
        var str1 = '<iframe class="shop_iframe" name="iframe' + dataIndex +'" width="100%" height="100%" src="' + dataUrl +'" frameborder="0" data-id="' + dataUrl +'" data-panel="' + paneUrl +'" seamless></iframe>';
        $(".mainContent", topwindow).find("iframe.shop_iframe").hide().parents(".mainContent").append(str1);
        //添加选项卡
        $(".menuTabs .page-tabs-content", topwindow).append(str);
        scrollToTab($(".menuTab.active"), topwindow);
    }
    return false;
}
// 滚动到指定选项卡
function scrollToTab(element) {
    var topWindow = $(window.parent.document);
    var marginLeftVal = calSumWidth($(element).prevAll()),
        marginRightVal = calSumWidth($(element).nextAll());
    // 可视区域非tab宽度
    var tabOuterWidth = calSumWidth($(".content-tabs", topWindow).children().not(".menuTabs"));
    //可视区域tab宽度
    var visibleWidth = $(".content-tabs", topWindow).outerWidth(true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if ($(".page-tabs-content", topWindow).outerWidth() < visibleWidth) {
        scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
            scrollVal = marginLeftVal;
            var tabElement = element;
            while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content", topWindow).outerWidth() - visibleWidth)) {
                scrollVal -= $(tabElement).prev().outerWidth();
                tabElement = $(tabElement).prev();
            }
        }
    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    $('.page-tabs-content', topWindow).animate({ marginLeft: 0 - scrollVal + 'px' }, "fast");
}
//计算元素集合的总宽度
function calSumWidth(elements) {
    var width = 0;
    $(elements).each(function () {
       width += $(this).outerWidth(true);
    });
    return width;
}
//密码规则范围验证
function checkpwd(chrtype, password) {
    if (chrtype == 1) {
        if (!$.common.numValid(password)) {
            $.modal.alertWarning("密码只能为0-9数字");
            return false;
        }
    }else if(chrtype == 2) {
        if (!$.common.enValid(password)) {
            $.modal.alertWarning("密码只能为a-z和A-Z字母");
            return false;
        }
    }else if (chrtype == 3) {
        if (!$.common.enNumValid(password)) {
            $.modal.alertWarning("密码必须包含字母以及数字");
            return false;
        }
    }else if (chrtype == 4) {
        if (!$.common.charValid(password)) {
            $.modal.alertWarning("密码必须包含字母、数字、以及特殊符号-、_");
            return false;
        }
    }
    return true;
}

// 日志打印封装处理
var log = {
    log: function(msg) {
        console.log(msg);
    },
    info: function(msg) {
        console.info(msg);
    },
    warn: function(msg) {
        console.warn(msg);
    },
    error: function(msg) {
        console.error(msg);
    }
};
// 本地缓存处理
var storage = {
    set: function(key, value) {
        window.localStorage.setItem(key, value);
    },
    get: function(key) {
        return window.localStorage.getItem(key);
    },
    remove: function(key) {
        window.localStorage.removeItem(key);
    },
    clear: function() {
        window.localStorage.clear();
    }
};
$(function () {
    //取消回车自动提交表单
    $(document).on("keypress",":input:not(textarea):not([type=submit])",function (event){
        if (event.keyCode == 13) {
            event.preventDefault();
        }
    });
});
//消息状态码
web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301
};
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};
//弹窗状态码
modal_staus = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};




