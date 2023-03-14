YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

"use strict";

var bookmarksTestCase = new Y.Test.Case({

    name : 'Bookmarks Editor Test Case',

    editor : L.BookmarksEditor,

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
    }

});

new Y.Test.Console().render();

Y.Test.Runner.add(bookmarksTestCase);
Y.Test.Runner.masterSuite.name = "bookmarks-editor-test.js";
Y.Test.Runner.run();

});
