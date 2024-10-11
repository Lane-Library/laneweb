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
            this.render = args.render;
            this.editing = false;
            if (this.render) {
                this.bindUI();
                this.syncUI();
            }
        }



        bindUI() {
            let srcNode = this.srcNode,
                bookmarks = this.bookmarks,
                dragManager = Y.DD.DDM;
            const buttons = this.srcNode.querySelectorAll("fieldset button");
            buttons.forEach(button => {
                button.addEventListener("click", (event) => this._handleButtonClick(event));
            });
            bookmarks.on("removeSync", (e) => this._handleBookmarksRemove(e));
            bookmarks.on("addSync", (e) => this._handleBookmarkAdd(e));
            bookmarks.on("updateSync", (e) => this._handleBookmarkUpdate(e));
            bookmarks.on("moveSync", (e) => this._handleBookmarkMove(e));

            // dragManager.on('drag:start', this._handleDragStart, this);
            // dragManager.on('drag:end', this._handleDragEnd, this);
            this._lastY = 0;
            this._goingUp = false;
            // dragManager.on('drag:drag', this._handleDrag, this);
            // dragManager.on('drop:over', this._handleDropOver, this);
            //this.publish("move", { defaultFn: this._editorMoved });
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
                editor = new BookmarkEditor({ srcNode: items.item(i), render: true, bookmark: bookmarks.getBookmark(i) });
                editor.on("destroy", (e) => this._handleDestroyEditor(e));
                editors.push(editor);
            }
            this.editors = editors;
            this._syncDD();
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
                editor = new BookmarkEditor({ srcNode: item, render: true });
                editor.on("destroy", (e) => this._handleDestroyEditor(e));
                editors.unshift(editor);
                editor.setEditing(true);
            }
        }

        _editorMoved() {
            this.bookmarks.moveBookmark(this._to, this._from);
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
            this.editors[0].update();
            this._syncDD();
        }

        /**
         * Responds to the bookmarks:moveSync event, rearranges the editors appropriately
         * @method _handleBookmarkMove
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkMove(event) {
            let editors = this._getSerializedEditors();
            editors.splice(event.to, 0, editors.splice(event.from, 1)[0]);
            this.editors = editors;
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

                // this._dd[event.positions[i]].destroy(true);
                // this._dd.splice(event.positions[i], 1);

                editors[event.positions[i]].destroy(true);
            }
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
        }

        /**
         * @method _handleDrag
         * @private
         * @param event {CustomEvent}
         */
        _handleDrag(event) {
            //Get the last y point
            let y = event.target.lastXY[1];
            //is it greater than the lastY var?
            if (y < this._lastY) {
                //We are going up
                this._goingUp = true;
            } else {
                //We are going down.
                this._goingUp = false;
            }
            //Cache for next check
            this._lastY = y;
        }

        /**
         * @method _handleDragEnd
         * @private
         * @param event {CustomEvent}
         */
        _handleDragEnd(event) {
            let drag = event.target;
            //Put our styles back
            drag.get('node').setStyles({
                visibility: '',
                opacity: '1'
            });
            this._to = this.srcNode.querySelectorAll("bookmark-editor").indexOf(drag.get("node"));
            if (this._to !== this._from) {
                this.fire("move", { to: this._to, from: this._from });
            }
        }

        /**
         * @method _handleDragStart
         * @private
         * @param event {CustomEvent}
         */
        _handleDragStart(event) {
            //Get our drag object
            let drag = event.target, node = drag.get("node"), dragNode = drag.get("dragNode");
            //Set some styles here
            dragNode.empty();
            node.setStyle('opacity', '.25');
            dragNode.append(node.one("a").cloneNode(true));
            dragNode.setStyles({
                opacity: '.5',
                textAlign: "left",
                borderColor: node.getStyle('borderColor'),
                backgroundColor: node.getStyle('backgroundColor')
            });
            dragNode.one("a").setStyles({
                display: "block",
                padding: "12px 0 12px 36px",
                fontSize: "12px"
            });
            this._from = this.srcNode.querySelectorAll("bookmark-editor").indexOf(node);
        }

        /**
         * @method _handleDragOver
         * @private
         * @param event {CustomEvent}
         */
        _handleDropOver(event) {
            //Get a reference to our drag and drop nodes
            let drag = event.drag.get('node'),
                drop = event.drop.get('node');

            //Are we dropping on an editor node?
            if (drop.hasClass('yui3-bookmark-editor')) {
                //Are we not going up?
                if (!this._goingUp) {
                    drop = drop.get('nextSibling');
                }
                //Add the node to this list
                event.drop.get('node').get('parentNode').insertBefore(drag, drop);
                //Resize this nodes shim, so we can drop on it later.
                event.drop.sizeShim();
            }
        }

        _syncDD() {
            let i, srcNode = this.srcNode,
                editors = this.editors;
            this._dd = this._dd || [];
            for (i = 0; i < this._dd.length; i++) {
                this._dd[i].destroy();
            }
            this._dd = [];
            // for (i = 0; i < editors.length; i++) {
            //     this._dd.push(new Y.DD.Drag({
            //         node: editors[i].get("boundingBox"),
            //         target: {
            //             padding: '0 0 0 20',
            //             useShim: false
            //         }
            //     }).plug(Y.Plugin.DDConstrained, {
            //         constrain: srcNode.one("ul")
            //     }).plug(Y.Plugin.DDProxy, {
            //         moveOnEnd: false
            //     }).removeInvalid('a'));
            // }
        }
    };


    //Create a new BookmarksEditor
    if (editorsNode) {
        if (L.BookmarksWidget) {
            L.BookmarksEditor = new BookmarksEditor({
                srcNode: document.querySelector("#bookmarks-editor"),
                bookmarks: L.BookmarksWidget.bookmarks,
                render: true
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
