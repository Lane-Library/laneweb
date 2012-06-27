(function() {

    var Bookmark, Bookmarks, BookmarksWidget, BookmarkLink, BookmarkEditor, BookmarksEditor;

    if (Y.one("#bookmarks")) {

        /**
         * A class for representing a bookmark with attributes for the label and url.
         * 
         * @class Bookmark
         * @uses EventTarget
         * @constructor
         * @param label {string} the label
         * @param url {string} the url
         */
        Bookmark = function(label, url) {
            this.publish("valueChange", {defaultFn : this._valueChange});
            this.setValues(label, url);
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
         * @uses EventTarget
         * @constructor
         * @param bookmarks {array} may be undefined
         */
        Bookmarks = function(bookmarks) {
            this._bookmarks = [];
            if (bookmarks && !Y.Lang.isArray(bookmarks)) {
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
                    this.fire("remove", {positions : positions});
                },
                
                /**
                 * fires a bookmark:update event
                 * @method updateBookmark
                 * @param bookmark {Bookmark}
                 */
                updateBookmark : function(bookmark) {
                    var position = Y.Array.indexOf(this._bookmarks, bookmark);
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
                 * bookmarks:addSync.
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
                                this.fire("addSync", {success : true, bookmark : event.bookmark});
                            },
                            failure : function() {
                                this._handleSyncFailure("Sorry, add bookmark failed.");
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
                    var indexes = Y.JSON.stringify(event.positions);
                    Y.io("/././bookmarks?indexes=" + indexes, {
                        method : "delete",
                        on : {
                            success : function() {
                                this.fire("removeSync", {success: true, positions : event.positions});
                            },
                            failure : function() {
                                this._handleSyncFailure("Sorry, delete bookmark failed.");
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
                                this.fire("updateSync", {success : true, position : event.position});
                            },
                            failure :  function() {
                                this._handleSyncFailure("Sorry, bookmark update failed.");
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
                        for (var i = event.positions.length - 1; i >=0; --i) {
                            this._bookmarks.splice(event.positions[i], 1);
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
                        event.bookmark.after("valueChange", this._handleValueChange, this);
                        this._bookmarks.unshift(event.bookmark);
                },
                _handleSyncFailure : function(message) {
                    alert(message);
                }
        };


        //Add EventTarget attributes to the Bookmarks prototype
        Y.augment(Bookmarks, Y.EventTarget, null, null, {
            emitFacade : true,
            prefix     : 'bookmarks'
        });
        
        /**
         * The Bookmarks Widget.  This is created by parsing the ul element
         * with id #bookmarks.
         * 
         * @class BookmarksWidget
         * @extends Widget
         * @constructor
         */
        BookmarksWidget = Y.Base.create("bookmarks", Y.Widget, [], {
            
            /**
             * Set up event listeners to respond to events when the server has been updated
             * so the bookmark markup can change appropriately
             * @method bindUI
             */
            bindUI : function() {
                var bookmarks = this.get("bookmarks");
                bookmarks.after("addSync", this._bookmarkAdded, this);
                bookmarks.after("removeSync", this._bookmarksRemoved, this);
                bookmarks.after("updateSync", this._bookmarkUpdated, this);
            },
            
            /**
             * Set up the UI, in this case truncate text in links to 32 characters,
             * and hide items > displayLimit.
             * @method syncUI
             */
            syncUI : function() {
                this._truncateLabels();
                this._hideSomeItems();
            },
            
            /**
             * Provide a text representation of this widget.
             * @method toString
             * @returns "BookmarksWidget: " and the list of bookmarks.
             */
            toString : function() {
                return "BookmarksWidget:" + this.get("bookmarks");
            },
            
            /**
             * Respond to a successful bookmark add event.  Adds a list item with a link to the top of the list.
             * @method _bookmarkAdded
             * @private
             * @param event {CustomEvent}
             */
            _bookmarkAdded : function(event) {
                this.get("srcNode").prepend("<li><a href='" + event.bookmark.getUrl() + "'>" + event.bookmark.getLabel() + "</a></li>");
                this.syncUI();
            },
            
            /**
             * Respond to a successful bookmarks remove event.  Removes list items of bookmarks that were deleted.
             * @method _bookmarksRemoved
             * @private
             * @param event {CustomEvent}
             */
            _bookmarksRemoved : function(event) {
                var i, items = this.get("srcNode").all("li");
                for (i = event.positions.length - 1; i >= 0; --i) {
                    items.item(event.positions[i]).remove(true);
                }
                this.syncUI();
            },
            
            /**
             * Respond to a successful bookmark update event.  Alters the anchor text and/or url for a bookmark.
             * @method _bookmarkUpdated
             * @private
             * @param event {CustomEvent}
             */
            _bookmarkUpdated : function(event) {
                var bookmark = this.get("bookmarks").getBookmark(event.position),
                anchor = this.get("srcNode").all("li").item(event.position).one("a");
                anchor.set("innerHTML", bookmark.getLabel());
                anchor.set("href", bookmark.getUrl());
                this.syncUI();
            },
            
            /**
             * Shorten all anchor text to less than 32 characters, append ... if shortened.
             * @method _truncateLabels
             * @private
             */
            _truncateLabels : function() {
                var i, anchor, label, anchors = this.get("srcNode").all("a");
                for (i = 0; i < anchors.size(); i++) {
                    anchor = anchors.item(i);
                    label = anchor.get("innerHTML");
                    if (label.length > 32) {
                        anchor.set("innerHTML", label.substring(0, 32) + "...");
                    }
                }
            },
            
            /**
             * Hide items > displayLimit
             * @method _hideSomeItems()
             * @private;
             */
            _hideSomeItems : function() {
                var i, displayLimit = this.get("displayLimit"), items = this.get("srcNode").all("li");
                for (i = 0; i < displayLimit && i < items.size(); i++) {
                    items.item(i).setStyle("display", "block");
                }
                for (i = displayLimit; i < items.size(); i++) {
                    items.item(i).setStyle("display", "none");
                }
            }
        }, {
            ATTRS : {
                    items : {},
                    bookmarks : {},
                    displayLimit : {}
            },
            HTML_PARSER : {
                    items : ["li"],
                    bookmarks : function(srcNode) {
                        var i, anchor, label, url, query, bookmarks = [], anchors = srcNode.all("a");
                        for (i = 0; i < anchors.size(); i++) {
                            anchor = anchors.item(i);
                            anchor.plug(Y.lane.LinkPlugin);
                            label = anchor.link.get("title");
                            if (anchor.link.get("local")) {
                                url = anchor.link.get("path");
                                query = anchor.link.get("query");
                                url = query ? url + query : url;
                            } else {
                                url = anchor.link.get("url");
                            }
                            bookmarks.push(new Bookmark(label, url));
                        }
                        return new Bookmarks(bookmarks);
                    }
            }
        });

        //create a new widget and keep a global reference to it
        //may be able to use Widget.getByNode("#bookmarks") rather than the global reference . . . .
        Y.lane.BookmarksWidget = new BookmarksWidget({srcNode:Y.one("#bookmarks"), render:true, displayLimit:5});
        
        
        //don't create BookmarkLink if there is a class=no-bookmarking
        if (!Y.one(".no-bookmarking")) {

        /**
         * A link that appears when mousing over bookmarkable links and adds that link to bookmarks
         * when clicked.
         * 
         * @class BookmarksLink
         * @uses Base
         * @uses LinkPlugin
         * @constructor
         */
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
            
            /**
             * Sets up event handlers and creates the timer attribute.
             * @method initializer
             */
            initializer : function() {
                this._timer = null;
                Y.delegate("mouseover", this._handleTargetMouseover,".content", "a", this);
                Y.delegate("mouseout", this._handleTargetMouseout,".content", "a", this);
                if (LANE.SearchResult.getSearchTerms()) {
                    var bookmarkSearch = Y.one("#bookmarkSearch");
                    if (bookmarkSearch) {
                        bookmarkSearch.setStyle("display", "block");
                        bookmarkSearch.on("click", this._handleBookmarkSearchClick, this);
                    }
                }
                this.on("statusChange", this._handleStatusChange);
                this.get("bookmarks").after("addSync", this._handleSyncEvent, this);
            },
            
            /**
             * Responds to bookmarks:addSync event, changes the status to SUCCESSFUL
             * @method _handleSyncEvent
             * @private
             * @param event {CustomEvent}
             */
            _handleSyncEvent : function(event) {
                this.set("status", BookmarkLink.SUCCESSFUL);
            },
            
            /**
             * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
             * @method _handleBookmarkMouseOut
             * @private
             * @param event {CustomEvent}
             */
            _handleBookmarkMouseout : function(event) {
                this.set("status", BookmarkLink.TIMING);
            },
            
            /**
             * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
             * @method _handleBookmarkMouseover
             * @private
             * @param event {CustomEvent}
             */
            _handleBookmarkMouseover : function(event) {
                this.set("status", BookmarkLink.ACTIVE);
            },
            
            /**
             * Responds to BookmarkLink clicks.  Adds the LinkPlugin to the target link and uses
             * that to determine the url (translates proxy links to the base url).  Changes the
             * status to BOOKMARKING
             * @method _handleClick
             * @private
             */
            _handleClick : function() {
                var target = this.get("target"), label, url, query;
                target.plug(Y.lane.LinkPlugin);
                label = target.link.get("title");
                if (target.link.get("local")) {
                    url = target.link.get("path");
                    //case 71646 local links lack query string
                    query = target.link.get("query");
                    if (query) {
                        url += query;
                    }
                } else {
                    url = target.link.get("url");
                }
                this.set("status", BookmarkLink.BOOKMARKING);
                this.get("bookmarks").addBookmark(new Y.lane.Bookmark(label, url));
            },
            
            /**
             * Handle clicks on the bookmarkSearch link.
             * @method _handleBookmarkSearchClick
             * @private
             */
            _handleBookmarkSearchClick : function(event) {
                label = "Search for: " + LANE.SearchResult.getSearchTerms();
                url = "/search.html?source=" + LANE.SearchResult.getSearchSource() + "&q=" + LANE.SearchResult.getSearchTerms();
                this.get("bookmarks").addBookmark(new Y.lane.Bookmark(label, url));
            },
            
            /**
             * Responds to mouseout on target anchors, checks if they are bookmarkable, changes the status to TIMING.
             * @method _handleTargetMouseout
             * @private
             * @param event {CustomEvent}
             */
            _handleTargetMouseout : function(event) {
                if (this._isBookmarkable(event.currentTarget)) {
                    this.set("status", BookmarkLink.TIMING);
                }
            },
            
            /**
             * Responds to mouseover on anchors, checks if they are bookmarkable, changes the status to READY.
             * @method _handleTargetMouseover
             * @private
             * @param event {CustomEvent}
             */
            _handleTargetMouseover : function(event) {
                if (this._isBookmarkable(event.currentTarget)) {
                    this.set("target", event.currentTarget);
                    this.set("status", BookmarkLink.READY);
                }
            },
            
            /**
             * Determine if a link is bookmarkable.  For now true if its display property is inline and
             * it does not contain an img element.  Added #topResources links, case 71323.
             * @method _isBookmarkable
             * @private
             * @param target the target anchor
             * @returns {Boolean}
             */
            _isBookmarkable : function(target) {
                var bookmarkable = false;
                if (target.getStyle("display") == "inline" && !target.one("img")) {
                    bookmarkable = true;
                } else if (target.ancestor("#topResources")) {
                    bookmarkable = true;
                }
                return bookmarkable;
            },
            
            /**
             * Changes the status to OFF
             * @method _turnOff
             * @private
             */
            _turnOff : function() {
                this.set("status", BookmarkLink.OFF);
            },
            
            /**
             * Cancels the timer and sets it to null.
             * @method _clearTimer
             * @private
             */
            _clearTimer : function() {
                if (this._timer) {
                    this._timer.cancel();
                    this._timer = null;
                }
            },
            
            /**
             * Respond to statusChange events, adds and removes the link and changes the link's class,
             * and starts the timer to turn off the link when appropriate.
             * @method _handleStatusChange
             * @private
             * @param event {CustomEvent}
             */
            _handleStatusChange : function(event) {
                this._clearTimer();
                var node = this.get("node"), target = this.get("target");
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
                    break;
                //READY: visible but not enabled
                case BookmarkLink.READY :
                    target.insert(node, "after");
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
                //TIMING: waiting to hide
                case BookmarkLink.TIMING :
                    node.on("mouseover",this._handleBookmarkMouseover, this);
                    node.removeClass("active");
                    node.removeClass("bookmarking");
                    node.removeClass("successful");
                    this._timer = Y.later(this.get("hideDelay"), this, this._turnOff);
                default:
                }
            }
        }, {
            OFF : 0,
            READY : 1,
            ACTIVE : 2,
            BOOKMARKING : 3,
            SUCCESSFUL : 4,
            TIMING : 5
        });

        //create a BookmarkLink and save reference
        Y.lane.BookmarkLink = new BookmarkLink({bookmarks:Y.lane.BookmarksWidget.get("bookmarks")});
            
        }

        if (Y.one("#bookmarks-editor")) {

            /**
             * An editor widget for an individual bookmark.
             * @class BookmarkEditor
             * @uses Widget
             * @uses TextInput
             * @constructor
             */
            BookmarkEditor = Y.Base.create("bookmark-editor", Y.Widget, [], {
                
                /**
                 * Creates text inputs and buttons for the editor.
                 * @method renderUI
                 */
                renderUI : function() {
                    this.get("srcNode").append(
                        "<input type=\"text\" name=\"label\"/>" +
                        "<input type=\"text\" name=\"url\"/>" +
                        "<button name=\"action\" value=\"save\" type=\"submit\">save</button>" +
                        "<button value=\"reset\" type=\"reset\">undo</button>" +
                        "<button name=\"action\" value=\"cancel\" type=\"submit\">cancel</button>");
                },
                
                /**
                 * Sets up event handlers.
                 * @method bindUI
                 */
                bindUI : function() {
                    this.get("srcNode").all("button").on("click", this._handleButtonClick, this);
                    this.on("editingChange", this._handleEditingChange, this);
                },
                
                /**
                 * Sets up the TextInput objects for the inputs and truncates long labels.
                 * @method syncUI
                 */
                syncUI : function() {
                    var srcNode = this.get("srcNode");
                    this._labelInput = new Y.lane.TextInput(srcNode.one("input[name='label']"));
                    this._urlInput = new Y.lane.TextInput(srcNode.one("input[name='url']"));
                    this._urlInput.getInput().after("focus", this._setDefaultUrlInputText, this);
                    this._truncateLabel();
                },
                
                /**
                 * Set the checkbox state.
                 * @method setChecked
                 * @param checked {boolean}
                 */
                setChecked : function(checked) {
                    this.get("srcNode").one("input[type='checkbox']").set("checked", checked);
                },
                
                /**
                 * Get the checkbox state.
                 * @method isChecked
                 * @return whether or not the checkbox is checked.
                 */
                isChecked : function() {
                    return this.get("srcNode").one("input[type='checkbox']").get("checked");
                },
                
                /**
                 * Responds to the cancel button.  If there is no associated bookmark, like when this editor
                 * is for a new bookmark that hasn't been created yet, this editor gets destroyed, otherwise
                 * the editing attribute is set to false
                 * @method cancel
                 */
                cancel : function() {
                    if (this.get("bookmark")) {
                        this.set("editing", false);
                    } else {
                        this._labelInput.destroy();
                        this._urlInput.destroy();
                        this.destroy(true);
                    }
                },
                
                /**
                 * Responds to the save button.  If the inputs lack value, puts 'required' in to the value
                 * and does nothing else.  If there is no associated bookmark, creates a new one, otherwise
                 * changes the bookmark label and url based on what is in the inputs.
                 * @method save
                 */
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
                
                /**
                 * Responds to the reset button.  Resets the text inputs to the bookmark's values.
                 * If there is no bookmark, resets the TextInput object.
                 * @method reset
                 */
                reset : function() {
                    var bookmark = this.get("bookmark");
                    this._labelInput.setHintText("Name");
                    this._urlInput.setHintText("Location");
                    if (bookmark) {
                        this._labelInput.setValue(bookmark.getLabel());
                        this._urlInput.setValue(bookmark.getUrl());
                    } else {
                        this._labelInput.reset();
                        this._urlInput.reset();
                    }
                },
                
                /**
                 * Update the editors anchor text and url with the bookmark's label and url.
                 * @method update
                 */
                update : function() {
                    var anchor = this.get("srcNode").one("a"),
                        bookmark = this.get("bookmark");
                    anchor.set("innerHTML", bookmark.getLabel());
                    anchor.set("href", bookmark.getUrl());
                    this._truncateLabel();
                },
                
                /**
                 * The click handler for buttons, delegates to the function named the same as the buttons value.
                 * @method _handleButtonClick
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleButtonClick : function(event) {
                    event.preventDefault();
                    this[event.target.getAttribute("value")].call(this, event);
                },
                
                /**
                 * Called when the editing attribute changes.  Toggles the yui3-bookmark-editor-active class.
                 * @method _handleEditingChange
                 * @private
                 * @param event {CustomEvent}
                 */
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
                
                /**
                 * Truncates the link text to 130 characters if necessary.
                 * @method _truncateLabel
                 * @private
                 */
                _truncateLabel : function() {
                    var anchor = this.get("srcNode").one("a"),
                        label = anchor.get("innerHTML");
                    if (label.length > 130) {
                        anchor.set("innerHTML", label.substring(0, 130) + "...");
                    }
                },
                
                /**
                 * Put the text http:// into url input if it is empty
                 * @method _setDefaultUrlInputText
                 * @private
                 */
                _setDefaultUrlInputText : function() {
                    if (this._urlInput.getValue() === "") {
                        this._urlInput.setValue("http://");
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

            /**
             * The BookmarksEditor.
             * Contains one BookmarkEditor for each bookmark.
             */
            BookmarksEditor = Y.Base.create("bookmarks-editor", Y.Widget, [], {
                
                /**
                 * Adds a checkbox that will trigger all other checkboxes to toggle to a similar state.
                 * @method renderUI
                 */
                renderUI : function() {
                    this.get("srcNode").one("fieldset").prepend(Y.Node.create("<input type=\"checkbox\"/>"));
                },
                
                /**
                 * Sets up various event handlers.
                 * @method bindUI
                 */
                bindUI : function() {
                    var srcNode = this.get("srcNode"),
                        bookmarks = this.get("bookmarks");
                    srcNode.all("fieldset button").on("click", this._handleButtonClick, this);
                    bookmarks.after("removeSync", this._handleBookmarksRemove, this);
                    bookmarks.after("addSync", this._handleBookmarkAdd, this);
                    bookmarks.after("updateSync", this._handleBookmarkUpdate, this);
                    srcNode.one("fieldset input[type='checkbox']").on("click", this._handleCheckboxClick, this);
                    
                },
                
                /**
                 * Creates the BookmarkEditors.
                 * @method syncUI
                 */
                syncUI : function() {
                    var editor, editors = [], i,
                        srcNode = this.get("srcNode"),
                        items = srcNode.all("li"),
                        bookmarks = this.get("bookmarks");
                    for (i = 0; i < items.size(); i++) {
                        editor = new BookmarkEditor({srcNode : items.item(i), render : true, bookmark : bookmarks.getBookmark(i)});
                        editor.after("destroy", this._handleDestroyEditor, this);
                        editors.push(editor);
                    }
                    this.set("editors", editors);
                },
                
                /**
                 * Responds to a click on the add button.  Adds a list item and associated BookmarkEditor to
                 * the top of the list and sets it editing state to true.
                 * @method add
                 */
                add : function() {
                    var items = this.get("srcNode").one("ul"),
                        item = Y.Node.create("<li><input type=\"checkbox\" checked=\"checked\"/><a></a></li>"),
                        editor;
                        
                    items.prepend(item);
                    editor = new BookmarkEditor({srcNode : item, render : true});
                    editor.after("destroy", this._handleDestroyEditor, this);
                    this.get("editors").unshift(editor);
                    editor.set("editing", true);
                },
                  
                /**
                 * Responds to a click on the delete button.  Gets an array of the indexes of the checked editors
                 * and calls removeBookmarks on the bookmarks object.
                 * @method delete
                 */
                "delete" : function() {
                    var checked = this._getCheckedIndexes();
                    if (checked.length > 0) {
                        this._clearChecked();
                        this.get("bookmarks").removeBookmarks(checked);
                    }
                },
                  
                /**
                 * Responds to a click on the edit button.  Gets an array of the indexes of the checked editors
                 * and sets there editing attribute to true.
                 * @method edit
                 */
                edit : function() {
                    var i, checked = this._getCheckedIndexes(), editors = this.get("editors");
                    for (i = 0; i < checked.length; i++) {
                        editors[checked[i]].set("editing", true);
                    }
                },
                  
                /**
                 * The click handler for buttons, delegates to the function named the same as the buttons value.
                 * @method _handleButtonClick
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleButtonClick : function(event) {
                    event.preventDefault();
                    //see case 67695
                    //pressing return generates a click on the add button for some reason
                    //pageX is 0 in that situation
                    if (event.pageX !== 0) {
                        var fn = this[event.target.getAttribute("value")];
                        if (fn) {
                            fn.call(this);
                        }
                    }
                },
                  
                /**
                 * Responds to the bookmarks:removeSync event, calls destroy on each BookmarkEditor
                 * associated with removed bookmarks.
                 * @method _handleBookmarksRemove
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleBookmarksRemove : function(event) {
                    var i, editors = this.get("editors");
                    for (i = event.positions.length - 1; i >= 0; --i) {
                        editors[event.positions[i]].destroy(true);
                    }
                },
                
                /**
                 * Removes a destroyed editor from the backing Array.
                 * @method _handleDestroyEditor
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleDestroyEditor : function(event) {
                    var editors = this.get("editors"),
                        position = Y.Array.indexOf(editors, event.target);
                    editors.splice(position, 1);
                },
                
                /**
                 * Responds to the bookmarks:addSync event, call update() on the appropriate BookmarkEditor.
                 * @method _handleBookmarkAdd
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleBookmarkAdd : function(event) {
                    this.get("editors")[event.target.indexOf(event.bookmark)].update();
                },
                
                /**
                 * Responds to the bookmarks:updateSync event, calls update() on the appropriate BookmarkEditor.
                 * @method _handleBookmarkUpdate
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleBookmarkUpdate : function(event) {
                    var editors = this.get("editors");
                    editors[event.position].update();
                },
                
                /**
                 * Responds to click event on the master checkbox.  Sets the checked state of all BookmarkEditors
                 * to the whatever the master is.
                 * @method _handleCheckboxClick
                 * @private
                 * @param event {CustomEvent}
                 */
                _handleCheckboxClick : function(event) {
                    var i, checked = event.target.get("checked"), editors = this.get("editors");
                    for (i = 0; i < editors.length; i++) {
                        editors[i].setChecked(checked);
                    }
                },
                
                /**
                 * Returns an Array of indexes of those BookmarkEditors for which isChecked() is true.
                 * @method _getCheckIndexes
                 * @private
                 * @returns {Array}
                 */
                _getCheckedIndexes : function() {
                    var indexes = [], i, editors = this.get("editors");
                    for (i = 0; i < editors.length; i++) {
                        if (editors[i].isChecked()) {
                            indexes.push(i);
                        }
                    }
                    return indexes;
                },
                
                /**
                 * Unchecks all BookmarkEditors
                 * @method _clearChecked
                 * @private
                 */
                _clearChecked : function() {
                    var i, editors = this.get("editors");
                    for (i = 0; i < editors.length; i++) {
                        editors[i].setChecked(false);
                    }
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

            //Create a new BookmarksEditor
            Y.lane.BookmarksEditor = new BookmarksEditor({
                srcNode:Y.one("#bookmarks-editor"),
                bookmarks : Y.lane.BookmarksWidget.get("bookmarks"),
                render : true});
        }
    }
})();
