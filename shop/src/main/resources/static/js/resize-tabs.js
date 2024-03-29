(function (c, a, e) {
    var b, d = "resizeTabs", f = {
        navSelector: ".nav-tabs",
        itemSelector: ">li",
        dropdownSelector: ">.dropdown",
        dropdownItemSelector: "li",
        tabParentSelector: "",
        tabSelector: ".tab-pane",
        activeClassName: "active",
        noNavClassName: ".no-menu",
        fnCallback: ""
    };
    b = function (j, i) {
        var h = this.$tabs = e(j), k = this.options = e.extend(true, {}, f, i), g = this.$nav = h.find(k.navSelector),
            l = this.$dropdown = g.find(k.dropdownSelector);
        this.$items = g.find(k.itemSelector).filter(function () {
            return !e(this).is(l)
        });
        this.$dropdownItems = l.find(k.dropdownItemSelector);
        if (k.tabParentSelector !== "") {
            this.$tabPanel = e(k.tabParentSelector).find(k.tabSelector)
        } else {
            this.$tabPanel = h.find(k.tabSelector)
        }
        this.init()
    };
    b.prototype = {
        init: function () {
            var j = this.itemsLenth = this.$items.length, h;
            if (j === 0) {
                throw"There should be some tags here "
            }
            if (this.$dropdown.length === 0) {
                this.flag = true;
                this.$nav.append('<li class="dropdown" role="presentation">' + '<a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">' + '<i class="glyphicon glyphicon-align-justify"></i> <b class="caret"></b></a><ul class="dropdown-menu" role="menu"></ul></li>');
                this.$dropdown = this.$nav.find(this.options.dropdownSelector);
                this.$dropdown.css("opacity", 0);
                h = this.$dropdown.width();
                h = h === 0 ? 90 : h;
                this.$dropdown.addClass("hidden").css("opacity", 1)
            } else {
                h = this.$dropdown.width()
            }
            this.breakpoints = [];
            for (var g = 0; g < j + 1; g++) {
                var k = this.$items.eq(g).width(), l = 0;
                switch (g) {
                    case 0:
                        l = k + h;
                        break;
                    case j - 1:
                        l = this.breakpoints[g - 1] + k - h;
                        break;
                    case j:
                        l = this.breakpoints[g - 1] + h;
                        break;
                    default:
                        l = this.breakpoints[g - 1] + k
                }
                this.breakpoints.push(l)
            }
            if (typeof this.options.fnCallback === "function") {
                this.options.fnCallback(this.$tabs)
            }
            this.bind();
            this.layout()
        }, layout: function () {
            if (this.breakpoints.length <= 0) {
                return
            }
            var l = this.$tabs.width() - 500, j = 0, h = this, g = this.options.activeClassName,
                m = this.$tabPanel.filter("." + g).index(), k = function (p) {
                    var o = p;
                    if (p === h.itemsLenth) {
                        o = p - 1
                    }
                    for (; o < h.itemsLenth; o++) {
                        if (h.flag) {
                            h.$dropdown.find("ul").append(h.$items.eq(o).prop("outerHTML"))
                        } else {
                            h.$dropdown.find("ul>li" + h.options.noNavClassName + ":first").before(h.$items.eq(o).prop("outerHTML"))
                        }
                        h.$items.eq(o).hide()
                    }
                }, n = function (p) {
                    for (var o = 0; o < h.itemsLenth + 1; o++) {
                        if (o < p) {
                            h.$items.eq(o).show()
                        } else {
                            k(p);
                            h.$dropdown.find("ul>li").show();
                            break
                        }
                    }
                    h.$dropdownItems = h.$dropdown.find(h.options.dropdownItemSelector)
                };
            for (; j < this.breakpoints.length; j++) {
                if (this.breakpoints[j] > l) {
                    break
                }
            }
            this.$items.removeClass(g);
            this.$dropdownItems.removeClass(g);
            this.$dropdown.removeClass(g);
            if (j === this.breakpoints.length) {
                if (this.flag) {
                    this.$dropdown.addClass("hidden")
                } else {
                    this.$dropdown.find("ul>li:not(li" + this.options.noNavClassName + ")").remove()
                }
                this.$items.show();
                if (m != -1) {
                    this.$items.eq(m - 1).addClass(g)
                }
            } else {
                this.$dropdown.removeClass("hidden");
                if (this.flag) {
                    this.$dropdown.find("ul>li").remove()
                } else {
                    this.$dropdown.find("ul>li:not(li" + this.options.noNavClassName + ")").remove()
                }
                n(j);
                if (m < j) {
                    if (m != -1) {
                        this.$items.eq(m - 1).addClass(g)
                    }
                } else {
                    this.$dropdown.addClass(g);
                    this.$dropdownItems.eq(m - j).addClass(g)
                }
            }
        }, throttle: function (h, g) {
            var k = h, j, i = true;
            return function () {
                var m = arguments, l = this;
                if (i) {
                    k.apply(l, m);
                    i = false
                }
                if (j) {
                    return false
                }
                j = setInterval(function () {
                    clearInterval(j);
                    j = null;
                    k.apply(l, m)
                }, g || 500)
            }
        }, bind: function () {
            var g = this;
            e(c).resize(function () {
                g.throttle(function () {
                    g.layout()
                }, 1000)()
            })
        }
    };
    e.fn[d] = function (g) {
        if (typeof g === "string") {
            var i = g, h = Array.prototype.slice.call(arguments, 1);
            if (/^_/.test(i)) {
                console.error("No such method : " + g)
            } else {
                return this.each(function () {
                    var j = e.data(this, d);
                    if (j && typeof j[i] === "function") {
                        j[i].apply(j, h)
                    }
                })
            }
        } else {
            return this.each(function () {
                if (!e.data(this, d)) {
                    e.data(this, d, new b(this, g))
                } else {
                    e.data(this, d).init()
                }
            })
        }
    }
})(window, document, jQuery);
$("#navMenu").resizeTabs({tabParentSelector: "#side-menu"});