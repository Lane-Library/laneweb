(function () {

    "use strict";

    let Bookmark = L.Bookmark,
        HTMLTemplate = document.querySelector("#bookmark-editor-template");

    class BookmarkEditor {
        constructor(args) {
            this.bookmark = args.bookmark;
            this.srcNode = args.srcNode;
            this.position = args.position;
            this.className = "bookmark-editor";
            this.editing = false;
            this.renderUI();
            this.bindUI();
            this.syncUI();
        }



        /**
         * Creates text inputs and buttons for the editor using the #bookmark-editor-template from form.stx.
         * @method renderUI
         */
        renderUI() {
            this.srcNode.classList.add("bookmark-editor-content");
            this.srcNode.innerHTML += HTMLTemplate.innerHTML;
        }

        /**
         * Sets up event handlers.
         * @method bindUI
         */
        bindUI() {
            //Add EventTarget attributes to the Bookmarks prototype
            L.addEventTarget(this, {
                prefix: 'bookmarkEditor'
            });

            const buttons = this.srcNode.querySelectorAll("button");
            buttons.forEach(button => {
                button.addEventListener("click", (event) => this._handleButtonClick(event));
            });
            this.on("editingChange", (e) => this._handleEditingChange(e));
            this.srcNode.draggable = true;
            this.srcNode.addEventListener("dragstart", (event) => { this._handleDragStart(event) });
            this.srcNode.addEventListener("dragend", (event) => { this._handleDragEnd(event) });
            this.srcNode.addEventListener("drop", (event) => { this._handleDragDrop(event) });
            this.srcNode.addEventListener("dragover", (event) => { this._handleDragOver(event) });
        }

        /**
         * Sets up the inputs and truncates long labels.
         * @method syncUI
         */
        syncUI() {
            let srcNode = this.srcNode;
            this._labelInput = srcNode.querySelector("input[name='label']");
            this._urlInput = srcNode.querySelector("input[name='url']");
            const urlInputElement = srcNode.querySelector("input[name='url']");
            urlInputElement.addEventListener("focus", (event) => this._setDefaultUrlInputText(event));
            this._truncateLabel();
        }

        setEditing(newVal) {
            this.editing = newVal;
            this.fire("editingChange", { newVal: newVal });
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
                this.setEditing(false);
            } else {
                this._labelInput = null;
                this._urlInput = null;
                this.destroy();
                if (addBookmarkContainer) {
                    addBookmarkContainer.classList.toggle("active");
                }
            }
        }

        destroy() {
            this.setEditing(false);
            this.bookmark = null;
            this.srcNode.remove(true);
            this.fire("destroy", { editor: this });
        }

        /**
         * Responds to the edit button by showing the editContainer form and hiding the bookmark anchor
         * @method edit
         */
        edit() {
            if (this.bookmark) {
                this.setEditing(true);
                this.reset();
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
                    this._labelInput.placeholder = "required";
                }
                if (!newurl) {
                    this._urlInput.placeholder = "required";
                }
                return;
            }
            if (bookmark) {
                if (newlabel !== bookmark.getLabel() || newurl !== bookmark.getUrl()) {
                    bookmark.setValues(newlabel, newurl);
                    this.srcNode.querySelector("a").textContent = newlabel;
                    this.srcNode.querySelector("a").href = newurl;
                }
            } else {
                bookmark = new Bookmark(newlabel, newurl);
                this.bookmark = bookmark;
                L.BookmarksWidget.bookmarks.addBookmark(bookmark);
            }
            this.setEditing(false);
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
            let anchor = this.srcNode.querySelector("a"),
                bookmark = this.bookmark;
            anchor.innerHTML = bookmark.getLabel();
            anchor.href = bookmark.getUrl();
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
            this[event.currentTarget.getAttribute("value")].call(this, event);
        }

        // /**
        //  * Called when the editing attribute changes.  Toggles the yui3-bookmark-editor-active class.
        //  * @method _handleEditingChange
        //  * @private
        //  * @param event {CustomEvent}
        //  */
        _handleEditingChange(event) {
            let srcNode = this.srcNode,
                activeClass = this.className + "-active";
            srcNode.classList.toggle('active');
            if (event.newVal) {
                srcNode.classList.add(activeClass);
                this.reset();
            } else {
                srcNode.classList.remove(activeClass);
            }
        }

        /**
         * Truncates the link text to 130 characters if necessary.
         * @method _truncateLabel
         * @private
         */
        _truncateLabel() {
            let anchor = this.srcNode.querySelector("a"),
                label = anchor.innerHTML;
            if (label.length > 130) {
                anchor.innerHTML = label.substring(0, 130) + "...";
            }
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

        _handleDragStart(event) {
            this.startNodePositon = this.position;
            const draggedNode = event.currentTarget;
            draggedNode.style.border = "1px solid #000";
            draggedNode.querySelector("div").classList.toggle("hidden");
            this.fire("dragStart", { position: this.position, target: draggedNode });
        }

        _handleDragEnd(event) {
            event.preventDefault();
            const draggedNode = event.currentTarget;
            draggedNode.style.border = "none";
            draggedNode.querySelector("div").classList.toggle("hidden");
            this.fire("dragEnd");
        }

        _handleDragDrop(event) {
            event.preventDefault();
        }

        _handleDragOver(event) {
            event.preventDefault();
            if (this.startNodePositon != this.position) {
                this.fire("dragOver", { position: this.position, target: event.currentTarget });
            }
        }
    }

    L.addEventTarget(BookmarkEditor, {
        prefix: 'bookmarkEditor'
    });


    L.BookmarkEditor = BookmarkEditor;

})();

