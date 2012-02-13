
//TODO: remove logging before deployment.
(function() {

    var Bookmark, Bookmarks, BookmarksWidget, BookmarkLink, BookmarkEditor, BookmarksEditor;

    if (Y.one("#bookmarks")) {

        /**
         * A class for representing a bookmark with attributes for the label and url.
         * 
         * @class Bookmark
         * @constructor
         * @param label {string} the label
         * @param url {string} the url
         */
        Bookmark = function(label, url) {
            this.publish("valueChange", {defaultFn : this._valueChange});
            this.setValues(label, url);
            Y.log("created new " + this);
        };

        Bookmark.prototype = {
                
                /**
                 * The default changeEvent handler
                 * @method _valueChange
                 * @private
                 * @param event {CustomEvent} the valueChange event
                 */
                _valueChange : function(event) {
                    this._label = event.newLabel;
                    this._url = event.newUrl;
                },
                
                /**
                 * getter for the label
                 * @method getLabel
                 * @returns {string} the label
                 */
                getLabel : function() {
                    return this._label;
                },

                /**
                 * getter for the url
                 * @method getUrl
                 * @returns {string} the url
                 */
                getUrl : function() {
                    return this._url;
                },
                
                /**
                 * setter for the label, delegates to setValues with the current
                 * url as the url value.
                 * @method setLabel
                 * @param newLabel {string}
                 */
                setLabel : function(newLabel) {
                    this.setValues(newLabel, this._url);
                },
                
                /**
                 * setter for the url, delegates to setValues with the current
                 * @method setUrl
                 * label as the label value.
                 * 
                 * @param newUrl {string}
                 */
                setUrl : function(newUrl) {
                    this.setValues(this._label, newUrl);
                },

                /**
                 * Set both the label and url then fire a changed event
                 * @method setValues
                 * @param newLabel {string}
                 * @param newUrl {string}
                 */
                setValues : function(newLabel, newUrl) {
                    if (!newLabel) {
                        throw("null or empty newLabel");
                    }
                    if (!newUrl) {
                        throw("null or empty newUrl");
                    }
                    var changed = false;
                        if (newLabel !== this._label) {
                            changed = true;
                        } else if (newUrl !== this._url) {
                            changed = true;
                        }
                    if (changed) {
                        Y.log(this + " firing valueChange: newLabel:" + newLabel + ",newUrl:" + newUrl);
                        this.fire("valueChange", {prevLabel : this._label, prevUrl : this._url, newLabel : newLabel,  newUrl : newUrl});
                    }
                },

                /**
                 * @method toString
                 * @return {string} a string with the label and url values
                 */
                toString : function() {
                    return "Bookmark{label:" + this._label + ",url:" + this._url + "}";
                }
        };


        //Add EventTarget attributes to the Bookmark prototype
        Y.augment(Bookmark, Y.EventTarget, null, null, {
            emitFacade : true,
            prefix     : 'bookmark'
        });

        //make the Bookmark constructor globally accessible
        Y.lane.Bookmark = Bookmark;

        /**
         * A class for representing an ordered collection of Bookmarks.
         * It fires events when the collection changes
         * 
         * @class Bookmarks
         * @constructor
         * @param bookmarks {array} may be undefined
         */
        Bookmarks = function(bookmarks) {
            this._bookmarks = [];
            if (bookmarks && !Y.Lang.isArray(bookmarks)) {
                Y.log('bad config', 'error', 'bookmarks');
                throw("bad config");
            }
            if (bookmarks) {
                for (var i = 0; i < bookmarks.length; i++) {
                    bookmarks[i].after("valueChange", this._handleValueChange, this);
                    this._bookmarks.push(bookmarks[i]);
                }
            }

            /**
             * @event add
             * @description Fired when a bookmark is added.
             * @preventable _defAddFn
             */
            this.publish("add", {defaultFn: this._defAddFn});
            
            /**
             * @event addSync
             * @description fired after an add is successfully synced with the server
             */
            this.publish("addSync", {defaultFn: this._successfulAdd, preventable : false});

            /**
             * @event remove
             * @description Fired when a bookmark is removed.
             * @preventable _defRemoveFn
             */
            this.publish("remove", {defaultFn: this._defRemoveFn});
            
            /**
             * @event removeSync
             * @description fired when a removal is successfully synced with the server
             */
            this.publish("removeSync", {defaultFn: this._successfulRemove, preventable : false});

            /**
             * @event update
             * @description Fired when a bookmark is updated.
             * @preventable _defUpdateFn
             */            
            this.publish("update", {defaultFn: this._defUpdateFn});
            
            /**
             * @event updateSync
             * @description fired when an update is successfully synced with the server
             */
            this.publish("updateSync", {preventable : false});
            Y.log("created new " + this);
        };

        Bookmarks.prototype = {

                /**
                 * fires a bookmark:add event
                 * @method addBookmark
                 * @param bookmark {Bookmark}
                 */
                addBookmark : function(bookmark) {
                    if (!Y.Lang.isObject(bookmark)) {
                        throw ("bad bookmark");
                    }
                    Y.log(this + " firing add: " + bookmark);
                    this.fire("add", {bookmark : bookmark});
                },
                
                /**
                 * @method getBookmark
                 * @param position {number}
                 * @returns the bookmark at the position
                 */
                getBookmark : function(position) {
                    return this._bookmarks[position];
                },
                
                /**
                 * fires a bookmark:remove event
                 * @method removeBookmark
                 * @param position {number} the bookmark to remove
                 */
                removeBookmark : function(position) {
                    Y.log(this + " firing remove: " + position);
                    this.fire("remove", {position : position});
                },
                
                /**
                 * fires a bookmark:update event
                 * @method updateBookmark
                 * @param bookmark {Bookmark}
                 */
                updateBookmark : function(bookmark) {
                    var position = Y.Array.indexOf(this._bookmarks, bookmark);
                    Y.log(this + " firing update: position: " + position + " " + bookmark);
                    this.fire("update", {bookmark : bookmark, position : position});
                },
                
                /**
                 * @method size
                 * @returns {number} the number of bookmarks
                 */
                size : function() {
                    return this._bookmarks.length;
                },
                
                /**
                 * @method indexOf
                 * @param bookmark {Bookmark}
                 * @return {number} the index of the given bookmark
                 */
                indexOf : function(bookmark) {
                    return Y.Array.indexOf(this._bookmarks, bookmark);
                },
                
                /**
                 * @method toString
                 * @returns {String} a string representation
                 */
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
                
                /**
                 * The default response to bookmarks:add, attempts to sync with server, fires
                 * bookmarks:addSync if successful.
                 * @method _defAddFn
                 * @private
                 * @param event {CustomEvent}
                 */
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
                            failure : this._syncFailed
                        },
                        "arguments" : {
                            bookmark : event.bookmark
                        },
                        context : this
                    });
                },
                
                /**
                 * The default response to bookmarks:remove, attempts to sync with server,
                 * fires bookmarks:removeSync if successful
                 * @method _defRemoveFn
                 * @private
                 * @param event {CustomEvent}
                 */
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
                            failure : this._syncFailed
                        },
                        "arguments" : {
                            position : event.position
                        },
                        context : this
                    });
                    this._bookmarks.splice(event.position, 1);
                },
                
                /**
                 * The default response to bookmarks:update, attempts to sync with server,
                 * fires bookmarks:updateSync if successful.
                 * @method _defUpdateFn
                 * @private
                 * @param event {CustomEvent}
                 */
                _defUpdateFn : function(event) {
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
                            failure : this._syncFailed
                        },
                        "arguments" : {
                            position : event.position
                        },
                        context : this
                    });

                },
                
                /**
                 * handler for bookmark:valueChange events
                 * @method _handleValueChange
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleValueChange : function(event) {
                    this.updateBookmark(event.target);
                },
                
                /**
                 * handler for bookmarks:removeSync event, removes a bookmark from the
                 * backing Array
                 * @method _successfulRemove
                 * @private
                 * @param event {CustomEvent}
                 */
                _successfulRemove : function(event) {
                    this._bookmarks.splice(event.position, 1);
                },
                
                /**
                 * handler for bookmarks:addSync event, adds a bookmark to index 0 of the
                 * backing Array
                 * @method _successfulAdd
                 * @private
                 * @param event {CustomEvent}
                 */
                _successfulAdd : function(event) {
                    event.bookmark.after("valueChange", this._handleValueChange, this);
                    this._bookmarks.unshift(event.bookmark);
                },
                
                _syncFailed : function() {
                    alert("sync failed");
                }
        };


        //Add EventTarget attributes to the Bookmarks prototype
        Y.augment(Bookmarks, Y.EventTarget, null, null, {
            emitFacade : true,
            prefix     : 'bookmarks'
        });

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
                        bookmarks.push(new Bookmark(label, url));
                    }
                    return new Bookmarks(bookmarks);
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

        Y.lane.BookmarksWidget = new BookmarksWidget({srcNode:Y.one("#bookmarks")});
        Y.lane.BookmarksWidget.render();

//        BookmarkLink = Y.Base.create("bookmark-link", Y.Widget, [], {
//            bindUI : function() {
//                var display = this.get("contentBox").getStyle("display");
//                this.get("boundingBox").setStyle("display", display);
//                this.get("boundingBox").setStyle("position", "relative");
//                this.get("boundingBox").append("<span style='cursor:pointer;position:absolute;right:-20px;bottom:0;display:block;border:1px black solid;padding-left:15px;background-color:pink' class='foo'>&#160;</span>");
//            },
//            syncUI : function() {
//                this.get("boundingBox").on("mouseout", this.hide, this);
//                this.get("boundingBox").one(".foo").on("mouseover", this.activate, this);
//                this.get("boundingBox").one(".foo").on("mouseout", this.hide, this);
//            },
//            show : function() {
//                this.get("boundingBox").one(".foo").setStyle("display", "block");
//            },
//            hide : function() {
//                this.get("boundingBox").one(".foo").setStyle("display", "none");
//                this.get("boundingBox").one(".foo").setStyle("background-color", "pink");
//            },
//            activate : function() {
//                this.show();
//                this.get("boundingBox").one(".foo").setStyle("background-color", "red");
//            }
//        });
//        
//        Y.delegate("mouseover", function(event) {
//            if (!this.hasClass("yui3-bookmark-link-content")) {
//                var foo = new BookmarkLink({srcNode:event.target});
//                foo.render();
//            } else {
//                Y.Widget.getByNode(this).show();
//            }
//        }, document, "a");
        
        
        BookmarkLink = function() {
            BookmarkLink.superclass.constructor.apply(this, arguments);
        };

        BookmarkLink.NAME = "bookmark-link";

        BookmarkLink.ATTRS = {
                node : {
                    valueFn : function() {
                        return Y.Node.create("<span id='bookmark-link'>b</span>");
                    }
                },
                bookmarks : {
                    value : null
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
                Y.delegate("mouseover", this._handleTargetMouseover,".content", "a", this);
                Y.delegate("mouseout", this._handleTargetMouseout,".content", "a", this);
                node.on("mouseover",this._handleBookmarkMouseover, this);
                node.on("mouseout", this._handleBookmarkMouseout, this);
                node.on("click", this._handleClick, this);
                this.after("inTargetChange", this._handleInTargetChange);
                this.after("inBookmarkChange", this._handleInBookmarkChange);
            },
            startTimer : function() {
            },
            clearTimer : function() {
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
                this.get("bookmarks").addBookmark(new Y.lane.Bookmark(label, url));
            },
            _handleInBookmarkChange : function(event) {
                if (event.newVal) {
                    this.get("target").insert(this.get("node"), "after");
                } else {
                    this.get("node").remove(false);
                }
            },
            _handleInTargetChange : function(event) {
                var target = this.get("target"), display = target.getStyle("display");
                if (display == "inline" && !target.one("img")) {
                    if (event.newVal) {
                        this.get("target").insert(this.get("node"), "after");
                    } else {
                        this.get("node").remove(false);
                    }
                }
            },
            _handleTargetMouseout : function(event) {
                this.set("inTarget", false);
            },
            _handleTargetMouseover : function(event) {
                this.set("target", event.currentTarget);
                this.set("inTarget", true);
            }
        });

        Y.lane.BookmarkLink = new BookmarkLink({bookmarks:Y.lane.BookmarksWidget.get("bookmarks")});

        if (Y.one("#bookmarks-editor")) {

            BookmarkEditor = Y.Base.create("bookmark-editor", Y.Widget, [], {
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
                    if (this.get("bookmark")) {
                        this.set("editing", false);
                    } else {
                        this.destroy(true);
                    }
                },
                save : function() {
                    var srcNode = this.get("srcNode"),
                    newlabel = srcNode.one("input[name='label']").get("value"),
                    newurl = srcNode.one("input[name='url']").get("value"),
                    bookmark = this.get("bookmark");
                    if (bookmark) {
                        if (newlabel != bookmark.getLabel() || newurl != bookmark.getUrl()) {
                            bookmark.setValues(newlabel, newurl);
                        }
                    } else if (newlabel && newurl) {
                        bookmark = new Bookmark(newlabel, newurl);
                        this.set("bookmark", bookmark);
                        Y.lane.BookmarksWidget.get("bookmarks").addBookmark(bookmark);
                    } else {
                        //do nothing if empty input field
                        //TODO: some sort of error reporting to user
                        return;
                    }
                    this.set("editing", false);
                },
                reset : function() {
                    var srcNode = this.get("srcNode"),
                    bookmark = this.get("bookmark"),
                    resetLabel = bookmark ? bookmark.getLabel() : "",
                    resetUrl = bookmark ? bookmark.getUrl() : "";
                    srcNode.one("input[name='label']").set("value", resetLabel);
                    srcNode.one("input[name='url']").set("value", resetUrl);
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
            });

            BookmarksEditor = Y.Base.create("bookmarks-editor", Y.Widget, [], {
                renderUI : function() {
                    var editor, editors = [], i, items = this.get("srcNode").all("li"), bookmarks = this.get("bookmarks");
                    for (i = 0; i < items.size(); i++) {
                        editor = new BookmarkEditor({srcNode : items.item(i), render : true, bookmark : bookmarks.getBookmark(i)});
                        editor.after("destroy", this._handleDestroyEditor, this);
                        editors.push(editor);
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
                    var items = this.get("srcNode").one("ul");
                    var item = Y.Node.create("<li><input type=\"checkbox\" checked=\"checked\"/><a></a></li>");
                    items.prepend(item);
                    var editor = new BookmarkEditor({srcNode : item, render : true});
                    editor.after("destroy", this._handleDestroyEditor, this);
                    this.get("editors").unshift(editor);
                    editor.set("editing", true);
//                      this.get("bookmarks").addBookmark(new Y.lane.Bookmark({label : "", url : "", href : ""}));
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
                      this.get("editors")[event.position].destroy(true);
                  },
                  _handleDestroyEditor : function(event) {
                      var editors = this.get("editors"),
                      position = Y.Array.indexOf(editors, event.target);
                      editors.splice(position, 1);
                  },
                  _handleBookmarkAdd : function(event) {
                      this.get("editors")[event.target.indexOf(event.bookmark)].update();
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

            Y.lane.BookmarksEditor = new BookmarksEditor({srcNode:Y.one("#bookmarks-editor"), bookmarks : Y.lane.BookmarksWidget.get("bookmarks")});
            Y.lane.BookmarksEditor.render();
        }
    }
})();
