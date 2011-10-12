//TODO:  this is mostly a duplicate of lane-feedback.js, combine into one class.
(function() {
    
    function PurchaseSuggestions(config) {
        PurchaseSuggestions.superclass.constructor.apply(this, arguments);
    };
    
    PurchaseSuggestions.NAME = "purchase";
    
    PurchaseSuggestions.HTML_PARSER = {
            menu : ["#purchaseMenu > li"],
            items : ["#purchaseItems > li"]
    };
    
    PurchaseSuggestions.ATTRS = {
        activeItem : {
            value : 0
        },
        menu : {
            value : null,
            writeOnce : true
        },
        items : {
            value : null,
            writeOnce : true
        },
        validator : {
            value : null
        }
    };
    
    Y.extend(PurchaseSuggestions, Y.Widget, {
        renderUI : function() {
            this.get("menu").addClass(this.getClassName("menu"));
        },
        bindUI : function() {
            this.get("menu").on("click", this._handleMenuClick, this);
            this.on("activeItemChange", this._handleActiveItemChange);
            var required = this.get("srcNode").all("input[title='required']");
            required.on("focus", this._clearIncorrect);
        },
        syncUI : function() {
            var items = this.get("items");
            for (var i = 0; i < items.size(); i++) {
                items.item(i).remove(false);
            }
        },
        _clearIncorrect : function(event) {
            event.target.removeClass("incorrect");
        },
        _createNewValidator : function (activeItem) {
            var nodes, fieldJSON, node, i, inputFields,
                validator = this.get("validator");
            if (validator) {
                validator.destroy();
            }
            nodes = activeItem.all("input[title='required']");
            fieldJSON = [];
            fieldJSON.push({
                type : Y.TextBaseField,
                atts : {
                    inputDOM : Y.Node.getDOMNode(this.get("srcNode").one("input[name='full-name']")),
                    correctCss : "correct",
                    incorrectCss : "incorrect",
                    isOn : false
                }
            });
            fieldJSON.push({
                type : Y.TextBaseField,
                atts : {
                    inputDOM : Y.Node.getDOMNode(this.get("srcNode").one("input[name='email']")),
                    correctCss : "correct",
                    incorrectCss : "incorrect",
                    isOn : false
                }
            });
            for (i = 0; i < nodes.size(); i++) {
                node = nodes.item(i);
                fieldJSON.push({
                    type : Y.TextBaseField,
                    atts : {
                        inputDOM : Y.Node.getDOMNode(node),
                        correctCss : "correct",
                        incorrectCss : "incorrect",
                        isOn : false
                    }
                });
            }
            validator = new Y.Validator( {
                        form : Y.Node.getDOMNode(this.get("srcNode").one("form")),
                        fieldJSON : fieldJSON,
                        checkOnSubmit : true
                    });
            inputFields = validator.get("inputFields");
            for (i = 0; i < inputFields.length; i++) {
                inputFields[i].isEmpty = function() {
                    var node = this.get("inputDOM");
                    return node.value === '' || node.value == node.title;
                };
                inputFields[i].set("isOn", true);
            }
            this.set("validator", validator);
        },
        _handleActiveItemChange : function(event) {
            var menu = this.get("menu"),
                items = this.get("items"),
                menuActiveClass = this.getClassName("menu", "active"),
                itemsList = Y.one("#purchaseItems"),
                item = itemsList.one("li"),
                focusElement;
            menu.item(event.prevVal).removeClass(menuActiveClass);
            menu.item(event.newVal).addClass(menuActiveClass);
            if (item) {
                item.remove(false);
            }
            item = items.item(event.newVal);
            itemsList.append(item);
            focusElement = itemsList.one("textarea, input[type='text']");
            this._createNewValidator(item);
            if (focusElement) {
                focusElement.focus();
            }
        },
        _handleMenuClick : function(event) {
            event.preventDefault();
            this.set("activeItem", this.get("menu").indexOf(event.currentTarget));
        }
    });
    
    Y.lane.PurchaseSuggestions = PurchaseSuggestions;
    
    Y.lane.Lightbox.on("contentChanged", function(event) {
        if (Y.one("#purchase")) {
            var purchase = new Y.lane.PurchaseSuggestions({srcNode : "#purchase"});
            purchase.render();
        }
    });
    
})();
