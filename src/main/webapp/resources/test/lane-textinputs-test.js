/**
 * @author ceyates
 */
YUI({
//    debug:true,
//	filter:"debug"
}).use("attribute", "node-event-simulate", "console", "test", function(Y){
	
	Y.namespace("lane");
	
	Y.lane.TextInput = function(node, hint) {
		var defaultConfig = {
			value:{
				value:node.get("value"),
				getter: function(val, name) {
					return this.get('hintEnabled') ? "" : val;
				}
			},
			hint:{
				value:hint ? hint : ""
			},
			hintEnabled: {
				value:false
			}
		}
		this.addAttrs(defaultConfig);
		node.on("valueChange", function(event) {
			this.set("value", event.newVal);
		});
		node.on("focus", function() {
			if (hintEnabled) {
				this.set("value", "");
			}
		});
		node.on("blur", function() {
			if (!this.get("value")) {
				this.set("value", this.get("hint"));
				this.set("hintEnabled", true);
			}
		});
		this.on("valueChange", function(event) {
			node.set("value", event.newVal);
		});
		this.after("hintChange", function(event) {
			if (this.get("hintEnabled")) {
				this.set("value", event.newVal);
			}
		});
		this.on("hintEnabledChange", function(event) {
			if (event.newVal == true) {
				node.addClass("inputHint");
			} else {
				node.removeClass("inputHint");
			}
		});
		if (!node.get("value")) {
			this.set("value", this.get("hint"));
			this.set("hintEnabled", true);
		}
	};
	
	Y.augment(Y.lane.TextInput, Y.Attribute);
	
	

    var textinputsTestCase = new Y.Test.Case({
		name: "Lane TextInputs Test Case",
        testConstructorHintText : function() {
            var input = Y.one("#input1");
			Y.Assert.areEqual("", input.get("value"));
            var textInput = new Y.lane.TextInput(input, "hint text");
            Y.Assert.areEqual("", textInput.get("value"));
            Y.Assert.areEqual("hint text", input.get("value"));
            Y.Assert.isTrue(input.hasClass("inputHint"));
        },
        testSetHintText: function() {
            var input = Y.one("#input2");
            var textInput = new Y.lane.TextInput(input, "hint text");
            Y.Assert.areEqual("hint text", input.get("value"));
            textInput.set("hint", "new hint text");
            Y.Assert.areEqual("new hint text", input.get("value"));
            Y.Assert.isTrue(input.hasClass("inputHint"));
        },
        testFocus: function() {
            var input = Y.one("#input3");
            var textInput = new Y.lane.TextInput(input, "hint text");
            Y.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var focusHandle = input.after("focus", function() {
				input.detach(focusHandle);
                Y.Assert.areEqual("", input.get("value"), "value = " + input.get("value"));
                Y.Assert.isFalse(input.hasClass("inputHint"), "has class inputHint");
            });
            input.simulate("focus");
        },
        testBlur: function() {
            var input = Y.one("#input4");
            var textInput = new Y.lane.TextInput(input, "hint text");
            Y.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var blurHandle = input.after("blur", function() {
                input.detach(blurHandle);
                Y.Assert.areEqual("hint text", input.get("value"));
                Y.Assert.isTrue(input.hasClass("inputHint"));
            });
            var focusHandle = input.after("focus", function() {
                input.detach(focusHandle);
                input.simulate("blur");
            });
            input.simulate("focus");
        },
        testNoHintText: function() {
            var input = Y.one("#input5");
            var textInput = new Y.lane.TextInput(input);
            Y.Assert.areEqual(input.get("value"), "");
            Y.Assert.isTrue(input.hasClass("inputHint"));
        }
	});
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(textinputsTestCase);
    Y.Test.Runner.run();
});