Y.applyConfig({fetchCSS:true});
Y.use("node-event-simulate", "console", "test", function(Y){

    var searchSelectTestCase = new Y.Test.Case({
        name: "Lane Search Select Test Case",
        eventHandle : null,
        searchSelect : null,
        setUp : function() {
        	this.searchSelect = new Y.lane.SearchSelect(["foo","bar","baz"]);
        },
        tearDown : function() {
        	if (this.eventHandle) {
        		this.searchSelect.detach(this.eventHandle);
        	}
        },
        testExists : function() {
        	Y.Assert.isObject(this.searchSelect);
        },
        testGetSelected : function() {
        	Y.Assert.areEqual("foo", this.searchSelect.getSelected());
        },
        testGetSelectedWithIndex : function() {
        	Y.Assert.areEqual("bar", new Y.lane.SearchSelect(["foo","bar"], 1).getSelected());
        },
        testSetSelected : function() {
        	this.searchSelect.setSelected(1);
        	Y.Assert.areEqual("bar", this.searchSelect.getSelected());
        },
        testSetSelectedString : function() {
        	this.searchSelect.setSelected("bar");
        	Y.Assert.areEqual("bar", this.searchSelect.getSelected());
        },
        testSelectedChange : function() {
        	var selectedChange = null;
            this.eventHandle = this.searchSelect.on("selectedChange", function(event) {
                selectedChange = event;
            });
            this.searchSelect.setSelected(1);
            Y.Assert.areEqual(1, selectedChange.newIndex);
        },
        testSelectedChangePreventDefault : function() {
            this.eventHandle = this.searchSelect.on("selectedChange", function(event) {
                event.preventDefault();
            });
            this.searchSelect.setSelected("bar");
            Y.Assert.areEqual("foo", this.searchSelect.getSelected());
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(searchSelectTestCase);
    Y.Test.Runner.run();
});
