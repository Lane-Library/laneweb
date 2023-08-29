(function() {

    "use strict";

    var BookmarkEditor,
        Bookmark = L.Bookmark,
        HTMLTemplate = document.querySelector("#bookmark-editor-template");

    /**
     * An editor widget for an individual bookmark.
     * @class BookmarkEditor
     * @constructor
     */
    BookmarkEditor = Y.Base.create("bookmark-editor", Y.Widget, [], {

        /**
         * Creates text inputs and buttons for the editor using the #bookmark-editor-template from form.stx.
         * @method renderUI
         */
        renderUI : function() {
            this.get("srcNode").append(HTMLTemplate.innerHTML);
        },

        /**
         * Sets up event handlers.
         * @method bindUI
         */
        bindUI : function() {
            this.get("srcNode").all("button").on("click", this._handleButtonClick, this);
            this.on("editingChange", this._handleEditingChange, this);
        },

        /**
         * Sets up the inputs and truncates long labels.
         * @method syncUI
         */
        syncUI : function() {
            var srcNode = this.get("srcNode");
            this._labelInput = srcNode.one("input[name='label']");
            this._urlInput = srcNode.one("input[name='url']");
            srcNode.one("input[name='url']").after("focus", this._setDefaultUrlInputText, this);
            this._truncateLabel();
        },

        /**
         * Responds to the cancel button.  If there is no associated bookmark, like when this editor
         * is for a new bookmark that hasn't been created yet, this editor gets destroyed, otherwise
         * the editing attribute is set to false
         * @method cancel
         */
        cancel : function() {
            var addBookmarkContainer = document.querySelector(".addBookmarkContainer");
            if (this.get("bookmark")) {
                this.set("editing", false);
            } else {
                this._labelInput.destroy();
                this._urlInput.destroy();
                this.destroy(true);
                if (addBookmarkContainer) {
                    addBookmarkContainer.classList.toggle("active");
                }
            }
        },

        /**
         * Responds to the edit button by showing the editContainer form and hiding the bookmark anchor
         * @method edit
         */
        edit : function() {
            if (this.get("bookmark")) {
                this.set("editing", true);
            }
        },

        /**
         * Responds to a click on the delete button. Relies on the bookmarks object to find the index 
         * of the bookmark to delete and to remove the bookmark.
         * @method delete
         */
        "delete" : function() {
            var bookmarks = L.BookmarksWidget.get("bookmarks"),
                index = bookmarks.indexOf(this.get("bookmark"));
            bookmarks.removeBookmarks([index]);
        },

        /**
         * Responds to the save button.  If the inputs lack value, puts 'required' in to the placeholder
         * and does nothing else.  If there is no associated bookmark, creates a new one, otherwise
         * changes the bookmark label and url based on what is in the inputs.
         * @method save
         */
        save : function() {
            var newlabel = this._labelInput.get("value"),
            newurl = this._urlInput.get("value"),
            bookmark = this.get("bookmark");
            if (!newlabel || !newurl) {
                if (!newlabel) {
                    this._labelInput.set("placeholder", "required");
                }
                if (!newurl) {
                    this._urlInput.set("placeholder", "required");
                }
                return;
            }
            if (bookmark) {
                if (newlabel !== bookmark.getLabel() || newurl !== bookmark.getUrl()) {
                    bookmark.setValues(newlabel, newurl);
                }
            } else {
                bookmark = new Bookmark(newlabel, newurl);
                this.set("bookmark", bookmark);
                L.BookmarksWidget.get("bookmarks").addBookmark(bookmark);
            }
            this.set("editing", false);
        },

        /**
         * Responds to the reset button.  Resets the text inputs to the bookmark's values.
         * If there is no bookmark, sets the inputs to empty strings.
         * @method reset
         */
        reset : function() {
            var bookmark = this.get("bookmark");
            this._labelInput.set("placeholder", "Name");
            this._urlInput.set("placeholder", "Location");
            if (bookmark) {
                this._labelInput.set("value", bookmark.getLabel());
                this._urlInput.set("value", bookmark.getUrl());
            } else {
                this._labelInput.set("value", "");
                this._urlInput.set("value", "");
            }
        },

        /**
         * Update the editors anchor text and url with the bookmark's label and url.
         * @method update
         */
        update : function() {
            var anchor = this.get("srcNode").one("a"),
            bookmark = this.get("bookmark");
            anchor.set("innerHTML", bookmark.getLabel());
            anchor.set("href", bookmark.getUrl());
            this._truncateLabel();
        },

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @method _handleButtonClick
         * @private
         * @param event {CustomEvent}
         */
        _handleButtonClick : function(event) {
            event.preventDefault();
            this[event.currentTarget.getAttribute("value")].call(this, event);
        },

        /**
         * Called when the editing attribute changes.  Toggles the yui3-bookmark-editor-active class.
         * @method _handleEditingChange
         * @private
         * @param event {CustomEvent}
         */
        _handleEditingChange : function(event) {
            var srcNode = this.get("srcNode"),
            activeClass = this.getClassName() + "-active";
            srcNode._node.classList.toggle('active');
            if (event.newVal) {
                srcNode.addClass(activeClass);
                this.reset();
            } else {
                srcNode.removeClass(activeClass);
            }
        },

        /**
         * Truncates the link text to 130 characters if necessary.
         * @method _truncateLabel
         * @private
         */
        _truncateLabel : function() {
            var anchor = this.get("srcNode").one("a"),
            label = anchor.get("innerHTML");
            if (label.length > 130) {
                anchor.set("innerHTML", label.substring(0, 130) + "...");
            }
        },

        /**
         * Put the text https:// into url input if it is empty
         * @method _setDefaultUrlInputText
         * @private
         */
        _setDefaultUrlInputText : function() {
            if (this._urlInput.get("value") === "") {
                this._urlInput.set("value", "https://");
            }
        }
    }, {
        ATTRS : {
            bookmark : {
                value : null
            },
            editing : {
                value : false
            }
        }
    });

    L.BookmarkEditor = BookmarkEditor;

})();
