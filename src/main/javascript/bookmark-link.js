(function() {

    "use strict";

    let BookmarkLink,
        Model = L.Model,
        OFF = "off",
        READY = "ready",
        ACTIVE = "active",
        BOOKMARKING = "bookmarking",
        TIMING = "timing";

    //create BookmarkLink only if bookmarking = rw
    if (Model.get(Model.BOOKMARKING) === "rw") {

        /**
         * A link that appears when mousing over bookmarkable links and adds that link to bookmarks
         * when clicked.
         *
         * @class BookmarksLink
         * @requires Base
         * @requires LinkInfo
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
                        return L.BookmarksWidget ? L.BookmarksWidget.get("bookmarks") : null;
                    }
                },
                target : {
                    value : null
                },
                hideDelay : {
                    value:500
                },
                status : {
                    value : OFF
                }
        };

        Y.extend(BookmarkLink, Y.Base, {

            /**
             * Sets up event handlers and creates the timer attribute.
             * @method initializer
             */
            initializer : function() {
                this._timer = null;
                Y.delegate("mouseover", this._handleTargetMouseover,"ul.content", "a", this);
                Y.delegate("mouseout", this._handleTargetMouseout,"ul.content", "a", this);
                Y.delegate("mouseover", this._handleTargetMouseover,"section.content", "a", this);
                Y.delegate("mouseout", this._handleTargetMouseout,"section.content", "a", this);
                this.on("statusChange", this._handleStatusChange);
                let bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    bookmarks.after("addSync", this._handleSyncEvent, this);
                }
            },

            /**
             * Responds to bookmarks:addSync event, changes the status to OFF
             * @method _handleSyncEvent
             * @private
             */
            _handleSyncEvent : function() {
                this.set("status", OFF);
                // fire an added event for favorites animation
                L.fire("bookmarks:added");
            },

            /**
             * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
             * @method _handleBookmarkMouseOut
             * @private
             */
            _handleBookmarkMouseout : function() {
                this.set("status", TIMING);
            },

            /**
             * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
             * @method _handleBookmarkMouseover
             * @private
             */
            _handleBookmarkMouseover : function() {
                this.set("status", ACTIVE);
            },

            /**
             * Responds to BookmarkLink clicks.  Wraps the target link in LinkInfo and uses
             * that to determine the url (translates proxy links to the base url).  Changes the
             * status to BOOKMARKING
             * @method _handleClick
             * @private
             */
            _handleClick : function() {
                let target = this.get("target"),
                    linkinfo = new L.LinkInfo(target._node),
                    label, url, query, bookmarks;
                label = linkinfo.title;
                if (linkinfo.local) {
                    url = linkinfo.path;
                    //case 71646 local links lack query string
                    query = linkinfo.query;
                    url = query ? url + query : url;
                } else {
                    url = linkinfo.url;
                }
                this.set("status", BOOKMARKING);
                bookmarks = this.get("bookmarks");
                if (bookmarks) {
                    bookmarks.addBookmark(new L.Bookmark(label, url));
                } else {
                    L.BookmarkLogin.addBookmark(label, url);
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
                    this.set("status", TIMING);
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
                    this.set("status", READY);
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
                let url, bookmarks, query,
                    linkinfo = new L.LinkInfo(target._node);
                if (linkinfo.local) {
                    url = linkinfo.path;
                    query = linkinfo.query;
                    url = query ? url + query : url;
                } else {
                    url = linkinfo.url;
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
                this.set("status", OFF);
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
                let node = this.get("node"), target = this.get("target");
                //IE messes up the event handling if set up on initialization
                //so purging and selectively set them when the status changes.
                node.purge(false);
                switch(event.newVal) {
                //OFF: not visible
                case OFF :
                    node.remove(false);
                    node.removeClass("active");
                    node.removeClass("bookmarking");
                    break;
                //READY: visible but not enabled
                case READY :
                    target.insert(node, "after");
                    break;
                //ACTIVE: enabled (mouseover)
                case ACTIVE :
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.on("click", this._handleClick, this);
                    node.addClass("active");
                    break;
                //BOOKMARKING: clicked and waiting for server sync message
                case BOOKMARKING :
                    node.on("mouseout", this._handleBookmarkMouseout, this);
                    node.replaceClass("active", "bookmarking");
                    break;
                //TIMING: waiting to hide
                case TIMING :
                    node.on("mouseover",this._handleBookmarkMouseover, this);
                    node.removeClass("active");
                    node.removeClass("bookmarking");
                    this._timer = Y.later(this.get("hideDelay"), this, this._turnOff);
                    break;
                default:
                }
            }
        });

        //create a BookmarkLink and save reference
        L.BookmarkLink = new BookmarkLink();
    }
})();
