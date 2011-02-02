(function() {
	var Y = LANE.Y;
	function Bookmark(config) {
		Bookmark.superclass.constructor.apply(this, arguments);
	};
	Bookmark.NAME = "bookmark";
	Bookmark.ATTRS = {
	    editing : {
	    	value:false
	    },
	    label : {
	    	value: null
	    },
	    url : {
	    	value : null
	    }
	};
	Bookmark.HTML_PARSER = {
			label : function(contentBox) {
				return contentBox.get("textContent");
			},
			url : function(contentBox) {
				return contentBox.one("a").get("href");
			}
	};
	Bookmark.CREATE_TEMPLATE = "<li><a></a></li>";
    Bookmark.EDIT_TEMPLATE = '<a class="yui3-bookmark-edit">delete</a>';
	Y.extend(Bookmark, Y.Widget, {
		renderUI : function() {
			var contentBox = this.get("contentBox");
			contentBox.set("innerHTML",  Bookmark.EDIT_TEMPLATE + this.get("srcNode").get("innerHTML"));
		}
	});
	Y.Bookmark = Bookmark;
	
    function Bookmarks(config) {
        Bookmarks.superclass.constructor.apply(this, arguments);
    }
    Bookmarks.NAME = "bookmarks";
    Bookmarks.ATTRS = {
        editing: {
            value: false
        },
        bookmarks: {
            valueFn : function() {
            	var i, b, value = [], lis = this.get("contentBox").all("li");
            	for (i = 0; i < lis.size(); i++) {
            		value.push(new Y.Bookmark({srcNode:lis.item(i), render:true}));
            	}
            	return value;
            }
        },
        strings: {
        	value: {
        		editing:"done",
        		notEditing:"edit"
        	}
        },
        toggle: {
        	value: null
        }
    };
    Bookmarks.HTML_PARSER = {
    		toggle : function(contentBox) {
    			return contentBox.one("h3 a");
    		}
        };
    Bookmarks.ADD_BOOKMARK_TEMPLATE = '<div class="yui3-bookmarks-edit"><h4>add a bookmark</h4><div><label>url:</label><input name="url" type="text" /></div><div><label>label:</label><input name="label" type="text" /></div><input type="submit" value="add" /></div>';
    Y.extend(Bookmarks, Y.Widget, {
    	addBookmark : function(bookmark, position) {
    		position = position === undefined ? 0 : position;
    		if (bookmark.label && bookmark.url) {
    			var node = Y.Node.create(Bookmark.CREATE_TEMPLATE);
    			node.one("a").set("innerHTML", bookmark.label);
    			node.one("a").set("href", bookmark.url);
        		this.get("contentBox").one("ul").insert(node, position);
        		this.get("bookmarks").splice(position, 0, new Y.Bookmark({srcNode:node,render:true}));
        		node.on("click", this._handleDeleteClick, this);
    		}
    	},
    	removeBookmark : function(position) {
    		this.get("bookmarks")[position].destroy();
    		this.get("bookmarks").splice(position, 1);
    	},
//    	moveUp : function(position) {
//    		var bookmarks = this.get("bookmarks");
//    		bookmarks.splice(position - 1, 2, bookmarks[position], bookmarks[position - 1]);
//    		var nodeList = this.get("contentBox").all("li");
//    		nodeList.item(position).swap(nodeList.item(position - 1));
//    	},
//    	moveDown : function(position) {
//    		var bookmarks = this.get("bookmarks");
//    		bookmarks.splice(position, 2, bookmarks[position + 1], bookmarks[position]);
//    		var nodeList = this.get("contentBox").all("li");
//    		nodeList.item(position).swap(nodeList.item(position + 1));
//    	},
    	renderUI : function() {
    		this.get("contentBox").appendChild(Y.Node.create(Bookmarks.ADD_BOOKMARK_TEMPLATE));
    		this.get("contentBox").all(".yui3-bookmark-edit, .yui3-bookmarks-edit").addClass("yui3-bookmarks-hide");
    	},
    	bindUI : function() {
    		this.after("editingChange", this._afterEditingChange);
    		this.get("toggle").on("click", this._toggleEdit, this);
    		this.get("contentBox").all(".yui3-bookmark-edit").on("click", this._handleDeleteClick, this);
    		this.get("contentBox").one("input[type='submit']").on("click", this._handleAddClick, this);
    	},
    	_handleAddClick: function(e) {
    		var div = e.target.ancestor(".yui3-bookmarks-edit");
    		var label = div.one("input[name='label']");
    		var url = div.one("input[name='url']");
    		this.addBookmark({label:label.get("value"),url:url.get("value")});
    		label.set("value","");
    		url.set("value","");
    	},
    	_handleDeleteClick : function(e) {
    		var ul = e.target.ancestor("ul").all(".yui3-bookmark-edit");
    		for (var i = 0; i < ul.size(); i++) {
    			if (ul.item(i) === e.target) {
    				this.removeBookmark(i);
    				break;
    			}
    		}
    	},
    	_afterEditingChange : function(e) {
    		var strings = this.get("strings");
    		this.get("toggle").set("innerHTML", e.newVal ? strings.editing : strings.notEditing);
    		var editables = this.get("contentBox").all(".yui3-bookmark-edit, .yui3-bookmarks-edit");
    		if (e.newVal) {
    			editables.removeClass("yui3-bookmarks-hide");
    		} else {
    			editables.addClass("yui3-bookmarks-hide");
    		}
    	},
    	_toggleEdit : function(e) {
    		e.preventDefault();
    		var editing = this.get("editing");
    		this.set("editing", !editing);
    	}
    });
    Y.Bookmarks = Bookmarks;
})();