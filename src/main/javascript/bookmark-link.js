(() => {

    "use strict";

    const Model = L.Model;

    // create BookmarkLink only if bookmarking = rw
    if (Model.get(Model.BOOKMARKING) !== "rw") {
        return;
    }

    const STATUS = {
        OFF: "off",
        READY: "ready",
        ACTIVE: "active",
        BOOKMARKING: "bookmarking",
        TIMING: "timing"
    };

    const HIDE_DELAY = 500;

    class BookmarkLink {

        #element;
        #bookmarks;
        #target;
        #status;
        #timer;
        #isInitialized;

        // --- Constructor and Initialization ---
        constructor() {
            this.#element = this.#createElement();
            this.#bookmarks = L.BookmarksWidget?.bookmarks;
            this.#target = null;
            this.#status = STATUS.OFF;
            this.#timer = null;
            // Flag for one-time event listener setup
            this.#isInitialized = false;

            this.#bindEvents();
        }

        /**
         * Creates the bookmark link DOM element.
         * @returns {HTMLElement} The created span element.
         * @private
         */
        #createElement() {
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
        #bindEvents() {
            L.addEventTarget(this, { prefix: 'bookmarkLink' });
            const links = document.querySelectorAll("main a");
            links.forEach(link => {
                link.addEventListener("mouseover", event => this.#handleTargetMouseover(event));
                link.addEventListener("mouseout", event => this.#handleTargetMouseout(event));
            });

            this.on("statusChange", this.#handleStatusChange);
            this.#bookmarks?.on("addSync", this.#handleSyncEvent);
        }

        // --- Public Methods ---
        setStatus(newStatus) {
            if (this.#status !== newStatus) {
                this.#status = newStatus;
                this.fire("statusChange", { newVal: newStatus });
            }
        }

        // --- Event Handlers ---
        /**
         * Responds to bookmarks:addSync event, changes the status to OFF
         * @private
         */
        #handleSyncEvent = () => {
            this.setStatus(STATUS.OFF);
            // fire an added event for favorites animation
            L.fire("bookmarks:added");
        }

        /**
         * Responds to mouseout event on the BookmarkLink, changes the status to TIMING
         * @private
         */
        #handleBookmarkMouseout = () => {
            this.setStatus(STATUS.TIMING);
        }

        /**
         * Responds to mouseover events on the BookmarkLink, changes the status to ACTIVE
         * @private
         */
        #handleBookmarkMouseover = () => {
            this.setStatus(STATUS.ACTIVE);
        }

        /**
         * Responds to BookmarkLink clicks.  Wraps the target link in LinkInfo and uses
         * that to determine the url (translates proxy links to the base url).  Changes the
         * status to BOOKMARKING
         * @private
         */
        #handleClick = () => {
            const linkInfo = new L.LinkInfo(this.#target);
            const label = linkInfo.title;

            // case 71646 local links lack query string
            // Use a let variable for url since it can be reassigned.
            let url = linkInfo.local ? (linkInfo.path + (linkInfo.query || '')) : linkInfo.url;

            if (this.#bookmarks) {
                this.#bookmarks.addBookmark(new L.Bookmark(label, url));
            } else {
                L.BookmarkLogin.addBookmark(label, url);
            }
            this.setStatus(STATUS.BOOKMARKING);
        }

        /**
         * Responds to mouseout on target anchors, checks if they are bookmarkable, changes the status to TIMING.
         * @private
         * @param event {CustomEvent}
         */
        #handleTargetMouseout = (event) => {
            if (this.#isBookmarkable(event.target)) {
                this.setStatus(STATUS.TIMING);
            }
        }

        /**
         * Responds to mouseover on anchors, checks if they are bookmarkable, changes the status to READY.
         * @private
         * @param event {CustomEvent}
         */
        #handleTargetMouseover = (event) => {
            if (this.#isBookmarkable(event.target)) {
                this.#target = event.target;
                this.setStatus(STATUS.READY);
            }
        }

        /**
         * State machine for handling UI changes based on status.
         * @private
         */
        #handleStatusChange = (event) => {
            this.#clearTimer();

            switch (event.newVal) {
                case STATUS.OFF:
                    this.#element.remove();
                    this.#element.classList.remove("active", "bookmarking");
                    break;

                case STATUS.READY:
                    // Lazy-add event listeners only once for the element itself.
                    if (!this.#isInitialized) {
                        this.#element.addEventListener("mouseout", this.#handleBookmarkMouseout);
                        this.#element.addEventListener("click", this.#handleClick);
                        this.#element.addEventListener("mouseover", this.#handleBookmarkMouseover);
                        this.#isInitialized = true;
                    }

                    if (this.#target.closest(".ellipsis")) {
                        this.#target.insertAdjacentElement("beforebegin", this.#element);
                    } else {
                        this.#target.insertAdjacentElement("afterend", this.#element);
                    }
                    break;

                case STATUS.ACTIVE:
                    this.#element.classList.add("active");
                    break;

                case STATUS.BOOKMARKING:
                    this.#element.classList.replace("active", "bookmarking");
                    break;

                case STATUS.TIMING:
                    this.#element.classList.remove("active", "bookmarking");
                    this.#timer = setTimeout(() => this.#turnOff(), HIDE_DELAY);
                    break;
            }
        }

        // --- Utility Methods ---
        /**
         * Determine if a link has already been bookmarked. (case 71323)
         * @private
         * @param target the target anchor
         * @returns {Boolean}
         */
        #isAlreadyBookmarked(target) {
            if (!this.#bookmarks) return false;

            const linkInfo = new L.LinkInfo(target);
            const url = linkInfo.local ? (linkInfo.path + (linkInfo.query || '')) : linkInfo.url;

            return this.#bookmarks.hasURL(url);
        }

        /**
         * Determine if a link is bookmarkable.  For now true if its display property is inline
         * or inline-block and it does not contain an img element.
         * Added logic for if link was already bookmarked case 75199
         * added logic for using for individual nodes and descendants for
         * case 101724
         * 2/4/15 added bookmarkable = false if no href
         * @private
         * @param target the target anchor
         * @returns {Boolean}
         */
        #isBookmarkable(target) {
            return target.href &&
                !target.classList.contains("no-bookmarking") &&
                target.closest(".bookmarking") &&
                window.getComputedStyle(target).display.includes("inline") &&
                !target.querySelector("img") &&
                !this.#isAlreadyBookmarked(target);
        }

        /**
         * Changes the status to OFF
         * @private
         */
        #turnOff() {
            this.setStatus(STATUS.OFF);
        }

        /**
         * Cancels the timer and sets it to null.
         * @private
         */
        #clearTimer() {
            clearTimeout(this.#timer);
            this.#timer = null;
        }
    }

    // Create and assign the singleton instance.
    L.BookmarkLink = new BookmarkLink();

})();
