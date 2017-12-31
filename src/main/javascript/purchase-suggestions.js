(function() {

    "use strict";

    var PurchaseSuggestions = function(config) {
        PurchaseSuggestions.superclass.constructor.apply(this, arguments);
    };

    PurchaseSuggestions.NAME = "purchase";

    PurchaseSuggestions.ATTRS = {
        form : {
            value : null
        },
        activeItem : {
            value : null
        }
    };

    PurchaseSuggestions.HTML_PARSER = {
            menu : ["#purchaseMenu > li"],
            items : ["#purchaseItems > li"],
            form : "form"
    };

    Y.extend(PurchaseSuggestions, L.Feedback, {
        renderUI : function() {
            this.get("menu").addClass(this.getClassName("menu"));
            this.get("items").addClass(this.getClassName("item"));
        },
        bindUI : function() {
            this.get("menu").on("click", this._handleMenuClick, this);
            this.after("activeItemChange", this._handleActiveItemChange);
        },
        syncUI : function() {
            var items = this.get("items");
            for (var i = 0; i < items.size(); i++) {
                items.item(i).remove(false);
            }
        },
        _handleActiveItemChange : function(event) {
            var items = this.get("items"),
                itemsList = document.querySelector("#purchaseItems"),
                item = itemsList.querySelector("li"),
                focusElement;
            if (item) {
                item = new Y.Node(item);
                item.remove(false);
            }
            item = items.item(event.newVal);
            item.addClass(this.getClassName("item", "active"));
            itemsList.appendChild(item._node);
            focusElement = itemsList.querySelector("textarea, input[type='text']");
            if (focusElement) {
                focusElement.focus();
            }
        }
    });

    L.PurchaseSuggestions = PurchaseSuggestions;

    L.Lightbox.on("contentChanged", function() {
        if (document.querySelector("#purchase")) {
            var purchase = new L.PurchaseSuggestions({srcNode : "#purchase"});
            purchase.render();
        }
    });
})();
