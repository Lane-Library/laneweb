YUI({ fetchCSS: false }).use("test", "test-console", "node-event-simulate", function (Y) {

    "use strict";

    let bookmarkTestCase = new Y.Test.Case({

        name: 'Bookmark Editor Test Case',

        editor: new L.BookmarkEditor({
            srcNode: document.getElementById("editor"),
            bookmark: new L.Bookmark("url", "label"),
            render: true
        }),

        // testSetEditingTrue: function () {
        //     let srcNode = this.editor.srcNode;
        //     Y.Assert.isFalse(srcNode.classList.contains("bookmark-editor-active"));
        //     this.editor.setEditing(true);
        //     Y.Assert.isTrue(srcNode.classList.contains("bookmark-editor-active"));
        // },

        // testSetEditingFalse: function () {
        //     let srcNode = this.editor.srcNode;
        //     Y.Assert.isTrue(srcNode.classList.contains("bookmark-editor-active"));
        //     this.editor.setEditing(false);
        //     Y.Assert.isFalse(srcNode.classList.contains("bookmark-editor-active"));
        // },

        // testCancel: function () {
        //     this.editor.setEditing(true);
        //     Y.one("button[value='cancel']").simulate("click");
        //     Y.Assert.isFalse(this.editor.editing);
        // },

        testCancelNew: function () {
            Y.one("body").append("<div id='new'><input type='hidden'/><a>a</a></div>");
            let anew = new L.BookmarkEditor({ srcNode: Y.one("#new")._node, render: true });
            anew.setEditing(true);
            Y.one("#new button[value='cancel']").simulate("click");
            Y.Assert.areSame(null, Y.one("#new"));
        },

        testSaveNoLabel: function () {
            this.editor.setEditing(true);
            let label = this.editor.bookmark.getLabel();
            let input = Y.one("input[name='label']")._node;
            input.value = "";
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(label, this.editor.bookmark.getLabel());
            let value = input.placeholder;
            if (!value) {
                value = input.value
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },

        testSaveNoUrl: function () {
            this.editor.setEditing(true);
            let url = this.editor.bookmark.getUrl();
            let input = Y.one("input[name='url']")._node;
            input.value = "";
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(url, this.editor.bookmark.getUrl());
            let value = input.placeholder;
            if (!value) {
                value = input.value
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },

        testSave: function () {
            this.editor.setEditing(true);
            let input = Y.one("input[name='url']")._node;
            input.value = "a new url";
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame("a new url", this.editor.bookmark.getUrl());
        },


        // Doesn't work due to request to database
        // testDelete: function () {
        //     let fired = false;
        //     let bookmarks = L.BookmarksWidget.bookmarks;
        //     bookmarks.on("remove", function (event) {
        //         event.preventDefault();
        //         fired = true;
        //     });
        //     Y.one("button[value='delete']").simulate("click");
        //     Y.Assert.isTrue(fired);
        // }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-editor-test.js";
    Y.Test.Runner.run();

});
