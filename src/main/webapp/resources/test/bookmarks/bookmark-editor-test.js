Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y) {

    var bookmarkTestCase = new Y.Test.Case({

        name : 'Bookmark Editor Test Case',
        
        editor : new Y.lane.BookmarkEditor({
            srcNode:Y.one("#editor"),
            bookmark:new Y.lane.Bookmark("url", "label"),
            render:true
        }),
            
        testNotChecked : function() {
            var checked = Y.one("input[type='checkbox']").get("checked");
            Y.Assert.isFalse(checked);
            Y.Assert.isFalse(this.editor.isChecked());
        },

        testSetCheckedTrue : function() {
            this.editor.setChecked(true);
            var checked = Y.one("#editor input[type='checkbox']").get("checked");
            Y.Assert.isTrue(checked);
            Y.Assert.isTrue(this.editor.isChecked());
        },

        testSetCheckedFalse : function() {
            this.editor.setChecked(false);
            var checked = Y.one("#editor input[type='checkbox']").get("checked");
            Y.Assert.isFalse(checked);
            Y.Assert.isFalse(this.editor.isChecked());
        },

        testSetEditingTrue : function() {
            var elements = [];
            elements.push(Y.one("input[name='label']"));
            elements.push(Y.one("input[name='url']"));
            elements.push(Y.one("button[value='save']"));
            elements.push(Y.one("button[value='cancel']"));
            elements.push(Y.one("button[type='reset']"));
            for (var i = 0; i < elements.length; i++) {
                Y.Assert.areEqual("none", elements[i].getStyle("display"));
            }
            this.editor.set("editing", true);
            for (i = 0; i < elements.length; i++) {
                Y.Assert.areEqual("inline-block", elements[i].getStyle("display"));
            }
        },

        testSetEditingFalse : function() {
            var elements = [];
            elements.push(Y.one("input[name='label']"));
            elements.push(Y.one("input[name='url']"));
            elements.push(Y.one("button[value='save']"));
            elements.push(Y.one("button[value='cancel']"));
            elements.push(Y.one("button[type='reset']"));
            for (var i = 0; i < elements.length; i++) {
                Y.Assert.areEqual("inline-block", elements[i].getStyle("display"));
            }
            this.editor.set("editing", false);
            for (i = 0; i < elements.length; i++) {
                Y.Assert.areEqual("none", elements[i].getStyle("display"));
            }
        },

        testCancel : function() {
            this.editor.set("editing", true);
            Y.one("button[value='cancel']").simulate("click");
            Y.Assert.isFalse(this.editor.get("editing"));
        },
        
        testCancelNew : function() {
            Y.one("body").append("<div id='new'><input type='checkbox'/><a>a</a></div>");
            var anew = new Y.lane.BookmarkEditor({srcNode:Y.one("#new"),render:true});
            anew.set("editing", true);
            Y.one("#new button[value='cancel']").simulate("click");
            Y.Assert.areSame(null, Y.one("#new"));
        },

        testSaveNoLabel : function() {
            this.editor.set("editing", true);
            var label = this.editor.get("bookmark").getLabel();
            var input = Y.one("input[name='label']");
            input.set("value", "");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(label, this.editor.get("bookmark").getLabel());
            var value = input.getAttribute("placeholder");
            if (!value) {
                value = input.get("value")
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },

        testSaveNoUrl : function() {
            this.editor.set("editing", true);
            var url = this.editor.get("bookmark").getUrl();
            var input = Y.one("input[name='url']");
            input.set("value", "");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame(url, this.editor.get("bookmark").getUrl());
            var value = input.getAttribute("placeholder");
            if (!value) {
                value = input.get("value")
            }
            Y.Assert.areSame("required", value);
            this.editor.cancel();
        },
        
        testSave : function() {
            this.editor.set("editing", true);
            var input = Y.one("input[name='url']");
            input.set("value", "a new url");
            Y.one("button[value='save']").simulate("click");
            Y.Assert.areSame("a new url", this.editor.get("bookmark").getUrl());
        }

    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-editor-test.js";
    Y.Test.Runner.run();
});
