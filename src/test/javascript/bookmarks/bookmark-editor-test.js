YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let bookmarkTestCase = new Y.Test.Case({

        name : 'Bookmark Editor Test Case',

        editor : new L.BookmarkEditor({
            srcNode:Y.one("#editor"),
            bookmark:new L.Bookmark("url", "label"),
            render:true
        }),

        testSetEditingTrue : function() {
            let srcNode = this.editor.get("srcNode");
            Y.Assert.isFalse(srcNode.hasClass("yui3-bookmark-editor-active"));
            this.editor.set("editing", true);
            Y.Assert.isTrue(srcNode.hasClass("yui3-bookmark-editor-active"));
        },

        testSetEditingFalse : function() {
            let srcNode = this.editor.get("srcNode");
            Y.Assert.isTrue(srcNode.hasClass("yui3-bookmark-editor-active"));
            this.editor.set("editing", false);
            Y.Assert.isFalse(srcNode.hasClass("yui3-bookmark-editor-active"));
        },

        testCancel : function() {
            this.editor.set("editing", true);
            Y.one("button[value='cancel']").simulate("click");
            Y.Assert.isFalse(this.editor.get("editing"));
        },

        testCancelNew : function() {
            Y.one("body").append("<div id='new'><input type='hidden'/><a>a</a></div>");
            let anew = new L.BookmarkEditor({srcNode:Y.one("#new"),render:true});
            anew.set("editing", true);
            Y.one("#new button[value='cancel']").simulate("click");
            Y.Assert.areSame(null, Y.one("#new"));
        },

        testSaveNoLabel : function() {
            this.editor.set("editing", true);
            let label = this.editor.get("bookmark").getLabel();
            let input = Y.one("input[name='label']");
            input.set("value", "");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(label, this.editor.get("bookmark").getLabel());
            let value = input.getAttribute("placeholder");
            if (!value) {
                value = input.get("value")
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },

        testSaveNoUrl : function() {
            this.editor.set("editing", true);
            let url = this.editor.get("bookmark").getUrl();
            let input = Y.one("input[name='url']");
            input.set("value", "");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(url, this.editor.get("bookmark").getUrl());
            let value = input.getAttribute("placeholder");
            if (!value) {
                value = input.get("value")
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },

        testSave : function() {
            this.editor.set("editing", true);
            let input = Y.one("input[name='url']");
            input.set("value", "a new url");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame("a new url", this.editor.get("bookmark").getUrl());
        },

        testDelete : function() {
            let fired = false;
            let bookmarks = L.BookmarksWidget.get("bookmarks");
            let handler = bookmarks.on("remove", function(event) {
                event.preventDefault();
                fired = true;
            });
            Y.one("button[value='delete']").simulate("click");
            handler.detach();
            Y.Assert.isTrue(fired);
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-editor-test.js";
    Y.Test.Runner.run();

});
