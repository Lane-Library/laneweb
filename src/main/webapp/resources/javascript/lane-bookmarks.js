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
            }
        };
    Y.extend(Bookmarks, Y.Widget, {
    });
    Y.Bookmarks = Bookmarks;
}, "${project.version}", {requires:["widget", "substitute"]});

//YUI().use("bookmarks", function(Y) {
//    var bookmarks = new Y.Bookmarks({srcNode:"#bookmarks"});
//});