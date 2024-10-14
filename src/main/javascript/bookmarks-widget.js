(function () {

    "use strict";

    let
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
    class BookmarksWidget {
        constructor(args) {
            this.items = ["li"];
            this.bookmarks = args.bookmarks;
            this.srcNode = args.srcNode;
            this.displayLimit = args.displayLimit;
            this.events = {};

            this.bindUI();
            this.syncUI();
        }

        /**
         * Set up event listeners to respond to events when the server has been updated
         * so the bookmark markup can change appropriately
         * @method bindUI
         */
        bindUI() {
            this.bookmarks.on("addSync", (e) => this._bookmarkAdded(e));
            this.bookmarks.on("moveSync", (e) => this._bookmarkMoved(e));
            this.bookmarks.on("removeSync", (e) => this._bookmarksRemoved(e));
            this.bookmarks.on("updateSync", (e) => this._bookmarkUpdated(e));
        }

        /**
         * Set up the UI, in this case truncate text in links to 32 characters,
         * and hide items > displayLimit.
         * @method syncUI
         */
        syncUI() {
            this._truncateLabels();
            this._hideSomeItems();
            this._showManageBookmarks();
        }
        /**
         * Provide a text representation of this widget.
         * @method toString
         * @returns "BookmarksWidget: " and the list of bookmarks.
         */
        toString() {
            return "BookmarksWidget:" + this.get("bookmarks");
        }

        /**
         * Respond to a successful bookmark add event.  Adds a list item with a link to the top of the list.
         * @method _bookmarkAdded
         * @private
         * @param event {CustomEvent}
         */
        _bookmarkAdded(event) {
            let url = event.bookmark.getUrl();
            if (PROXY_LINKS && url.match(/^http[s]?:/)) {
                url = BASE_PATH + "/apps/proxy/credential?url=" + url;
            }
            const li = document.createElement('li');
            const a = document.createElement('a');
            a.href = url;
            a.textContent = event.bookmark.getLabel();
            li.appendChild(a);
            this.srcNode.prepend(li);
            this.syncUI();
        }

        /**
         * Respond to a successful bookmark moved revent.  Moves the corresponding list item.
         * @method _bookmarkMoved
         * @private
         * @param event {CustomEvent}
         */
        _bookmarkMoved(event) {
            let srcNode = this.srcNode,
                children = srcNode.children,
                moved = children.item(event.from),
                current = children.item(event.to),
                position = event.to > event.from ? "after" : "before";
            srcNode.removeChild(moved);
            current.insert(moved, position);
            this.syncUI();
        }

        /**
         * Respond to a successful bookmarks remove event.  Removes list items of bookmarks that were deleted.
         * @method _bookmarksRemoved
         * @private
         * @param event {CustomEvent}
         */
        _bookmarksRemoved(event) {
            let i, items = this.srcNode.querySelectorAll("li");
            for (i = event.positions.length - 1; i >= 0; --i) {
                items.item(event.positions[i]).remove(true);
            }
            this.syncUI();
        }

        /**
         * Respond to a successful bookmark update event.  Alters the anchor text and/or url for a bookmark.
         * @method _bookmarkUpdated
         * @private
         * @param event {CustomEvent}
         */
        _bookmarkUpdated(event) {
            let bookmark = this.bookmarks.getBookmark(event.position),
                anchor = this.srcNode.querySelectorAll("li").item(event.position).querySelector("a");
            anchor.innerHTML = bookmark.getLabel();
            anchor.href, bookmark.getUrl();
            this.syncUI();
        }

        /**
         * Shorten all anchor text to less than 32 characters, append ... if shortened.
         * @method _truncateLabels
         * @private
         */
        _truncateLabels() {
            let i, anchor, label, anchors = this.srcNode.querySelectorAll("a");
            for (i = 0; i < anchors.length; i++) {
                anchor = anchors.item(i);
                label = anchor.innerHTML;
                if (label.length > 32) {
                    anchor.innerHTML = label.substring(0, 32) + "...";
                }
            }
        }

        /**
         * Hide items > displayLimit
         * @method _hideSomeItems()
         * @private;
         */
        _hideSomeItems() {
            let i, displayLimit = this.displayLimit, items = this.srcNode.querySelectorAll("li");
            for (i = 0; i < displayLimit && i < items.length; i++) {
                items.item(i).style.display = "block";
            }
            for (i = displayLimit; i < items.length; i++) {
                items.item(i).style.display = "none";
            }
        }

        /**
         * Show manage bookmarks link when more items than displayLimit
         * @method _showManageBookmarks()
         * @private;
         */
        _showManageBookmarks() {
            let displayLimit = this.displayLimit, items = this.srcNode.querySelectorAll("li"),
                manageBookmarks = document.querySelector(".manageBookmarks");
            if (manageBookmarks) {
                if (items.length == 0) {
                    manageBookmarks.style.display = 'block';
                    manageBookmarks.textContent = 'Add a Bookmark';
                } else if (items.length > displayLimit) {
                    manageBookmarks.style.display = 'block';
                } else if (items.length < displayLimit) {
                    manageBookmarks.style.display = 'none';
                }
            }
        }
    }




    //create a new widget and keep a global reference to it
    //may be able to use Widget.getByNode("#bookmarks") rather than the global reference . . . .
    if (document.querySelector("#bookmarks")) {
        let srcNode = document.querySelector("#bookmarks"),
            i, anchor, linkinfo, label, url, query, bookmarks = [], anchors = srcNode.querySelectorAll("a");
        for (i = 0; i < anchors.length; i++) {
            anchor = anchors.item(i);
            linkinfo = new L.LinkInfo(anchor);
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
        bookmarks = new Bookmarks(bookmarks);

        L.BookmarksWidget = new BookmarksWidget({ srcNode: srcNode, bookmarks: bookmarks, displayLimit: 10 });
    }


})();
