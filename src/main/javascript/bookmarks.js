(function () {

    "use strict";

    let Model = L.Model,
        BookmarkEvent = L.BookmarkEvent,
        BASE_PATH = Model.get(Model.BASE_PATH) || "";

    class Bookmarks extends L.BookmarkEvent {
        constructor(bookmarks) {
            super();
            let i;
            this._bookmarks = [];
            if (bookmarks && !(bookmarks instanceof Array)) {
                throw ("bad config");
            }
            if (bookmarks) {
                for (i = 0; i < bookmarks.length; i++) {
                    bookmarks[i].on("valueChange", (e) => this._handleValueChange(e));
                    this._bookmarks.push(bookmarks[i]);
                }
            }

            /**
             * @event add
             * @description Fired when a bookmark is added.
             */
            this.on("add", (e) => this._defAddFn(e));

            /**
             * @event addSync
             * @description emitd after an add is successfully synced with the server
             */
            this.first("addSync", (e) => this._handleAddSync(e));

            /**
             * @event move
             * @description Fired when a bookmark is moved
             */
            this.on("move", (e) => this._defMoveFn(e));

            /**
             * @event moveSync
             * @description Fired when a move is successfully synced with the server
             */
            this.on("moveSync", (e) => this._handleMoveSync(e));

            /**
             * @event remove
             * @description Fired when a bookmark is removed.
             */
            this.on("remove", (e) => this._defRemoveFn(e));

            /**
             * @event removeSync
             * @description emitd when a removal is successfully synced with the server
             */
            this.on("removeSync", (e) => this._handleRemoveSync(e));

            /**
             * @event update
             * @description Fired when a bookmark is updated.
             */
            this.on("update", (e) => this._defUpdateFn(e));

            /**
             * @event updateSync
             * @description emitd when an update is successfully synced with the server
             */
            // this.on("updateSync", { preventable: false });
        }



        /**
         * emits a bookmark:add event
         * @method addBookmark
         * @param bookmark {Bookmark}
         */
        addBookmark(bookmark) {
            if (bookmark instanceof L.Bookmark) {
                this.emit("add", { bookmark: bookmark });
            } else {
                throw ("bad bookmark");
            }
        }

        /**
         * @method getBookmark
         * @param position {number}
         * @returns the bookmark at the position
         */
        getBookmark(position) {
            return this._bookmarks[position];
        }

        /**
         * @method moveBookmark
         * @param to {number} where the bookmark goes to
         * @param from {number} where the bookmark comes from
         */
        moveBookmark(to, from) {
            this.emit("move", { to: to, from: from });
        }

        /**
         * emits a bookmark:remove event
         * @method removeBookmarks
         * @param positions {Array} the bookmarks to remove
         */
        removeBookmarks(positions) {
            this.emit("remove", { positions: positions });
        }

        /**
         * emits a bookmark:update event
         * @method updateBookmark
         * @param bookmark {Bookmark}
         */
        updateBookmark(bookmark) {
            let position = this._bookmarks.indexOf(bookmark);
            this.emit("update", { bookmark: bookmark, position: position });
        }

        /**
         * @method size
         * @returns {number} the number of bookmarks
         */
        size() {
            return this._bookmarks.length;
        }

        hasURL(url) {
            for (let i = 0; i < this._bookmarks.length; i++) {
                if (url === this._bookmarks[i].getUrl()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @method indexOf
         * @param bookmark {Bookmark}
         * @return {number} the index of the given bookmark
         */
        indexOf(bookmark) {
            return this._bookmarks.indexOf(bookmark);
        }

        /**
         * @method toString
         * @returns {String} a string representation
         */
        toString() {
            let string = "Bookmarks[";
            for (let i = 0; i < this._bookmarks.length; i++) {
                string += this._bookmarks[i];
                if (i < this._bookmarks.length - 1) {
                    string += ",";
                }
            }
            string += "]";
            return string;
        }



        /**
         * The default response to bookmarks:add, attempts to sync with server, emits
         * bookmarks:addSync.
         * @method _defAddFn
         * @private
         * @param event {CustomEvent}
         */
        _defAddFn(event) {
            let data = JSON.stringify({ label: event.bookmark.getLabel(), url: event.bookmark.getUrl() });
            L.io(BASE_PATH + "/bookmarks", {
                method: "post",
                data: data,
                headers: {
                    "Content-Type": "application/json"
                },
                on: {
                    success() {
                        this.emit("addSync", { success: true, bookmark: event.bookmark, target: this });
                    },
                    failure() {
                        this._handleSyncFailure("add");
                    }
                },
                "arguments": {
                    bookmark: event.bookmark
                },
                context: this
            });
        }

        /**
         * The default response to bookmarks:move, attempts to sync the move with the
         * server, emits bookmarks:moveSync if successful
         * @method _defMoveFn
         * @private
         * @param event {CustomEvent}
         */
        _defMoveFn(event) {
            let data = JSON.stringify({ to: event.to, from: event.from });
            L.io(BASE_PATH + "/bookmarks/move", {
                method: "post",
                data: data,
                headers: {
                    "Content-Type": "application/json"
                },
                on: {
                    success() {
                        this.emit("moveSync", { success: true, to: event.to, from: event.from });
                    },
                    failure() {
                        this._handleSyncFailure("move");
                    }
                },
                context: this
            });
        }

        /**
         * The default response to bookmarks:remove, attempts to sync with server,
         * emits bookmarks:removeSync if successful
         * @method _defRemoveFn
         * @private
         * @param event {CustomEvent}
         */
        _defRemoveFn(event) {
            let indexes = JSON.stringify(event.positions);
            L.io(BASE_PATH + "/bookmarks?indexes=" + encodeURIComponent(indexes), {
                method: "delete",
                on: {
                    success() {
                        this.emit("removeSync", { success: true, positions: event.positions });
                    },
                    failure() {
                        this._handleSyncFailure("delete");
                    }
                },
                "arguments": {
                    positions: event.positions
                },
                context: this
            });
        }

        /**
         * The default response to bookmarks:update, attempts to sync with server,
         * emits bookmarks:updateSync if successful.
         * @method _defUpdateFn
         * @private
         * @param event {CustomEvent}
         */
        _defUpdateFn(event) {
            let data = JSON.stringify({ position: event.position, label: event.bookmark.getLabel(), url: event.bookmark.getUrl() });
            L.io(BASE_PATH + "/bookmarks", {
                method: "put",
                data: data,
                headers: {
                    "Content-Type": "application/json"
                },
                on: {
                    success() {
                        this.emit("updateSync", { success: true, position: event.position });
                    },

                    failure() {
                        this._handleSyncFailure("update");
                    }
                },
                "arguments": {
                    position: event.position
                },
                context: this
            });
        }




        /**
         * handler for bookmark:valueChange events
         * @method _handleValueChange
         * @private
         * @param event {CustomEvent}
         */
        _handleValueChange(event) {
            this.updateBookmark(event.target);
        }

        /**
         * handler for bookmarks:addSync event, adds a bookmark to index 0 of the
         * backing Array, also emits a tracking event
         * @method _handleAddSync
         * @private
         * @param event {CustomEvent}
         */
        _handleAddSync(event) {
            event.bookmark.on("valueChange", (e) => this._handleValueChange(e));
            this._bookmarks.unshift(event.bookmark);
            L.fire("tracker:trackableEvent", {
                category: "lane:bookmarkAdd",
                action: Model.get(Model.AUTH),
                label: event.bookmark.getLabel()
            });
        }

        /**
         * handler from bookmarks:moveSync event, moves a bookmark.
         * @method _handleMoveSync
         * @private
         * @param event {CustomEvent}
         */
        _handleMoveSync(event) {
            this._bookmarks.splice(event.to, 0, this._bookmarks.splice(event.from, 1)[0]);
        }

        /**
         * handler for bookmarks:removeSync event, removes bookmarks from the
         * backing Array
         * @method _handleRemoveSync
         * @private
         * @param event {CustomEvent}
         */
        _handleRemoveSync(event) {
            for (let i = event.positions.length - 1; i >= 0; --i) {
                this._bookmarks.splice(event.positions[i], 1);
            }
        }

        /**
         * handler for sync failures, shows a message.
         * @param message {String}
         */
        _handleSyncFailure(message) {
            L.showMessage("Sorry, " + message + " bookmark failed. Please reload the page and try again later.");
        }

    }

    //Add EventTarget attributes to the Bookmarks prototype
    L.addEventTarget(Bookmarks, {
        emitFacade: true,
        prefix: 'bookmarks'
    });

    //make the Bookmarks constructor globally accessible
    L.Bookmarks = Bookmarks;

})();
