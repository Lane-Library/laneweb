(function() {
    
    function PurchaseSuggestions(config) {
        PurchaseSuggestions.superclass.constructor.apply(this, arguments);
    };
    
    PurchaseSuggestions.NAME = "purchase";
    
    PurchaseSuggestions.HTML_PARSER = {
            menu : ["#purchaseMenu > li"],
            items : ["#purchaseItems > li"]
    };
    
    Y.extend(PurchaseSuggestions, Y.lane.Feedback, {
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
        focusElement = itemsList.one("textarea, input[type='text']");
        this.set("validator", new Y.lane.FormValidator(this.get("srcNode").one("form")));
        if (focusElement) {
            focusElement.focus();
        }
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
