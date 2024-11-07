(function () {

    "use strict";

    let model = L.Model,
        lightbox = L.Lightbox,
        basePath = model.get(model.BASE_PATH),

        /**
         * An Object that controls interactions when bookmarking occurs without the
         * user being logged in.
         */
        BookmarkLogin = {

            /**
             * Add a bookmark when not logged in.  Constructs a query string with the bookmark
             * information and the page to return to, then fetches a popup page and passes control
             * to the _handleSuccess method.
             * @method addBookmark
             * @param label {string} the label
             * @param url {string} the url
             */
            addBookmark: function (label, url) {
                let queryString = "&label=" + encodeURIComponent(label);
                queryString += "&url=" + encodeURIComponent(url);
                queryString += "&redirect=" + encodeURIComponent(location.href);
                fetch(basePath + "/plain/bookmark-login.html", {
                    method: 'GET'
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.text();
                    })
                    .then(data => {
                        BookmarkLogin._handleSuccess(null, { responseText: data }, { queryString: queryString });
                    })
                    .catch(error => {
                        BookmarkLogin._handleFailure();
                    });
            },

            /**
             * Handler for when the login popup fetch fails.  Simply informs user that logging in
             * is necessary for bookmarking.
             * @method _handleFailure
             * @private
             */
            _handleFailure: function () {
                L.showMessage("You must log in in order to create bookmarks.");
            },

            /**
             * Handler for when the login popup fetch succeeds.  Adds the query string to the login
             * link in the popup then puts the popup into the lightbox.
             * @method _handleSuccess
             * @private
             * @param id {number} the L.io transaction id
             * @param o {object} the ajax response object
             * @param args {object} the arguments passed to L.io, in this case the query string
             */
            _handleSuccess: function (id, o, args) {
                let queryString = args.queryString, yes, no;
                lightbox.setContent(o.responseText);
                yes = document.querySelector("#yes-bookmark-login");
                no = document.querySelector("#no-bookmark-login");
                yes.href += queryString;
                no.addEventListener("click", function () {
                    lightbox.hide();
                });
                lightbox.show();
            }
        };

    //make BookmarkLogin globally available
    L.BookmarkLogin = BookmarkLogin;
})();
