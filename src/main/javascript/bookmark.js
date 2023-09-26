(function() {

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
    var Bookmark = function(label, url) {
        this.publish("valueChange", {
            defaultFn : this._valueChange
        });
        this.setValues(label, url);
    };

    Bookmark.prototype = {

        /**
         * The default changeEvent handler
         *
         * @method _valueChange
         * @private
         * @param event
         *            {CustomEvent} the valueChange event
         */
        _valueChange : function(event) {
            this._label = event.newLabel;
            this._url = event.newUrl;
        },

        /**
         * getter for the label
         *
         * @method getLabel
         * @returns {string} the label
         */
        getLabel : function() {
            return this._label;
        },

        /**
         * getter for the url
         *
         * @method getUrl
         * @returns {string} the url
         */
        getUrl : function() {
            return this._url;
        },

        /**
         * setter for the label, delegates to setValues with the current url as
         * the url value.
         *
         * @method setLabel
         * @param newLabel
         *            {string}
         */
        setLabel : function(newLabel) {
            this.setValues(newLabel, this._url);
        },

        /**
         * setter for the url, delegates to setValues with the current
         *
         * @method setUrl label as the label value.
         *
         * @param newUrl
         *            {string}
         */
        setUrl : function(newUrl) {
            this.setValues(this._label, newUrl);
        },

        /**
         * Set both the label and url then fire a changed event
         *
         * @method setValues
         * @param newLabel
         *            {string}
         * @param newUrl
         *            {string}
         */
        setValues : function(newLabel, newUrl) {
            if (!newLabel) {
                throw ("null or empty newLabel");
            }
            if (!newUrl) {
                throw ("null or empty newUrl");
            }
            var changed = newLabel !== this._label || newUrl !== this._url;
            if (changed) {
                this.fire("valueChange", {
                    prevLabel : this._label,
                    prevUrl : this._url,
                    newLabel : newLabel,
                    newUrl : newUrl
                });
            }
        },

        /**
         * @method toString
         * @return {string} a string with the label and url values
         */
        toString : function() {
            return "Bookmark{label:" + this._label + ",url:" + this._url + "}";
        }
    };

    // Add EventTarget attributes to the Bookmark prototype
    L.addEventTarget(Bookmark, {
        emitFacade : true,
        prefix : 'bookmark'
    });

    // make the Bookmark constructor globally accessible
    L.Bookmark = Bookmark;

})();
