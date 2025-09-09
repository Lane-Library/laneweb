(() => {

    "use strict";

    const Model = L.Model;

    const STATUS = {
        OFF: "off",
        READY: "ready",
        ACTIVE: "active",
        BOOKMARKING: "bookmarking",
        TIMING: "timing"
    };

    // create BookmarkLink only if bookmarking = rw
    if (Model.get(Model.BOOKMARKING) !== "rw") {
        return;
    }

    class BookmarkLink {
        constructor() {
            this.element = this._createElement();
            this.bookmarks = L.BookmarksWidget?.bookmarks;
            this.target = null;
            this.status = STATUS.OFF;
            this.hideDelay = 500;
            this._timer = null;
            // Flag for one-time event listener setup
            this._isInitialized = false;

            this._bindEvents();
        }

        /**
         * Creates the bookmark link DOM element.
         * @returns {HTMLElement} The created span element.
         * @private
         */
        _createElement() {
            const span = document.createElement('span');
            span.title = 'Add to My Bookmarks';
            span.className = 'bookmark-link';

            const icon = document.createElement('i');
            icon.className = 'fa fa-star';

            span.append(icon);
            return span;
        }

        /**
         * Sets up all event listeners for the class instance.
         * @private
         */
        _bindEvents() {
            L.addEventTarget(this, { prefix: 'bookmarkLink' });
            const links = document.querySelectorAll("section a");
            links.forEach(link => {
                link.addEventListener("mouseover", event => this._handleTargetMouseover(event));
                link.addEventListener("mouseout", event => this._handleTargetMouseout(event));
            });

            this.on("statusChange", this._handleStatusChange);
            this.bookmarks?.on("addSync", this._handleSyncEvent);
        }

        setStatus(newStatus) {
            if (this.status !== newStatus) {
                this.status = newStatus;
                this.fire("statusChange", { newVal: newStatus });
            }
        }

        /**
         * Responds to bookmarks:addSync event, changes the status to OFF
         * @method _handleSyncEvent
         * @private
         */
        _handleSyncEvent = () => {
            this.setStatus(STATUS.OFF);
            // fire an added event for favorites animation
            L.fire("bookmarks:added");
        }

        /**
         * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
         * @method _handleBookmarkMouseOut
         * @private
         */
        _handleBookmarkMouseout = () => {
            this.setStatus(STATUS.TIMING);
        }

        /**
         * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
         * @method _handleBookmarkMouseover
         * @private
         */
        _handleBookmarkMouseover = () => {
            this.setStatus(STATUS.ACTIVE);
        }

        /**
         * Responds to BookmarkLink clicks.  Wraps the target link in LinkInfo and uses
         * that to determine the url (translates proxy links to the base url).  Changes the
         * status to BOOKMARKING
         * @method _handleClick
         * @private
         */
        _handleClick = () => {
            const linkInfo = new L.LinkInfo(this.target);
            const label = linkInfo.title;

            // case 71646 local links lack query string
            // Use a let variable for url since it can be reassigned.
            let url = linkInfo.local ? (linkInfo.path + (linkInfo.query || '')) : linkInfo.url;

            if (this.bookmarks) {
                this.bookmarks.addBookmark(new L.Bookmark(label, url));
            } else {
                L.BookmarkLogin.addBookmark(label, url);
            }
            this.setStatus(STATUS.BOOKMARKING);
        }

        /**
         * Responds to mouseout on target anchors, checks if they are bookmarkable, changes the status to TIMING.
         * @method _handleTargetMouseout
         * @private
         * @param event {CustomEvent}
         */
        _handleTargetMouseout = (event) => {
            if (this._isBookmarkable(event.target)) {
                this.setStatus(STATUS.TIMING);
            }
        }

        /**
         * Responds to mouseover on anchors, checks if they are bookmarkable, changes the status to READY.
         * @method _handleTargetMouseover
         * @private
         * @param event {CustomEvent}
         */
        _handleTargetMouseover = (event) => {
            if (this._isBookmarkable(event.target)) {
                this.target = event.target;
                this.setStatus(STATUS.READY);
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
            if (!this.bookmarks) return false;

            const linkInfo = new L.LinkInfo(target);
            const url = linkInfo.local ? (linkInfo.path + (linkInfo.query || '')) : linkInfo.url;

            return this.bookmarks.hasURL(url);
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
            // Use a series of clear checks for better readability.
            return target.href &&
                !target.classList.contains("no-bookmarking") &&
                target.closest(".bookmarking") &&
                window.getComputedStyle(target).display.includes("inline") &&
                !target.querySelector("img") &&
                !this._isAlreadyBookmarked(target);
        }

        /**
         * Changes the status to OFF
         * @method _turnOff
         * @private
         */
        _turnOff() {
            this.setStatus(STATUS.OFF);
        }

        /**
         * Cancels the timer and sets it to null.
         * @method _clearTimer
         * @private
         */
        _clearTimer() {
            clearTimeout(this._timer);
            this._timer = null;
        }

        /**
         * State machine for handling UI changes based on status.
         * @private
         */
        _handleStatusChange = (event) => {
            this._clearTimer();

            switch (event.newVal) {
                case STATUS.OFF:
                    this.element.remove();
                    this.element.classList.remove("active", "bookmarking");
                    break;

                case STATUS.READY:
                    // Lazy-add event listeners only once for the element itself.
                    if (!this._isInitialized) {
                        this.element.addEventListener("mouseout", this._handleBookmarkMouseout);
                        this.element.addEventListener("click", this._handleClick);
                        this.element.addEventListener("mouseover", this._handleBookmarkMouseover);
                        this._isInitialized = true;
                    }

                    if (this.target.closest(".ellipsis")) {
                        this.target.insertAdjacentElement("beforebegin", this.element);
                    } else {
                        this.target.insertAdjacentElement("afterend", this.element);
                    }
                    break;

                case STATUS.ACTIVE:
                    this.element.classList.add("active");
                    break;

                case STATUS.BOOKMARKING:
                    this.element.classList.replace("active", "bookmarking");
                    break;

                case STATUS.TIMING:
                    this.element.classList.remove("active", "bookmarking");
                    // Use arrow function for setTimeout to maintain `this`.
                    this._timer = setTimeout(() => this._turnOff(), this.hideDelay);
                    break;
            }
        }
    }

    // Create and assign the singleton instance.
    L.BookmarkLink = new BookmarkLink();

})();
