YUI.add('lane-ac-plugin', function(Y) {
/**
 * copy of http://yui.yahooapis.com/combo?gallery-2010.03.30-17-26/build/gallery-ac-plugin/gallery-ac-plugin.js
 * lane modifications: query delay, hiding on zero results
 */

/**
 * ACPlugin - A plugin that exposes the proper events to make AutoComplete work
 * on a form element (input or textarea, typically).
 * 
 * This utility is not intended to be used in isolation, but rather as a glue
 * layer to work with ACWidget or some other display mechanism.
 **/
function ACPlugin () { ACPlugin.superclass.constructor.apply(this, arguments) };


// shorthands
var autocomplete = "autocomplete",
    YLang = Y.Lang,
    YArrayeach = Y.Array.each,
    eventDefaultBehavior = {
        // the default behavior of the query event is to check for a datasource,
        // and then make a request with it.
        query : function (e) {
            var self = this,
                ds = self.get("dataSource"),
                query = e.value,
                handler = Y.bind(handleQueryResponse, self);
            // if we have a datasource, then make the request.
            if (ds) ds.sendRequest({
                request : self.get("queryTemplate")(query),
                callback : {
                    success : handler,
                    failure : handler
                }
            });
        }
    };

Y.Plugin.ACPlugin = Y.extend(
    ACPlugin,
    Y.Plugin.Base,
    { // prototype
        initializer : function () {
            var self = this,
                host = self.get("host");
            attachHandles(self, host);

            // publish events:
            // "query" for when value changes.
            // "load" for when data returns.
            // "show" for when it's time to show something
            // "hide" for when it's time to hide
            var defaults = eventDefaultBehavior;
            YArrayeach([
                "query",
                "load",
                "show",
                "hide",
                "next",
                "previous"
            ], function (ev) { self.publish("ac:"+ev, {
                broadcast : 1,
                bubbles : 1,
                context : self,
                preventable : true,
                defaultFn : defaults[ev] || null,
                prefix : "ac"
            }) }, self);

            // manage the browser's autocomplete, since that'll interefere,
            // but we need to make sure that we don't prevent pre-filling 
            // when the user navs back to the page, unless the developer has
            // specifically disabled that feature in the markup.
            manageBrowserAC(host);
        },
        destructor : function () {
            Y.detach(Y.stamp(this)+"|");
        },
        open : function () { this.fire("ac:show") },
        next : function (e) { e.preventDefault(); this.fire("ac:next") },
        previous : function (e) { e.preventDefault(); this.fire("ac:previous") },
        close : function () { this.fire("ac:hide") }
    },
    { // statics
        NAME : "ACPlugin",
        NS : "ac",
        ATTRS : {
            /**
             * The value that will be queried.
             * 
             * By default, this is just a proxy to the host's value attr, which in
             * Node objects passes through to the underlying DOM node.
             * 
             * However, in some use cases, it may be useful to override the queryValue
             * getters and setters, for example, in the delimited case.
             *
             * Setting caches the value so that we only make new requests for user-entered
             * data, and not for programmatically-set values.  (For example, when a user
             * is scrolling through the items displayed in an  ACWidget.)
             * 
             * @for ACPlugin
             * @type {String}
             * @public
             **/
            queryValue : {
                // override these in AC plugin children as necessary.
                // for instance, the delimited getter could get the cursor location,
                // split on the delimiter, and then return the selected one.
                // the inline-replacing setter could set-and-select the rest of the word.
                getter : function () {
                    return this.get("host").get("value");
                },
                setter : function (q) {
                    this.get("host").set("value", q);
                    // keep track of what it has been explicitly set to, so that we don't
                    // try to make a query repeatedly when the user hasn't done anything.
                    return (this._cachedValue = q);
                }
            },

            /**
             * A data source object to be used to make queries and such.
             * It is not required that it be a DataSource object per se, but it
             * must provide a "sendRequest" function that takes the same sort of
             * argument as the DataSource classes.
             * 
             * It is not required to use this, as the implementor can listen to
             * ac:query events and handle them in any ad-hoc way desired.  However,
             * for the 99% use case, it's simpler to just provide a data source
             * and do things in the normal way.
             * 
             * @for ACPlugin
             * @type Object
             **/
            dataSource : {
                validator : function (ds) {
                    // quack.
                    return ds && YLang.isFunction(ds.sendRequest);
                }
            },

            /**
             * The minimum number of characters required before kicking off a query.
             * @for ACPlugin
             * @public
             * @type Number
             * @default 3
             **/
            minQueryLength : {
                value : 3,
                validator : YLang.isNumber
            },
            
        	/**
        	 * The amount of time in seconds to delay before submitting the query.
        	 *
        	 * @attribute queryDelay
        	 * @default 0.2
        	 * @type Number
        	 */
        	queryDelay: {
        		value: 0.2,
        		getter: function(value) {
        			return value * 1000;
        		}
        	},
        	_delayId : {
        		value: -1
        	},
        	
            /**
             * Attribute used to convert a value into a request for the
             * DataSource.  Can be a string containing "{query}" somewhere,
             * or a function that takes a value and returns a string.
             *
             * @for ACPlugin
             * @type {Function|String}
             * @default encodeURIComponent
             * @public
             **/
            queryTemplate : {
                value : encodeURIComponent,
                setter : function (q) {
                    return (
                        YLang.isFunction(q) ? q
                        : function (query) {
                            // exchange {query} with the query,
                            // but turn \{query} into {query}, if for some reason that
                            // string needs to appear in the URL.
                            return q
                                .replace(
                                    /(^|[^\\])((\\{2})*)\{query\}/,
                                    '$1$2'+encodeURIComponent(query)
                                ).replace(
                                    /(^|[^\\])((\\{2})*)\\(\{query\})/,
                                    '$1$2$4'
                                );
                        }
                    );
                }
            }
        	
        } // end attrs
    } // end statics
);

// helpers below

/**
 * Attach the required event handles to the host node.
 * 
 * @param self {Object} The ACPlugin instance
 * @param host {Object} The host object
 * @return {Array} A list of handles
 * @private
 **/
function attachHandles (self, host) {
    var category = Y.stamp(this)+"|";
    Y.on(category+"valueChange", valueChangeHandler, host, self);
    // next/open on down
    Y.on(category+"key", self.next, host, "down:40", self);
    // previous on up
    Y.on(category+"key", self.previous, host, "down:38", self);
    // close on escape
    Y.on(category+"key", self.close, host, "down:27", self);
};

/**
 * The handler that listens to valueChange events and decides whether or not
 * to kick off a new query.
 *
 * @param {Object} The event object
 * @private
 **/
function valueChangeHandler (e) {
    var value = e.value, instance = this;
    if (!value) return this.close();
    if (value === this._cachedValue || value.length < this.get("minQueryLength")) return;

    if (this._delayId != -1) {
		clearTimeout(this._delayId);
	}
    instance._delayId = setTimeout(
		function() {
			instance._cachedValue = value;
			instance.fire( "ac:query", { value : e.value });
		},
		instance.get('queryDelay')
	);
};


/**
 * A factory method that returns a function to re-enable the browser's builtin
 * AutoComplete, so that form values will be tracked.
 *
 * @private
 * @param domnode {HTMLElement} The dom node to re-enable on unload
 * @return {Function} A function that will re-enable the browser autocomplete
 **/
function browserACFixer (domnode) { return function () {
    if (domnode) domnode.setAttribute(autocomplete, "on");
    domnode = null;
}};

/**
 * Manage the browser's builtin AutoComplete behavior, so that form values
 * will be tracked in browsers that do that.
 * 
 * First, disable the browser's autocomplete, since that'll cause issues.
 * If the element is not set up to disable the browser's builtin autocomplete,
 * then set an unload listener to re-enable it.
 * 
 * @private
 * @param host {Object} The node to manage
 * @see {browserACFixer}
 **/
function manageBrowserAC (host) {
    // turn off the browser's autocomplete, but take note of it to turn
    // it back on later.
    var domnode = Y.Node.getDOMNode(host),
        bac = domnode.getAttribute(autocomplete);

    // turn the autocomplete back on so back button works, but only
    // if the user hasn't disabled it in the first place.
    if ((bac && bac !== "off") || bac === null || bac === undefined) {
        var bacf = browserACFixer(domnode);
        // hook onto both.  Concession to browser craziness.
        Y.on("beforeunload", bacf, window);
        Y.on("unload", bacf, window);
    }

    // turn off the browser's autocomplete feature, since that'll interfere.
    domnode.setAttribute(autocomplete, "off");
};

/**
 * Handle the responses from the DataSource utility, firing ac:load if there
 * are results.
 *
 * @private
 **/
function handleQueryResponse (e) {
    var res = (e && e.response && e.response.results) ? e.response.results : e;
    
    // if there is a result, and it's not an empty array
    if (res && !(res && ("length" in res) && res.length === 0)){
    	this.fire("ac:load", {
	        results : res,
	        query : this.get("queryValue")
	    });
    }
    else{
    	this.fire("ac:hide");
    }
};


}, '1.11.0-SNAPSHOT' ,{requires:['node', 'plugin', 'gallery-value-change', 'event-key'], optional:['event-custom']});


YUI().add('lane-suggest', function (Y) {
    
    Y.namespace('lane');
    
    Y.lane.Suggest = function (input, limit, id) {
        var self = this, acWidget, i,
            baseUrl = '/././apps/suggest/json?',
            acDS  = new Y.DataSource.IO({source:baseUrl}),
            setDSLimit = function(input){
                var sourceElm = input.ancestor("form").one('#searchSource'), searchSource, minQueryLength = 3, limit = null;
                searchSource = (sourceElm) ? sourceElm.get('value') : null;
                if (searchSource && searchSource.match(/^(all|articles|catalog)/)) {
                    limit = baseUrl + "l=er-mesh&";
                } else if (searchSource && searchSource.match(/^bioresearch/)) {
                    limit = baseUrl + "l=mesh&";
                } else if (searchSource && searchSource.match(/^history/)) {
                    limit = baseUrl + "l=history&";
                } else if (searchSource && searchSource.match(/^bassett/)) {
                	limit = baseUrl + "l=bassett&";
                } else if (null == searchSource) { // assume source-less is metasearch form on peds portal, etc.
                    limit = baseUrl + "l=mesh-di&";
                } else {
                    minQueryLength = 999;
                }
                input.ac.set("minQueryLength", minQueryLength);
                if(null != limit){
                    input.ac.get("dataSource").set("source",limit);
                }
            };
        	this.publish("lane:suggestSelect",{
        		broadcast:2,
        		suggestion:null,
        		parentForm:null
    		});
            acDS.plug({fn : Y.Plugin.DataSourceJSONSchema, cfg : {
                schema : { resultListLocator : "suggest" }
            }});
            input.plug(Y.Plugin.ACPlugin, {
                queryTemplate : function (q) { return "q=" + encodeURIComponent(
                    q.replace(/(["\\])/g, '\\$1')
                );},
                dataSource : acDS
            });
            
        acWidget = (function () {
            var data = [],
                contNode,
                selected = -1;
            //some ac elements need id for positioning:
            if (id) {
                contNode = Y.Node.create('<ul id=' + id + ' class="aclist"> </ul>');
            } else {
                contNode = Y.Node.create('<ul class="aclist"> </ul>')
            }
            
            input.insert(contNode,"after");
            
            contNode.delegate("click", function (e) {
                var target = e.currentTarget, index = target.get("className").split('-')[1];
                acWidget.focus(index).select(e);
            }, "li");
            contNode.delegate("mouseover", function (e) {
                var target = e.currentTarget, index = target.get("className").split('-')[1];
                acWidget.focus(index);
            }, "li");
            Y.on("click", function () { acWidget.hide(); }, document);
            
            return {
                setData : function (d) {
                    if (d.length) {
                        data = d;
                        selected = -1;
                    }
                    return this;
                },
                render : function () {
                    if (!data.length){
                        return this;
                    }
                    this.visible = true;
                    contNode.set("innerHTML", "");
                    for (var i = 0, l = data.length; i < l; i ++) {
                        contNode.appendChild(Y.Node.create('<li class="ac-'+i+'">'+data[i]+"</li>"));
                    }
                    contNode.setStyle("display", "block");
                    return this;
                },
                hide : function () {
                    contNode.setStyle("display", "none");
                    this.visible = false;
                    selected = -1;
                    return this;
                },
                focus : function (i) {
                    i = i || selected;
                    if (!data.length) {
                        return this;
                    }
                    if (i >= data.length) {
                        i = 0;
                    }
                    if (i < 0) {
                        i = data.length - 1;
                    }
                    var list = contNode.all("li"),
                        current = contNode.one(".selected"),
                        intent = list.item(i);
                    if (!intent || intent === current) {
                        return this;
                    }
                    selected = i;
                    if (current) {
                        current.removeClass("selected");
                    }
                    intent.addClass("selected");
                    return this;
                },
                next : function () {
                    selected = selected || 0;
                    selected ++;
                    if (!this.visible) {
                        this.render();
                    }
                    return this.focus(selected);
                },
                previous : function () {
                    selected = selected || data.length;
                    selected --;
                    return this.focus(selected);
                },
                getValue : function () {
                    if (!data.length || !data[selected]) {
                        return "";
                    }
                    return data[selected];
                },
                select : function (e) {
                    if(acWidget.getValue()){
                        input.ac.set("queryValue", acWidget.getValue());
                    }
                    else{
                        input.ac.set("queryValue", input.get("value"));
                    }
                    acWidget.hide();
                    input.focus();
                    if (e) {
                        e.preventDefault();
                    }
                    self.fire("lane:suggestSelect",{
                    	suggestion:input.ac.get("queryValue"),
                    	parentForm:Y.Node.getDOMNode(input.ancestor("form"))
                    });
                },
                setWidth : function (w) {
                    if (w) {
                        contNode.setStyle("width",w);
                    }
                }
            };
        })();
        
        input.on("focus", function (e) {
            if (!limit) {
                setDSLimit(input);
            } else {
                input.ac.set("minQueryLength", 3);
                input.ac.get("dataSource").set("source", baseUrl + limit);
            }
        });
        input.ac.on("ac:load", function (e) {
            acWidget.setWidth(input.getStyle("width"));
            acWidget.setData(e.results).render();
        });
        input.ac.on("ac:query", function (e) {
            if (acWidget.visible && e.value === acWidget.value) {
                e.halt();
            }
            acWidget.value = e.value;
        });
        input.ac.on("ac:show", function () {
            if (!acWidget.visible) {
                acWidget.render();
            }
        }, acWidget);
        input.ac.on("ac:hide", acWidget.hide, acWidget);
        input.ac.on("ac:next", acWidget.next, acWidget);
        input.ac.on("ac:previous", acWidget.previous, acWidget);
        Y.on("key", acWidget.select, input, "down:13,10");
    };
    Y.augment(Y.lane.Suggest,Y.EventTarget);
}, '1.11.0-SNAPSHOT', {requires:['lane', 'lane-ac-plugin', 'plugin', 'node-base', 'datasource','event-custom']});