(function() {

    //only do this if this is a bassett page
    if (Y.one("#accordion") && Y.one("#bassettContent")) {
        
        /**
         * the Bassett object, meant to function as a singleton
         * 
         * @constructor
         * @base Y.Base
         */
        var Bassett = function() {
            Bassett.superclass.constructor.apply(this, arguments);
        };
        
        /**
         * the class name
         * @final
         */
        Bassett.NAME = "bassett";
        
        /**
         * the default attributes
         */
        Bassett.ATTRS = {
                content : {
                    valueFn : function() {
                        return Y.one('#bassettContent');
                    }
                },
                accordion : {
                    valueFn : function() {
                        return Y.one('#accordion');
                    }
                },
                diagramDisplay : {
                    value : false
                },
                history : {
                    valueFn : function() {
                        return new Y.History();
                    }
                },
                io : {
                    value : Y.io
                }
        };
        
        /**
         * extend Base
         */
        Y.extend(Bassett, Y.Base, {
            
            /**
             * delegates initialization to private functions.
             */
            initializer : function() {
                this._initializeHistory();
                this._registerLinksContainer(this.get("content"));
                this._registerLinksContainer(this.get("accordion"));
            },
            
            /**
             * sets up the yui history
             */
            _initializeHistory : function() {
                var history = this.get("history"),
                    bassettHistory = history.get("bassett");
                if (bassettHistory) {
                    this._loadContent(bassettHistory);
                }
                history.on("bassettChange", function(e) {
                    this._loadContent(e.newVal);
                }, this);
                history.on("bassettRemove", function(e) {
                    this._loadContent(this._formatAjaxUrl(window.location.toString()));
                }, this);
            },
            
            /**
             * registers click handlers for the anchors in the container
             * 
             * @param container
             */
            _registerLinksContainer : function(container) {
                var anchors, anchor, i, size, rel;
                if (container) {
                    anchors = container.all('a');
                    size = anchors.size();
                    for (i = 0; i < size; i++) {
                        anchor = anchors.item(i);
                        rel = anchor.get("rel");
                        if (!rel || rel === "propagation") {
                            anchor.on('click', this._handleClickEvent, this);
                        }
                    }
                }
            },
            
            /**
             * figures out the url for the ajax call
             * 
             * @param href an anchors href attribute
             * @returns the url
             */
            _formatAjaxUrl : function(href) {
                var url;
                href = href.replace("search.html", "/biomed-resources/bassett/bassettsView.html");
                href = href.substr(href.indexOf("/bassett/") + 8);
                href = href.split("?");
                if (href.length == 1) {
                    url = href[0];
                }
                if (href.length > 1) {
                    url = href[0] + "?" + href[1];
                }
                if (url !== "/index.html" && this.get("diagramDisplay")) {
                    url = url + "&t=diagram";
                }
                return url;
            },
            
            /**
             * event handler for clicks that need an ajax call
             * @param event
             */
            _handleClickEvent : function(event) {
                var url;
                event.preventDefault();
                if (event.currentTarget.get('id') == "diagram-choice") {
                    this.set("diagramDisplay", true);
                }
                if (event.currentTarget.get('id') == "photo-choice") {
                    this.set("diagramDisplay", false);
                }
                url = this._formatAjaxUrl(event.currentTarget.get('href'));
                try {
                    this.get("history").addValue("bassett", url);
                } catch (e) {
                    this._loadContent(url);
                }

            },
            
            /**
             * makes the ajax call for more content
             * @param url
             */
            _loadContent : function(url) {
                url = "/././plain/biomed-resources/bassett/raw".concat(url);
                this.get("io").call(this, url, {
                    on : {
                        success : this._successHandler
//                        failure : this._failureHandler
                    },
                    arguments : {
                        bassett : this
                    }
                });

            },
            
            /**
             * handles the successful ajax
             * @param id
             * @param o
             * @param args
             */
            _successHandler : function(id, o, args) {
                var content = new Y.Node(o.responseText),
                container = args.bassett.get("content");
                container.setContent(content);
                args.bassett._registerLinksContainer(container);
            }
        });
        
        //instantiate
        Y.lane.Bassett = new Bassett();
    }
})();
