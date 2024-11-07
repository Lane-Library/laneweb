(function () {

    "use strict";

    let Model = L.Model,
        OFF = "off",
        READY = "ready",
        ACTIVE = "active",
        BOOKMARKING = "bookmarking",
        TIMING = "timing";

    //create BookmarkLink only if bookmarking = rw
    if (Model.get(Model.BOOKMARKING) === "rw") {

        class BookmarkLink {

            constructor() {
                const span = document.createElement('span');
                span.title = 'Add to My Bookmarks';
                span.className = 'bookmark-link';
                const icon = document.createElement('i');
                icon.className = 'fa fa-star';
                span.appendChild(icon);
                this.node = span;
                this.bookmarks = L.BookmarksWidget ? L.BookmarksWidget.bookmarks : null;
                this.target = null;
                this.status = OFF;
                this.hideDelay = 500;
                this.bindUI();
                this.initializer();
                this._timer = null;
            }

            bindUI() {
                L.addEventTarget(this, {
                    prefix: 'bookmarkLink'
                });
            }

            /**
             * Sets up event handlers and creates the timer attribute.
             * @method initializer
             */
            initializer() {
                const ulContent = document.querySelector("ul.content"),
                    sectionContent = document.querySelector("section.content");
                if (ulContent) {
                    ulContent.addEventListener("mouseover", function (event) {
                        if (event.target.matches("a")) {
                            this._handleTargetMouseover(event);
                        }
                    }.bind(this));
                    ulContent.addEventListener("mouseout", function (event) {
                        if (event.target.matches("a")) {
                            this._handleTargetMouseout(event);
                        }
                    }.bind(this));
                }
                if (sectionContent) {
                    sectionContent.addEventListener("mouseover", function (event) {
                        if (event.target.matches("a")) {
                            this._handleTargetMouseover(event);
                        }
                    }.bind(this));
                    sectionContent.addEventListener("mouseout", function (event) {
                        if (event.target.matches("a")) {
                            this._handleTargetMouseout(event);
                        }
                    }.bind(this));
                }
                this.on("statusChange", this._handleStatusChange);
                let bookmarks = this.bookmarks;
                if (bookmarks) {
                    bookmarks.on("addSync", (e) => this._handleSyncEvent(e));
                }
            }


            setStatus(status) {
                this.status = status;
                this.fire("statusChange", { newVal: status });
            }

            /**
             * Responds to bookmarks:addSync event, changes the status to OFF
             * @method _handleSyncEvent
             * @private
             */
            _handleSyncEvent() {
                this.setStatus(OFF);
                // fire an added event for favorites animation
                L.fire("bookmarks:added");
            }

            /**
             * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
             * @method _handleBookmarkMouseOut
             * @private
             */
            _handleBookmarkMouseout(event) {
                this.setStatus(TIMING);
            }

            /**
             * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
             * @method _handleBookmarkMouseover
             * @private
             */
            _handleBookmarkMouseover(event) {
                this.setStatus(ACTIVE)
            }

            /**
             * Responds to BookmarkLink clicks.  Wraps the target link in LinkInfo and uses
             * that to determine the url (translates proxy links to the base url).  Changes the
             * status to BOOKMARKING
             * @method _handleClick
             * @private
             */
            _handleClick() {
                let target = this.target,
                    linkinfo = new L.LinkInfo(target),
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
                bookmarks = this.bookmarks;
                if (bookmarks) {
                    bookmarks.addBookmark(new L.Bookmark(label, url));
                } else {
                    L.BookmarkLogin.addBookmark(label, url);
                }
                this.setStatus(BOOKMARKING);

            }

            /**
             * Responds to mouseout on target anchors, checks if they are bookmarkable, changes the status to TIMING.
             * @method _handleTargetMouseout
             * @private
             * @param event {CustomEvent}
             */
            _handleTargetMouseout(event) {
                if (this._isBookmarkable(event.target)) {
                    this.setStatus(TIMING);
                }
            }

            /**
             * Responds to mouseover on anchors, checks if they are bookmarkable, changes the status to READY.
             * @method _handleTargetMouseover
             * @private
             * @param event {CustomEvent}
             */
            _handleTargetMouseover(event) {
                if (this._isBookmarkable(event.target)) {
                    this.target = event.target;
                    this.setStatus(READY);
                }
            }

            /**
             * Determine if a link has already been bookmarked. (case 71323)
             * @method _isAlreadyBookmarked
             * @private
             * @param target the target anchor
             * @returns {Boolean}
             */
            _isAlreadyBookmarked(target) {
                let url, bookmarks, query,
                    linkinfo = new L.LinkInfo(target);
                if (linkinfo.local) {
                    url = linkinfo.path;
                    query = linkinfo.query;
                    url = query ? url + query : url;
                } else {
                    url = linkinfo.url;
                }
                bookmarks = this.bookmarks;
                if (bookmarks) {
                    return bookmarks.hasURL(url);
                }
                return false;
            }

            /**
             * Determine if a link is bookmarkable.  For now true if its display property is inline
             * or inline-block and it does not contain an img element.
             * Added logic for if link was already bookmarked case 75199
             * added logic for using for individual nodes and descendants for
             * case 101724
             * 2/4/15 added bookmarkable = false if no href
             * @method _isBookmarkable
             * @private
             * @param target the target anchor
             * @returns {Boolean}
             */
            _isBookmarkable(target) {
                return target.href
                    && window.getComputedStyle(target).display.includes("inline")
                    && !target.querySelector("img")
                    && target.closest(".bookmarking")
                    && !target.classList.contains("no-bookmarking")
                    && !this._isAlreadyBookmarked(target);
            }

            /**
             * Changes the status to OFF
             * @method _turnOff
             * @private
             */
            _turnOff() {
                this.setStatus(OFF);
            }

            /**
             * Cancels the timer and sets it to null.
             * @method _clearTimer
             * @private
             */
            _clearTimer() {
                if (this._timer) {
                    clearTimeout(this._timer);
                }
                this._timer = null;
            }

            /**
             * Respond to statusChange events, adds and removes the link and changes the link's class,
             * and starts the timer to turn off the link when appropriate.
             * @method _handleStatusChange
             * @private
             * @param event {CustomEvent}
             */
            _handleStatusChange(event) {
                this._clearTimer();
                let target = this.target;
                switch (event.newVal) {
                    //OFF: not visible
                    case OFF:
                        if (this._node) {
                            this._node.remove();
                            this._node.classList.remove("active");
                            this._node.classList.remove("bookmarking");
                        }
                        break;
                    //READY: visible but not enabled
                    case READY:
                        if (!this._node) {
                            this._node = this.node;
                            this._node.addEventListener("mouseleave", (e) => this._handleBookmarkMouseout(e));
                            this._node.addEventListener("click", (e) => this._handleClick(e));
                            this._node.addEventListener("mouseover", this._handleBookmarkMouseover.bind(this));
                        }
                        target.insertAdjacentElement("afterend", this._node);
                        break;
                    //ACTIVE: enabled (mouseover)
                    case ACTIVE:
                        this._node.classList.add("active");
                        break;
                    //BOOKMARKING: clicked and waiting for server sync message
                    case BOOKMARKING:
                        this._node.classList.replace("active", "bookmarking");
                        break;
                    //TIMING: waiting to hide
                    case TIMING:
                        this._node.classList.remove("active");
                        this._node.classList.remove("bookmarking");
                        this._timer = setTimeout(() => { this._turnOff(); }, this.hideDelay);
                        break;
                    default:
                }
            }
        }


        //create a BookmarkLink and save reference
        L.BookmarkLink = new BookmarkLink();

    }


})();
