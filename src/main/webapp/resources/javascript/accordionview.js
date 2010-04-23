
YUI().use('yui2-event','yui2-dom','yui2-animation','yui2-element', function(Y) {

    /**
    *
    * By Marco van Hylckama Vlieg (marco@i-marco.nl)
    *
    * THIS IS A WORK IN PROGRESS
    *
    * Many, many thanks go out to Daniel Satyam Barreiro!
    * Please read his article about YUI widget development
    * http://yuiblog.com/blog/2008/06/24/buildingwidgets/
    * Without his excellent help and advice this widget would not
    * be half as good as it is now.
    */
    
    /**
    * The accordionview module provides a widget for managing content bound to an 'accordion'.
    * @module accordionview
    * @requires yahoo, dom, event, element, animation
    */
    
    var YUD = Y.YUI2.util.Dom, YUE = Y.YUI2.util.Event, YUA = Y.YUI2.util.Anim,
    
    /**
    * A widget to control accordion views.
    * @namespace Y.YUI2.widget
    * @class AccordionView
    * @extends Y.YUI2.util.Element
    * @constructor
    * @param {HTMLElement | String} el The id of the html element that represents the AccordionView. 
    * @param {Object} oAttr (optional) A key map of the AccordionView's 
    * initial oAttributes.  
    */

    AccordionView = function(el, oAttr) {
        
        el = YUD.get(el);
        
        // some sensible defaults
        
        oAttr = oAttr || {};
        if(!el) {
            el = document.createElement(this.CONFIG.TAG_NAME);
        }
        if (el.id) {oAttr.id = el.id; }
        Y.YUI2.widget.AccordionView.superclass.constructor.call(this, el, oAttr); 

        this.initList(el, oAttr);
                
        // This refresh forces all defaults to be set
         
        this.refresh(['id', 'width','hoverActivated'],true);  
        
    },


    /**
     * @event panelClose
     * @description Fires before a panel closes.
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    panelCloseEvent = 'panelClose',

    /**
     * @event panelOpen
     * @description Fires before a panel opens.
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    panelOpenEvent = 'panelOpen',
    
    /**
     * @event afterPanelClose
     * @description Fires after a panel has finished closing.
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    afterPanelCloseEvent = 'afterPanelClose',
 
    /**
     * @event afterPanelOpen
     * @description Fires after a panel has finished opening.
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    afterPanelOpenEvent = 'afterPanelOpen',

    /**
     * @event stateChanged
     * @description Fires after the accordion has fully changed state (after opening and/or closing (a) panel(s)).
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    stateChangedEvent = 'stateChanged',
    
    /**
     * @event beforeStateChange
     * @description Fires before a state change.
     * This is useful to cancel an entire change operation
     * See <a href="Y.YUI2.util.Element.html#addListener">Element.addListener</a>
     * for more information on listening for this event.
     * @type Y.YUI2.util.CustomEvent
     */
    
    beforeStateChangeEvent = 'beforeStateChange';

    Y.YUI2.widget.AccordionView = AccordionView;
    
    Y.YUI2.extend(AccordionView, Y.YUI2.util.Element, {
                
        /**
        * Initialize attributes for the Accordion
        * @param {Object} oAttr attributes key map
        * @method initAttributes
        */
        
        initAttributes: function (oAttr) {
            AccordionView.superclass.initAttributes.call(this, oAttr);
            var bAnimate = (Y.YUI2.env.modules.animation) ? true : false;    
            this.setAttributeConfig('id', {
                writeOnce: true,
                validator: function (value) {
                    return (/^[a-zA-Z][\w0-9\-_.:]*$/.test(value));
                },
                value: YUD.generateId(),
                method: function (value) {
                    this.get('element').id = value;
                }
            });
            this.setAttributeConfig('animationSpeed', {
                value: 0.35
                }
            );
            this.setAttributeConfig('animate', {
                value: bAnimate,
                validator: Y.YUI2.lang.isBoolean
                }
            );          
            this.setAttributeConfig('collapsible', {
                value: true,
                validator: Y.YUI2.lang.isBoolean
                }
            );
            this.setAttributeConfig('expandable', {
                value: true,
                validator: Y.YUI2.lang.isBoolean
                }
            );
            this.setAttributeConfig('effect', {
                value: Y.YUI2.util.Easing.easeBoth,
                validator: Y.YUI2.lang.isString

                }
            );
            this.setAttributeConfig('hoverActivated', {
                    value: false,
                    validator: Y.YUI2.lang.isBoolean,
                    method: function (value) {
                            if (value) {
                                    YUE.on(this, 'mouseover', this._onMouseOver, this, true);                        
                            } else {
                                    YUE.removeListener(this, 'mouseover', this._onMouseOver);
                            }        
                    }
            });
            this.setAttributeConfig('_hoverTimeout', {
                value: 500,
                validator: Y.YUI2.lang.isInteger
                }
            );
        },
        
        /**
         * Configuration object containing tag names used in the AccordionView component.
         * See sourcecode for explanation in case you need to change this
         * @property CONFIG
         * @public
         * @type Object
         */
        
        CONFIG : {
          // tag name for the entire accordion
          TAG_NAME : 'UL',
          // tag name for the wrapper around a toggle + content pair
          ITEM_WRAPPER_TAG_NAME : 'LI',
          // tag name for the wrapper around the content for a panel
          CONTENT_WRAPPER_TAG_NAME : 'UL'         
        },
 
        /**
         * Configuration object containing classes used in the AccordionView component.
         * See sourcecode for explanation in case you need to change this
         * @property CLASSES
         * @public
         * @type Object
         */
        
        CLASSES : {
            // the entire accordion
            ACCORDION : 'yui-acc',
            // the wrapper around a toggle + content pair
            PANEL : 'yui-acc-panel',
            // the element that toggles a panel
            TOGGLE : 'yui-acc-toggle',
            // the element that contains the content of a panel
            CONTENT : 'yui-acc-content',
            // to indicate that a toggle is active
            ACTIVE : 'yui-acc-active',
            // to indicate that content is hidden
            HIDDEN : 'yui-acc-hidden',
            // the opened/closed indicator
            INDICATOR : 'yui-acc-indicator',
            //almost hidden
            ALMOST_HIDDEN : 'yui-acc-almosthidden'                      
        },
        
        /**
        * Internal counter to make sure id's are unique
        * @property _idCounter
        * @private
        * @type Integer
        */
        
        _idCounter : '1',
        
        /**
        * Holds the timer for hover activated accordions
        * @property _hoverTimer
        * @private
        */
        
        _hoverTimer : null,      

        /**
        * Holds references to all accordion panels (list elements) in an array
        * @property _panels
        * @private
        * @type Array
        */

        _panels : null,
        
        /**
        * Keeps track of whether a panel is currently in the process of opening.
        * Used to time when a full change is finished (open and close panel)
        * @property _opening
        * @private
        * @type Boolean
        */        
        _opening : false,
        
        /**
        * Keeps track of whether a panel is currently in the process of closing.
        * Used to time when a full change is finished (open and close panel)
        * @property _closing
        * @private
        * @type Boolean
        */        
        
        _closing : false,
                
        /**
        * Whether we're running FF2 or older (or another derivate of Gecko < 1.9)
        * @property _ff2
        * @private
        * @type Boolean
        */
        
        _ff2 : (Y.YUI2.env.ua.gecko > 0 && Y.YUI2.env.ua.gecko < 1.9),

        /**
        * Whether we're running IE6 or IE7
        * @property _ie
        * @private
        * @type Boolean
        */

        _ie : (Y.YUI2.env.ua.ie < 8 && Y.YUI2.env.ua.ie > 0),
        
        /**
        * Initialize the list / accordion
        * @param {HTMLElement} el The element for the accordion
        * @param {Object} oAttr attributes key map
        * @method initList
        * @public
        */

        initList : function(el, oAttr) {
            var i, aCollectedItems = [], eHeader, eContent, eLink,
                aListItems = el.getElementsByTagName(this.CONFIG.ITEM_WRAPPER_TAG_NAME);

            for(i=0;i<aListItems.length;i++) {           
                if(YUD.hasClass(aListItems[i], 'nopanel')) {         
                    aCollectedItems.push({label: 'SINGLE_LINK', content: aListItems[i].innerHTML.replace(/^\s\s*/, '').replace(/\s\s*$/, '')});
                }
                else {
                    if(aListItems[i].parentNode === el) {
                        for (eHeader = aListItems[i].firstChild; eHeader && eHeader.nodeType != 1; eHeader = eHeader.nextSibling) {
                        // This loop looks for the first non-textNode element
                        }
                        if (eHeader) {
                            for (eContent = eHeader.nextSibling; eContent && eContent .nodeType != 1; eContent = eContent .nextSibling) {
                            // here we go for the second non-textNode element, if there was a first one
                            }
                        aCollectedItems.push({panel: aListItems[i], label: eHeader, content: eContent});
                        }
                    }
                }
            }
            
            if(aCollectedItems.length > 0) {
                this.setupPanels(aCollectedItems);
            }
            
            for (i = 0; i < oAttr.expandItems.length; i++) {
                                                 
                eLink = this._panels[oAttr.expandItems[i]].firstChild;
                eContent = this._panels[oAttr.expandItems[i]].firstChild.nextSibling;
                YUD.removeClass(eContent, this.CLASSES.HIDDEN);
                if(eLink && eContent) {
                    YUD.addClass(eLink, this.CLASSES.ACTIVE);
                }
            }
  
            this.initEvents();
        },
        
        /**
        * Attach all event listeners
        * @method initEvents
        * @public
        */
        
        initEvents : function() {
            
            if(true === this.get('hoverActivated')) {
                    this.on('mouseover', this._onMouseOver, this, true);        
                    this.on('mouseout', this._onMouseOut, this, true);         
            }
            
            this.on('click', this._onClick, this, true);
            this.on('keydown', this._onKeydown, this, true);
            
            // set this._opening and this._closing before open/close operations begin
            
            this.on('panelOpen', function(){this._opening = true;}, this, true);
            this.on('panelClose', function(){this._closing = true;}, this, true);
            
            // This makes sure that this._fixTabindexes is called after a change has
            // fully completed
            
            this.on('afterPanelClose', function(){
                this._closing = false;
            }, this, true);
            this.on('afterPanelOpen', function(){
                this._opening = false;
            }, this, true);
        },
        
        /**
        * Closes all panels 
        * @method _collapseAccordion
        * @private
        */

        _collapseAccordion : function() {
            YUD.batch(this._panels, function(e) {
                var elContent = this.firstChild.nextSibling;
                if(elContent) { 
                    YUD.removeClass(e.firstChild, this.CLASSES.ACTIVE);
                    YUD.addClass(elContent, this.CLASSES.HIDDEN);
                }
            }, this);
        },

        /**
        * Adds an Accordion panel to the AccordionView instance.  
        * If no index is specified, the panel is added to the end of the tab list.
        * @method setupPanel
        * @param {Object} oAttr A key map of the Panel's properties
        */

        setupPanel : function(oAttr) {
            var i, t, elPanelLink, elPanelContent, oPanelParent = oAttr.panel;//document.createElement(this.CONFIG.ITEM_WRAPPER_TAG_NAME);

            for (i = 0; i < oPanelParent.childNodes.length; i++) {
                if (oPanelParent.childNodes[i].nodeType == 3) {//Node.TEXT_NODE not available IE6
                    oPanelParent.removeChild(oPanelParent.childNodes[i]);
                    i--;
                }
            }
                
            YUD.addClass(oPanelParent, this.CLASSES.PANEL);
            
            // single links that have no panel get class link and
            // no +/- indicator
            
            if(oAttr.label === 'SINGLE_LINK') {
                oPanelParent.innerHTML = oAttr.content;
                YUD.addClass(oPanelParent.firstChild, this.CLASSES.TOGGLE);
                YUD.addClass(oPanelParent.firstChild, 'link');
            }
            else {
                elPanelLink = oAttr.label;
                // commented out b/c want to retain original element ID
                // not sure if this will cause other problems ... doesn't look like concat'd ID is used
                //elPanelLink.id = this.get('element').id + '-' + this._idCounter + '-label';
                YUD.addClass(elPanelLink, this.CLASSES.TOGGLE);
                elPanelContent = oAttr.content;
                YUD.addClass(elPanelContent, this.CLASSES.CONTENT);       
            }
            this._idCounter++;
            if(this._panels === null) {
                this._panels = [];
            }
                if(this.get('element') === oPanelParent.parentNode) {
                    this._panels[this._panels.length] = oPanelParent;
                }
            if(oAttr.label !== 'SINGLE_LINK') {
                if(oAttr.expand) {
                    if(!this.get('expandable')) {
                        this._collapseAccordion();
                    }
                    YUD.removeClass(elPanelContent, this.CLASSES.HIDDEN);
                    YUD.addClass(elPanelLink, this.CLASSES.ACTIVE);
                }
                else {
                    YUD.addClass(elPanelContent, this.CLASSES.HIDDEN);
                }
            }
            t= Y.YUI2.lang.later(0, this, function(){
            this.fireEvent(stateChangedEvent);});
        },

        /**
        * Wrapper around setupPanel to setup multiple panels in one call
        * @method setupPanels
        * @param {Array} oPanels array holding all individual panel configs
        */

        setupPanels : function(oPanels) {
            for(var i=0;i<oPanels.length;i++) {
                this.setupPanel(oPanels[i]);
            }
        },

        /**
        * Removes the specified Panel from the AccordionView.
        * @method removePanel
        * @param {Integer} index of the panel to be removed
        */

        removePanel : function(index) {
            this.removeChild(YUD.getElementsByClassName(this.CLASSES.PANEL, this.CONFIG.ITEM_WRAPPER_TAG_NAME, this)[index]); 
            var i, t, aNewPanels = [],
                nLength = this._panels.length;
            for(i=0;i<nLength;i++) {
                if(i !== index) {
                    aNewPanels.push(this._panels[i]);
                }
            }
            this._panels = aNewPanels;
            t= Y.YUI2.lang.later(0, this, function(){
                this.fireEvent(stateChangedEvent);
            });
        },


        /**
        * Returns the HTMLElement of the panel at the specified index.
        * @method getPanel
        * @param {Integer} nIndex The position of the Panel.
        * @return {HTMLElement} the requested panel element
        */

        getPanel : function(nIndex) {
            return this._panels[nIndex];
        },

        /**
        * Returns the Array containing all panels
        * @method getPanels
        * @return {Array} An array with references to the panels in the correct order
        */

        getPanels : function() {
            return this._panels;
        },

        /**
        * Open a panel
        * @method openPanel
        * @param {Integer} nIndex The position of the Panel.
        * @return {Boolean} whether action resulted in opening a panel
        * that was previously closed
        */

        openPanel : function(nIndex) {
            var ePanelNode = this._panels[nIndex];
            if(!ePanelNode) {return false;} // invalid node
            if(YUD.hasClass(ePanelNode.firstChild, this.CLASSES.ACTIVE)) {return false;} // already open
            this._onClick(ePanelNode.firstChild);
            return true;
        },

        /**
        * Close a panel
        * @method closePanel
        * @param {Integer} nIndex The position of the Panel.
        * @return {Boolean} whether action resulted in closing a panel or not
        * that was previously open
        *
        * This method honors all constraints imposed by the properties collapsible and expandable
        * and will return false if the panel can't be closed because of a constraint in addition
        * to if it was already closed
        *
        */

        closePanel : function(nIndex) {
            var i, ePanelLink, aItems = this._panels,
                ePanelNode = aItems[nIndex];
            if(!ePanelNode) {return false;} // invalid node
            ePanelLink = ePanelNode.firstChild;
            if(!YUD.hasClass(ePanelLink, this.CLASSES.ACTIVE)) {return true;} // already closed
            if(this.get('collapsible') === false) {
                if(this.get('expandable') === true) {
                    this.set('collapsible', true);
                    for(i=0;i<aItems.length;i++) {
                        if((YUD.hasClass(aItems[i].firstChild, this.CLASSES.ACTIVE) && i !== nIndex)) {
                            this._onClick(ePanelLink);
                            this.set('collapsible', false);            
                            return true;
                        }
                    }
                    this.set('collapsible', false);                
                }
            } // can't collapse
            this._onClick(ePanelLink);
            return true;
        },

        /**
        * Keyboard event handler for keyboard control of the widget
        * @method _onKeydown
        * @param {Event} ev The Dom event
        * @private
        */

        _onKeydown : function(ev) {
            var i, eCurrentPanel = YUD.getAncestorByClassName(YUE.getTarget(ev), this.CLASSES.PANEL),
                nKeyCode = YUE.getCharCode(ev),
                nLength = this._panels.length;
            if(nKeyCode === 37 || nKeyCode === 38) {
                for(i=0;i<nLength;i++) {
                    if((eCurrentPanel === this._panels[i]) && i>0) {
                        this._panels[i-1].firstChild.focus();
                        return;
                    } 
                }
            }
            if(nKeyCode === 39 || nKeyCode === 40) {
                for(i=0;i<nLength;i++) {
                    if((eCurrentPanel === this._panels[i]) && i<nLength-1) {
                        this._panels[i+1].firstChild.focus();
                        return;
                    } 
                }
            }
        },

        /**
        * Mouseover event handler
        * @method _onMouseOver
        * @param {Event} ev The Dom event
        * @private
        */

        _onMouseOver : function(ev) {
            YUE.stopPropagation(ev);
            // must provide the TARGET or IE will destroy the event before we can
            // use it. Thanks Nicholas Zakas for pointing this out to me
            var target = YUE.getTarget(ev);
            this._hoverTimer = Y.YUI2.lang.later(this.get('_hoverTimeout'), this, function(){
                this._onClick(target);
             });
        },

        /**
        * Mouseout event handler
        * Cancels the timer set by AccordionView::_onMouseOver
        * @method _onMouseOut
        * @param {Event} ev The Dom event
        * @private
        */
        
        _onMouseOut : function() {
            if (this._hoverTimer) { 
                this._hoverTimer.cancel();
                this._hoverTimer = null;
            }
        },
        
        /**
        * Global event handler for mouse clicks
        * This method can accept both an event and a node so it can be called internally if needed
        * @method _onClick
        * @param {HTMLElement|Event} arg The Dom event or event target
        * @private
        */

        _onClick : function(arg) {
            var i, ev, elClickedNode, that, eTargetListNode, containedPanel, nLength, bMustToggle;
            if(arg.nodeType === undefined) {
                ev = YUE.getTarget(arg);
                if(!YUD.hasClass(ev, this.CLASSES.TOGGLE) && !YUD.hasClass(ev, this.CLASSES.INDICATOR)) {
                    //this used to just return false, I walk up the node tree to find the toggle
                    while (ev && !YUD.hasClass(ev, this.CLASSES.TOGGLE)) {
                        //if it has a href, just return false;
                        if (ev.href) {
                            return false;
                        }
                        ev = ev.parentNode;
                    }
                    if (!ev) {
                        return false;
                    } else {
                        return this._onClick(ev);
                    }
                }
                if(YUD.hasClass(ev, 'link')) {
                    return true;
                }
//                YUE.preventDefault(arg);
//                YUE.stopPropagation(arg);
            }
            else {
                ev = arg;
            }

            elClickedNode = ev;
            that = this;
            /**
            *
            * helper function to fix IE problems with nested accordions
            * still looking for something better but for now this will have to do
            * @param {Object} el element to apply the fix to
            * @param {String} sHide whether to set visibility to hidden or visible
            *
            */

            function iehide(el, sHide) {
                if(that._ie) {
                    var aInnerAccordions = YUD.getElementsByClassName(that.CLASSES.ACCORDION, that.CONFIG.TAG_NAME, el);
                    if(aInnerAccordions[0]) {
                        YUD.setStyle(aInnerAccordions[0], 'visibility', sHide);
                    }
                }
            }

            /**
            *
            * Toggle an accordion panel
            * @param {Object} el element to toggle
            * @param {Object} elClicked the element that was clicked to toggle the corresponding panel
            *
            */

            function toggleItem(el, elClicked) {      
                var i, p, bHideAfter, nSpeed, sEffect, oAnimator, oOptions = {}, nHeight = 0, that = this;
                function fireEvent(type,panel) {
                    if (!YUD.hasClass(panel,that.CLASSES.PANEL)) {
                        panel = YUD.getAncestorByClassName(panel, that.CLASSES.PANEL);
                    }
                    for (i = 0, p = panel; p.previousSibling; i++) {
                        p = p.previousSibling;
                    }
                    return that.fireEvent(type, {panel: panel, index: i});
                }
                      
                if(!elClicked) {
                    if(!el) { return false ;}
                    elClicked = el.parentNode.firstChild;
                }

                bHideAfter = (!YUD.hasClass(el, this.CLASSES.HIDDEN));

                if(this.get('animate')) {
                    if(!bHideAfter) {
                        // this eliminates a flash in Gecko < 1.9
                        if(this._ff2) {
                            YUD.addClass(el, this.CLASSES.ALMOST_HIDDEN);
                            YUD.setStyle(el, 'width', this.get('width'));
                            }
                        YUD.removeClass(el, this.CLASSES.HIDDEN);
                        nHeight = el.offsetHeight;
                        YUD.setStyle(el, 'height', 0);
                        if(this._ff2) {
                            YUD.removeClass(el, this.CLASSES.ALMOST_HIDDEN);
                            YUD.setStyle(el, 'width', 'auto');
                            }
                        oOptions = {height: {from: 0, to: nHeight}};
                    }
                    else {
                        nHeight = el.offsetHeight;
                        oOptions = {height: {from: nHeight, to: 0}};
                    }
                    nSpeed = (this.get('animationSpeed')) ? this.get('animationSpeed') : 0.5;
                    sEffect = (this.get('effect')) ? this.get('effect') : Y.YUI2.util.Easing.easeBoth;
                    oAnimator = new YUA(el, oOptions, nSpeed, sEffect);
                    if(bHideAfter) {
                        if (this.fireEvent(panelCloseEvent, el) === false) { return; }
                        YUD.removeClass(elClicked, that.CLASSES.ACTIVE);

                        iehide(el, 'hidden');

                        oAnimator.onComplete.subscribe(function(){
                            YUD.addClass(el, that.CLASSES.HIDDEN);
                            YUD.setStyle(el, 'height', 'auto');
                            fireEvent('afterPanelClose', el);
                        });
                    }
                    else {
                        if (fireEvent(panelOpenEvent, el) === false) { return; }
                        //changed from visible to hidden so it doesn't show up behind the parent accordion until after the animation
                        iehide(el, 'hidden');
                        oAnimator.onComplete.subscribe(function(){
                            YUD.setStyle(el, 'height', 'auto');
                            //Added to make the inner accordion visible again
                            iehide(el, 'visible');
                            fireEvent(afterPanelOpenEvent, el);
                        });                 
                        YUD.addClass(elClicked, this.CLASSES.ACTIVE);
                    }
                    oAnimator.animate();
                }
                else {
                    if(bHideAfter) {
                        if (fireEvent(panelCloseEvent, el) === false) { return; }
                        YUD.addClass(el, that.CLASSES.HIDDEN);
                        YUD.setStyle(el, 'height', 'auto');
                        YUD.removeClass(elClicked, that.CLASSES.ACTIVE);
                        fireEvent(afterPanelCloseEvent, el);
                    }
                    else {
                        if (fireEvent(panelOpenEvent, el) === false) { return; }
                        YUD.removeClass(el, that.CLASSES.HIDDEN);
                        YUD.setStyle(el, 'height', 'auto');
                        YUD.addClass(elClicked, that.CLASSES.ACTIVE);
                        fireEvent(afterPanelOpenEvent, el);
                    }
                }
                return true;
            }
            eTargetListNode = (elClickedNode.nodeName.toUpperCase() === 'SPAN') ? elClickedNode.parentNode.parentNode : elClickedNode.parentNode;

            containedPanel = eTargetListNode.childNodes[1];
            if (this.fireEvent(beforeStateChangeEvent, this) === false) { return; }
            if(this.get('collapsible') === false) {
                if (!YUD.hasClass(containedPanel, this.CLASSES.HIDDEN)) {
                    return false;
                }
            }
            else {
                if(!YUD.hasClass(containedPanel, this.CLASSES.HIDDEN)) {
                    toggleItem.call(this, containedPanel);
                    return false;               
                }
            }
                    
            if(this.get('expandable') !== true) {
                nLength = this._panels.length;
                for(i=0; i<nLength; i++) {
                    bMustToggle = YUD.hasClass(this._panels[i].firstChild.nextSibling, this.CLASSES.HIDDEN);
                    if(!bMustToggle) {
                        toggleItem.call(this,this._panels[i].firstChild.nextSibling);
                    }
                }
            }
            
            if(elClickedNode.nodeName.toUpperCase() === 'SPAN')  {
                toggleItem.call(this, containedPanel, elClickedNode.parentNode);
            }
            else {
                toggleItem.call(this, containedPanel, elClickedNode);
            }
            return true;
        },
       
        /**
        * Provides a readable name for the AccordionView instance.
        * @method toString
        * @return {String} String representation of the object 
        */
        
        toString : function() {
            var name = this.get('id') || this.get('tagName');
            return "AccordionView " + name; 
        }
    });
    LANE.namespace('expandy');
    LANE.expandy.AccordionView = AccordionView;
    YUI().use('event-custom', function(Y) {
        Y.publish('lane:accordionready', {broadcast: 2});
        Y.fire('lane:accordionready');
    });
});
