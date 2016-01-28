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

    Y.extend(PurchaseSuggestions, Y.lane.Feedback, {
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
                itemsList = Y.one("#purchaseItems"),
                item = itemsList.one("li"),
                focusElement;
            if (item) {
                item.remove(false);
            }
            item = items.item(event.newVal);
            item.addClass(this.getClassName("item", "active"));
            itemsList.append(item);
            Y.Widget.getByNode("#feedback").resetValidator();
            focusElement = itemsList.one("textarea, input[type='text']");
            if (focusElement) {
                focusElement.focus();
            }
        }
    });

    Y.lane.PurchaseSuggestions = PurchaseSuggestions;

    Y.lane.Lightbox.on("contentChanged", function() {
        if (Y.one("#purchase")) {
            var purchase = new Y.lane.PurchaseSuggestions({srcNode : "#purchase"});
            purchase.render();
        }
    });
})();
