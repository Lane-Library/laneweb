YUI.applyConfig({fetchCSS:true});
YUI().use("node-event-simulate", "console", "test", function(T){

    var searchResetTestCase = new T.Test.Case({
        name: "Lane Search Reset Test Case",
        instance : new Y.lane.SearchReset(),
        testConstructorCreatesLink: function() {
            T.Assert.isObject(T.one("#searchReset"));
        },
        testShow: function() {
            var searchReset = T.one("#searchReset");
            this.instance.show();
            T.Assert.areEqual("block", searchReset.getStyle("display"));
        },
        testHide: function() {
            var searchReset = T.one("#searchReset");
            this.instance.hide();
            T.Assert.areEqual("none", searchReset.getStyle("display"));
        },
        testResetEvent : function() {
        	var event = false;
        	this.instance.on("reset", function() {
        		event = true;
        	});
        	T.one("#searchReset").simulate("click");
        	T.Assert.isTrue(event);
        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");
    
    
    T.Test.Runner.add(searchResetTestCase);
    T.Test.Runner.run();
});
