(function() {

    function PurchaseSuggestions(config) {
        PurchaseSuggestions.superclass.constructor.apply(this, arguments);
    }

    PurchaseSuggestions.NAME = "purchase";

    PurchaseSuggestions.ATTRS = {
        form : {
            value : null
        }
    };

    PurchaseSuggestions.HTML_PARSER = {
            menu : ["#purchaseMenu > li"],
            items : ["#purchaseItems > li"],
            form : "form"
    };

    Y.extend(PurchaseSuggestions, Y.lane.Feedback, {
        bindUI : function() {
            this.get("menu").on("click", this._handleMenuClick, this);
            this.on("activeItemChange", this._handleActiveItemChange);
            this.on("validatorChange", this._handleValidatorChange);
        },
        syncUI : function() {
            var items = this.get("items");
            for (var i = 0; i < items.size(); i++) {
                items.item(i).remove(false);
            }
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
            item.addClass(this.getClassName("item", "active"));
            itemsList.append(item);
            this.set("validator", new Y.lane.FormValidator(this.get("form")));
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
