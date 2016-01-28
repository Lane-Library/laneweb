"use strict";

var selectTestCase = new Y.Test.Case({
    name: "Select Test Case",
    eventHandle : null,
    select : null,
    setUp : function() {
        this.select = new Y.lane.Select(["foo","bar","baz"]);
    },
    tearDown : function() {
        if (this.eventHandle) {
            this.select.detach(this.eventHandle);
        }
    },
    testExists : function() {
        Y.Assert.isNotNull(this.select);
    },
    testGetSelected : function() {
        Y.Assert.areEqual("foo", this.select.getSelected());
    },
    testGetSelectedWithIndex : function() {
        Y.Assert.areEqual("bar", new Y.lane.Select(["foo","bar"],["Foo","Bar"], 1).getSelected());
    },
    testSetSelected : function() {
        this.select.setSelected(1);
        Y.Assert.areEqual("bar", this.select.getSelected());
    },
    testSetSelectedString : function() {
        this.select.setSelected("bar");
        Y.Assert.areEqual("bar", this.select.getSelected());
    },
    testSelectedChange : function() {
        var selectedChange = null;
        this.eventHandle = this.select.on("selectedChange", function(event) {
            selectedChange = event;
        });
        this.select.setSelected(1);
        Y.Assert.areEqual(1, selectedChange.newIndex);
    },
    testSelectedChangePreventDefault : function() {
        this.eventHandle = this.select.on("selectedChange", function(event) {
            event.preventDefault();
        });
        this.select.setSelected("bar");
        Y.Assert.areEqual("foo", this.select.getSelected());
    }
});


var searchSelectWidgetTestCase = new Y.Test.Case({
    name: "SearchSelectWidget Test Case",
    setUp : function() {
        this.srcNode = this.srcNode || Y.one("#searchSource");
        this.widget =  this.widget || new Y.lane.SearchSelectWidget({srcNode:this.srcNode,render:true});
        this.model = this.model || this.widget.get("model");
    },
    testExists : function() {
        Y.Assert.isNotNull(this.widget);
    },
    testGetSelect : function() {
        Y.Assert.isNotNull(this.model);
    },
    testGetSelected : function() {
        Y.Assert.areEqual("baz", this.model.getSelected());
    },
    testViewChanges : function() {
        this.srcNode.set("value","bar");
        this.srcNode.simulate("change");
        Y.Assert.areEqual("bar", this.model.getSelected());
    },
    testSelectedContentChanges : function() {
        var selectedContent = Y.one("." + this.widget.getClassName() + "-selected");
        this.model.setSelected("foo");
        Y.Assert.areEqual("foo content", selectedContent.get("text"));
        this.model.setSelected("bar");
        Y.Assert.areEqual("bar content", selectedContent.get("text"));
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.add(selectTestCase);
Y.Test.Runner.add(searchSelectWidgetTestCase);
Y.Test.Runner.masterSuite.name = "search-select-test.js";
Y.Test.Runner.run();
