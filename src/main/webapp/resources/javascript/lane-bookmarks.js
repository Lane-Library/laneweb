YUI().add("bookmarks", function(Y) {
    function Bookmarks(config) {
        Bookmarks.superclass.constructor.apply(this, arguments);
    }
    Bookmarks.NAME = "bookmarks";
    Bookmarks.ATTRS = {
        editing: {
            value: false
        },
        bookmarks: {
            value: null
        },
        strings: {
        	value: {
        		editing:"edit",
        		notEditing:"done"
        	}
        },
        toggle: {
        	value: null
        }
    };
    Bookmarks.HTML_PARSER = {
            bookmarks : function (contentBox) {
                var nodes = contentBox.all("ul li a");
                var values = [];
                for (var i = 0; i < nodes.size(); i++) {
                	var node = nodes.item(i);
                	values.push({label:node.get("textContent"), url:node.get("href")});
                }
                return values;
            },
    		toggle : function(contentBox) {
    			return contentBox.one("h3 a");
    		}
        };
    Y.extend(Bookmarks, Y.Widget, {
    	addBookmark : function(bookmark, position) {
    		position = position === undefined ? 0 : position;
    		this.get("bookmarks").splice(position, 0, bookmark);
    		this._redrawBookmarks();
    	},
    	removeBookmark : function(position) {
    		this.get("bookmarks").splice(position, 1);
    		this._redrawBookmarks();
    	},
    	renderUI : function() {
    		this.get("toggle").on("click", this._toggleEdit, this);
    	},
    	bindUI : function() {
    		this.after("editingChange", this._afterEditingChange);
    	},
    	_afterEditingChange : function(e) {
    		var strings = this.get("strings");
    		this.get("toggle").set("innerHTML", e.newVal ? strings.editing : strings.notEditing);
    	},
    	_toggleEdit : function(e) {
    		e.preventDefault();
    		var editing = this.get("editing");
    		this.set("editing", !editing);
    	},
    	_redrawBookmarks : function() {
    		var bd = this.get("contentBox").one(".bd");
    		var newUl = "<ul>";
    		var bookmarks = this.get("bookmarks");
    		for (var i = 0; i < bookmarks.length; i++) {
    			newUl += ("<li><a href=\"" + bookmarks[i].url + "\">" + bookmarks[i].label + "</a></li>");
    		}
    		bd.set("innerHTML", newUl)
    	}
    });
    Y.Bookmarks = Bookmarks;
}, "${project.version}", {requires:["widget", "substitute"]});

//YUI().use("bookmarks", function(Y) {
//    var bookmarks = new Y.Bookmarks({srcNode:"#bookmarks"});
//});