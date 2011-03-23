YUI({
    logInclude: {
        TestRunner: true
    }
}).use("node-event-simulate", "console", "test", function(T){

    var searchResetTestCase = new T.Test.Case({
        name: "Lane Search Reset Test Case",
        setUp: function() {
            var searchReset = T.one("#searchReset");
            if (searchReset) {
                searchReset.remove();
            }
        },
        testSearchResetNotPresent: function() {
            T.Assert.isNull(T.one("#searchReset"));
        },
        testConstructorCreatesLink: function() {
            var instance = new Y.lane.SearchReset();
            T.Assert.isObject(T.one("#searchReset"));
        },
        testShow: function() {
            var instance = new Y.lane.SearchReset();
            var searchReset = T.one("#searchReset");
            instance.show();
            T.Assert.areEqual("block", searchReset.getStyle("display"));
        },
        testHide: function() {
            var instance = new Y.lane.SearchReset();
            var searchReset = T.one("#searchReset");
            instance.hide();
            T.Assert.areEqual("none", searchReset.getStyle("display"));
        },
        testSyncUI: function() {
            var instance = new Y.lane.SearchReset();
            var searchReset = T.one("#searchReset"), searchTerms = T.one("#searchTerms");
            searchTerms.set('value','foo');
            instance.syncUI();
            T.Assert.areEqual("block", searchReset.getStyle("display"));
            searchTerms.set('value','');
            instance.syncUI();
            T.Assert.areEqual("none", searchReset.getStyle("display"));
        },
        testResetSearch: function() {
            var instance = new Y.lane.SearchReset();
            var searchReset = T.one("#searchReset"), searchTerms = T.one("#searchTerms");
            searchTerms.set('value','foo');
            instance.syncUI();
            T.Assert.areEqual("block", searchReset.getStyle("display"));
            searchReset.simulate('click');
            T.Assert.areEqual("", searchTerms.get("value"));
            T.Assert.areEqual("none", searchReset.getStyle("display"));
        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");
    
    
    T.Test.Runner.add(searchResetTestCase);
    T.Test.Runner.run();
});
