(function() {

    "use strict";

    var DEFAULT_THANKS = "Thank you for your feedback.",

    Feedback = function(config) {
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
            value : DEFAULT_THANKS
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
            this.after("activeItemChange", this._handleActiveItemChange);
            this.get("srcNode")._node.querySelectorAll("form").forEach(function(form) {
                form.addEventListener("submit", function(event) {
                    self._handleSubmit.call(self, event);
                });
            });
            eventHandle1 = L.Lightbox.on("animEnd", function() {
                self.get("items").item(self.get("activeItem")).one("textarea, input[type='text']").focus();
            });
            eventHandle2 = L.Lightbox.on("visibleChange", function(event) {
                if (!event.newVal) {
                    eventHandle1.detach();
                    eventHandle2.detach();
                    self.destroy();
                }
            }, this);
            document.querySelector("#feedback .close").addEventListener("click", function(event) {
                event.preventDefault();
                L.Lightbox.hide();
            });
        },
        syncUI : function() {
            var activeItem = this.get("activeItem"),
                items = this.get("items"),
                srcNode = this.get("srcNode"),
                sending = srcNode.one("#sending");
            this.get("menu").item(activeItem).addClass(this.getClassName("menu", "active"));
            items.item(activeItem).addClass(this.getClassName("item", "active"));
            if (sending) {
                this.set("sending", sending.get("innerHTML"));
            }
            this._resetThanks();
        },
        sendFeedback : function(form) {
            var contentBox = this.get("contentBox"),
                data = JSON.stringify(this._getFeedback(form));
            contentBox.one(".feedback-contents").set("innerHTML", this.get("sending"));
            contentBox.scrollIntoView();
            L.io(form.getAttribute("action"), {
                method : "post",
                data : data,
                headers : {
                    "Content-Type" : "application/json"
                },
                on : {
                    success : function() {
                        this.get("contentBox").one(".feedback-contents").set("innerHTML", this.get("thanks"));
                    },
                    failure : function() {
                        L.showMessage("Sorry, sending feedback failed.");
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
                newItem = items.item(event.newVal),
                newItemThanks = newItem.one(".feedback-item-thanks"),
                focusElement;
            menu.item(event.prevVal).removeClass(menuActiveClass);
            items.item(event.prevVal).removeClass(itemActiveClass);
            menu.item(event.newVal).addClass(menuActiveClass);
            newItem.addClass(itemActiveClass);
            if (newItemThanks) {
                this.set("thanks", newItemThanks.get("innerHTML"));
            } else {
                this._resetThanks();
            }
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
            this.sendFeedback(new Y.Node(event.currentTarget));
        },
        _resetThanks: function() {
            var srcNode = this.get("srcNode"),
                thanks = srcNode.one("#thanks");
            this.set("thanks", thanks ? thanks.get("innerHTML") : DEFAULT_THANKS);
        }
    });

    L.Feedback = Feedback;

    L.Lightbox.on("contentChanged", function() {
        if (document.querySelector("#feedback")) {
            var feedback = new L.Feedback({srcNode : "#feedback"}),
                hash = L.Lightbox.get("hash"),
                items, index;
            feedback.render();
            //if lightbox has a hash, choose that as the active item
            if (hash) {
                items = feedback.get("items");
                index = items.indexOf(feedback.get("contentBox").one(hash));
                if (index > -1) {
                    feedback.set("activeItem", index);
                }
            }
        }
    });

})();
