(() => {
    "use strict";

    const Bookmark = L.Bookmark;
    const HTMLTemplate = document.querySelector("#bookmark-editor-template");

    class BookmarkEditor {
        constructor({ bookmark, srcNode, position }) {
            this.bookmark = bookmark;
            this.srcNode = srcNode;
            this.position = position;
            this.className = "bookmark-editor";
            this.editing = false;

            // Properties to cache DOM elements, initialized to null.
            this._labelInput = null;
            this._urlInput = null;
            this._anchorElement = null;

            this.renderUI();
            this.bindUI();
            this.syncUI();
        }

        /**
         * Creates text inputs and buttons for the editor.
         */
        renderUI() {
            this.srcNode.classList.add("bookmark-editor-content");
            this.srcNode.insertAdjacentHTML('beforeend', HTMLTemplate.innerHTML);
        }

        /**
         * Sets up event handlers.
         */
        bindUI() {
            L.addEventTarget(this, { prefix: 'bookmarkEditor' });

            const buttons = this.srcNode.querySelectorAll("button");
            buttons.forEach(button => {
                button.addEventListener("click", (event) => this._handleButtonClick(event));
            });
            this.on("editingChange", this._handleEditingChange.bind(this));
            this.srcNode.draggable = true;
            this.srcNode.addEventListener("dragstart", (event) => this._handleDragStart(event));
            this.srcNode.addEventListener("dragend", (event) => this._handleDragEnd(event));
            this.srcNode.addEventListener("drop", (event) => this._handleDragDrop(event));
            this.srcNode.addEventListener("dragover", (event) => this._handleDragOver(event));
        }

        /**
         * Sets up the inputs and truncates long labels.
         */
        syncUI() {
            this._labelInput = this.srcNode.querySelector("input[name='label']");
            this._urlInput = this.srcNode.querySelector("input[name='url']");
            this._anchorElement = this.srcNode.querySelector("a");
            this._urlInput.addEventListener("focus", () => this._setDefaultUrlInputText());
            this._truncateLabel();
        }

        setEditing(newVal) {
            if (this.editing !== newVal) {
                this.editing = newVal;
                this.fire("editingChange", { newVal });
            }
        }

        /**
         * Responds to the cancel button.
         */
        cancel() {
            if (this.bookmark) {
                this.setEditing(false);
            } else {
                this.destroy();
                document.querySelector(".addBookmarkContainer")?.classList.toggle("active");
            }
        }

        destroy() {
            this.setEditing(false);
            this.bookmark = null;
            this.srcNode.remove();
            this.fire("destroy", { editor: this });
        }

        /**
         * Responds to the edit button by showing the editContainer form and hiding the bookmark anchor
         */
        edit() {
            if (this.bookmark) {
                this.setEditing(true);
                this.reset();
            }
        }

        /**
         * Deletes the associated bookmark.
         */
        delete() {
            const { bookmarks } = L.BookmarksWidget;
            const index = bookmarks.indexOf(this.bookmark);
            if (index > -1) {
                bookmarks.removeBookmarks([index]);
            }
        }

        /**
         * Responds to the save button.  If the inputs lack value, puts 'required' in to the placeholder
         * and does nothing else.  If there is no associated bookmark, creates a new one, otherwise
         * changes the bookmark label and url based on what is in the inputs.
        */
        save() {
            const newLabel = this._labelInput.value.trim();
            const newUrl = this._urlInput.value.trim();

            let isValid = true;
            if (!newLabel) {
                this._labelInput.placeholder = "required";
                isValid = false;
            }
            if (!newUrl) {
                this._urlInput.placeholder = "required";
                isValid = false;
            }
            if (!isValid) return;

            if (this.bookmark) {
                if (newLabel !== this.bookmark.label || newUrl !== this.bookmark.url) {
                    this.bookmark.setValues(newLabel, newUrl);
                    this._anchorElement.textContent = newLabel;
                    this._anchorElement.href = newUrl;
                }
            } else {
                this.bookmark = new Bookmark(newLabel, newUrl);
                L.BookmarksWidget.bookmarks.addBookmark(this.bookmark);
            }
            this.setEditing(false);
        }

        /**
         * Responds to the reset button.  Resets the text inputs to the bookmark's values.
         * If there is no bookmark, sets the inputs to empty strings.
         */
        reset() {
            this._labelInput.placeholder = "Name";
            this._urlInput.placeholder = "Location";
            if (this.bookmark) {
                this._labelInput.value = this.bookmark.label;
                this._urlInput.value = this.bookmark.url;
            } else {
                this._labelInput.value = "";
                this._urlInput.value = "";
            }
        }

        /**
         * Update the editors anchor text and url with the bookmark's label and url.
         */
        update() {
            this._anchorElement.textContent = this.bookmark.label;
            this._anchorElement.href = this.bookmark.url;
            this._truncateLabel();
        }

        /**
         * Handles button clicks, delegating to the appropriate method.
         * @private
         */
        _handleButtonClick(event) {
            event.preventDefault();
            const action = event.currentTarget.value;
            if (typeof this[action] === 'function') {
                this[action]();
            }
        }

        /**
         * Toggles the active class when editing state changes.
         * @private
         */
        _handleEditingChange(event) {
            const activeClass = `${this.className}-active`;
            this.srcNode.classList.toggle('active', event.newVal);
            this.srcNode.classList.toggle(activeClass, event.newVal);

            if (event.newVal) {
                this.reset();
            }
        }

        /**
         * Truncates the link text if it's too long.
         * @private
         */
        _truncateLabel() {
            const label = this._anchorElement.innerHTML;
            if (label.length > 130) {
                this._anchorElement.innerHTML = `${label.substring(0, 130)}...`;
            }
        }

        /**
         * Sets a default value for the URL input on focus.
         * @private
         */
        _setDefaultUrlInputText() {
            if (this._urlInput.value === "") {
                this._urlInput.value = "https://";
            }
        }

        // --- Drag and Drop Handlers ---

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
            if (this.startNodePositon !== this.position) {
                this.fire("dragOver", { position: this.position, target: event.currentTarget });
            }
        }
    }

    L.addEventTarget(BookmarkEditor, { prefix: 'bookmarkEditor' });
    L.BookmarkEditor = BookmarkEditor;
})();