(function() {

    "use strict";

    var BookmarksEditor,
        BookmarkEditor = L.BookmarkEditor,
        editorsNode = document.querySelector("#bookmarks-editor");


    /**
     * The BookmarksEditor.
     * Contains one BookmarkEditor for each bookmark.
     */
    BookmarksEditor = Y.Base.create("bookmarks-editor", Y.Widget, [], {

        /**
         * Sets up various event handlers.
         * @method bindUI
         */
        bindUI : function() {
            var srcNode = this.get("srcNode"),
                bookmarks = this.get("bookmarks"),
                dragManager = Y.DD.DDM;
            srcNode.all("fieldset button").on("click", this._handleButtonClick, this);
            bookmarks.after("removeSync", this._handleBookmarksRemove, this);
            bookmarks.after("addSync", this._handleBookmarkAdd, this);
            bookmarks.after("updateSync", this._handleBookmarkUpdate, this);
            bookmarks.after("moveSync", this._handleBookmarkMove, this);

            dragManager.on('drag:start', this._handleDragStart, this);
            dragManager.on('drag:end', this._handleDragEnd, this);
            this._lastY = 0;
            this._goingUp = false;
            dragManager.on('drag:drag', this._handleDrag, this);
            dragManager.on('drop:over', this._handleDropOver, this);
            this.publish("move", {defaultFn : this._editorMoved});
        },

        /**
         * Creates the BookmarkEditors.
         * @method syncUI
         */
        syncUI : function() {
            var editor, editors = [], i,
                srcNode = this.get("srcNode"),
                items = srcNode.all("li"),
                bookmarks = this.get("bookmarks");
            for (i = 0; i < items.size(); i++) {
                editor = new BookmarkEditor({srcNode : items.item(i), render : true, bookmark : bookmarks.getBookmark(i)});
                editor.after("destroy", this._handleDestroyEditor, this);
                editors.push(editor);
            }
            this.set("editors", editors);
            this._syncDD();
        },

        /**
         * Responds to a click on the add button.  Adds a list item and associated BookmarkEditor to
         * the top of the list and sets it editing state to true.
         * @method add
         */
        add : function() {
            var items = this.get("srcNode").one("ul"),
                item = Y.Node.create("<li><input type=\"checkbox\" checked=\"checked\"/><a></a></li>"),
                editors = this.get("editors"),
                editing = false,
                editor;

            editors.forEach(function(edtr) {
                if (edtr.get("editing")) {
                    editing = true;
                }
            });
                
            if(!editing) {
                items.prepend(item);
                editor = new BookmarkEditor({srcNode : item, render : true});
                editor.after("destroy", this._handleDestroyEditor, this);
                editors.unshift(editor);
                editor.set("editing", true);
            }
        },

        /**
         * @method _editorMoved
         * @private
         */
        _editorMoved : function() {
            this.get("bookmarks").moveBookmark(this._to, this._from);
        },

        /**
         * Return an Array of BookmarkEditors that have been written to disk (have an a/href]).
         * LANEWEB-10988: returning unsaved "add" editor causes many bugs
         * @method _getSerializedEditors
         * @private
         * @returns {Array}
         */
        _getSerializedEditors : function() {
            var filteredEditors = [];
            this.get("editors").forEach(function(editor) {
                if (editor.get("srcNode").one("a").get("href") != "") {
                    filteredEditors.push(editor);
                }
            });
            return filteredEditors;
        },

        /**
         * Responds to the bookmarks:addSync event, call update() on the appropriate BookmarkEditor.
         * @method _handleBookmarkAdd
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkAdd : function(event) {
            this.get("editors")[event.target.indexOf(event.bookmark)].update();
            this._syncDD();
        },

        /**
         * Responds to the bookmarks:moveSync event, rearranges the editors appropriately
         * @method _handleBookmarkMove
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkMove : function(event) {
            var editors = this._getSerializedEditors();
            editors.splice(event.to, 0, editors.splice(event.from, 1)[0]);
        },

        /**
         * Responds to the bookmarks:removeSync event, calls destroy on each BookmarkEditor
         * associated with removed bookmarks.
         * @method _handleBookmarksRemove
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarksRemove : function(event) {
            var i, editors = this._getSerializedEditors();
            for (i = event.positions.length - 1; i >= 0; --i) {
                this._dd[event.positions[i]].destroy(true);
                this._dd.splice(event.positions[i], 1);
                editors[event.positions[i]].destroy(true);
            }
        },

        /**
         * Responds to the bookmarks:updateSync event, calls update() on the appropriate BookmarkEditor.
         * @method _handleBookmarkUpdate
         * @private
         * @param event {CustomEvent}
         */
        _handleBookmarkUpdate : function(event) {
            var editors = this._getSerializedEditors();
            editors[event.position].update();
        },

        /**
         * The click handler for buttons, delegates to the function named the same as the buttons value.
         * @method _handleButtonClick
         * @private
         * @param event {CustomEvent}
         */
        _handleButtonClick : function(event) {
            event.preventDefault();
            //see case 67695
            //pressing return generates a click on the add button for some reason
            //pageX is 0 in that situation
            if (event.pageX !== 0) {
                var fn = this[event.currentTarget.getAttribute("value")];
                if (fn) {
                    fn.call(this);
                }
            }
        },

        /**
         * Removes a destroyed editor from the backing Array.
         * @method _handleDestroyEditor
         * @private
         * @param event {CustomEvent}
         */
        _handleDestroyEditor : function(event) {
            var editors = this.get("editors"),
                position = editors.indexOf(event.target);
            editors.splice(position, 1);
        },

        /**
         * @method _handleDrag
         * @private
         * @param event {CustomEvent}
         */
        _handleDrag :  function(event) {
            //Get the last y point
            var y = event.target.lastXY[1];
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
        },

        /**
         * @method _handleDragEnd
         * @private
         * @param event {CustomEvent}
         */
        _handleDragEnd : function(event) {
            var drag = event.target;
            //Put our styles back
            drag.get('node').setStyles({
                visibility: '',
                opacity: '1'
            });
            this._to = this.get("srcNode").all(".yui3-bookmark-editor").indexOf(drag.get("node"));
            if (this._to !== this._from) {
                this.fire("move", {to : this._to, from : this._from});
            }
        },

        /**
         * @method _handleDragStart
         * @private
         * @param event {CustomEvent}
         */
        _handleDragStart : function(event) {
            //Get our drag object
            var drag = event.target, node = drag.get("node"), dragNode = drag.get("dragNode");
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
            this._from = this.get("srcNode").all(".yui3-bookmark-editor").indexOf(node);
        },

        /**
         * @method _handleDragOver
         * @private
         * @param event {CustomEvent}
         */
        _handleDropOver : function(event) {
            //Get a reference to our drag and drop nodes
            var drag = event.drag.get('node'),
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
        },

        _syncDD : function() {
            var i, srcNode = this.get("srcNode"),
                editors = this.get("editors");
            this._dd = this._dd || [];
            for (i = 0; i < this._dd.length; i++) {
                this._dd[i].destroy();
            }
            this._dd = [];
            for (i = 0; i < editors.length; i++) {
                this._dd.push(new Y.DD.Drag({
                    node : editors[i].get("boundingBox"),
                    target: {
                        padding: '0 0 0 20',
                        useShim: false
                    }}).plug(Y.Plugin.DDConstrained, {
                        constrain : srcNode.one("ul")
                    }).plug(Y.Plugin.DDProxy, {
                        moveOnEnd: false
                    }).removeInvalid('a'));
            }
        }
    },
    {
        ATTRS : {
            editors : {
                value : null
            },
            bookmarks : {
                value : null
            }
        }
    });

    //Create a new BookmarksEditor
    if (editorsNode) {
        if (L.BookmarksWidget) {
            L.BookmarksEditor = new BookmarksEditor({
                srcNode : "#bookmarks-editor",
                bookmarks : L.BookmarksWidget.get("bookmarks"),
                render : true});
        } else {
            // case 141805 bookmark edit buttons fail if bookmarks editor not initialized
            editorsNode.querySelectorAll("button").forEach(function(node) {
                node.addEventListener("click", function(event) {
                    event.preventDefault();
                });
            });
        }
    }
})();
