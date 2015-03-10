(function() {

    /**
     * A class that handles mouseover and mouseleave events on search and browse
     * resources that have abstracts or descriptions.
     * @class ResultDescriptionController
     * @constructor
     */
    var ResultDescriptionController = function() {

        //timer for activating hover state
        var timer = null,

            //the mouseleave handle so it can be detached when done
            mouseLeaveHandler = null,

            // delay for all but iPhone/p*d
            timeout = Y.UA.ios ? 0 : 1000,

            /**
             * Turn the hover state on by adding the "active" class.
             * @method activate
             * @private
             */
            activate = function() {
                this.addClass("active");
            },

            /**
             * Detach the mouseleave event handler and cancel the timer.
             * @method reset
             * @private
             */
            reset = function() {
                if (mouseLeaveHandler) {
                    mouseLeaveHandler.detach();
                    mouseLeaveHandler = null;
                }
                if (timer) {
                    timer.cancel();
                    timer = null;
                }
            },

            /**
             * Turn the hover state off by removing the "active" class.  Also calls
             * reset to cancel the timer and detach the mouseleave event handler.
             * @method deactivate
             * @private
             */
            deactivate = function() {
                this.removeClass("active");
                reset();
            };

        return {

            /**
             * Initiates control of the hover state of a node.  It first removes the class "hvrTrig"
             * to prevent immediate css based hover state and adds the class "hoverTrigger".  It
             * then sets up the mouseleave handler and sets the timer.
             * @method setTarget
             * @param node {Node} the node on which to control hover state
             */
            setTarget : function(node) {
                node.removeClass("hvrTrig");
                node.addClass("hoverTrigger");
                reset();
                mouseLeaveHandler = node.on("mouseleave", deactivate, node);
                timer = Y.later(timeout, node, activate);
            }
        };
    },

    //create a ResultDescriptionController
    rdc = new ResultDescriptionController(),

    initializeDescriptionToggles = function() {
        var triggers = Y.all(".descriptionTrigger");
        triggers.each(function(node) {
            if (node.hasClass("eresource")) {
                node.set("innerHTML", "<a href=\"#\">View Description <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (node.hasClass("searchContent")) {
                node.set("innerHTML", "<a href=\"#\">Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>");
            }
        });

        Y.delegate("click", function(event) {
            var node = event.currentTarget,
            ancestor = node.ancestor("li"),
            active = ancestor.hasClass("active"),
            eresource = node.hasClass("eresource"),
            searchContent = node.hasClass("searchContent");

            event.preventDefault();
            ancestor.toggleClass("active");
            if (active && eresource) {
                node.set("innerHTML", "<a href=\"#\">View Description <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (active && searchContent) {
                node.set("innerHTML", "<a href=\"#\">Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (!active) {
                node.set("innerHTML", "<a href=\"#\">close... <i class=\"fa fa-angle-double-up\"></i></a>");
            }
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:descriptionTrigger",
                action: event.target.get('textContent'),
                label: ancestor.one('.primaryLink').get('textContent')
            });
        }, "#searchResults", ".descriptionTrigger");
    };

    //add trigger markup and delegate click events on class "descriptionTrigger"
    if (Y.one("#searchResults")) {
        initializeDescriptionToggles();
    }

    //reinitialize when content has changed
    Y.lane.on("lane:new-content", function() {
        initializeDescriptionToggles();
    });

    //delegate mouseenter events on class "hvrTrig" and "hoverTrigger"
    if (Y.one("#searchResults")) {
        Y.delegate("mouseenter", function(event) {
            rdc.setTarget(event.currentTarget);
        }, "#searchResults", ".hvrTrig, .hoverTrigger");
    }

})();