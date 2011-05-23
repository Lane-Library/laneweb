(function() {
    
    function Feedback(config) {
        Feedback.superclass.constructor.apply(this, arguments);
    };
    
    Feedback.NAME = "feedback";
    
    Feedback.HTML_PARSER = {
            menu : ["#feedbackMenu > li"],
            items : ["#feedbackItems > li"]
    };
    
    Feedback.ATTRS = {
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
    
    Y.extend(Feedback, Y.Widget, {
        renderUI : function() {
            this.get("menu").addClass(this.getClassName("menu"));
            this.get("items").addClass(this.getClassName("item"));
        },
        bindUI : function() {
        	var self = this, eventHandle1, eventHandle2;
            this.get("menu").on("click", this._handleMenuClick, this);
            this.on("activeItemChange", this._handleActiveItemChange);
            eventHandle1 = Y.lane.Lightbox.on("animEnd", function() {
                self.get("items").item(self.get("activeItem")).one("textarea").focus();
            });
            eventHandle2 = Y.lane.Lightbox.on("visibleChange", function(event) {
            	if (event.newVal) {
                    if (Y.UA.ie === 6) {
                    	self._fixForIE6();
                    }
            	} else {
            		eventHandle1.detach();
            		eventHandle2.detach();
                	self.destroy();
            	}
            });
        },
        syncUI : function() {
            var activeItem = this.get("activeItem");
            this.get("menu").item(activeItem).addClass(this.getClassName("menu", "active"));
            this.get("items").item(activeItem).addClass(this.getClassName("item", "active"));
        },
        _fixForIE6 : function() {
        	var boundingBox = this.get("boundingBox");
//        	//this forces the markup to be rendered, not sure why it is needed.
        	boundingBox.setStyle("visibility", "hidden");
        	boundingBox.setStyle("visibility", "visible");
        },
        _handleActiveItemChange : function(event) {
            var menu = this.get("menu"),
                items = this.get("items"),
                menuActiveClass = this.getClassName("menu", "active"),
                itemActiveClass = this.getClassName("item", "active");
            menu.item(event.prevVal).removeClass(menuActiveClass);
            items.item(event.prevVal).removeClass(itemActiveClass);
            menu.item(event.newVal).addClass(menuActiveClass);
            items.item(event.newVal).addClass(itemActiveClass);
            items.item(event.newVal).one("textarea").focus();
        },
        _handleMenuClick : function(event) {
            event.preventDefault();
            this.set("activeItem", this.get("menu").indexOf(event.currentTarget));
        }
    });
    
    Y.lane.Feedback = Feedback;
    
    Y.lane.Lightbox.on("contentChanged", function(event) {
    if (Y.one("#feedback")) {
        var feedback = new Y.lane.Feedback({srcNode : "#feedback"});
        feedback.render();
      }
    });
    
})();
