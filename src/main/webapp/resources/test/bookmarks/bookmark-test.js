/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {
    
    var Bookmark = Y.lane.Bookmark,

    bookmarkTestCase = new T.Test.Case({
        
        name : 'Bookmark Test Case',
        
        bookmark : null,
        
        setUp : function() {
            this.bookmark = new Bookmark("label", "url");
        },
        
        testNewBookmarkNoParams : function() {
            this.bookmark = null;
            try {
                this.bookmark = new Bookmark();
            } catch(e) {}
            T.Assert.isNull(this.bookmark);
        },
        
        testNewBookmarkNoUrl : function() {
            var bookmark = null;
            try {
                bookmark = new Bookmark(null, "url");
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testNewBookmarkNoLabel : function() {
            var bookmark = null;
            try {
                bookmark = new Bookmark("label", null);
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testSetNullValues : function() {
            try {
                this.bookmark.setValues(null, null);
            } catch(e) {}
            T.Assert.areEqual("label", this.bookmark.getLabel());
            T.Assert.areEqual("url", this.bookmark.getUrl());
        },
        
        testGetSetLabel : function() {
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
        },
        
        testGetSetUrl : function() {
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual("newurl", this.bookmark.getUrl());    
        },
        
        testGetSetValues : function() {
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
            T.Assert.areEqual("newurl", this.bookmark.getUrl());   
        },
        
        testChangeEventSetLabel : function() {
            var label = this.bookmark.getLabel();
            this.bookmark.on("valueChange", function(event) {
                label = event.newLabel;
            });
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual(label, "newlabel");
        },
        
        testSetLabelPreventDefault : function() {
            var label = this.bookmark.getLabel();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual(label, this.bookmark.getLabel());
        },
        
        testChangeEventSetUrl : function() {
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                url = event.newUrl;
            });
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual(url, "newurl");   
        },
        
        testSetUrlPreventDefault : function() {
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual(url, this.bookmark.getUrl());   
        },
        
        testChangeEventSetValues : function() {
            var label = this.bookmark.getLabel();
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                label = event.newLabel;
                url = event.newUrl;
            });
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual(label, "newlabel");
            T.Assert.areEqual(url, "newurl");   
        },
        
        testSetValuesPreventDefault : function() {
            var label = this.bookmark.getLabel();
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual(label, this.bookmark.getLabel());
            T.Assert.areEqual(url, this.bookmark.getUrl());   
        }
        
    });

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');

    T.Test.Runner.add(bookmarkTestCase);
    T.Test.Runner.run();
});
