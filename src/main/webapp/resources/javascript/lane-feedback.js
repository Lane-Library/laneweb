(function() {
    
    function Feedback(config) {
        Feedback.superclass.constructor.apply(this, arguments);
    }
    
    Feedback.NAME = "feedback";
    
    Feedback.HTML_PARSER = {
            menu : ["#feedbackMenu > li"],
            items : ["#feedbackItems > li"]
    };
    
    Feedback.ATTRS = {
        activeItem : {
            value : 0
        },
        items : {
            value : null,
            writeOnce : true
        },
        menu : {
            value : null,
            writeOnce : true
        },
        sending : {
            value : "Sending feedback."
        },
        thanks : {
            value : "Thank you for your feedback."
        },
        validator : {
        	value : null
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
            this.on("validatorChange", this._handleValidatorChange);
            this.get("srcNode").all("form").on("submit", this._handleSubmit, this);
            eventHandle1 = Y.lane.Lightbox.on("animEnd", function() {
                self.get("items").item(self.get("activeItem")).one("textarea, input[type='text']").focus();
            });
            eventHandle2 = Y.lane.Lightbox.on("visibleChange", function(event) {
                if (event.newVal) {
                    if (Y.UA.ie === 6) {
                        self._toggleVisibility();
                    }
                } else {
                    eventHandle1.detach();
                    eventHandle2.detach();
                    self.destroy();
                }
            });
            //case 81447 add the 'x' button back to the feedback widget
            Y.one("#feedbackClose").on("click", function(event) {
                event.preventDefault();
                Y.lane.Lightbox.hide();
            });
        },
        syncUI : function() {
            var activeItem = this.get("activeItem"),
                items = this.get("items"),
                srcNode = this.get("srcNode"),
                sending = srcNode.one("#sending"),
                thanks = srcNode.one("#thanks");
            this.get("menu").item(activeItem).addClass(this.getClassName("menu", "active"));
            items.item(activeItem).addClass(this.getClassName("item", "active"));
            this.set("validator", new Y.lane.FormValidator(items.item(activeItem).one("form")));
            if (sending) {
            	this.set("sending", sending.get("innerHTML"));
            }
            if (thanks) {
            	this.set("thanks", thanks.get("innerHTML"));
            }
        },
        sendFeedback : function(form) {
            var contentBox = this.get("contentBox"),
                data = Y.JSON.stringify(this._getFeedback(form));
        	contentBox.set("innerHTML", this.get("sending"));
        	contentBox.scrollIntoView();
            Y.io(form.getAttribute("action"), {
                method : "post",
                data : data,
                headers : {
                    "Content-Type" : "application/json"
                },
                on : {
                    success : function() {
                    	this.get("contentBox").set("innerHTML", this.get("thanks"));
                    },
                    failure : function() {
                        alert("Sorry, sending feedback failed.");
                    }
                },
                context : this
            });
        },
        _toggleVisibility : function() {
            var boundingBox = this.get("boundingBox");
//            //this forces the markup to be rendered, not sure why it is needed.
            boundingBox.setStyle("visibility", "hidden");
            boundingBox.setStyle("visibility", "visible");
        },
        _getFeedback : function(form) {
            var nodes = form.all("input, textarea, select"), feedback = {}, i, node, name;
            for (i = 0; i < nodes.size(); i++) {
            	node = nodes.item(i);
            	name = node.get("name");
            	if (name) {
                	feedback[name] = node.get("value");
            	}
            }
            return feedback;
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
            this.set("validator", new Y.lane.FormValidator(items.item(event.newVal).one("form")));
            focusElement = items.item(event.newVal).one("textarea, input[type='text']");
            if (focusElement) {
                try {
                    focusElement.focus();
                } catch (e) {
                    //IE6 throws an error here:
                    //Can't move focus to the control because it is invisible, not enabled, or
                    //of a type that does not accept the focus.
                }
            }
        },
        _handleMenuClick : function(event) {
            event.preventDefault();
            this.set("activeItem", this.get("menu").indexOf(event.currentTarget));
        },
        _handleSubmit : function(event) {
        	event.preventDefault();
        	if (this.get("validator").isValid()) {
        		this.sendFeedback(event.currentTarget);
        	}
        },
        _handleValidatorChange : function(event) {
        	if (event.prevVal) {
        		event.prevVal.destroy();
        	}
        }
    });
    
    Y.lane.Feedback = Feedback;
    
    Y.lane.Lightbox.on("contentChanged", function(event) {
        if (Y.one("#feedback")) {
            var feedback = new Y.lane.Feedback({srcNode : "#feedback"}),
                url = Y.lane.Lightbox.get("url"),
                hash, items, index;//, textInputs, title, i;
            feedback.render();
            //if there is a hash in the url, choose that as the active item
            if (url.indexOf("#") > -1) {
                hash = url.substring(url.indexOf("#"));
                items = feedback.get("items");
                index = items.indexOf(feedback.get("contentBox").one(hash));
                if (index > -1) {
                    feedback.set("activeItem", index);
                }
            }

            //create a TextInput object for each <input type="text"/>
//            textInputs = new Y.one("#feedback").all('input[type="text"]');
//            for (i = 0; i < textInputs.size(); i++) {
//                title = textInputs.item(i).get('title');
//                if (title) {
//                    (new Y.lane.TextInput(textInputs.item(i), title));
//                }
//            }
        }
    });
    
})();
