(function() {

    "use strict";

    var BookmarkLink,
        Lane = Y.lane,
        Model = Lane.Model;

    //don't create BookmarkLink if in disaster mode
    if (!Model.get(Model.DISASTER_MODE)) {

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
                        return Y.Node.create("<span title='Add to My Bookmarks' class='bookmark-link'><i class='fa fa-star'></i></span>");
                    }
                },
                bookmarks : {
                    valueFn : function() {
                        return Lane.BookmarksWidget ? Lane.BookmarksWidget.get("bookmarks") : null;
                    }
                },
                target : {
                    value : null
                },
                hideDelay : {
                    value:500
                },
                status : {
                    value : 0 //BookmarkLink.OFF
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
                this.on("statusChange", this._handleStatusChange);
                var bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    bookmarks.after("addSync", this._handleSyncEvent, this);
                }
            },

            /**
             * Responds to bookmarks:addSync event, changes the status to SUCCESSFUL
             * @method _handleSyncEvent
             * @private
             */
            _handleSyncEvent : function() {
                this.set("status", BookmarkLink.SUCCESSFUL);
            },

            /**
             * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
             * @method _handleBookmarkMouseOut
             * @private
             */
            _handleBookmarkMouseout : function() {
                this.set("status", BookmarkLink.TIMING);
            },

            /**
             * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
             * @method _handleBookmarkMouseover
             * @private
             */
            _handleBookmarkMouseover : function() {
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
                var target = this.get("target"), label, url, query, bookmarks;
                target.plug(Lane.LinkPlugin);
                label = target.link.get("title");
                if (target.link.get("local")) {
                    url = target.link.get("path");
                    //case 71646 local links lack query string
                    query = target.link.get("query");
                    url = query ? url + query : url;
                } else {
                    url = target.link.get("url");
                }
                this.set("status", BookmarkLink.BOOKMARKING);
                bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    bookmarks.addBookmark(new Lane.Bookmark(label, url));
                } else {
                    Y.lane.BookmarkLogin.addBookmark(label, url);
                }
            },

            /**
             * Handle clicks on the bookmarkSearch link.
             * @method _handleBookmarkSearchClick
             * @private
             */
            _handleBookmarkSearchClick : function(event) {
                var encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
                    encodedSource = Model.get(Model.URL_ENCODED_SOURCE),
                    label = "Search for: " + decodeURIComponent(encodedQuery),
                    url = "/search.html?source=" + encodedSource + "&q=" + encodedQuery,
                    bookmarkSearch = event.currentTarget,
                    eventHandle = null,
                    bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    bookmarkSearch.addClass("bookmarking");
                    eventHandle = bookmarks.after("addSync", function() {
                        bookmarkSearch.removeClass("active");
                        bookmarkSearch.removeClass("bookmarking");
                        eventHandle.detach();
                    }, this);
                    bookmarks.addBookmark(new Lane.Bookmark(label, url));
                } else {
                    Y.lane.BookmarkLogin.addBookmark(label, url);
                }
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
             * Determine if a link has already been bookmarked. (case 71323)
             * @method _isAlreadyBookmarked
             * @private
             * @param target the target anchor
             * @returns {Boolean}
             */
            _isAlreadyBookmarked : function(target) {
                var url, bookmarks, query;
                target.plug(Lane.LinkPlugin);
                if (target.link.get("local")) {
                    url = target.link.get("path");
                    query = target.link.get("query");
                    url = query ? url + query : url;
                } else {
                    url = target.link.get("url");
                }
                bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    return bookmarks.hasURL(url);
                }
                return false;
            },

            /**
             * Determine if a link is bookmarkable.  For now true if its display property is inline
             * or inline-block and it does not contain an img element.
             * Added logic for if link was already bookmarked case 75199
             * added logic for using class="no-bookmarking" for individual nodes and descendants for
             * case 101724
             * 2/4/15 added bookmarkable = false if no href
             * @method _isBookmarkable
             * @private
             * @param target the target anchor
             * @returns {Boolean}
             */
            _isBookmarkable : function(target) {
                return target.get("href")
                    && target.getStyle("display").indexOf("inline") === 0
                    && !target.one("img")
                    && !target.ancestor(".no-bookmarking", true)
                    && !this._isAlreadyBookmarked(target);
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
                    break;
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
        Lane.BookmarkLink = new BookmarkLink();
    }
})();
