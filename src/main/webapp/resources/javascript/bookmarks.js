
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
            this.publish("addSync", {defaultFn: this._handleAddSync, preventable : false});

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
            this.publish("removeSync", {defaultFn: this._handleRemoveSync, preventable : false});

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
            //TODO: handle updateSync failure
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
                 * @method removeBookmarks
                 * @param positions {Array} the bookmarks to remove
                 */
                removeBookmarks : function(positions) {
                    Y.log(this + " firing remove: " + positions);
                    this.fire("remove", {positions : positions});
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
                                Y.log(this + " firing addSync: successful " + event.bookmark);
                                this.fire("addSync", {success : true, bookmark : event.bookmark});
                            },
                            failure : function() {
                                Y.log(this + " firing addSync: failed " + event.bookmark);
                                this.fire("addSync", {success : false, bookmark : event.bookmark});
                            }
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
                    var data = Y.JSON.stringify(event.positions);
                    Y.io("/././bookmarks", {
                        method : "delete",
                        data : data,
                        headers : {
                            "Content-Type" : "application/json"
                        },
                        on : {
                            success : function() {
                                Y.log(this + " firing removeSync: successful " + event.positions);
                                this.fire("removeSync", {success: true, positions : event.positions});
                            },
                            failure : function() {
                                Y.log(this + " firing removeSync: failed " + event.positions);
                                this.fire("removeSync", {success: false, positions : event.positions});
                            }
                        },
                        "arguments" : {
                            positions : event.positions
                        },
                        context : this
                    });
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
                                Y.log(this + " firing updateSync: successful " + event.position);
                                this.fire("updateSync", {success : true, position : event.position});
                            },
                            failure :  function() {
                                Y.log(this + " firing updateSync: failed" + event.position);
                                this.fire("updateSync", {success: false, position : event.position});
                            }
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
                 * handler for bookmarks:removeSync event, removes bookmarks from the
                 * backing Array
                 * @method _handleRemoveSync
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleRemoveSync : function(event) {
                    if (event.success) {
                        for (var i = event.positions.length - 1; i >=0; --i) {
                            this._bookmarks.splice(event.positions[i], 1);
                        }
                    }
                },
                
                /**
                 * handler for bookmarks:addSync event, adds a bookmark to index 0 of the
                 * backing Array
                 * @method _handleAddSync
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleAddSync : function(event) {
                    if (event.success) {
                        event.bookmark.after("valueChange", this._handleValueChange, this);
                        this._bookmarks.unshift(event.bookmark);
                    }
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
                bookmarks.after("removeSync", this._bookmarksRemoved, this);
                bookmarks.after("updateSync", this._bookmarkUpdated, this);
            },
            toString : function() {
                return "BookmarksWidget:" + this.get("bookmarks");
            },
            _bookmarkAdded : function(event) {
                this.get("srcNode").prepend("<li><a href='" + event.bookmark.getUrl() + "'>" + event.bookmark.getLabel() + "</a></li>");
            },
            _bookmarksRemoved : function(event) {
                var i, items = this.get("srcNode").all("li");
                for (i = event.positions.length - 1; i >= 0; --i) {
                    items.item(event.positions[i]).remove(true);
                }
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
                        return Y.Node.create("<span title='Bookmark it' id='bookmark-link'>&#160;</span>");
                    }
                },
                bookmarks : {
                    value : null
                },
                target : {
                    value : null
                },
                hideDelay : {
                    value:500
                },
                state : {
                    value : BookmarkLink.OFF
                }
        };

        Y.extend(BookmarkLink, Y.Base, {
            
            initializer : function() {
                this._timer = null;
                var node = this.get("node");
                Y.delegate("mouseover", this._handleTargetMouseover,".content", "a", this);
                Y.delegate("mouseout", this._handleTargetMouseout,".content", "a", this);
                this.on("statusChange", this._handleStatusChange);
                this.get("bookmarks").on("addSync", this._handleSyncEvent, this);
            },
            _handleSyncEvent : function(event) {
                if (event.success) {
                    this.set("status", BookmarkLink.SUCCESSFUL);
                } else {
                    this.set("status", BookmarkLink.FAILED);
                }
            },
            _handleBookmarkMouseout : function(event) {
                this.set("status", BookmarkLink.TIMING);
            },
            _handleBookmarkMouseover : function(event) {
                this.set("status", BookmarkLink.ACTIVE);
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
                this.set("status", BookmarkLink.BOOKMARKING);
                this.get("bookmarks").addBookmark(new Y.lane.Bookmark(label, url));
            },
            _handleTargetMouseout : function(event) {
                this.set("status", BookmarkLink.TIMING);
            },
            _handleTargetMouseover : function(event) {
                if (this._isBookmarkable(event.currentTarget)) {
                    this.set("target", event.currentTarget);
                    this.set("status", BookmarkLink.READY);
                }
            },
            _isBookmarkable : function(target) {
                return target.getStyle("display") == "inline" && !target.one("img");
            },
            _hide : function() {
                this.set("status", BookmarkLink.OFF);
            },
            _clearTimer : function() {
                if (this._timer) {
                    this._timer.cancel();
                    this._timer = null;
                }
            },
            _handleStatusChange : function(event) {
                this._clearTimer();
                var node = this.get("node");
                //IE messes up the event handling if set up on initialization
                //so purging and selectively set them when the status changes.
                node.purge(false);
                switch(event.newVal) {
                //OFF: not visible
                case BookmarkLink.OFF : 
                    node.remove(false);
                    node.removeClass("active");
                    node.removeClass("bookmarking");
                    node.removeClass("successful");
                    node.removeClass("failed");
                    break;
                //READY: visible but not enabled
                case BookmarkLink.READY :
                    this.get("target").insert(node, "after");
                    break;
                //ACTIVE: enabled (mouseover)
                case BookmarkLink.ACTIVE : 
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.on("click", this._handleClick, this);
                    node.addClass("active");
                    break;
                //BOOKMARKING: clicked and waiting for server sync message
                case BookmarkLink.BOOKMARKING : 
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.replaceClass("active", "bookmarking");
                    break;
                //SUCCESSFUL: server sync was successful
                case BookmarkLink.SUCCESSFUL : 
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.replaceClass("bookmarking", "successful");
                    break;
                //FAILED: server sync failed
                case BookmarkLink.FAILED : 
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.replaceClass("bookmarking", "failed");
                    break;
                //TIMING: waiting to hide
                case BookmarkLink.TIMING :
                    node.on("mouseover",this._handleBookmarkMouseover, this);
                    node.removeClass("active");
                    node.removeClass("bookmarking");
                    node.removeClass("successful");
                    node.removeClass("failed");
                    this._timer = Y.later(this.get("hideDelay"), this, this._hide);
                default:
                }
            }
        }, {
            OFF : 0,
            READY : 1,
            ACTIVE : 2,
            BOOKMARKING : 3,
            SUCCESSFUL : 4,
            FAILED : 5,
            TIMING : 6
        });

        Y.lane.BookmarkLink = new BookmarkLink({bookmarks:Y.lane.BookmarksWidget.get("bookmarks")});

        if (Y.one("#bookmarks-editor")) {

            BookmarkEditor = Y.Base.create("bookmark-editor", Y.Widget, [], {
                renderUI : function() {
                    this.get("srcNode").append(
                        "<input type=\"text\" name=\"label\"/>" +
                        "<input type=\"text\" name=\"url\"/>" +
                        "<button name=\"action\" value=\"save\" type=\"submit\">save</button>" +
                        "<button value=\"reset\" type=\"reset\">reset</button>" +
                        "<button name=\"action\" value=\"cancel\" type=\"submit\">cancel</button>");
                },
                bindUI : function() {
                    this.get("srcNode").all("button").on("click", this._handleButtonClick, this);
                    this.get("srcNode").one("input[type='checkbox']").on("change", this._handleChange, this);
                    this.on("editingChange", this._handleEditingChange, this);
                    this.on("checkedChange", this._handleCheckedChange, this);
                },
                syncUI : function() {
                    var srcNode = this.get("srcNode");
                    this._labelInput = new Y.lane.TextInput(srcNode.one("input[name='label']"));
                    this._urlInput = new Y.lane.TextInput(srcNode.one("input[name='url']"));
                },
                cancel : function() {
                    if (this.get("bookmark")) {
                        this.set("editing", false);
                    } else {
                        this._labelInput.destroy();
                        this._urlInput.destroy();
                        this.destroy(true);
                    }
                },
                save : function() {
                    var newlabel = this._labelInput.getValue(),
                        newurl = this._urlInput.getValue(),
                        bookmark = this.get("bookmark");
                    if (!newlabel || !newurl) {
                        if (!newlabel) {
                            this._labelInput.setHintText("required");
                        }
                        if (!newurl) {
                            this._urlInput.setHintText("required");
                        }
                        return;
                    }
                    if (bookmark) {
                        if (newlabel != bookmark.getLabel() || newurl != bookmark.getUrl()) {
                            bookmark.setValues(newlabel, newurl);
                        }
                    } else {
                        bookmark = new Bookmark(newlabel, newurl);
                        this.set("bookmark", bookmark);
                        Y.lane.BookmarksWidget.get("bookmarks").addBookmark(bookmark);
                    }
                    this.set("editing", false);
                },
                reset : function() {
                    var bookmark = this.get("bookmark");
                    this._labelInput.setHintText("Name");
                    this._urlInput.setHintText("location");
                    if (bookmark) {
                        this._labelInput.setValue(bookmark.getLabel());
                        this._urlInput.setValue(bookmark.getUrl());
                    } else {
                        this._labelInput.reset();
                        this._urlInput.reset();
                    }
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
                    var srcNode = this.get("srcNode"),
                        activeClass = this.getClassName() + "-active";
                    if (event.newVal) {
                        srcNode.addClass(activeClass);
                        this.reset();
                    } else {
                        srcNode.removeClass(activeClass);
                    }
                },
                _handleCheckedChange : function(event) {
                    var checkBox = this.get("srcNode").one("input[type='checkbox']");
                    checkBox.set("checked", event.newVal);
                },
                _handleChange : function(event) {
                    this.set("checked", event.target.get("checked"));
                }
            }, {
                ATTRS : {
                    bookmark : {
                        value : null
                    },
                    editing : {
                        value : false
                    },
                    checked : {
                        value : false
                    }
                }
            });

            BookmarksEditor = Y.Base.create("bookmarks-editor", Y.Widget, [], {
                renderUI : function() {
                    var editor, editors = [], i,
                        srcNode = this.get("srcNode"),
                        items = srcNode.all("li"),
                        bookmarks = this.get("bookmarks"),
                        checkBox = Y.Node.create("<input type=\"checkbox\"/>");
                    for (i = 0; i < items.size(); i++) {
                        editor = new BookmarkEditor({srcNode : items.item(i), render : true, bookmark : bookmarks.getBookmark(i)});
                        editor.after("destroy", this._handleDestroyEditor, this);
                        editors.push(editor);
                    }
                    this.set("editors", editors);
                    bookmarks.after("removeSync", this._handleBookmarksRemove, this);
                    bookmarks.after("addSync", this._handleBookmarkAdd, this);
                    bookmarks.after("updateSync", this._handleBookmarkUpdate, this);
                    srcNode.one("fieldset").prepend(checkBox);
                    checkBox.on("change", this._handleCheckedChange, this);
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
                          this.get("bookmarks").removeBookmarks(checked);
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
                  _handleBookmarksRemove : function(event) {
                      //TODO: handle event.success == false
                      var i, editors = this.get("editors");
                      for (i = event.positions.length - 1; i >= 0; --i) {
                          editors[event.positions[i]].destroy(true);
                      }
                  },
                  _handleDestroyEditor : function(event) {
                      var editors = this.get("editors"),
                      position = Y.Array.indexOf(editors, event.target);
                      editors.splice(position, 1);
                  },
                  _handleBookmarkAdd : function(event) {
                      //TODO: handle event.success == false
                      this.get("editors")[event.target.indexOf(event.bookmark)].update();
                  },
                _handleBookmarkUpdate : function(event) {
                    //TODO: handle event.success == false
                    var editors = this.get("editors");
                    editors[event.position].update();
                },
                _handleCheckedChange : function(event) {
                    var i, editors = this.get("editors");
                    for (i = 0; i < editors.length; i++) {
                        editors[i].set("checked", event.target.get("checked"));
                    }
                },
                _getCheckedIndexes : function() {
                    var indexes = [], i, editors = this.get("editors");
                    for (i = 0; i < editors.length; i++) {
                        if (editors[i].get("checked")) {
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
