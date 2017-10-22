(function() {

    "use strict";

    var lane = Y.lane,
        model = lane.Model,
        lightbox = lane.Lightbox,
        basePath = model.get(model.BASE_PATH),
        location = lane.Location,

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
            addBookmark: function(label, url) {
                var queryString = "&label=" + encodeURIComponent(label);
                queryString += "&url=" + encodeURIComponent(url);
                queryString += "&redirect=" + encodeURIComponent(location.get("href"));
                Y.io(basePath + "/plain/bookmark-login.html", {
                    on: {
                        success: BookmarkLogin._handleSuccess,
                        failure: BookmarkLogin._handleFailure
                    },
                    "arguments": {
                        queryString: queryString
                    }
                });
            },

            /**
             * Handler for when the login popup fetch fails.  Simply informs user that logging in
             * is necessary for bookmarking.
             * @method _handleFailure
             * @private
             */
            _handleFailure: function() {
                lane.showMessage("You must log in in order to create bookmarks.");
            },

            /**
             * Handler for when the login popup fetch succeeds.  Adds the query string to the login
             * link in the popup then puts the popup into the lightbox.
             * @method _handleSuccess
             * @private
             * @param id {number} the Y.io transaction id
             * @param o {object} the ajax response object
             * @param args {object} the arguments passed to Y.io, in this case the query string
             */
            _handleSuccess: function(id, o, args) {
                var queryString = args.queryString, yes, no, handler;
                lightbox.setContent(o.responseText);
                yes = Y.one("#yes-bookmark-login");
                no = Y.one("#no-bookmark-login");
                yes.set("href", yes.get("href") + queryString);
                handler = no.on("click", function() {
                    handler.detach();
                    lightbox.hide();
                });
                lightbox.show();
            }
    };

    //make BookmarkLogin globally available
    Y.lane.BookmarkLogin = BookmarkLogin;
})();