(() => {

    "use strict";

    const Bookmark = L.Bookmark;
    const HTMLTemplate = document.querySelector("#bookmark-editor-template");

    class BookmarkEditor {

        #anchorElement;
        #bookmark;
        #className;
        #editing;
        #labelInput;
        #position;
        #srcNode;
        #urlInput;

        constructor({ bookmark, srcNode, position }) {
            this.#bookmark = bookmark;
            this.#srcNode = srcNode;
            this.#position = position;
            this.#className = "bookmark-editor";
            this.#editing = false;

            // Properties to cache DOM elements, initialized to null.
            this.#labelInput = null;
            this.#urlInput = null;
            this.#anchorElement = null;

            this.#renderUI();
            this.#bindUI();
            this.#syncUI();
        }

        // --- Constructor and Lifecycle Methods ---

        /**
         * Creates text inputs and buttons for the editor.
         */
        #renderUI() {
            this.#srcNode.classList.add("bookmark-editor-content");
            this.#srcNode.insertAdjacentHTML('beforeend', HTMLTemplate.innerHTML);
        }

        /**
         * Sets up event handlers.
         */
        #bindUI() {
            L.addEventTarget(this, { prefix: 'bookmarkEditor' });

            const buttons = this.#srcNode.querySelectorAll("button");
            buttons.forEach(button => {
                button.addEventListener("click", (event) => this.#handleButtonClick(event));
            });
            this.on("editingChange", this.#handleEditingChange.bind(this));
            this.#srcNode.draggable = true;
            this.#srcNode.addEventListener("dragstart", (event) => this.#handleDragStart(event));
            this.#srcNode.addEventListener("dragend", (event) => this.#handleDragEnd(event));
            this.#srcNode.addEventListener("drop", (event) => this.#handleDragDrop(event));
            this.#srcNode.addEventListener("dragover", (event) => this.#handleDragOver(event));
        }

        /**
         * Sets up the inputs and truncates long labels.
         */
        #syncUI() {
            this.#labelInput = this.#srcNode.querySelector("input[name='label']");
            this.#urlInput = this.#srcNode.querySelector("input[name='url']");
            this.#anchorElement = this.#srcNode.querySelector("a");
            this.#urlInput.addEventListener("focus", () => this.#setDefaultUrlInputText());
            this.#truncateLabel();
        }

        // --- Public Methods ---

        get bookmark() { return this.#bookmark; }

        set editing(newVal) {
            if (this.#editing !== newVal) {
                this.#editing = newVal;
                this.fire("editingChange", { newVal });
            }
        }

        set position(newVal) {
            if (this.#position !== newVal) {
                this.#position = newVal;
            }
        }

        /**
         * Responds to the cancel button.
         */
        cancel() {
            if (this.#bookmark) {
                this.editing = false;
            } else {
                this.destroy();
                document.querySelector(".addBookmarkContainer")?.classList.toggle("active");
            }
        }

        destroy() {
            this.editing = false;
            this.#bookmark = null;
            this.#srcNode.remove();
            this.fire("destroy", { editor: this });
        }

        /**
         * Responds to the edit button by showing the editContainer form and hiding the bookmark anchor
         */
        edit() {
            if (this.#bookmark) {
                this.editing = true;
                this.reset();
            }
        }

        /**
         * Deletes the associated bookmark.
         */
        delete() {
            const { bookmarks } = L.BookmarksWidget;
            const index = bookmarks.indexOf(this.#bookmark);
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
            const newLabel = this.#labelInput.value.trim();
            const newUrl = this.#urlInput.value.trim();

            let isValid = true;
            if (!newLabel) {
                this.#labelInput.placeholder = "required";
                isValid = false;
            }
            if (!newUrl) {
                this.#urlInput.placeholder = "required";
                isValid = false;
            }
            if (!isValid) return;

            if (this.#bookmark) {
                if (newLabel !== this.#bookmark.label || newUrl !== this.#bookmark.url) {
                    this.#bookmark.setValues(newLabel, newUrl);
                    this.#anchorElement.textContent = newLabel;
                    this.#anchorElement.href = newUrl;
                }
            } else {
                this.#bookmark = new Bookmark(newLabel, newUrl);
                L.BookmarksWidget.bookmarks.addBookmark(this.#bookmark);
            }
            this.editing = false;
        }

        /**
         * Responds to the reset button.  Resets the text inputs to the bookmark's values.
         * If there is no bookmark, sets the inputs to empty strings.
         */
        reset() {
            this.#labelInput.placeholder = "Name";
            this.#urlInput.placeholder = "Location";
            if (this.#bookmark) {
                this.#labelInput.value = this.#bookmark.label;
                this.#urlInput.value = this.#bookmark.url;
            } else {
                this.#labelInput.value = "";
                this.#urlInput.value = "";
            }
        }

        /**
         * Update the editors anchor text and url with the bookmark's label and url.
         */
        update() {
            this.#anchorElement.textContent = this.#bookmark.label;
            this.#anchorElement.href = this.#bookmark.url;
            this.#truncateLabel();
        }

        // --- Private Methods ---

        /**
         * Handles button clicks, delegating to the appropriate method.
         * @private
         */
        #handleButtonClick(event) {
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
        #handleEditingChange(event) {
            const activeClass = `${this.#className}-active`;
            this.#srcNode.classList.toggle('active', event.newVal);
            this.#srcNode.classList.toggle(activeClass, event.newVal);

            if (event.newVal) {
                this.reset();
            }
        }

        /**
         * Truncates the link text if it's too long.
         * @private
         */
        #truncateLabel() {
            const label = this.#anchorElement.innerHTML;
            if (label.length > 130) {
                this.#anchorElement.innerHTML = `${label.substring(0, 130)}...`;
            }
        }

        /**
         * Sets a default value for the URL input on focus.
         * @private
         */
        #setDefaultUrlInputText() {
            if (this.#urlInput.value === "") {
                this.#urlInput.value = "https://";
            }
        }

        // --- Drag and Drop Handlers ---

        #handleDragStart(event) {
            this.startNodePositon = this.#position;
            const draggedNode = event.currentTarget;
            draggedNode.style.border = "1px solid #000";
            draggedNode.querySelector("div").classList.toggle("hidden");
            this.fire("dragStart", { position: this.#position, target: draggedNode });
        }

        #handleDragEnd(event) {
            event.preventDefault();
            const draggedNode = event.currentTarget;
            draggedNode.style.border = "none";
            draggedNode.querySelector("div").classList.toggle("hidden");
            this.fire("dragEnd");
        }

        #handleDragDrop(event) {
            event.preventDefault();
        }

        #handleDragOver(event) {
            event.preventDefault();
            if (this.startNodePositon !== this.#position) {
                this.fire("dragOver", { position: this.#position, target: event.currentTarget });
            }
        }
    }

    L.addEventTarget(BookmarkEditor, { prefix: 'bookmarkEditor' });
    L.BookmarkEditor = BookmarkEditor;
})();