(() => {

    "use strict";

    const bookmarksNode = document.querySelector("#bookmarks");
    if (!bookmarksNode) {
        return;
    }

    const Model = L.Model;
    const Bookmark = L.Bookmark;
    const Bookmarks = L.Bookmarks;
    const BASE_PATH = Model.get(Model.BASE_PATH) ?? "";
    const PROXY_LINKS = Model.get(Model.PROXY_LINKS);
    const MAX_ANCHOR_LENGTH = 32;

    class BookmarksWidget {

        #bookmarks;
        #displayLimit;
        #srcNode;

        constructor({ srcNode, bookmarks, displayLimit }) {
            this.#bookmarks = bookmarks;
            this.#srcNode = srcNode;
            this.#displayLimit = displayLimit;

            this.#bindEvents();
            this.#refreshUI();
        }

        /**
         * Set up event listeners to respond to events when the server has been updated
         * so the bookmark markup can change appropriately
         */
        #bindEvents = () => {
            this.#bookmarks.after("addSync", this.#bookmarkAdded);
            this.#bookmarks.after("moveSync", this.#bookmarkMoved);
            this.#bookmarks.after("removeSync", this.#bookmarksRemoved);
            this.#bookmarks.after("updateSync", this.#bookmarkUpdated);
        }

        /**
         * Set up the UI, in this case truncate text in links to MAX_ANCHOR_LENGTH characters,
         * and hide items > displayLimit.
         */
        #refreshUI() {
            this.#truncateLabels();
            this.#hideSomeItems();
            this.#showManageBookmarks();
        }

        get bookmarks() { return this.#bookmarks; }

        /**
         * Provide a text representation of this widget.
         * @returns "BookmarksWidget: " and the list of bookmarks.
         */
        toString() {
            return `BookmarksWidget:${this.get("bookmarks")}`;
        }

        /**
         * Respond to a successful bookmark add event.  Adds a list item with a link to the top of the list.
         * @private
         * @param event {CustomEvent}
         */
        #bookmarkAdded = ({ bookmark }) => {
            const li = document.createElement('li');
            const a = document.createElement('a');
            const href = PROXY_LINKS && /^https?:/.test(bookmark.url)
                ? `${BASE_PATH}/apps/proxy/credential?url=${bookmark.url}`
                : bookmark.url;

            a.href = href;
            a.textContent = bookmark.label;
            li.append(a);
            this.#srcNode.prepend(li);
            this.#refreshUI();
        }

        /**
         * Respond to a successful bookmark moved revent.  Moves the corresponding list item.
         * @private
         * @param event {CustomEvent}
         */
        #bookmarkMoved = ({ from, to }) => {
            const children = this.#srcNode.children;
            // get node references before DOM is changed
            const movedNode = children[from];
            const targetNode = children[to];

            if (to > from) {
                targetNode.after(movedNode);
            } else {
                targetNode.before(movedNode);
            }
            this.#refreshUI();
        }

        /**
         * Respond to a successful bookmarks remove event.  Removes list items of bookmarks that were deleted.
         * @private
         * @param event {CustomEvent}
         */
        #bookmarksRemoved = ({ positions }) => {
            positions.slice().reverse().forEach(pos => {
                this.#srcNode.children[pos]?.remove();
            });
            this.#refreshUI();
        }

        /**
         * Respond to a successful bookmark update event.  Alters the anchor text and/or url for a bookmark.
         * @private
         * @param event {CustomEvent}
         */
        #bookmarkUpdated = ({ position }) => {
            const bookmark = this.#bookmarks.getBookmark(position);
            const anchor = this.#srcNode.querySelectorAll("li").item(position).querySelector("a");
            anchor.innerHTML = bookmark.label;
            anchor.href = bookmark.url;
            this.#refreshUI();
        }

        /**
         * Shorten all anchor text to less than MAX_ANCHOR_LENGTH characters, append ... if shortened.
         * @private
         */
        #truncateLabels() {
            this.#srcNode.querySelectorAll("a").forEach(anchor => {
                if (anchor.textContent.length > MAX_ANCHOR_LENGTH) {
                    anchor.textContent = anchor.textContent.substring(0, MAX_ANCHOR_LENGTH) + "...";
                }
            });
        }

        /**
         * Hide items > displayLimit
         * @private;
         */
        #hideSomeItems() {
            const items = this.#srcNode.querySelectorAll("li");
            items.forEach((item, index) => {
                item.style.display = index < this.#displayLimit ? "block" : "none";
            });
        }

        /**
         * Show manage bookmarks link when more items than displayLimit
         * @private;
         */
        #showManageBookmarks() {
            const items = this.#srcNode.querySelectorAll("li");
            const manageBookmarks = document.querySelector(".manageBookmarks");

            if (manageBookmarks) {
                manageBookmarks.style.display = items.length === 0 || items.length > this.#displayLimit ? 'block' : 'none';
                if (items.length === 0) {
                    manageBookmarks.textContent = 'Add a Bookmark';
                }
            }
        }
    }

    // create a new widget and keep a global reference to it
    const initialBookmarks = Array.from(bookmarksNode.querySelectorAll("a")).map(anchor => {
        const linkInfo = new L.LinkInfo(anchor);
        const label = linkInfo.title;
        const url = linkInfo.local ? (linkInfo.path + (linkInfo.query ?? "")) : linkInfo.url;
        return new Bookmark(label, url);
    });

    const bookmarks = new Bookmarks(initialBookmarks);

    L.BookmarksWidget = new BookmarksWidget({ srcNode: bookmarksNode, bookmarks: bookmarks, displayLimit: 10 });

})();
