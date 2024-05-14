(function() {

    "use strict";

    let Bookmarks,
        Model = L.Model,
        BASE_PATH = Model.get(Model.BASE_PATH) || "";

    /**
     * A class for representing an ordered collection of Bookmarks.
     * It fires events when the collection changes
     *
     * @class Bookmarks
     * @requires EventTarget
     * @constructor
     * @param bookmarks {array} may be undefined
     */
    Bookmarks = function(bookmarks) {
        let i;
        this._bookmarks = [];
        if (bookmarks && !(bookmarks instanceof Array)) {
            throw("bad config");
        }
        if (bookmarks) {
            for (i = 0; i < bookmarks.length; i++) {
                bookmarks[i].after("valueChange", this._handleValueChange, this);
                this._bookmarks.push(bookmarks[i]);
            }
        }

        /**
         * @event add
         * @description Fired when a bookmark is added.
         */
        this.publish("add", {defaultFn: this._defAddFn});

        /**
         * @event addSync
         * @description fired after an add is successfully synced with the server
         */
        this.publish("addSync", {defaultFn: this._handleAddSync, preventable : false});

        /**
         * @event move
         * @description Fired when a bookmark is moved
         */
        this.publish("move", {defaultFn : this._defMoveFn});

        /**
         * @event moveSync
         * @description Fired when a move is successfully synced with the server
         */
        this.publish("moveSync", {defaultFn : this._handleMoveSync, preventable : false});

        /**
         * @event remove
         * @description Fired when a bookmark is removed.
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
         */
        this.publish("update", {defaultFn: this._defUpdateFn});

        /**
         * @event updateSync
         * @description fired when an update is successfully synced with the server
         */
        this.publish("updateSync", {preventable : false});
    };

    Bookmarks.prototype = {

            /**
             * fires a bookmark:add event
             * @method addBookmark
             * @param bookmark {Bookmark}
             */
            addBookmark : function(bookmark) {
                if (bookmark instanceof L.Bookmark) {
                    this.fire("add", {bookmark : bookmark});
                } else {
                    throw ("bad bookmark");
                }
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
             * @method moveBookmark
             * @param to {number} where the bookmark goes to
             * @param from {number} where the bookmark comes from
             */
            moveBookmark : function(to, from) {
                this.fire("move", {to : to, from : from});
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
                let position = this._bookmarks.indexOf(bookmark);
                this.fire("update", {bookmark : bookmark, position : position});
            },

            /**
             * @method size
             * @returns {number} the number of bookmarks
             */
            size : function() {
                return this._bookmarks.length;
            },

            hasURL : function(url) {
                for (let i = 0; i < this._bookmarks.length; i++) {
                    if (url === this._bookmarks[i].getUrl()) {
                        return true;
                    }
                }
                return false;
            },

            /**
             * @method indexOf
             * @param bookmark {Bookmark}
             * @return {number} the index of the given bookmark
             */
            indexOf : function(bookmark) {
                return this._bookmarks.indexOf(bookmark);
            },

            /**
             * @method toString
             * @returns {String} a string representation
             */
            toString : function() {
                let string = "Bookmarks[";
                for (let i = 0; i < this._bookmarks.length; i++) {
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
                let data = JSON.stringify({label : event.bookmark.getLabel(), url : event.bookmark.getUrl()});
                L.io(BASE_PATH + "/bookmarks", {
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
                            this._handleSyncFailure("add");
                        }
                    },
                    "arguments" : {
                        bookmark : event.bookmark
                    },
                    context : this
                });
            },

            /**
             * The default response to bookmarks:move, attempts to sync the move with the
             * server, fires bookmarks:moveSync if successful
             * @method _defMoveFn
             * @private
             * @param event {CustomEvent}
             */
            _defMoveFn : function(event) {
                let data = JSON.stringify({to : event.to, from : event.from});
                L.io(BASE_PATH + "/bookmarks/move", {
                    method : "post",
                    data : data,
                    headers : {
                        "Content-Type" : "application/json"
                    },
                    on : {
                        success : function() {
                            this.fire("moveSync", {success : true, to : event.to, from : event.from});
                        },
                        failure : function() {
                            this._handleSyncFailure("move");
                        }
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
                let indexes = JSON.stringify(event.positions);
                L.io(BASE_PATH + "/bookmarks?indexes=" + encodeURIComponent(indexes), {
                    method : "delete",
                    on : {
                        success : function() {
                            this.fire("removeSync", {success: true, positions : event.positions});
                        },
                        failure : function() {
                            this._handleSyncFailure("delete");
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
                let data = JSON.stringify({position : event.position, label : event.bookmark.getLabel(), url : event.bookmark.getUrl()});
                L.io(BASE_PATH + "/bookmarks", {
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
                            this._handleSyncFailure("update");
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
             * handler for bookmarks:addSync event, adds a bookmark to index 0 of the
             * backing Array, also fires a tracking event
             * @method _handleAddSync
             * @private
             * @param event {CustomEvent}
             */
            _handleAddSync : function(event) {
                event.bookmark.after("valueChange", this._handleValueChange, this);
                this._bookmarks.unshift(event.bookmark);
                L.fire("tracker:trackableEvent", {
                    category: "lane:bookmarkAdd",
                    action: Model.get(Model.AUTH),
                    label: event.bookmark.getLabel()
                });
            },

            /**
             * handler from bookmarks:moveSync event, moves a bookmark.
             * @method _handleMoveSync
             * @private
             * @param event {CustomEvent}
             */
            _handleMoveSync : function(event) {
                this._bookmarks.splice(event.to, 0, this._bookmarks.splice(event.from, 1)[0]);
            },

            /**
             * handler for bookmarks:removeSync event, removes bookmarks from the
             * backing Array
             * @method _handleRemoveSync
             * @private
             * @param event {CustomEvent}
             */
            _handleRemoveSync : function(event) {
                for (let i = event.positions.length - 1; i >=0; --i) {
                    this._bookmarks.splice(event.positions[i], 1);
                }
            },

            /**
             * handler for sync failures, shows a message.
             * @param message {String}
             */
            _handleSyncFailure : function(message) {
                L.showMessage("Sorry, " + message + " bookmark failed. Please reload the page and try again later.");
            }
    };


    //Add EventTarget attributes to the Bookmarks prototype
    L.addEventTarget(Bookmarks, {
        emitFacade : true,
        prefix     : 'bookmarks'
    });

    //make the Bookmarks constructor globally accessible
    L.Bookmarks = Bookmarks;
})();
