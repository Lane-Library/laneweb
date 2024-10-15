(function () {

    "use strict";

    /**
     * A class for representing a bookmark with attributes for the label and url.
     *
     * @class Bookmark
     * @requires EventTarget
     * @constructor
     * @param label  {string} the label
     * @param url {string} the url
     */
    class Bookmark extends L.BookmarkEvent {
        constructor(label, url) {
            super();
            this.first("valueChange", (e) => this._valueChange(e));
            this.setValues(label, url);
        };



        /**
         * The default changeEvent handler
         *
         * @method _valueChange
         * @private
         * @param event
         *            {CustomEvent} the valueChange event
         */
        _valueChange(event) {
            this._label = event.newLabel;
            this._url = event.newUrl;
        }

        /**
         * getter for the label
         *
         * @method getLabel
         * @returns {string} the label
         */
        getLabel() {
            return this._label;
        }

        /**
         * getter for the url
         *
         * @method getUrl
         * @returns {string} the url
         */
        getUrl() {
            return this._url;
        }

        /**
         * setter for the label, delegates to setValues with the current url as
         * the url value.
         *
         * @method setLabel
         * @param newLabel
         *            {string}
         */
        setLabel(newLabel) {
            this.setValues(newLabel, this._url);
        }

        /**
         * setter for the url, delegates to setValues with the current
         *
         * @method setUrl label as the label value.
         *
         * @param newUrl
         *            {string}
         */
        setUrl(newUrl) {
            this.setValues(this._label, newUrl);
        }

        /**
         * Set both the label and url then fire a changed event
         *
         * @method setValues
         * @param newLabel
         *            {string}
         * @param newUrl
         *            {string}
         */
        setValues(newLabel, newUrl) {
            if (!newLabel) {
                throw ("null or empty newLabel");
            }
            if (!newUrl) {
                throw ("null or empty newUrl");
            }
            let changed = newLabel !== this._label || newUrl !== this._url;
            if (changed) {
                this.emit("valueChange", {
                    prevLabel: this._label,
                    prevUrl: this._url,
                    newLabel: newLabel,
                    newUrl: newUrl,
                    target: this
                });
            }
        }

        /**
         * @method toString
         * @return {string} a string with the label and url values
         */
        toString() {
            return "Bookmark{label:" + this._label + ",url:" + this._url + "}";
        }
    };

    // Add EventTarget attributes to the Bookmark prototype
    L.addEventTarget(Bookmark, {
        emitFacade: true,
        prefix: 'bookmark'
    });

    // make the Bookmark constructor globally accessible
    L.Bookmark = Bookmark;

})();
