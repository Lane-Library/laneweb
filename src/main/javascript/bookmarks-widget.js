(function() {

    "use strict";

    var BookmarksWidget,
        Bookmark = L.Bookmark,
        Bookmarks = L.Bookmarks,
        Model = L.Model,
        BASE_PATH = Model.get(Model.BASE_PATH) || "",
        PROXY_LINKS = Model.get(Model.PROXY_LINKS);

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
            bookmarks.after("moveSync", this._bookmarkMoved, this);
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
            this._showManageBookmarks();
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
            var url = event.bookmark.getUrl();
            if (PROXY_LINKS && url.match(/^http[s]?:/)) {
                url = BASE_PATH + "/apps/proxy/credential?url=" + url;
            }
            this.get("srcNode").prepend("<li><a href='" +url + "'>" + event.bookmark.getLabel() + "</a></li>");
            this.syncUI();
        },

        /**
         * Respond to a successful bookmark moved revent.  Moves the corresponding list item.
         * @method _bookmarkMoved
         * @private
         * @param event {CustomEvent}
         */
        _bookmarkMoved : function(event) {
            var srcNode = this.get("srcNode"),
                children = srcNode.get("children"),
                moved = children.item(event.from),
                current = children.item(event.to),
                position = event.to > event.from ? "after" : "before";
            srcNode.removeChild(moved);
            current.insert(moved, position);
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
        },

        /**
         * Show manage bookmarks link when more items than displayLimit
         * @method _showManageBookmarks()
         * @private;
         */
        _showManageBookmarks: function() {
            var displayLimit = this.get("displayLimit"), items = this.get("srcNode").all("li"),
                manageBookmarks = document.querySelector(".manageBookmarks");
            if (manageBookmarks) {
                if (items.size() == 0) {
                    manageBookmarks.style.display = 'block';
                    manageBookmarks.textContent = 'Add a Bookmark';
                } else if (items.size() > displayLimit) {
                    manageBookmarks.style.display = 'block';
                } else if (items.size() < displayLimit) {
                    manageBookmarks.style.display = 'none';
                }
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
                var i, anchor, linkinfo, label, url, query, bookmarks = [], anchors = srcNode.all("a");
                for (i = 0; i < anchors.size(); i++) {
                    anchor = anchors.item(i);
                    linkinfo = new L.LinkInfo(anchor._node);
                    label = linkinfo.title;
                    if (linkinfo.local) {
                        url = linkinfo.path;
                        query = linkinfo.query;
                        url = query ? url + query : url;
                    } else {
                        url = linkinfo.url;
                    }
                    bookmarks.push(new Bookmark(label, url));
                }
                return new Bookmarks(bookmarks);
            }
        }
    });

    //create a new widget and keep a global reference to it
    //may be able to use Widget.getByNode("#bookmarks") rather than the global reference . . . .
    if (document.querySelector("#bookmarks")) {
        L.BookmarksWidget = new BookmarksWidget({srcNode:"#bookmarks", render:true, displayLimit:10});
    }

})();
