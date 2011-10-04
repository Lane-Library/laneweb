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
        }
    };
    
    Y.extend(PurchaseSuggestions, Y.Widget, {
        renderUI : function() {
            this.get("menu").addClass(this.getClassName("menu"));
            this.get("items").addClass(this.getClassName("item"));
        },
        bindUI : function() {
            this.get("menu").on("click", this._handleMenuClick, this);
            this.on("activeItemChange", this._handleActiveItemChange);
        },
        _handleActiveItemChange : function(event) {
            var menu = this.get("menu"),
                items = this.get("items"),
                menuActiveClass = this.getClassName("menu", "active"),
                itemActiveClass = this.getClassName("item", "active"),
                focusElement;
            menu.item(event.prevVal).removeClass(menuActiveClass);
            items.item(event.prevVal).removeClass(itemActiveClass);
            menu.item(event.newVal).addClass(menuActiveClass);
            items.item(event.newVal).addClass(itemActiveClass);
            focusElement = items.item(event.newVal).one("textarea, input[type='text']");
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
            var nodes = purchase.get("srcNode").all("input[title='required']");
            var fieldJSON = [];
            var node;
            for (var i = 0; i < nodes.size(); i++) {
            	node = nodes.item(i);
            	fieldJSON.push({
            		type : Y.TextBaseField,
            		atts : {
            			inputDOM : Y.Node.getDOMNode(node),
            			correctCss : "correct",
            			incorrectCss : "incorrect"
            		}
            	});
            }
            
            var validator = new Y.Validator( {
                        form : Y.Node.getDOMNode(purchase.get("srcNode").one("form")),
                        fieldJSON : fieldJSON,
                        checkOnSubmit : true
                    });
            purchase.render();
        }
    });
    
})();
