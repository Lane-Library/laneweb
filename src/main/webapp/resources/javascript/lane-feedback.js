(function() {

    var Feedback = function(config) {
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
        drag: {
            value: null
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
            this.set("drag", new Y.DD.Drag({node: ".yui3-lightbox"}));
        },
        bindUI : function() {
            var self = this, eventHandle1, eventHandle2;
            this.get("menu").on("click", this._handleMenuClick, this);
            this.after("activeItemChange", this._handleActiveItemChange);
            this.on("validatorChange", this._handleValidatorChange);
            this.get("srcNode").all("form").on("submit", this._handleSubmit, this);
            eventHandle1 = Y.lane.Lightbox.on("animEnd", function() {
                self.get("items").item(self.get("activeItem")).one("textarea, input[type='text']").focus();
            });
            eventHandle2 = Y.lane.Lightbox.on("visibleChange", function(event) {
                if (!event.newVal) {
                    eventHandle1.detach();
                    eventHandle2.detach();
                    self.destroy();
                }
            });
            Y.one("#feedback .close").on("click", function(event) {
                // TODO: shouldn't have to do this with drag, only because purchase-suggestions extends feedback
                var drag = this.get("drag");
                if (drag) {
                    drag.destroy();
                }
                event.preventDefault();
                Y.lane.Lightbox.hide();
            });
            //create a TelInput object for each input with type="tel" (see telinput.js)
            this.get("srcNode").all("input[type='tel']").each(function(input) {
                (new Y.lane.TelInput(input));
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
        resetValidator : function() {
            var activeItem = this.get("activeItem"),
                items = this.get("items");
            this.set("validator", new Y.lane.FormValidator(items.item(activeItem).one("form")));
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
        _getFeedback : function(form) {
            var nodes = form.all("input, textarea, select"), feedback = {}, i, node, name;
            for (i = 0; i < nodes.size(); i++) {
                node = nodes.item(i);
                name = node.get("name");
                if (name && (node.get("type") !== "radio" || node.get("checked"))) {
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
            this.resetValidator();
            focusElement = items.item(event.newVal).one("textarea, input[type='text']");
            if (focusElement) {
                focusElement.focus();
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

    Y.lane.Lightbox.on("contentChanged", function() {
        if (Y.one("#feedback")) {
            var feedback = new Y.lane.Feedback({srcNode : "#feedback"}),
                url = Y.lane.Lightbox.get("url"),
                hash, items, index;
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
        }
    });

})();
