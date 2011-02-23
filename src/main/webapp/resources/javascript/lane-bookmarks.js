(function() {
    
    //only do this if there are bookmarks
    if (Y.one("#bookmarks")) {

        /**
         * <p>This class represents the Bookmarks Widget.</p>
         * 
         * @class Bookmarks
         * @base Y.Widget
         * @constructor
         */
        var Bookmarks = function() {
            Bookmarks.superclass.constructor.apply(this, arguments);
        };
        
        /**
         * @final class name
         */
        Bookmarks.NAME = "bookmarks";
        
        /**
         * default attributes
         */
        Bookmarks.ATTRS = {
                editing: {
                    value: false
                },
                bookmarks: {
                    valueFn : function() {
                        var i, node,
                        value = [],
                        lis = this.get("contentBox").all("li"),
                        size = lis.size();
                        for (i = 0; i < size; i++) {
                            node = lis.item(i);
                            node.insert(Y.Node.create(Bookmarks.REMOVE_TEMPLATE));
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
        
        /**
         * @final
         * The markup template for the individual bookmark remove link.
         */
        Bookmarks.REMOVE_TEMPLATE = "<a class=\"yui3-bookmarks-remove yui3-bookmarks-edit\">remove</a>";
        
        /**
         * @final
         * The markup template for new bookmarks.
         */
        Bookmarks.CREATE_TEMPLATE = "<li><a></a>" + Bookmarks.REMOVE_TEMPLATE + "</li>";
        
        /**
         * @final
         * The markup template for the add bookmark form.
         */
        Bookmarks.ADD_BOOKMARK_TEMPLATE = "<div class=\"yui3-bookmarks-edit\">" +
            '<form><h4>add a bookmark</h4><div><label>url:</label><input name="url" type="text" /></div><div><label>label:</label><input name="label" type="text" /></div><input type="submit" value="add" /></form>' +
        "<p>or click on any link to add a bookmark.</p></div>";
        
        /**
         * extend Y.Widget
         */
        Y.extend(Bookmarks, Y.Widget, {
            
            /**
             * add the add bookmark form markup and hide edit related elements.
             */
            renderUI : function() {
                this.get("contentBox").appendChild(Y.Node.create(Bookmarks.ADD_BOOKMARK_TEMPLATE));
                this.get("contentBox").all(".yui3-bookmarks-edit").addClass("yui3-bookmarks-hide");
            },
            
            /**
             * setup event handlers
             */
            bindUI : function() {
                this.after("editingChange", this._afterEditingChange);
                this.get("toggle").on("click", this._toggleEdit, this);
                this.get("contentBox").all("a.yui3-bookmarks-edit").on("click", this._handleRemoveClick, this);
                this.get("contentBox").one("input[type='submit']").on("click", this._handleAddClick, this);
            },
            
            /**
             * add a bookmark at a particular position, or position 0 if not specified
             * @param bookmark
             * @param position
             */
            addBookmark : function(bookmark, position) {
                var node, ULNode, data;
                position = position === undefined ? 0 : position;
                if (bookmark.label && bookmark.url) {
                    node = Y.Node.create(Bookmarks.CREATE_TEMPLATE);
                    node.one("a").set("innerHTML", bookmark.label);
                    node.one("a").set("href", bookmark.url);
                    ULNode = this.get("contentBox").one("ul");
                    if (!ULNode) {
                        ULNode = Y.Node.create("<ul/>");
                        this.get("contentBox").one(".bd").set("innerHTML","<ul/>");
                        ULNode = this.get("contentBox").one("ul");
                    }
                    this.get("contentBox").one("ul").insert(node, position);
                    this.get("bookmarks").splice(position, 0, node);
                    node.one(".yui3-bookmarks-edit").on("click", this._handleRemoveClick, this);
                    data = Y.JSON.stringify(bookmark);//"url=" + bookmark.url + "&label=" + bookmark.label;
                    this.get("io")("/././bookmarks/add", {
                        method : "post",
                        data : data,
                        headers: {
                            "Content-Type" : "application/json"
                        }
                    });
                }
            },
            
            /**
             * remove the bookmark at position
             * @param position
             */
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
            
            /**
             * handler for click events on the add bookmark form submit button
             */
            _handleAddClick: function(e) {
                var form, labelTextInput, urlTextInput, label, url;
                form = e.target.ancestor("form");
                labelTextInput = form.one("input[name='label']");
                urlTextInput = form.one("input[name='url']");
                label = labelTextInput.get("value");
                url = urlTextInput.get("value");
                if (url && label) {
                    this.addBookmark({
                        label : label,
                        url : url
                    });
                    labelTextInput.set("value","");
                    urlTextInput.set("value","");
                }
            },
            
            /**
             * handler for the remove clicks
             * @param e
             */
            _handleRemoveClick : function(e) {
                var i, ul;
                ul = e.target.ancestor("ul").all(".yui3-bookmarks-edit");
                for (i = 0; i < ul.size(); i++) {
                    if (ul.item(i) === e.target) {
                        this.removeBookmark(i);
                        this.get("io")("/././bookmarks/remove", {
                            method : "post",
                            data : Y.JSON.stringify(i),
                            headers: {
                                "Content-Type" : "application/json"
                            }
                        });
                        break;
                    }
                }
            },
            
            /**
             * the event handle for the event handler for the clicks on links in the page saved
             * so it can be removed
             */
            _pageLinkEventHandle : null,
            
            /**
             * handler deals with editing state changes
             * @param e
             */
            _afterEditingChange : function(e) {
                var strings, editables;
                strings = this.get("strings");
                this.get("toggle").set("innerHTML", e.newVal ? strings.editing : strings.notEditing);
                editables = this.get("contentBox").all(".yui3-bookmarks-edit");
                if (e.newVal) {
                    Y.fire("stopTracking");
                    this._pageLinkEventHandle = Y.one("document").on("click", this._pageLinkHandler, this);
                    editables.removeClass("yui3-bookmarks-hide");
                } else {
                    this._pageLinkEventHandle.detach();
                    editables.addClass("yui3-bookmarks-hide");
                    Y.fire("startTracking");
                }
            },
            
            /**
             * handles clicks on the edit/done link
             * @param e
             */
            _toggleEdit : function(e) {
                var editing = this.get("editing");
                e.preventDefault();
                this.set("editing", !editing);
            },
            
            /**
             * handles clicks on links in the page when in edit mode
             * @param event
             */
            _pageLinkHandler : function(event) {
                var target, label, url;
                event.preventDefault();
                target = event.target;
                if (target.get("nodeName") === "A" && target.inDoc() && target.ancestor("#bookmarks") === null) {
                    if (!target.link) {
                        target.plug(Y.lane.LinkPlugin);
                    }
                    label = target.get("textContent");
                    if (target.link.isLocal()) {
                        url = target.link.getPath() + target.get("search") + target.get("hash");
                    } else {
                        url = target.link.getURL();
                    }
                    this.addBookmark({label:label,url:url});
                }
            }
        });
        
        //instantiate
        Y.lane.Bookmarks = new Bookmarks();
    }
})();