(function() {
    
    /**
     * A class that handles mouseover and mouseleave events on search and browse
     * resources that have abstracts or descriptions.
     * @class HoverController
     * @constructor
     */
    var HoverController = function() {
        
        //timer for activating hover state
        var timer = null,

            //the mouseleave handle so it can be detached when done
            mouseLeaveHandler = null,
            
            // delay for all but iPhone/p*d
            timeout = Y.UA.ios ? 0 : 1000,
            
            /**
             * Turn the hover state off by removing the "active" class.  Also calls
             * reset to cancel the timer and detach the mouseleave event handler.
             * @method deactivate
             * @private
             */
            deactivate = function() {
                this.removeClass("active");
                reset();
            },
            
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
    
    //create a HoverController
    hc = new HoverController();
    
    //delegate mouseenter events on class "hvrTrig" and "hoverTrigger"
    Y.delegate("mouseenter", function(event) {
        hc.setTarget(event.currentTarget);
    }, "#searchResults", ".hvrTrig, .hoverTrigger");
    
})();