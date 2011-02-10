(function() {
    if (Y.one("#bookmarks")) {

    	/**
    	 * <p>This class represents the Bookmarks Widget.</p>
    	 * 
         * @class Bookmarks
         * @constructor
    	 */
        Bookmarks = function() {
            Bookmarks.superclass.constructor.apply(this, arguments);
        };
        Bookmarks.NAME = "bookmarks";
        Bookmarks.ATTRS = {
                editing: {
                    value: false
                },
                bookmarks: {
                    valueFn : function() {
                        var i, b, node,
                        value = [],
                        lis = this.get("contentBox").all("li"),
                        size = lis.size();
                        for (i = 0; i < size; i++) {
                            node = lis.item(i);
                            node.insert(Y.Node.create(Bookmarks.DELETE_TEMPLATE));
                            value.push(node);
                        }
                        return value;
                    }
                },
                io : {
                    value : Y.io
                },
                render : {
                    value : true
                },
                srcNode : {
                    valueFn : function() {
                        return Y.one("#bookmarks");
                    }
                },
                strings: {
                    value: {
                        editing:"done",
                        notEditing:"edit"
                    }
                },
                toggle: {
                    valueFn: function() {
                        return Y.one("#bookmarks h3 a");
                    }
                }
        };
        Bookmarks.DELETE_TEMPLATE = "<a class=\"yui3-bookmark-remove yui3-bookmark-edit\">remove</a>";
        Bookmarks.CREATE_TEMPLATE = "<li><a></a>" + Bookmarks.DELETE_TEMPLATE + "</li>";
        Bookmarks.ADD_BOOKMARK_TEMPLATE = "<p class=\"yui3-bookmark-edit\">Click on any link to add a bookmark.</p>";
        Y.extend(Bookmarks, Y.Widget, {
            addBookmark : function(bookmark, position) {
                position = position === undefined ? 0 : position;
                if (bookmark.label && bookmark.url) {
                    var node = Y.Node.create(Bookmarks.CREATE_TEMPLATE);
                    node.one("a").set("innerHTML", bookmark.label);
                    node.one("a").set("href", bookmark.url);
                    var ULNode = this.get("contentBox").one("ul");
                    if (!ULNode) {
                        ULNode = Y.Node.create("<ul/>");
                        this.get("contentBox").one(".bd").set("innerHTML","<ul/>");
                        ULNode = this.get("contentBox").one("ul");
                    }
                    this.get("contentBox").one("ul").insert(node, position);
                    this.get("bookmarks").splice(position, 0, node);
                    node.one(".yui3-bookmark-edit").on("click", this._handleRemoveClick, this);
                    var data = "url=" + bookmark.url + "&label=" + bookmark.label;
                    this.get("io")("/././bookmarks/add", {
                        method : "post",
                        data : data
                    });
                }
            },
            removeBookmark : function(position) {
                var bookmarks = this.get("bookmarks"),
                node = bookmarks[position];
                Y.Event.purgeElement(node, true);
                node.get("parentNode").removeChild(node);
                bookmarks.splice(position, 1);
            },
//            moveUp : function(position) {
//            var bookmarks = this.get("bookmarks");
//            bookmarks.splice(position - 1, 2, bookmarks[position], bookmarks[position - 1]);
//            var nodeList = this.get("contentBox").all("li");
//            nodeList.item(position).swap(nodeList.item(position - 1));
//            },
//            moveDown : function(position) {
//            var bookmarks = this.get("bookmarks");
//            bookmarks.splice(position, 2, bookmarks[position + 1], bookmarks[position]);
//            var nodeList = this.get("contentBox").all("li");
//            nodeList.item(position).swap(nodeList.item(position + 1));
//            },
            renderUI : function() {
                this.get("contentBox").appendChild(Y.Node.create(Bookmarks.ADD_BOOKMARK_TEMPLATE));
                this.get("contentBox").all(".yui3-bookmark-edit, .yui3-bookmarks-edit").addClass("yui3-bookmarks-hide");
            },
            bindUI : function() {
                this.after("editingChange", this._afterEditingChange);
                this.get("toggle").on("click", this._toggleEdit, this);
                this.get("contentBox").all(".yui3-bookmark-edit").on("click", this._handleRemoveClick, this);
//                this.get("contentBox").one("input[type='submit']").on("click", this._handleAddClick, this);
            },
            _handleAddClick: function(e) {
                var div = e.target.ancestor(".yui3-bookmarks-edit");
                var label = div.one("input[name='label']");
                var url = div.one("input[name='url']");
                var form = div.one("form");
                var data = "url=" + url.get("value") + "&label=" + label.get("value");
                this.addBookmark({label:label.get("value"),url:url.get("value")});
                this.get("io")("/././bookmarks/add", {
                    method : "post",
                    data : data
                });
                label.set("value","");
                url.set("value","");
            },
            _handleRemoveClick : function(e) {
                var i, ul = e.target.ancestor("ul").all(".yui3-bookmark-edit");
                for (i = 0; i < ul.size(); i++) {
                    if (ul.item(i) === e.target) {
                        this.removeBookmark(i);
                        this.get("io")("/././bookmarks/remove", {
                            method : "post",
                            data : "position=" + i
                        });
                        break;
                    }
                }
            },
            _pageLinkEventHandle : null,
            _afterEditingChange : function(e) {
                var strings = this.get("strings");
                this.get("toggle").set("innerHTML", e.newVal ? strings.editing : strings.notEditing);
                var editables = this.get("contentBox").all(".yui3-bookmark-edit, .yui3-bookmarks-edit");
                if (e.newVal) {
                    this._pageLinkEventHandle = Y.one("document").on("click", this._pageLinkHandler, this);
                    editables.removeClass("yui3-bookmarks-hide");
                } else {
                    this._pageLinkEventHandle.detach();
                    editables.addClass("yui3-bookmarks-hide");
                }
            },
            _toggleEdit : function(e) {
                e.preventDefault();
                var editing = this.get("editing");
                this.set("editing", !editing);
            },
            _pageLinkHandler : function(event) {
                event.preventDefault();
                var target = event.target;
                if (target.get("nodeName") === "A" && target.inDoc() && target.ancestor("#bookmarks") === null) {
                    var div = this.get("contentBox");
                    var label = target.get("textContent");
                    var url = target.get("href");
                    this.addBookmark({label:label,url:url});
                }
            }
        });
        Y.lane.Bookmarks = new Bookmarks();
    }
})();