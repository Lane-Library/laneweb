(function() {
    
    Y.namespace("lane");
    
    var MenuAndItems = function() {
        MenuAndItems.superclass.constructor.apply(this, arguments);
    };
    
    Y.lane.MenuAndItems = MenuAndItems;
    
    Y.extend(MenuAndItems, Y.Widget, {
    	
    	bindUI : function() {
            this.get("menu").on("click", this._handleMenuClick, this);
    		this.after("activeItemChange", this._handleActiveItemChange);
    	},
    	
    	renderUI : function() {
            this.get("menu").addClass(this.getClassName("menu"));
            this.get("items").addClass(this.getClassName("item"));
    	},
    	
    	syncUI : function() {
            var activeItem = this.get("activeItem");
            this.get("menu").item(activeItem).addClass(this.getClassName("menu", "active"));
            this.get("items").item(activeItem).addClass(this.getClassName("item", "active"));
        },
    	
    	_handleActiveItemChange : function(event) {
            var prevVal = event.prevVal,
                newVal = event.newVal,
                menu = this.get("menu"),
                items = this.get("items"),
                menuActiveClass = this.getClassName("menu", "active"),
                itemActiveClass = this.getClassName("item", "active");
            menu.item(prevVal).removeClass(menuActiveClass);
            items.item(prevVal).removeClass(itemActiveClass);
            menu.item(newVal).addClass(menuActiveClass);
            items.item(newVal).addClass(itemActiveClass);
    	},
    	
    	_handleMenuClick : function(event) {
            event.preventDefault();
            this.set("activeItem", this.get("menu").indexOf(event.currentTarget));
    	}
    	
    }, {
    	ATTRS : {
            activeItem : {
                value : 0
            },
    		menu : {
                value : null
            },
    		items : {
                value : null
            }
    	},
    	NAME : "menu-items"
    });
    
})();