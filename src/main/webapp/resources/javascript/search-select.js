(function() {

    "use strict";

    var Select = function(values, titles, index) {
        this._values = values ? values : [];
        this._titles = titles ? titles : [];
        this._index = index ? index : 0;
        this.publish("selectedChange", {
            defaultFn : this._setIndex
        });
    };
    Select.prototype = {
        _setIndex : function(event) {
            var index = event.newIndex;
            if (index < 0 || index > this._values.length) {
                throw "array index out of range";
            }
            this._index = event.newIndex;
        },
        getSelected : function() {
            return this._values[this._index];
        },
        getSelectedTitle : function() {
            return this._titles[this._index];
        },
        setSelected : function(index) {
            var newIndex = typeof index === "string" ? Y.Array.indexOf(this._values, index) : index;
            if (newIndex !== this._index) {
                this.fire("selectedChange", {
                    newIndex : newIndex,
                    newVal : this._values[newIndex],
                    prevVal : this._values[this._index]
                });
            }
        }
    };
    // Add EventTarget attributes to the Select prototype
    Y.augment(Select, Y.EventTarget, null, null, {
        emitFacade : true,
        prefix : "select"
    });
    Y.lane.Select = Select;

    //TODO: use a constructor to create a wrapping div then use Y.extend instead of Y.Base.create
    var SearchSelectWidget = Y.Base.create("searchSelect", Y.Widget, [], {
        renderUI : function() {
            var srcNode = this.get("srcNode");
            var content = srcNode.all("option").item(srcNode.get("selectedIndex")).get("innerHTML");
            this.get("boundingBox").appendChild("<span class='" + this.getClassName() + "-selected'><span>" + content + "</span><i class=\"fa fa-angle-double-down\"></i></span>", srcNode);
        },
        bindUI : function() {
            this.get("model").after("selectedChange", this._handleModelChange, this);
            this.get("srcNode").after("change", this._handleViewChange, this);
        },
        _handleViewChange : function() {
            this.get("model").setSelected(this.get("srcNode").get("selectedIndex"));
        },
        _handleModelChange : function(event) {
            var content, srcNode = this.get("srcNode"),
                selected = event.newVal;
            srcNode.set("value", selected);
            content = srcNode.all("option").item(srcNode.get("selectedIndex")).get("innerHTML");
            this.get("boundingBox").one("." + this.getClassName() + "-selected").get("firstChild").set("innerHTML", content);
        }
    }, {
        ATTRS : {
            model : {}
        },
        HTML_PARSER : {
            model : function(srcNode) {
                var i, item, index = srcNode.get("selectedIndex"),
                    values = [], titles = [], options = srcNode.all("option");
                for (i = 0; i < options.size(); i++) {
                    item = options.item(i);
                    values.push(item.get("value"));
                    titles.push(item.get("title"));

                }
                return new Select(values, titles, index);
            }
        }
    });

    Y.lane.SearchSelectWidget = SearchSelectWidget;

})();
