Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y) {

    var bookmarksTestCase = new Y.Test.Case({

        name : 'Bookmarks Editor Test Case',
        
        editor : Y.lane.BookmarksEditor,
        
        setUp : function() {
            this.editor._clearChecked();
        },
        
        "test exists" : function() {
            Y.Assert.isObject(this.editor);
        },
        
        "test add" : function() {
            var editorsCount = Y.all("#editors li").size();
            this.editor.add();
            Y.Assert.areEqual(editorsCount + 1, Y.all("#editors li").size());
            this.editor.get("editors")[0].cancel();
        },
        
        "test delete" : function() {
            var item = null;
            var handler = this.editor.get("bookmarks").on("remove", function(event) {
                event.preventDefault();
                item = event.positions[0];
            });
            Y.one("#editors input[type='checkbox']").set("checked", true);
            this.editor["delete"]();
            handler.detach();
            Y.Assert.areSame(0,item);
        },
        
        "test delete then drag" : function() {
            var item = null, bookmarks = this.editor.get("bookmarks");
            var handler = bookmarks.on("remove", function(event) {
                event.preventDefault();
                bookmarks.fire("removeSync", {positions:event.positions});
            });
            Y.all("#editors input[type='checkbox']").item(3).set("checked", true);
            this.editor["delete"]();
            handler.detach();
        }

    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarksTestCase);
    Y.Test.Runner.masterSuite.name = "bookmarks-editor-test.js";
    Y.Test.Runner.run();
});
