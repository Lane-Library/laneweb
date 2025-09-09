(() => {

    "use strict";

    class Bookmark {

        constructor(label, url) {
            this._label = undefined;
            this._url = undefined;
            L.addEventTarget(this, {
                prefix: 'bookmark'
            });
            this.on("valueChange", this._handleValueChange);
            this.setValues(label, url);
        };

        /**
         * The default changeEvent handler
         *
         * @private
         * @param event
         *            {CustomEvent} the valueChange event
         */
        _handleValueChange = ({ newLabel, newUrl }) => {
            this._label = newLabel;
            this._url = newUrl;
        }

        /**
         * getter for the label
         *
         * @returns {string} the label
         */
        get label() {
            return this._label;
        }

        /**
         * getter for the url
         *
         * @returns {string} the url
         */
        get url() {
            return this._url;
        }

        /**
         * setter for the label, delegates to setValues with the current url as
         * the url value.
         *
         * @param newLabel
         *            {string}
         */
        set label(newLabel) {
            this.setValues(newLabel, this._url);
        }

        /**
         * setter for the url, delegates to setValues with the current
         *
         * @param newUrl
         *            {string}
         */
        set url(newUrl) {
            this.setValues(this._label, newUrl);
        }

        /**
         * Set both the label and url then fire a changed event
         *
         * @param newLabel
         *            {string}
         * @param newUrl
         *            {string}
         */
        setValues(newLabel, newUrl) {
            if (!newLabel) {
                throw new Error("Bookmark label cannot be null or empty.");
            }
            if (!newUrl) {
                throw new Error("Bookmark URL cannot be null or empty.");
            }
            const hasChanged = newLabel !== this._label || newUrl !== this._url;
            if (hasChanged) {
                this.fire("valueChange", {
                    prevLabel: this._label,
                    prevUrl: this._url,
                    newLabel,
                    newUrl,
                    target: this
                });
            }
        }

        /**
         * @return {string} a string with the label and url values
         */
        toString() {
            return `Bookmark{label:${this._label},url:${this._url}}`;
        }
    };

    // make the Bookmark constructor globally accessible
    L.Bookmark = Bookmark;

})();
