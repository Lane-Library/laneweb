(function () {

    "use strict";


    let BookmarkEditor = L.BookmarkEditor,
        editorsNode = document.querySelector("#bookmarks-editor");


    /**
     * The BookmarksEditor.
     * Contains one BookmarkEditor for each bookmark.
     */
    class BookmarksEditor {
        constructor(args) {
            this.srcNode = args.srcNode;
            this.bookmarks = args.bookmarks;
            this.editing = false;
            this.bindUI();
            this.syncUI();

        }



        bindUI() {
            let bookmarks = this.bookmarks,
                buttons = this.srcNode.querySelectorAll("fieldset button");
            buttons.forEach(button => {
                button.addEventListener("click", (event) => this._handleButtonClick(event));
            });
            bookmarks.on("removeSync", (e) => this._handleBookmarksRemove(e));
            bookmarks.on("addSync", (e) => this._handleBookmarkAdd(e));
            bookmarks.on("updateSync", (e) => this._handleBookmarkUpdate(e));
            bookmarks.on("moveSync", (e) => this._handleBookmarkMove(e));
        }

        /**
         * Creates the BookmarkEditors.
         * @method syncUI
         */
        syncUI() {
            let editor, editors = [], i,
                srcNode = this.srcNode,
                items = srcNode.querySelectorAll("li"),
                bookmarks = this.bookmarks;
            for (i = 0; i < items.length; i++) {
                editor = new BookmarkEditor({ srcNode: items.item(i), bookmark: bookmarks.getBookmark(i), position: i });
                editor.on("destroy", (e) => this._handleDestroyEditor(e));
                editor.on("dragDrop", (e) => this._handleDragDrop(e));
                editor.on("dragOver", (e) => this._handleDragOver(e));
                editor.on("dragStart", (e) => this._handleDragStart(e));
                editors.push(editor);
            }
            this.editors = editors;
            // show add bookmarks if no bookmarks present
            if (0 == items.length) {
                this.add();
            }
        }

        /**
         * Responds to a click on the add button.  Adds a list item and associated BookmarkEditor to
         * the top of the list and sets it editing state to true.
         * @method add
         */
        add() {
            let items = this.srcNode.querySelector("ul"),
                item = document.createElement('li'),
                addBookmarkContainer = document.querySelector(".addBookmarkContainer"),
                editors = this.editors,
                adding = editors.length && editors[0].editing,
                editor;
            item.innerHTML += "<a href=''></a>";
            // toggle add bookmark button
            if (addBookmarkContainer) {
                addBookmarkContainer.classList.toggle("active");
            }
            // destroy add bookmark editor if already present/open
            // otherwise, create a new add bookmark editor
            if (adding) {
                editors[0].destroy();
            } else {
                items.prepend(item);
                editor = new BookmarkEditor({ srcNode: item, position: 0 });
                editor.on("destroy", (e) => this._handleDestroyEditor(e));
                editor.on("dragDrop", (e) => this._handleDragDrop(e));
                editor.on("dragOver", (e) => this._handleDragOver(e));
                editor.on("dragStart", (e) => this._handleDragStart(e));
                editors.unshift(editor);
                editor.setEditing(true);
            }
        }

        /**
         * Return an Array of BookmarkEditors that have been written to disk (have an a/href]).
         * LANEWEB-10988: returning unsaved "add" editor causes many bugs
         * @method _getSerializedEditors
         * @private
         * @returns {Array}
         */
        _getSerializedEditors() {
            let filteredEditors = [];
            this.editors.forEach(function (editor) {
                if (editor.srcNode) {
                    filteredEditors.push(editor);
                }
            });
            return filteredEditors;
        }

        /**
         * Responds to the bookmarks:addSync event, call update() on the appropriate BookmarkEditor.
         * @method _handleBookmarkAdd
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkAdd(event) {
            this.editors[event.target.indexOf(event.bookmark)].update();
            this._syncPosition();
        }

        /**
         * Responds to the bookmarks:moveSync event, rearranges the editors appropriately
         * @method _handleBookmarkMove
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkMove(event) {
            this.editors.splice(this.to, 0, this.editors.splice(this.from, 1)[0]);
            this._syncPosition();
        }


        /**
     * Responds to the bookmarks:removeSync event, calls destroy on each BookmarkEditor
     * associated with removed bookmarks.
     * @method _handleBookmarksRemove
     * @private
     * @param event {CustomEvent}
     */
        _handleBookmarksRemove(event) {
            let i, editors = this._getSerializedEditors();
            for (i = event.positions.length - 1; i >= 0; --i) {
                editors[event.positions[i]].destroy(true);
            }
            this._syncPosition();
        }

        /**
         * Responds to the bookmarks:updateSync event, calls update() on the appropriate BookmarkEditor.
         * @method _handleBookmarkUpdate
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkUpdate(event) {
            let editors = this._getSerializedEditors();
            editors[event.position].update();
        }

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @method _handleButtonClick
         * @private
         * @param event {CustomEvent}
         */
        _handleButtonClick(event) {
            event.preventDefault();
            //see case 67695
            //pressing return generates a click on the add button for some reason
            //pageX is 0 in that situation
            if (event.pageX !== 0) {
                let fn = this[event.currentTarget.getAttribute("value")];
                if (fn) {
                    fn.call(this);
                }
            }
        }

        /**
         * Removes a destroyed editor from the backing Array.
         * @method _handleDestroyEditor
         * @private
         * @param event {CustomEvent}
         */
        _handleDestroyEditor(event) {
            let editors = this.editors,
                position = editors.indexOf(event.editor);
            editors.splice(position, 1);
            this._syncPosition();
        }


        _handleDrag(event) {
            let draggable = event.target,
                parent = draggable.parentElement,

                // Get the bounding rectangle of the parent element
                parentRect = parent.getBoundingClientRect();

            // Calculate the maximum allowed positions for the draggable element
            let maxX = parentRect.right - draggable.offsetWidth,
                maxY = parentRect.bottom - draggable.offsetHeight,

                // Ensure the draggable element stays within the parent's boundaries
                x = event.clientX - draggable.offsetWidth / 2,
                y = event.clientY - draggable.offsetHeight / 2;

            x = Math.max(0, Math.min(x, maxX));
            y = Math.max(0, Math.min(y, maxY));

            // Update the draggable element's position
            draggable.style.left = x + 'px';
            draggable.style.top = y + 'px';

        }


        _handleDragStart(e) {
            this.dragged_source = e.target;
            this.from = e.position;
        }

        _handleDragOver(event) {
            this.to = this.getNodeIndex(event.target);
            let ul = this.srcNode.querySelector("ul"),
                lis = ul.children,
                index_from = this.getNodeIndex(this.dragged_source),
                drag = lis[index_from],
                drop = lis[this.to];

            if (drop && drop.classList.contains('bookmark-editor-content')) {
                if (this.to > index_from) {
                    if (this.to + 1 == lis.length) {
                        ul.appendChild(drag);
                    }
                    else {
                        drop = lis[this.to + 1];
                    }
                }
                if (this.to + 1 != lis.length) {
                    drop.parentNode.insertBefore(drag, drop);
                }
            }
        }

        getNodeIndex(node) {
            let lis = this.srcNode.querySelector("ul").children;
            for (let i = 0; i < lis.length; i++) {
                if (lis[i] === node) {
                    return i;
                }
            }
            return -1;
        }


        _handleDragDrop(event) {
            this.bookmarks.moveBookmark(this.to, this.from);
        }
        _syncPosition() {
            let i = 0;
            this.editors.forEach(function (editor) {
                editor.position = i++;
            });
        };

    }
    //Create a new BookmarksEditor
    if (editorsNode) {
        if (L.BookmarksWidget) {
            L.BookmarksEditor = new BookmarksEditor({
                srcNode: document.querySelector("#bookmarks-editor"),
                bookmarks: L.BookmarksWidget.bookmarks
            });
        } else {
            // case 141805 bookmark edit buttons fail if bookmarks editor not initialized
            editorsNode.querySelectorAll("button").forEach(function (node) {
                node.addEventListener("click", function (event) {
                    event.preventDefault();
                });
            });
        }
    }
})();
