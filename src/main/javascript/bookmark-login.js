(() => {

    "use strict";

    const Model = L.Model;
    const lightbox = L.Lightbox;
    const basePath = Model.get(Model.BASE_PATH) ?? "";

    /**
     * An Object that controls interactions when bookmarking occurs without the
     * user being logged in.
     */
    class BookmarkLogin {

        /**
         * Add a bookmark when not logged in.  Constructs a query string with the bookmark
         * information and the page to return to, then fetches a popup page and passes control
         * to the _handleSuccess method.
         * @param label {string} the label
         * @param url {string} the url
         */
        async addBookmark(label, url) {
            const queryString = `&label=${encodeURIComponent(label)}&url=${encodeURIComponent(url)}&redirect=${encodeURIComponent(location.href)}`;
            const loginUrl = `${basePath}/plain/bookmark-login.html`;

            try {
                const response = await fetch(loginUrl);
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`);
                }
                const htmlContent = await response.text();
                this._handleSuccess(htmlContent, queryString);
            } catch (error) {
                console.error("Failed to fetch bookmark login form:", error);
                this._handleFailure();
            }
        }

        /**
         * Handler for when the login popup fetch fails.  Simply informs user that logging in
         * is necessary for bookmarking.
         * @private
         */
        _handleFailure() {
            L.showMessage("You must log in in order to create bookmarks.");
        }

        /**
         * Handler for when the login popup fetch succeeds.  Adds the query string to the login
         * link in the popup then puts the popup into the lightbox.
         * @private
         * @param {string} htmlContent - The HTML content for the lightbox.
         * @param {string} queryString - The query string to append to the login link.
         */
        _handleSuccess(htmlContent, queryString) {
            lightbox.setContent(htmlContent);
            const yesButton = document.querySelector("#yes-bookmark-login");
            const noButton = document.querySelector("#no-bookmark-login");
            if (yesButton) {
                yesButton.href += queryString;
            }
            noButton?.addEventListener("click", () => {
                lightbox.hide();
            });
            lightbox.show();
        }
    };

    // Create a singleton instance and make it globally available.
    L.BookmarkLogin = new BookmarkLogin();

})();
