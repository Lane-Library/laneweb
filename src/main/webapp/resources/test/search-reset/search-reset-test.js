YUI({
    logInclude: {
        TestRunner: true
    }
}).use("node-event-simulate", "console", "test", function(Y){

    var searchResetTestCase = new Y.Test.Case({
        name: "Lane Search Reset Test Case",
        setUp: function() {
            var searchReset = Y.one("#searchReset");
            if (searchReset) {
                searchReset.remove();
            }
        },
        testSearchResetNotPresent: function() {
            Y.Assert.isNull(Y.one("#searchReset"));
        },
        testConstructorCreatesLink: function() {
            var instance = new LANE.SearchReset();
            Y.Assert.isObject(Y.one("#searchReset"));
        },
        testShow: function() {
            var instance = new LANE.SearchReset();
            var searchReset = Y.one("#searchReset");
            instance.show();
            Y.Assert.areEqual("block", searchReset.getStyle("display"));
        },
        testHide: function() {
            var instance = new LANE.SearchReset();
            var searchReset = Y.one("#searchReset");
            instance.hide();
            Y.Assert.areEqual("none", searchReset.getStyle("display"));
        },
        testSyncUI: function() {
            var instance = new LANE.SearchReset();
            var searchReset = Y.one("#searchReset"), searchTerms = Y.one("#searchTerms");
            searchTerms.set('value','foo');
            instance.syncUI();
            Y.Assert.areEqual("block", searchReset.getStyle("display"));
            searchTerms.set('value','');
            instance.syncUI();
            Y.Assert.areEqual("none", searchReset.getStyle("display"));
        },
        testResetSearch: function() {
            var instance = new LANE.SearchReset();
            var searchReset = Y.one("#searchReset"), searchTerms = Y.one("#searchTerms");
            searchTerms.set('value','foo');
            instance.syncUI();
            Y.Assert.areEqual("block", searchReset.getStyle("display"));
            searchReset.simulate('click');
            Y.Assert.areEqual("", searchTerms.get("value"));
            Y.Assert.areEqual("none", searchReset.getStyle("display"));
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(searchResetTestCase);
    Y.Test.Runner.run();
});
