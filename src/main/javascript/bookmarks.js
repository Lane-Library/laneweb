(() => {

    "use strict";

    const Model = L.Model;
    const BASE_PATH = Model.get(Model.BASE_PATH) ?? "";

    /**
     * The `Bookmarks` class provides functionality to manage a collection of bookmarks.
     * It supports adding, removing, moving, and updating bookmarks, as well as syncing
     * these operations with the server. The class emits custom events for each operation,
     * allowing external listeners to respond to changes in the bookmarks collection.
     *
     * @fires bookmarks:add - Fired when a bookmark is added.
     * @fires bookmarks:addSync - Fired after a bookmark is successfully synced with the server.
     * @fires bookmarks:move - Fired when a bookmark is moved.
     * @fires bookmarks:moveSync - Fired after a bookmark move is successfully synced with the server.
     * @fires bookmarks:remove - Fired when bookmarks are removed.
     * @fires bookmarks:removeSync - Fired after bookmarks are successfully removed from the server.
     * @fires bookmarks:update - Fired when a bookmark is updated.
     * @fires bookmarks:updateSync - Fired after a bookmark update is successfully synced with the server.
     */
    class Bookmarks {

        #bookmarks;

        // --- Constructor and Initialization ---
        constructor(bookmarks = []) {
            if (!Array.isArray(bookmarks)) {
                throw new Error("Bookmarks constructor requires an array.");
            }

            this.#bookmarks = bookmarks;
            this.#bookmarks.forEach(bookmark =>
                bookmark.after("valueChange", this.#handleValueChange)
            );

            //Add EventTarget attributes to the Bookmarks prototype
            L.addEventTarget(this, {
                prefix: 'bookmarks'
            });

            this.#bindEvents();

        }

        /**
         * Binds the default event listeners for the class instance.
         * @private
         */
        #bindEvents = () => {
            /** @event add @description Fired when a bookmark is added. */
            this.on("add", this.#defAddFn);
            /** @event addSync @description Fired after an add is successfully synced. */
            this.on("addSync", this.#handleAddSync);
            /** @event move @description Fired when a bookmark is moved. */
            this.on("move", this.#defMoveFn);
            /** @event moveSync @description Fired when a move is successfully synced. */
            this.on("moveSync", this.#handleMoveSync);
            /** @event remove @description Fired when a bookmark is removed. */
            this.on("remove", this.#defRemoveFn);
            /** @event removeSync @description Fired when a removal is successfully synced. */
            this.on("removeSync", this.#handleRemoveSync);
            /** @event update @description Fired when a bookmark is updated. */
            this.on("update", this.#defUpdateFn);
        }

        // --- Public Methods ---
        /**
         * Fires a bookmark:add event.
         * @param {L.Bookmark} bookmark
         */
        addBookmark(bookmark) {
            if (bookmark instanceof L.Bookmark) {
                this.fire("add", { bookmark });
            } else {
                throw new Error("Invalid object passed to addBookmark. Expected a Bookmark instance.");
            }
        }

        /**
         * @param {number} position
         * @returns {L.Bookmark|undefined} The bookmark at the given position.
         */
        getBookmark(position) {
            return this.#bookmarks[position];
        }

        /**
         * Checks if a bookmark with the given URL already exists.
         * @param {string} url
         * @returns {boolean}
         */
        hasURL(url) {
            return this.#bookmarks.some(bookmark => bookmark.url === url);
        }

        /**
         * @param {L.Bookmark} bookmark
         * @returns {number} The index of the given bookmark, or -1 if not found.
         */
        indexOf(bookmark) {
            return this.#bookmarks.indexOf(bookmark);
        }

        /**
         * @param {number} to - Where the bookmark goes to.
         * @param {number} from - Where the bookmark comes from.
         */
        moveBookmark(to, from) {
            this.fire("move", { to, from });
        }

        /**
         * Fires a bookmark:remove event.
         * @param {number[]} positions - The indices of the bookmarks to remove.
         */
        removeBookmarks(positions) {
            this.fire("remove", { positions });
        }

        /**
         * @returns {number} The number of bookmarks.
         */
        size() {
            return this.#bookmarks.length;
        }

        /**
         * Fires a bookmark:update event
         * @param {L.Bookmark} bookmark
         */
        updateBookmark(bookmark) {
            const position = this.indexOf(bookmark);
            if (position > -1) {
                this.fire("update", { bookmark, position });
            }
        }

        /**
         * @returns {string} A string representation of the bookmarks collection.
         */
        toString() {
            return `Bookmarks[${this.#bookmarks.join(",")}]`;
        }

        // --- Default Event Handlers (for API interaction) ---

        /**
         * The default response to bookmarks:add, attempts to sync with server, fires
         * bookmarks:addSync.
         * @private
         * @param {CustomEvent} event
         */
        #defAddFn = async (event) => {
            try {
                await this.#fetchAPI('/bookmarks', {
                    method: "POST",
                    body: JSON.stringify({ label: event.bookmark.label, url: event.bookmark.url })
                });
                this.fire("addSync", { success: true, bookmark: event.bookmark, target: event.target });
            } catch (error) {
                this.#handleSyncFailure("add", error);
            }
        }

        /**
         * The default response to bookmarks:move, attempts to sync the move with the
         * server, fires bookmarks:moveSync if successful
         * @private
         * @param {CustomEvent} event
         */
        #defMoveFn = async (event) => {
            try {
                await this.#fetchAPI('/bookmarks/move', {
                    method: "POST",
                    body: JSON.stringify({ to: event.to, from: event.from })
                });
                this.fire("moveSync", { success: true, to: event.to, from: event.from });
            } catch (error) {
                this.#handleSyncFailure("move", error);
            }
        }

        /**
         * The default response to bookmarks:remove, attempts to sync with server,
         * fires bookmarks:removeSync if successful
         * @private
         * @param {CustomEvent} event
         */
        #defRemoveFn = async (event) => {
            const indexes = JSON.stringify(event.positions);
            try {
                await this.#fetchAPI(`/bookmarks?indexes=${encodeURIComponent(indexes)}`, {
                    method: "DELETE"
                });
                this.fire("removeSync", { success: true, positions: event.positions });
            } catch (error) {
                this.#handleSyncFailure("delete", error);
            }
        }

        /**
         * The default response to bookmarks:update, attempts to sync with server,
         * fires bookmarks:updateSync if successful.
         * @private
         * @param {CustomEvent} event
         */
        #defUpdateFn = async (event) => {
            try {
                await this.#fetchAPI('/bookmarks', {
                    method: "PUT",
                    body: JSON.stringify({ position: event.position, label: event.bookmark.label, url: event.bookmark.url })
                });
                this.fire("updateSync", { success: true, position: event.position });
            } catch (error) {
                this.#handleSyncFailure("update", error);
            }
        }

        // --- Private Utility Methods ---

        /**
         * handler for bookmark:valueChange events
         * @private
         * @param {CustomEvent} event
         */
        #handleValueChange = (event) => {
            this.updateBookmark(event.target);
        }

        /**
         * handler for bookmarks:addSync event, adds a bookmark to index 0 of the
         * backing Array, also fires a tracking event
         * @private
         * @param {CustomEvent} event
         */
        #handleAddSync = ({ bookmark }) => {
            bookmark.after("valueChange", this.#handleValueChange);
            this.#bookmarks.unshift(bookmark);
            L.fire("tracker:trackableEvent", {
                category: "lane:bookmarkAdd",
                action: Model.get(Model.AUTH),
                label: bookmark.label
            });
        }

        /**
         * handler from bookmarks:moveSync event, moves a bookmark.
         * @private
         * @param {CustomEvent} event
         */
        #handleMoveSync = ({ to, from }) => {
            this.#bookmarks.splice(to, 0, this.#bookmarks.splice(from, 1)[0]);
        }

        /**
         * handler for bookmarks:removeSync event, removes bookmarks from the
         * backing Array
         * @private
         * @param {CustomEvent} event
         */
        #handleRemoveSync = ({ positions }) => {
            positions.reverse().forEach(pos => this.#bookmarks.splice(pos, 1));
        }

        /**
         * handler for sync failures, shows a message.
         * @param {String} action - The action that failed (add, move, delete, update)
         * @param {Error} error - The error that occurred
         */
        #handleSyncFailure = (action, error) => {
            console.error(`Failed to ${action}.`, error);
            L.showMessage(`Sorry, ${action} bookmark failed. Please reload the page and try again later.`);
        }

        /**
         * A utility function to handle API requests
         * @param {string} endpoint - The URL path for the request.
         * @param {object} options - The options object for the fetch call (method, body, etc.).
         * @returns {Promise<object>} A promise that resolves with the parsed JSON response.
         * @private
         */
        #fetchAPI = async (endpoint, options = {}) => {
            const url = `${BASE_PATH}${endpoint}`;
            const defaultHeaders = { "Content-Type": "application/json" };

            const response = await fetch(url, {
                ...options, // merge passed options
                headers: { ...defaultHeaders, ...options.headers }
            });

            if (!response.ok) {
                throw new Error(`API request failed: ${response.status} ${response.statusText}`);
            }

            // Return JSON response if available
            const contentType = response.headers.get("content-type");
            if (contentType?.includes("application/json")) {
                return response.json();
            }
        }

    }

    //make the Bookmarks constructor globally accessible
    L.Bookmarks = Bookmarks;

})();
