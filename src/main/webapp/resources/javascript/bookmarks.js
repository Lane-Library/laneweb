
//TODO: remove logging before deployment.
(function() {
    
    var L = Y.Lang,

    /**
     * A class for representing a bookmark with attributes for the label, url and proxy url.
     * 
     * @class Bookmark
     * @constructor
     */
    Bookmark = function(config) {
        if (!L.isObject(config) || !(L.isString(config.label) && L.isString(config.url))) {
            Y.log('bad config', 'error', 'bookmark');
            throw("bad config");
        }
        this.publish("valueChange", {defaultFn : this._valueChange});
        this._value = {label:config.label,url:config.url};
        Y.log("created new " + this);
    };

    Bookmark.prototype = {
            _valueChange : function(event) {
                this._value = event.newVal;
            },
            getLabel : function() {
                return this._value.label;
            },

            getUrl : function() {
                return this._value.url;
            },

            /**
             * Set both the label and url then fire a changed event
             * @param labelAndUrl
             */
            setValue : function(newVal) {
                var changed = false;
                if (L.isObject(newVal)) {
                    if (L.isString(newVal.label) && newVal.label != this._label) {
                        changed = true;
                    } else if (L.isString(newVal.url) && newVal.url != this._url) {
                        changed = true;
                    }
                }
                if (changed) {
                    Y.log(this + " firing valueChange: newVal:{label:" + newVal.label + ",url:" + newVal.url+ "}");
                    this.fire("valueChange", {prevVal : this._value, newVal : newVal});
                }
            },
            
            toString : function() {
                return "Bookmark{label:" + this._value.label + ",url:" + this._value.url + "}";
            }
    };


    Y.augment(Bookmark, Y.EventTarget, null, null, {
        emitFacade : true,
        prefix     : 'bookmark',
        //TODO: figure out why I can prevent in the test despite this being false:
        preventable: false
    });

    Y.lane.Bookmark = Bookmark;

})();


(function() {

    var L = Y.Lang,
        
    /**
     * A class for representing an ordered collection of Bookmarks.
     * It fires events when the collection changes
     * 
     * @class Bookmarks
     * @constructor
     * @extends Base
     */
    Bookmarks = function(bookmarks) {
        this._bookmarks = [];
        if (bookmarks && !L.isArray(bookmarks)) {
            Y.log('bad config', 'error', 'bookmarks');
            throw("bad config");
        }
        if (bookmarks) {
            for (var i = 0; i < bookmarks.length; i++) {
                bookmarks[i].after("valueChange", this._handleBookmarkChanged, this);
                this._bookmarks.push(bookmarks[i]);
            }
        }
        
        /**
         * @event add
         * @description Fired when a bookmark is added.
         * @preventable _devAddFn
         */
        this.publish("add", {defaultFn: this._defAddFn});
        this.publish("addSync", {defaultFn: this._successfulAdd, preventable : false});
        
        /**
         * @event remove
         * @description Fired when a bookmark is removed.
         * @preventable _devRemoveFn
         */
        this.publish("remove", {defaultFn: this._defRemoveFn});
        this.publish("removeSync", {defaultFn: this._successfulRemove, preventable : false});
        
        /**
         * @event update
         * @description Fired when a bookmark is updated.
         */            
        this.publish("update", {defaultFn: this._defUpdateFn});
        this.publish("updateSync", {defaultFn : this._successfulUpdate, preventable : false});
        Y.log("created new " + this);
    };
    
    Bookmarks.prototype = {
        
        /**
         * 
         * @param bookmark {Bookmark}
         */
        addBookmark : function(bookmark) {
        	if (!Y.Lang.isObject(bookmark)) {
        		throw ("bad bookmark");
        	}
        	Y.log(this + " firing add: " + bookmark);
            this.fire("add", {bookmark : bookmark});
        },
        getBookmark : function(position) {
        	return this._bookmarks[position];
        },
        removeBookmark : function(position) {
            Y.log(this + " firing remove: " + position);
            this.fire("remove", {position : position});
        },
        updateBookmark : function(bookmark) {
            var position = Y.Array.indexOf(this._bookmarks, bookmark);
            Y.log(this + " firing update: position: " + position + " " + bookmark);
            this.fire("update", {bookmark : bookmark, position : position});
        },
        size : function() {
            return this._bookmarks.length;
        },
        setValue : function(position, bookmark) {
            this._bookmarks[position].setValue(bookmark);
        },
        toString : function() {
            var string = "Bookmarks[";
            for (var i = 0; i < this._bookmarks.length; i++) {
                string += this._bookmarks[i];
                if (i < this._bookmarks.length - 1) {
                    string += ",";
                }
            }
            string += "]";
            return string;
        },
        _getBookmarks : function() {
            return this._bookmarks;
        },
        _defAddFn : function(event) {
            var data = Y.JSON.stringify({label : event.bookmark.getLabel(), url : event.bookmark.getUrl()});
            Y.io("/././bookmarks", {
                method : "post",
                data : data,
                headers : {
                    "Content-Type" : "application/json"
                },
                on : {
                    success : function() {
                        Y.log(this + " firing addSync: " + event.bookmark);
                        this.fire("addSync", {bookmark : event.bookmark});
                    },
                    failure : this._failed
                },
                "arguments" : {
                    bookmark : event.bookmark
                },
                context : this
            });
        },
        _defRemoveFn : function(event) {
            var data = Y.JSON.stringify(event.position);
            Y.io("/././bookmarks", {
                method : "delete",
                data : data,
                headers : {
                    "Content-Type" : "application/json"
                },
                on : {
                    success : function() {
                        Y.log(this + " firing removeSync: " + event.position);
                        this.fire("removeSync", {position : event.position});
                    },
                    failure : this._failed
                },
                "arguments" : {
                    position : event.position
                },
                context : this
            });
            this._bookmarks.splice(event.position, 1);
        },
        _defUpdateFn : function(event) {
//            var position = Y.Array.indexOf(this._bookmarks, event.bookmark);
            var data = Y.JSON.stringify({position : event.position, label : event.bookmark.getLabel(), url : event.bookmark.getUrl()});
            Y.io("/././bookmarks", {
                method : "put",
                data : data,
                headers : {
                    "Content-Type" : "application/json"
                },
                on : {
                    success :  function() {
                        Y.log(this + " firing updateSync: " + event.position);
                        this.fire("updateSync", {position : event.position});
                    },
                    failure : this._failed
                },
                "arguments" : {
                    position : event.position
                },
                context : this
            });
            
        },
        _handleBookmarkChanged : function(event) {
            this.updateBookmark(event.target);
        },
        _successfulRemove : function(event) {
            this._bookmarks.splice(event.position, 1);
        },
        _successfulAdd : function(event) {
            event.bookmark.after("valueChange", this._handleBookmarkChanged, this);
            this._bookmarks.unshift(event.bookmark);
        },
        _successfulUpdate : function(event) {
            var bookmark = this._bookmarks[event.position];
//            bookmark.set("label", args.label);
//            bookmark.set("url", args.url);
//            this.fire("update", args.position, bookmark);
        }
    };


    Y.augment(Bookmarks, Y.EventTarget, null, null, {
        emitFacade : true,
        prefix     : 'bookmarks',
        preventable: false
    });

    Y.lane.Bookmarks = Bookmarks;

})();


(function() {
    
    var widget,
    
     BookmarksWidget = function() {
        BookmarksWidget.superclass.constructor.apply(this, arguments);
        Y.log("created new " + this);
    };
    
    BookmarksWidget.NAME = "bookmarks";
    
    BookmarksWidget.ATTRS = {
        items : {},
        bookmarks : {}
    };
    
    BookmarksWidget.HTML_PARSER = {
            items : ["li"],
            
            bookmarks : function(srcNode) {
                var i, anchor, label, url, bookmarks = [], anchors = srcNode.all("a");
                for (i = 0; i < anchors.size(); i++) {
                    anchor = anchors.item(i);
                    anchor.plug(Y.lane.LinkPlugin);
                    label = anchor.link.get("title");
                    if (anchor.link.get("local")) {
                        url = anchor.link.get("path");
                    } else {
                        url = anchor.link.get("url");
                    }
                    bookmarks.push(new Y.lane.Bookmark({label:label, url:url}));
                }
                return new Y.lane.Bookmarks(bookmarks);
            }
    };
    
    Y.extend(BookmarksWidget, Y.Widget, {
        bindUI : function() {
            var bookmarks = this.get("bookmarks");
            bookmarks.after("addSync", this._bookmarkAdded, this);
            bookmarks.after("removeSync", this._bookmarkRemoved, this);
            bookmarks.after("updateSync", this._bookmarkUpdated, this);
        },
        toString : function() {
            return "BookmarksWidget:" + this.get("bookmarks");
        },
        _bookmarkAdded : function(event) {
            this.get("srcNode").prepend("<li><a href='" + event.bookmark.getUrl() + "'>" + event.bookmark.getLabel() + "</a></li>");
        },
        _bookmarkRemoved : function(event) {
            this.get("srcNode").all("li").item(event.position).remove(true);
        },
        _bookmarkUpdated : function(event) {
            var bookmark = this.get("bookmarks").getBookmark(event.position),
                anchor = this.get("srcNode").all("li").item(event.position).one("a");
            anchor.set("innerHTML", bookmark.getLabel());
            anchor.set("href", bookmark.getUrl());
        }
    });
    
    Y.lane.BookmarksWidget = BookmarksWidget;
    
})();

(function() {
    
    var BookmarkLink = function() {
        BookmarkLink.superclass.constructor.apply(this, arguments);
    };
    
    BookmarkLink.NAME = "bookmark-link";
    
    BookmarkLink.ATTRS = {
        node : {
            valueFn : function() {
                return Y.Node.create("<a style='border:1px black solid;padding:3px;visibility:hidden;background-color:white'>bookmark this</a>");
            }
        },
        timeoutid : {
            value : null
        },
        inBookmark : {
            value : false
        },
        inTarget : {
            value : false
        },
        target : {
            value : null
        }
    };
    
    Y.extend(BookmarkLink, Y.Base, {
        initializer : function() {
            var node = this.get("node");
            Y.one("body").append(node);
            Y.delegate("mouseover", this._handleTargetMouseover,".content", "a", this);
            Y.delegate("mouseout", this._handleTargetMouseout,".content", "a", this);
            node.on("mouseover",this._handleBookmarkMouseover, this);
            node.on("mouseout", this._handleBookmarkMouseout, this);
            node.on("click", this._handleClick, this);
            this.on("inTargetChange", this._handleInTargetChange);
            this.on("inBookmarkChange", this._handleInBookmarkChange);
        },
        toggleVisibility : function(show) {
            var visibility = show ? "visible" : "hidden";
            this.get("node").setStyle("visibility", visibility);
        },
        startTimer : function() {
        },
        clearTimer : function() {
        },
        setNodeXY : function(xy) {
            this.get("node").setXY(xy);
        },
        _handleBookmarkMouseout : function(event) {
            this.set("inBookmark", false);
        },
        _handleBookmarkMouseover : function(event) {
            this.set("inBookmark", true);
        },
        _handleClick : function() {
    	    var target = this.get("target"), label, url;
            target.plug(Y.lane.LinkPlugin);
            label = target.link.get("title");
            if (target.link.get("local")) {
                url = target.link.get("path");
            } else {
                url = target.link.get("url");
            }
            this.get("bookmarks").add(new Y.lane.Bookmark({label:label, url:url}));
        },
        _handleInBookmarkChange : function(event) {
            this.toggleVisibility(event.newVal);
        },
        _handleInTargetChange : function(event) {
            this.toggleVisibility(event.newVal);
        },
        _handleTargetMouseout : function(event) {
            this.set("inTarget", false);
        },
        _handleTargetMouseover : function(event) {
            this.set("target", event.target);
            this.setNodeXY([event.pageX + 1,event.pageY + 1]);
            this.set("inTarget", true);
        }
    });
    
    Y.lane.BookmarkLink = BookmarkLink;
})();

(function() {
    
    var BookmarkEditor = Y.Base.create("bookmark-editor", Y.Widget, [], {
        syncUI : function(config) {
            this.on("editingChange", this._handleEditingChange, this);
        },
        renderUI : function() {
            this.get("srcNode").append(
                "<label style=\"display:none\">URL:</label>" +
                "<input style=\"display:none\" type=\"text\" name=\"url\"/>" +
                "<label style=\"display:none\">Label:</label>" +
                "<input style=\"display:none\" type=\"text\" name=\"label\"/>" +
                "<button style=\"display:none\" name=\"action\" value=\"save\" type=\"submit\">save</button>" +
                "<button style=\"display:none\" value=\"reset\" type=\"reset\">reset</button>" +
                "<button style=\"display:none\" name=\"action\" value=\"cancel\" type=\"submit\">cancel</button>");
        },
        bindUI : function() {
            this.get("srcNode").all("button").on("click", this._handleButtonClick, this);
        },
        cancel : function() {
            this.set("editing", false);
        },
        save : function() {
            var srcNode = this.get("srcNode"),
                newlabel = srcNode.one("input[name='label']").get("value"),
                newurl = srcNode.one("input[name='url']").get("value"),
                bookmark = this.get("bookmark"),
                oldlabel = bookmark.getLabel(),
                oldurl = bookmark.getUrl();
            if (newlabel != oldlabel || newurl != oldurl) {
                bookmark.setValue({label : newlabel, url : newurl});
            }
            this.set("editing", false);
        },
        reset : function() {
            var srcNode = this.get("srcNode"),
                bookmark = this.get("bookmark");
            srcNode.one("input[name='url']").set("value", bookmark.getUrl());
            srcNode.one("input[name='label']").set("value", bookmark.getLabel());
        },
        update : function() {
            var anchor = this.get("srcNode").one("a"),
                bookmark = this.get("bookmark");
            anchor.set("innerHTML", bookmark.getLabel());
            anchor.set("href", bookmark.getUrl());
        },
        _handleButtonClick : function(event) {
            event.preventDefault();
            this[event.target.getAttribute("value")].call(this, event);
        },
        _handleEditingChange : function(event) {
            var srcNode = this.get("srcNode");
            if (event.newVal) {
                srcNode.all("input[type='text'], button, label").setStyle("display", "inline");
                srcNode.one("a").setStyle("display", "none");
                this.reset();
                
            } else {
                srcNode.all("input[type='text'], button, label").setStyle("display", "none");
                srcNode.one("a").setStyle("display", "inline");
            }
        }
    }, {
        ATTRS : {
            bookmark : {
                value : null
            },
            editing : {
                value : false
            }
        }
    }),
    
    BookmarksEditor = Y.Base.create("bookmarks-editor", Y.Widget, [], {
        renderUI : function() {
            var editors = [], i, items = this.get("srcNode").all("li"), bookmarks = this.get("bookmarks");
            for (i = 0; i < items.size(); i++) {
                editors.push(new Y.lane.BookmarkEditor({srcNode : items.item(i), render : true, bookmark : bookmarks.getBookmark(i)}));
            }
            this.set("editors", editors);
            bookmarks.after("removeSync", this._handleBookmarkRemove, this);
            bookmarks.after("addSync", this._handleBookmarkAdd, this);
            bookmarks.after("updateSync", this._handleBookmarkUpdate, this);
        },
        bindUI : function() {
            this.get("srcNode").all("fieldset button").on("click", this._handleButtonClick, this);
        },
        add : function() {
            this.get("bookmarks").addBookmark(new Y.lane.Bookmark({label : "", url : "", href : ""}));
        },
        "delete" : function() {
            var checked = this._getCheckedIndexes();
            if (checked.length > 0) {
                this.get("bookmarks").removeBookmark(checked[0]);
            }
        },
        edit : function() {
            var i, checked = this._getCheckedIndexes(), editors = this.get("editors");
            for (i = 0; i < checked.length; i++) {
                editors[checked[i]].set("editing", true);
            }
        },
        _handleButtonClick : function(event) {
            event.preventDefault();
            var fn = this[event.target.getAttribute("value")];
            if (fn) {
                fn.call(this);
            }
        },
        _handleBookmarkRemove : function(event) {
            var editors = this.get("editors"), editor;
            editor = editors.splice(event.position, 1)[0];
            editor.destroy(true);
        },
        _handleBookmarkAdd : function(event) {
            var items = this.get("srcNode").one("ul");
            var item = Y.Node.create("<li><input type=\"checkbox\" checked=\"checked\"/><a></a></li>");
            items.prepend(item);
            var editor = new Y.lane.BookmarkEditor({srcNode : item, render : true, bookmark : event.bookmark});
            this.get("editors").unshift(editor);
            editor.set("editing", true);
        },
        _handleBookmarkUpdate : function(event) {
            var editors = this.get("editors");
            editors[event.position].update();
        },
        _getCheckedIndexes : function() {
            var indexes = [], i,
            checkboxes = this.get("srcNode").all("input[type='checkbox']");
            for (i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.item(i).get("checked")) {
                    indexes.push(i);
                }
            }
            return indexes;
        }
    },
    {
        ATTRS : {
            editors : {
                value : null
            },
            bookmarks : {
                value : null
            }
        }
    });
    
    Y.lane.BookmarkEditor = BookmarkEditor;
    Y.lane.BookmarksEditor = BookmarksEditor;
    
})();


(function() {
if (Y.one("#bookmarks")) {
	var widget, editor, link;
    widget = new Y.lane.BookmarksWidget({srcNode:Y.one("#bookmarks")});
    widget.render();
    if (Y.one("#bookmarks-editor")) {
        editor = new Y.lane.BookmarksEditor({srcNode:Y.one("#bookmarks-editor"), bookmarks : widget.get("bookmarks")});
        editor.render();
    };
    link = new Y.lane.BookmarkLink();
};

})();
