(function () {

    "use strict";

    let Bookmark = L.Bookmark,
        HTMLTemplate = document.querySelector("#bookmark-editor-template");


    class EventEmitter {
        constructor() {
            this.events = {};
        }

        on(event, listener) {
            if (!this.events[event]) {
                this.events[event] = [];
            }
            this.events[event].push(listener);
        }

        emit(event, ...args) {
            if (this.events[event]) {
                this.events[event].forEach(listener => listener.apply(this, args));
            }
        }
    }

    class BookmarkEditor extends EventEmitter {
        constructor(args) {
            super(); // Call the parent class constructor
            this.bookmark = args.bookmark;
            this.editing = args.render;
            this.srcNode = args.srcNode;
            this.initializer();
        }


        initializer() {
            if (HTMLTemplate != null) {
                this.renderUI();
                this.bindUI();
                this.syncUI();
            }
        }
        /**
         * Creates text inputs and buttons for the editor using the #bookmark-editor-template from form.stx.
         * @method renderUI
         */
        renderUI() {
            this.srcNode.append(HTMLTemplate.innerHTML);
        }

        /**
         * Sets up event handlers.
         * @method bindUI
         */
        bindUI() {
            this.srcNode.all("button").on("click", this._handleButtonClick, this);
            // const buttons = this.srcNode.querySelectorAll("button");
            // buttons.forEach(button => {
            //     button.addEventListener("click", (event) => this._handleButtonClick(event));
            // });
            this.on("editingChange", this._handleEditingChange, this);
        }

        /**
         * Sets up the inputs and truncates long labels.
         * @method syncUI
         */
        syncUI() {
            let srcNode = this.srcNode;
            this._labelInput = srcNode.one("input[name='label']");
            this._urlInput = srcNode.one("input[name='url']");
            const urlInputElement = srcNode.one("input[name='url']");
            // urlInputElement.addEventListener("focus", (event) => this._setDefaultUrlInputText(event));
            urlInputElement.on("focus", this._setDefaultUrlInputText, this);
            this._truncateLabel();
        }

        /**
         * Responds to the cancel button.  If there is no associated bookmark, like when this editor
         * is for a new bookmark that hasn't been created yet, this editor gets destroyed, otherwise
         * the editing attribute is set to false
         * @method cancel
         */
        cancel() {
            let addBookmarkContainer = document.querySelector(".addBookmarkContainer");
            if (this.bookmark) {
                this.editing = false;
            } else {
                this._labelInput.destroy();
                this._urlInput.destroy();
                this.destroy(true);
                if (addBookmarkContainer) {
                    addBookmarkContainer.classList.toggle("active");
                }
            }
        }

        /**
         * Responds to the edit button by showing the editContainer form and hiding the bookmark anchor
         * @method edit
         */
        edit() {
            if (this.bookmark) {
                this.editing = true;
            }
        }

        /**
         * Responds to a click on the delete button. Relies on the bookmarks object to find the index 
         * of the bookmark to delete and to remove the bookmark.
         * @method delete
         */
        delete() {
            let bookmarks = L.BookmarksWidget.bookmarks,
                index = bookmarks.indexOf(this.bookmark);
            bookmarks.removeBookmarks([index]);
        }

        /**
         * Responds to the save button.  If the inputs lack value, puts 'required' in to the placeholder
         * and does nothing else.  If there is no associated bookmark, creates a new one, otherwise
         * changes the bookmark label and url based on what is in the inputs.
         * @method save
         */
        save() {
            let newlabel = this._labelInput.value,
                newurl = this._urlInput.value,
                bookmark = this.bookmark;
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
                L.BookmarksWidget.bookmarks.addBookmark(bookmark);
            }
            this.set("editing", false);
        }

        /**
         * Responds to the reset button.  Resets the text inputs to the bookmark's values.
         * If there is no bookmark, sets the inputs to empty strings.
         * @method reset
         */
        reset() {
            let bookmark = this.bookmark;
            this._labelInput.placeholder = "Name";
            this._urlInput.placeholder = "Location";
            if (bookmark) {
                this._labelInput.value = bookmark.getLabel();
                this._urlInput.value = bookmark.getUrl();
            } else {
                this._labelInput.value = ""
                this._urlInput.value = ""
            }
        }

        /**
         * Update the editors anchor text and url with the bookmark's label and url.
         * @method update
         */
        update() {
            let anchor = this.srcNode.one("a"),
                bookmark = this.bookmark;
            anchor.set("innerHTML", bookmark.getLabel());
            anchor.set("href", bookmark.getUrl());
            this._truncateLabel();
        }

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @method _handleButtonClick
         * @private
         * @param event {CustomEvent}
         */
        _handleButtonClick(event) {
            event.preventDefault();
            this[event.currentTarget.getAttribute("value")].call(this, e);
        }

        // /**
        //  * Called when the editing attribute changes.  Toggles the yui3-bookmark-editor-active class.
        //  * @method _handleEditingChange
        //  * @private
        //  * @param event {CustomEvent}
        //  */
        _handleEditingChange(event) {
            let srcNode = this.srcNode,
                activeClass = this.getClassName() + "-active";
            srcNode._node.classList.toggle('active');
            if (event.newVal) {
                srcNode.addClass(activeClass);
                this.reset();
            } else {
                srcNode.removeClass(activeClass);
            }
        }

        /**
         * Truncates the link text to 130 characters if necessary.
         * @method _truncateLabel
         * @private
         */
        _truncateLabel() {
            let anchor = this.srcNode.one("a"),
                label = anchor.innerHTML;
            label = anchor.get("innerHTML");
            if (label.length > 130) {
                anchor.set("innerHTML", label.substring(0, 130) + "...");
            }
            // if (label.length > 130) {
            //     anchor.innerHTML = label.substring(0, 130) + "...";
            // }
        }


        /**
         * Put the text https:// into url input if it is empty
         * @method _setDefaultUrlInputText
         * @private
         */
        _setDefaultUrlInputText() {
            if (this._urlInput.value === "") {
                this._urlInput.value = "https://";
            }
        }
    }
    L.BookmarkEditor = BookmarkEditor;

})();

