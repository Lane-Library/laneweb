(() => {

    "use strict";

    class Bookmark {

        #label;
        #url;

        constructor(label, url) {
            this.#label = undefined;
            this.#url = undefined;
            L.addEventTarget(this, {
                prefix: 'bookmark'
            });
            this.on("valueChange", this.#handleValueChange);
            this.setValues(label, url);
        };

        /**
         * The default changeEvent handler
         *
         * @private
         * @param event
         *            {CustomEvent} the valueChange event
         */
        #handleValueChange = ({ newLabel, newUrl }) => {
            this.#label = newLabel;
            this.#url = newUrl;
        }

        /**
         * getter for the label
         *
         * @returns {string} the label
         */
        get label() {
            return this.#label;
        }

        /**
         * getter for the url
         *
         * @returns {string} the url
         */
        get url() {
            return this.#url;
        }

        /**
         * setter for the label, delegates to setValues with the current url as
         * the url value.
         *
         * @param newLabel
         *            {string}
         */
        set label(newLabel) {
            this.setValues(newLabel, this.#url);
        }

        /**
         * setter for the url, delegates to setValues with the current
         *
         * @param newUrl
         *            {string}
         */
        set url(newUrl) {
            this.setValues(this.#label, newUrl);
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
            const hasChanged = newLabel !== this.#label || newUrl !== this.#url;
            if (hasChanged) {
                this.fire("valueChange", {
                    prevLabel: this.#label,
                    prevUrl: this.#url,
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
            return `Bookmark{label:${this.#label},url:${this.#url}}`;
        }
    };

    // make the Bookmark constructor globally accessible
    L.Bookmark = Bookmark;

})();
