(() => {

    "use strict";

    const BookmarkEditor = L.BookmarkEditor;
    const editorsNode = document.querySelector("#bookmarks-editor");

    if (!editorsNode) {
        return;
    }

    /**
     * The BookmarksEditor.
     * Contains one BookmarkEditor for each bookmark.
     */
    class BookmarksEditor {
        #srcNode;
        #bookmarks;
        #editors;
        #listElement;

        // Instance properties for drag state
        #from;
        #to;
        #draggedSource;

        constructor({ srcNode, bookmarks }) {
            this.#srcNode = srcNode;
            this.#bookmarks = bookmarks;
            this.#editors = [];
            this.#listElement = this.#srcNode.querySelector("ul");

            this.#from = -1; // The original index from dragStart
            this.#to = -1;   // The destination index, updated on dragOver
            this.#draggedSource = null; // The actual DOM node being dragged

            this.#bindEvents();
            this.#createInitialEditors();
        }

        /**
         * Centralized method for creating a BookmarkEditor instance and binding its events.
         * @private
         */
        #createEditor({ srcNode, bookmark, position }) {
            const editor = new BookmarkEditor({ srcNode, bookmark, position });

            editor.on("destroy", event => this.#handleDestroyEditor(event));
            editor.on("dragOver", event => this.#handleDragOver(event));
            editor.on("dragStart", event => this.#handleDragStart(event));
            editor.on("dragEnd", event => this.#handleDragEnd(event));

            return editor;
        }

        /**
         * Sets up event handlers for the main editor and the bookmarks model.
         * @private
         */
        #bindEvents = () => {
            // Add EventTarget attributes to the Bookmarks prototype
            L.addEventTarget(this, { prefix: 'bookmarksEditor' });

            this.#srcNode.querySelectorAll("fieldset button").forEach(button => {
                button.addEventListener("click", this.#handleButtonClick);
            });

            this.#bookmarks.after("removeSync", this.#handleBookmarksRemove);
            this.#bookmarks.after("addSync", this.#handleBookmarkAdd);
            this.#bookmarks.after("updateSync", this.#handleBookmarkUpdate);
            this.#bookmarks.after("moveSync", this.#handleBookmarkMove);
        }

        /**
         * Creates the initial set of BookmarkEditors from the existing DOM.
         * @private
         */
        #createInitialEditors = () => {
            const items = this.#listElement.querySelectorAll("li");

            this.#editors = Array.from(items).map((item, index) =>
                this.#createEditor({
                    srcNode: item,
                    bookmark: this.#bookmarks.getBookmark(index),
                    position: index
                })
            );

            // Show 'add' UI if no bookmarks are present.
            if (this.#editors.length === 0) {
                this.add();
            }
        }

        /**
         * Responds to a click on the add button.  Adds a list item and associated BookmarkEditor to
         * the top of the list and sets it editing state to true.
         * @method add
         */
        add() {
            const addBookmarkContainer = document.querySelector(".addBookmarkContainer");

            // toggle add bookmark button
            addBookmarkContainer?.classList.toggle("active");

            // check if the first editor is an unsaved "add" editor.
            const isAlreadyAdding = this.#editors.length > 0 && !this.#editors[0].bookmark;

            // destroy add bookmark editor if already present/open
            // otherwise, create a new add bookmark editor
            if (isAlreadyAdding) {
                this.#editors[0].destroy();
            } else {
                const item = document.createElement('li');
                item.innerHTML = "<a href=''></a>";
                this.#listElement.prepend(item);

                const editor = this.#createEditor({ srcNode: item, position: 0 });
                this.#editors.unshift(editor);
                editor.editing = true;
            }
        }

        /**
         * Return an Array of BookmarkEditors that have been written to disk (have an a/href]).
         * LANEWEB-10988: returning unsaved "add" editor causes many bugs
         * @private
         * @returns {Array}
         */
        #getSerializedEditors() {
            return this.#editors.filter(editor => editor.bookmark);
        }

        // --- Event Handlers (defined as class properties for auto-binding) ---

        /**
         * Responds to the bookmarks:addSync event, call update() on the appropriate BookmarkEditor.
         * @private
         */
        #handleBookmarkAdd = () => {
            this.#editors[0].update();
            this.#syncPositions();
        }

        /**
         * Responds to the bookmarks:moveSync event, rearranges the editors appropriately
         * @private
         */
        #handleBookmarkMove = (event) => {
            this.#editors.splice(event.to, 0, this.#editors.splice(event.from, 1)[0]);
            this.#syncPositions();
        }


        /**
         * Responds to the bookmarks:removeSync event, calls destroy on each BookmarkEditor
         * associated with removed bookmarks.
         * @private
         * @param event {CustomEvent}
         */
        #handleBookmarksRemove = (event) => {
            const savedEditors = this.#getSerializedEditors();
            // iterate backwards to safely destroy editors without messing up indices
            for (let i = event.positions.length - 1; i >= 0; i--) {
                const position = event.positions[i];
                savedEditors[position]?.destroy(true);
            }
            this.#syncPositions();
        }

        /**
         * Responds to the bookmarks:updateSync event, calls update() on the appropriate BookmarkEditor.
         * @private
         * @param event {CustomEvent}
         */
        #handleBookmarkUpdate = (event) => {
            const editors = this.#getSerializedEditors();
            editors[event.position]?.update();
        }

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @private
         * @param event {CustomEvent}
         */
        #handleButtonClick = (event) => {
            event.preventDefault();
            // see case 67695
            // pressing return generates a click on the add button for some reason
            // pageX is 0 in that situation
            if (event.pageX === 0) return;

            const action = event.currentTarget.value;
            this[action]?.();
        }

        /**
         * Removes a destroyed editor from the backing Array.
         * @private
         * @param event {CustomEvent}
         */
        #handleDestroyEditor = (event) => {
            const position = this.#editors.indexOf(event.editor);
            if (position > -1) {
                this.#editors.splice(position, 1);
            }
            this.#syncPositions();
        }

        // --- Drag and Drop Handlers ---

        #handleDragStart = (event) => {
            this.#draggedSource = event.target;
            this.#from = event.position;
        }

        /**
         * Fired repeatedly as a dragged item is moved over other child editors.
         * @private
         */
        #handleDragOver = (event) => {
            // <li> being hovered over
            const dropTarget = event.target;

            // set the 'to' index for the final drop. This gets updated on every hover.
            this.#to = this.#getNodeIndex(dropTarget);

            // recalculate the dragged item's current index
            const currentIndexFrom = this.#getNodeIndex(this.#draggedSource);

            // guard against invalid states or no-op moves
            if (currentIndexFrom === -1 || this.#to === -1 || currentIndexFrom === this.#to) {
                return;
            }

            if (currentIndexFrom < this.#to) {
                // Moving DOWN: Insert the dragged item *after* the drop target.
                dropTarget.after(this.#draggedSource);
            } else {
                // Moving UP: Insert the dragged item *before* the drop target.
                dropTarget.before(this.#draggedSource);
            }
        }

        /**
         * Fired when the drag operation ends. Finalizes the move.
         * @private
         */
        #handleDragEnd = () => {
            // Only fire the event if the original position and final position are different.
            // `this.from` was set at the start, `this.to` was set by the last dragOver.
            if (this.#from !== this.#to && this.#to !== -1) {
                this.#bookmarks.moveBookmark(this.#to, this.#from);
            }

            // clean up state
            this.#draggedSource = null;
            this.#from = -1;
            this.#to = -1;
        }

        /**
         * Retrieves the index of a given node among its sibling elements.
         * @param {HTMLElement} node - The DOM element whose index is to be determined.
         * @returns {number} The zero-based index of the node among its siblings, or -1 if not found.
         */
        #getNodeIndex = (node) => {
            return Array.from(this.#listElement.children).indexOf(node);
        }

        /**
         * Updates the `position` property of all editors to match their current order.
         * @private
         */
        #syncPositions = () => {
            this.#editors.forEach((editor, index) => {
                editor.position = index;
            });
        }

    }
    // Create a new BookmarksEditor
    if (L.BookmarksWidget) {
        L.BookmarksEditor = new BookmarksEditor({
            srcNode: editorsNode,
            bookmarks: L.BookmarksWidget.bookmarks
        });
    } else {
        // case 141805 bookmark edit buttons fail if bookmarks editor not initialized
        editorsNode.querySelectorAll("button").forEach(node => {
            node.addEventListener("click", event => event.preventDefault());
        });
    }
})();
