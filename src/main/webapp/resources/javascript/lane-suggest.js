YUI().add('lane-suggest', function (Y) {
    
    Y.namespace('lane');
    
    Y.lane.Suggest = function (input, limit) {
        var acWidget, i,
            baseUrl = '/././apps/suggest/json?',
            acDS  = new Y.DataSource.IO({source:baseUrl});
            setDSLimit = function(input){
//            Y.log("set source");
                var sourceElm = input.ancestor("form").one('#searchSource'), searchSource, minQueryLength = 3, limit = null;
                searchSource = (sourceElm) ? sourceElm.get('value') : null;
                if (searchSource && searchSource.match(/^(all|articles|catalog)/)) {
                    limit = baseUrl + "l=er-mesh&";
                } else if (searchSource && searchSource.match(/^bioresearch/)) {
                    limit = baseUrl + "l=mesh&";
                } else if (searchSource && searchSource.match(/^history/)) {
                    limit = baseUrl + "l=history&";
                } else if (null == searchSource) { // assume source-less is metasearch form on peds portal, etc.
                    limit = baseUrl + "l=mesh-di&";
                } else {
                    minQueryLength = 999;
                }
                input.ac.set("minQueryLength", minQueryLength);
                if(null != limit){
                    input.ac.get("dataSource").set("source",limit);
                }
            };
            acDS.plug({fn : Y.Plugin.DataSourceJSONSchema, cfg : {
                schema : { resultListLocator : "suggest" }
            }});
            input.plug(Y.Plugin.ACPlugin, {
                queryTemplate : function (q) { return "q=" + encodeURIComponent(
                    q.replace(/(["\\])/g, '\\$1')
                );},
                dataSource : acDS
            });
            
        acWidget = (function () {
            var data = [],
                contNode = Y.Node.create('<ul class="aclist"> </ul>'),
                selected = -1;
            input.insert(contNode,"after");
            
            contNode.delegate("click", function (e) {
                Y.log("click");
                var target = e.currentTarget, index = target.get("className").split('-')[1];
                acWidget.focus(index).select(e);
            }, "li");
            contNode.delegate("mouseover", function (e) {
                Y.log("mouseover ");
                var target = e.currentTarget, index = target.get("className").split('-')[1];
                acWidget.focus(index);
            }, "li");
            Y.on("click", function () { acWidget.hide(); }, document);
            
            return {
                setData : function (d) {
                    if (d.length) {
                        data = d;
                        selected = -1;
                    }
                    return this;
                },
                render : function () {
                    Y.log("render");
                    if (!data.length){
                        return this;
                    }
                    this.visible = true;
                    contNode.set("innerHTML", "");
                    for (var i = 0, l = data.length; i < l; i ++) {
                        contNode.appendChild(Y.Node.create('<li class="ac-'+i+'">'+data[i]+"</li>"));
                    }
                    contNode.setStyle("display", "block");
                    return this;
                },
                hide : function () {
                    contNode.setStyle("display", "none");
                    this.visible = false;
                    selected = -1;
                    return this;
                },
                focus : function (i) {
                    i = i || selected;
                    if (!data.length) {
                        return this;
                    }
                    if (i >= data.length) {
                        i = 0;
                    }
                    if (i < 0) {
                        i = data.length - 1;
                    }
                    var list = contNode.all("li"),
                        current = contNode.one(".selected"),
                        intent = list.item(i);
                    if (!intent || intent === current) {
                        return this;
                    }
                    selected = i;
                    if (current) {
                        current.removeClass("selected");
                    }
                    intent.addClass("selected");
                    //input.ac.set("queryValue", acWidget.getValue());
                    return this;
                },
                next : function () {
                    selected = selected || 0;
                    selected ++;
                    if (!this.visible) {
                        this.render();
                    }
                    return this.focus(selected);
                },
                previous : function () {
                    selected = selected || data.length;
                    selected --;
                    return this.focus(selected);
                },
                getValue : function () {
                    if (!data.length || !data[selected]) {
                        return "";
                    }
                    return data[selected];
                },
                select : function (e) {
                    if(acWidget.getValue()){
                        input.ac.set("queryValue", acWidget.getValue());
                    }
                    else{
                        input.ac.set("queryValue", input.get("value"));
                    }
                    acWidget.hide();
                    input.focus();
                    if (e) {
                        e.preventDefault();
                    }
                    LANE.Search.startSearch();
                    Y.Node.getDOMNode(input.ancestor("form")).submit();
                },
                setWidth : function (w) {
                    if (w) {
                        contNode.setStyle("width",w);
                    }
                }
            };
        })();
        
        input.on("focus", function (e) {
            if (!limit) {
                setDSLimit(input);
            } else {
                input.ac.set("minQueryLength", 3);
                input.ac.get("dataSource").set("source", baseUrl + limit);
            }
        });
        input.ac.on("ac:load", function (e) {
            Y.log("ac:load");
            acWidget.setWidth(input.getStyle("width"));
            acWidget.setData(e.results).render();
        });
        input.ac.on("ac:query", function (e) {
            Y.log("ac:query");
            if (acWidget.visible && e.value === acWidget.value) {
                e.halt();
            }
            acWidget.value = e.value;
        });
        input.ac.on("ac:show", function () {
            if (!acWidget.visible) {
                acWidget.render();
            }
        }, acWidget);
        input.ac.on("ac:hide", acWidget.hide, acWidget);
        input.ac.on("ac:next", acWidget.next, acWidget);
        input.ac.on("ac:previous", acWidget.previous, acWidget);
        Y.on("key", acWidget.select, input, "down:13,10");
    };
}, '1.11.0-SNAPSHOT', {requires:['dump', 'lane','gallery-ac-plugin', 'plugin', 'node-base', 'datasource']});

YUI().use('lane-suggest', 'node-base', function(Y) {

    var i, suggestElms = Y.all('.laneSuggest');
    for (i = 0; i < suggestElms.size(); i++) {
        new Y.lane.Suggest(suggestElms.item(i));
    }
});
