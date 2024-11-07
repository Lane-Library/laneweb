
(function () {

    "use strict";

    let
        OX = -10000,
        OY = -10000,

        OFFSET_X = 15,
        OFFSET_Y = 15;


    class Tooltip {


        constructor(args) {
            this.showDelay = 250;
            this.hideDelay = 10;
            this.visible = false;
            this.xy = [OX, OY]
            this.content = args.content;
            this.triggerNodes = this.triggerNodes(args.triggerNodes);
            this.zIndex = args.zIndex;
            this.autoHideDelay = args.autoHideDelay;
            this.initializer();
            this.syncUI();
            this.delegate = this.setDelegate(args.delegate);
            this.bindUI();
        }

        // PROTOTYPE METHODS/PROPERTIES

        /*
         * Initialization Code: Sets up privately used state
         * properties, and publishes the events Tooltip introduces
         */
        initializer() {
            this._triggerClassName = "tooltip-trigger";
            // Currently bound trigger node information
            this._currTrigger = {
                node: null,
                title: null,
                mouseX: Tooltip.OFFSCREEN_X,
                mouseY: Tooltip.OFFSCREEN_Y,
                mouseClientX: Tooltip.OFFSCREEN_X,
                mouseClientY: Tooltip.OFFSCREEN_Y
            };

            // Show/hide timers
            this._timers = {
                show: null,
                hide: null
            };

        }

        /*
         * Destruction Code: Clears event handles, timers,
         * and current trigger information
         */
        destructor() {
            this._clearCurrentTrigger();
            this._clearTimers();
            this._clearHandles();
        }

        /*
         * bindUI is used to bind attribute change and dom event
         * listeners
         */
        bindUI() {
            L.addEventTarget(this, {
                prefix: 'tooltip',
            });

            // Publish events introduced by Tooltip. Note the triggerEnter event is preventable,
            // with the default behavior defined in the _defTriggerEnterFn method
            this.on("triggerEnter", (e) => this._defTriggerEnterFn(e));
            this._bindDelegate();
        }

        /*
         * syncUI is used to update the rendered DOM, based on the current
         * Tooltip state
         */
        syncUI() {
            this._uiSetNodes(this.triggerNodes);
        }

        /*
         * Public method, which can be used by triggerEvent event listeners
         * to set the content of the tooltip for the current trigger node
         */
        setTriggerContent(content) {
            this.deleteTooltip();
            let i, l, contentBox = document.createElement("div");
            contentBox.classList.add("tooltip");
            contentBox.classList.add("tooltip-content");
            document.body.appendChild(contentBox);
            contentBox.innerHTML = "";
            if (content) {
                if (content instanceof Node) {
                    for (i = 0, l = content.size(); i < l; ++i) {
                        contentBox.appendChild(content.item(i));
                    }
                }
                if (typeof content === 'string') {
                    contentBox.innerHTML = content;
                }
            }
        }



        /*
         * Updates the rendered DOM to reflect the
         * set of trigger nodes passed in
         */
        _uiSetNodes(nodes) {
            if (this._triggerNodes) {
                this._triggerNodes.removeClass(this._triggerClassName);
            }

            if (nodes) {
                this._triggerNodes = nodes;
                this._triggerNodes.forEach(node => {
                    if (node) {
                        node.classList.add(this._triggerClassName);
                    }
                });
            }
        }
        /*
         * Attaches the default mouseover DOM listener to the
         * current delegate node
         */
        _bindDelegate() {
            const delegateNode = this.delegate;
            if (delegateNode) {
                delegateNode.querySelectorAll("." + this._triggerClassName).forEach((node) => {
                    node.addEventListener("mouseenter", (e) => {
                        this._onNodeMouseEnter(e);
                    });
                });
            }
        }

        /*
         * Default mouse enter DOM event listener.
         *
         * Delegates to the _enterTrigger method,
         * if the mouseover enters a trigger node.
         */
        _onNodeMouseEnter(e) {
            let node = e.currentTarget;
            if (node) {
                this._enterTrigger(node, e.pageX, e.pageY, e.clientX, e.clientY);
            }
        }

        /*
         * Default mouse leave DOM event listener
         *
         * Delegates to _leaveTrigger if the mouse
         * leaves the current trigger node
         */
        _onNodeMouseLeave(e) {
            this._leaveTrigger(e.currentTarget);
        }

        /*
         * Default mouse move DOM event listener
         */
        _onNodeMouseMove(e) {
            this._overTrigger(e.pageX, e.pageY, e.clientX, e.clientY);
        }

        /*
         * Default handler invoked when the mouse enters
         * a trigger node. Fires the triggerEnter
         * event which can be prevented by listeners to
         * show the tooltip from being displayed.
         */
        _enterTrigger(node, x, y, mouseClientX, mouseClientY) {
            this._setCurrentTrigger(node, x, y, mouseClientX, mouseClientY);
            this.fire("triggerEnter", { node: node, pageX: x, pageY: y, mouseClientX: mouseClientX, mouseClientY: mouseClientY });
        }

        /*
         * Default handler for the triggerEvent event,
         * which will setup the timer to display the tooltip,
         * if the default handler has not been prevented.
         */
        _defTriggerEnterFn(e) {
            let delay, node = e.node;
            if (!this.disabled) {
                this._clearTimers();
                delay = (this.visible) ? 0 : this.showDelay;
                this._timers.show = setTimeout(() => this._showTooltip(node), delay);
            }
        }

        /*
         * Default handler invoked when the mouse leaves
         * the current trigger node. Fires the triggerLeave
         * event and sets up the hide timer
         */
        _leaveTrigger() {
            this._clearCurrentTrigger();
            this._clearTimers();
            this._timers.hide = setTimeout(() => this._hideTooltip(), this.hideDelay);
        }

        /*
         * Default handler invoked for mousemove events
         * on the trigger node. Stores the current mouse
         * x, y positions
         */
        _overTrigger(x, y, mouseClientX, mouseClientY) {
            this._currTrigger.mouseX = x;
            this._currTrigger.mouseY = y;
            this._currTrigger.mouseClientX = mouseClientX;
            this._currTrigger.mouseClientY = mouseClientY;
        }

        /*
         * Shows the tooltip, after moving it to the current mouse
         * position.
         */
        _showTooltip() {
            let tt = document.querySelector(".tooltip.tooltip-content"),
                height = tt.clientHeight,
                width = tt.clientWidth,
                x = this._currTrigger.mouseX,
                y = this._currTrigger.mouseY,
                mouseClientX = this._currTrigger.mouseClientX,
                mouseClientY = this._currTrigger.mouseClientY;

            if (mouseClientX >= document.documentElement.clientWidth - width) {
                x = x - width - OFFSET_X;
                if (x < 0) {
                    x = OFFSET_X;
                }

            } else {
                x = x + OFFSET_X;
            }

            if (mouseClientY >= document.documentElement.clientHeight - height) {
                y = y - height - OFFSET_Y;
            } else {
                y = y + OFFSET_Y;
            }


            tt.style.left = x + "px";
            tt.style.top = y + "px";




            this._clearTimers();

            this._timers.hide = setTimeout(() => this._hideTooltip(), this.autoHideDelay);
        }

        /*
         * Hides the tooltip, after clearing existing timers.
         */
        _hideTooltip() {
            this._clearTimers();
            this.deleteTooltip();
        }


        deleteTooltip() {
            let tooltip = document.body.querySelector(".tooltip.tooltip-content");
            if (tooltip) {
                document.body.removeChild(tooltip);
            }
        }

        /*
         * Set the rendered content of the tooltip for the current
         * trigger, based on (in order of precedence):
         *
         * a). The string/node content attribute value
         * b). From the content lookup map if it is set, or
         * c). From the title attribute if set.
         */
        _setTriggerContent(node) {
            let content = this.content;

            if (content) {
                content = content[node.id] || node.title;
            }
            this.setTriggerContent(content);
        }

        /*
         * Set the currently bound trigger node information, clearing
         * out the title attribute if set and setting up mousemove/out
         * listeners.
         */
        _setCurrentTrigger(node, x, y, mouseClientX, mouseClientY) {

            let currTrigger = this._currTrigger, title;
            this._setTriggerContent(node);
            node.addEventListener("mousemove", this._onNodeMouseMove.bind(this));
            node.addEventListener("mouseleave", this._onNodeMouseLeave.bind(this));

            title = node.getAttribute("title");
            if (title != '') {
                node.setAttribute("title", "");
            }
            currTrigger.mouseX = x;
            currTrigger.mouseY = y;
            currTrigger.mouseClientX = mouseClientX;
            currTrigger.mouseClientY = mouseClientY;
            currTrigger.node = node;
            currTrigger.title = title;
        }

        /*
         * Clear out the current trigger state, restoring
         * the title attribute on the trigger node,
         * if it was originally set.
         */
        _clearCurrentTrigger() {
            let currTrigger = this._currTrigger,
                node,
                title;

            if (currTrigger.node) {
                node = currTrigger.node;
                title = currTrigger.title || "";
                node.removeEventListener("mousemove", this._onNodeMouseMove.bind(this));
                node.removeEventListener("mouseleave", this._onNodeMouseLeave.bind(this));
                node = null;
                currTrigger.title = null;
                if (title != '') {
                    node.title = title;
                }
            }
        }

        /*
         * Cancel any existing show/hide timers
         */
        _clearTimers() {
            let timers = this._timers;
            if (timers.hide) {
                clearTimeout(timers.hide);
                timers.hide = null;
            }
            if (timers.show) {
                clearTimeout(timers.show);
                timers.show = null;
            }
        }


        triggerNodes(val) {
            if (val && typeof val === 'string') {
                let nodes = [];
                val.split(',').forEach((id) => {
                    let anchors = document.querySelectorAll(id);
                    if (anchors) {
                        anchors.forEach((el) => {
                            nodes.push(el)
                        })
                    }
                });
                return nodes;
            };
            return val;
        }


        /*
         * The delegate node to which event listeners should be attached.
         * This node should be an ancestor of all trigger nodes bound
         * to the instance. By default the document is used.
         */
        setDelegate(val) {
            let body = document.body;
            return body.querySelector(val) || document.body;
        }
    }


    let createTooltips = function () {
        let tooltipTriggerIds,
            tooltipContainer, tooltipId, i, j, tt, content = {},
            tooltipContainerNodeList = document.querySelectorAll('.tooltips');
        for (i = 0; i < tooltipContainerNodeList.length; i++) {
            tooltipContainer = tooltipContainerNodeList[i].children;
            for (j = 0; j < tooltipContainer.length; j++) {
                if (tooltipContainer[j].id) {
                    tooltipId = tooltipContainer[j].id.replace(/Tooltip$/, '');
                    tooltipTriggerIds += ', #' + tooltipId;
                    content[tooltipId] = tooltipContainer[j].innerHTML;
                }
            }
        }
        tt = new Tooltip({
            content: content,
            triggerNodes: tooltipTriggerIds,
            zIndex: 2,
            autoHideDelay: 60000,
            delegate: ".content"
        });
        tt.on('visibleChange', function (e) {
            if (!e.newVal) {
                e.target.reset();
            }
        });

        L.ToolTips = tt;
    };

    createTooltips();

    //reinitialize when content has changed
    L.on("lane:new-content", function () {
        L.ToolTips.destructor();
        createTooltips();
    });

})();
