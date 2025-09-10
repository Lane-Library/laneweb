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
        constructor({ srcNode, bookmarks }) {
            this.srcNode = srcNode;
            this.bookmarks = bookmarks;
            this.editors = [];
            this.editing = false;
            this._listElement = this.srcNode.querySelector("ul");

            // Instance properties for drag state
            this.from = -1; // The original index from dragStart
            this.to = -1;   // The destination index, updated on dragOver
            this.dragged_source = null; // The actual DOM node being dragged

            this._bindEvents();
            this._createInitialEditors();
        }

        /**
         * Centralized method for creating a BookmarkEditor instance and binding its events.
         * @private
         */
        _createEditor({ srcNode, bookmark, position }) {
            const editor = new BookmarkEditor({ srcNode, bookmark, position });

            editor.on("destroy", event => this._handleDestroyEditor(event));
            editor.on("dragOver", event => this._handleDragOver(event));
            editor.on("dragStart", event => this._handleDragStart(event));
            editor.on("dragEnd", event => this._handleDragEnd(event));

            return editor;
        }

        /**
         * Sets up event handlers for the main editor and the bookmarks model.
         * @private
         */
        _bindEvents = () => {
            // Add EventTarget attributes to the Bookmarks prototype
            L.addEventTarget(this, { prefix: 'bookmarksEditor' });

            this.srcNode.querySelectorAll("fieldset button").forEach(button => {
                button.addEventListener("click", this._handleButtonClick);
            });

            this.bookmarks.after("removeSync", this._handleBookmarksRemove);
            this.bookmarks.after("addSync", this._handleBookmarkAdd);
            this.bookmarks.after("updateSync", this._handleBookmarkUpdate);
            this.bookmarks.after("moveSync", this._handleBookmarkMove);
        }

        /**
         * Creates the initial set of BookmarkEditors from the existing DOM.
         * @private
         */
        _createInitialEditors = () => {
            const items = this._listElement.querySelectorAll("li");

            this.editors = Array.from(items).map((item, index) =>
                this._createEditor({
                    srcNode: item,
                    bookmark: this.bookmarks.getBookmark(index),
                    position: index
                })
            );

            // Show 'add' UI if no bookmarks are present.
            if (this.editors.length === 0) {
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
            const isAlreadyAdding = this.editors.length > 0 && !this.editors[0].bookmark;

            // destroy add bookmark editor if already present/open
            // otherwise, create a new add bookmark editor
            if (isAlreadyAdding) {
                this.editors[0].destroy();
            } else {
                const item = document.createElement('li');
                item.innerHTML = "<a href=''></a>";
                this._listElement.prepend(item);

                const editor = this._createEditor({ srcNode: item, position: 0 });
                this.editors.unshift(editor);
                editor.setEditing(true);
            }
        }

        /**
         * Return an Array of BookmarkEditors that have been written to disk (have an a/href]).
         * LANEWEB-10988: returning unsaved "add" editor causes many bugs
         * @private
         * @returns {Array}
         */
        _getSerializedEditors() {
            return this.editors.filter(editor => editor.bookmark);
        }

        // --- Event Handlers (defined as class properties for auto-binding) ---

        /**
         * Responds to the bookmarks:addSync event, call update() on the appropriate BookmarkEditor.
         * @private
         */
        _handleBookmarkAdd = () => {
            this.editors[0].update();
            this._syncPositions();
        }

        /**
         * Responds to the bookmarks:moveSync event, rearranges the editors appropriately
         * @private
         */
        _handleBookmarkMove = (event) => { 
            this.editors.splice(event.to, 0, this.editors.splice(event.from, 1)[0]);
            this._syncPositions();
        }


        /**
         * Responds to the bookmarks:removeSync event, calls destroy on each BookmarkEditor
         * associated with removed bookmarks.
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarksRemove = (event) => {
            const savedEditors = this._getSerializedEditors();
            // iterate backwards to safely destroy editors without messing up indices
            for (let i = event.positions.length - 1; i >= 0; i--) {
                const position = event.positions[i];
                savedEditors[position]?.destroy(true);
            }
            this._syncPositions();
        }

        /**
         * Responds to the bookmarks:updateSync event, calls update() on the appropriate BookmarkEditor.
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkUpdate = (event) => {
            const editors = this._getSerializedEditors();
            editors[event.position]?.update();
        }

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @private
         * @param event {CustomEvent}
         */
        _handleButtonClick = (event) => {
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
        _handleDestroyEditor = (event) => {
            const position = this.editors.indexOf(event.editor);
            if (position > -1) {
                this.editors.splice(position, 1);
            }
            this._syncPositions();
        }

        // --- Drag and Drop Handlers ---

        _handleDragStart = (event) => {
            this.dragged_source = event.target;
            this.from = event.position;
        }

        /**
         * Fired repeatedly as a dragged item is moved over other child editors.
         * @private
         */
        _handleDragOver = (event) => {
            // <li> being hovered over
            const dropTarget = event.target;

            // set the 'to' index for the final drop. This gets updated on every hover.
            this.to = this._getNodeIndex(dropTarget);

            // recalculate the dragged item's current index
            const currentIndexFrom = this._getNodeIndex(this.dragged_source);

            // guard against invalid states or no-op moves
            if (currentIndexFrom === -1 || this.to === -1 || currentIndexFrom === this.to) {
                return;
            }

            if (currentIndexFrom < this.to) {
                // Moving DOWN: Insert the dragged item *after* the drop target.
                dropTarget.after(this.dragged_source);
            } else {
                // Moving UP: Insert the dragged item *before* the drop target.
                dropTarget.before(this.dragged_source);
            }
        }

        /**
         * Fired when the drag operation ends. Finalizes the move.
         * @private
         */
        _handleDragEnd = () => {
            // Only fire the event if the original position and final position are different.
            // `this.from` was set at the start, `this.to` was set by the last dragOver.
            if (this.from !== this.to && this.to !== -1) {
                this.bookmarks.moveBookmark(this.to, this.from);
            }

            // clean up state
            this.dragged_source = null;
            this.from = -1;
            this.to = -1;
        }

        /**
         * Retrieves the index of a given node among its sibling elements.
         * @param {HTMLElement} node - The DOM element whose index is to be determined.
         * @returns {number} The zero-based index of the node among its siblings, or -1 if not found.
         */
        _getNodeIndex = (node) => {
            return Array.from(this._listElement.children).indexOf(node);
        }

        /**
         * Updates the `position` property of all editors to match their current order.
         * @private
         */
        _syncPositions = () => {
            this.editors.forEach((editor, index) => {
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
