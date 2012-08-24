/**
 * @author ceyates
 */
YUI({
//    debug:true,
//    filter:"debug"
}).use("attribute", "node-event-simulate", "console", "test", function(T){
    
    var textinputsTestCase = new T.Test.Case({
        name: "Lane TextInputs Test Case",
        testConstructorHintText : function() {
            var input = T.one("#input1");
            T.Assert.areEqual("", input.get("value"));
            var textInput = new Y.lane.TextInput(input, "hint text");
            T.Assert.areEqual("", textInput.getValue());
            T.Assert.areEqual("hint text", input.get("value"));
            T.Assert.isTrue(input.hasClass("inputHint"));
        },
        testSetHintText: function() {
            var input = T.one("#input2");
            var textInput = new Y.lane.TextInput(input, "hint text");
            T.Assert.areEqual("hint text", input.get("value"));
            textInput.setHintText("new hint text");
            T.Assert.areEqual("new hint text", input.get("value"));
            T.Assert.isTrue(input.hasClass("inputHint"));
        },
        testFocus: function() {
            var input = T.one("#input3");
            var textInput = new Y.lane.TextInput(input, "hint text");
            T.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var focusHandle = input.after("focus", function() {
                input.detach(focusHandle);
                T.Assert.areEqual("", input.get("value"), "value = " + input.get("value"));
                T.Assert.isFalse(input.hasClass("inputHint"), "has class inputHint");
            });
            input.simulate("focus");
        },
        testBlur: function() {
            var input = T.one("#input4");
            var textInput = new Y.lane.TextInput(input, "hint text");
            T.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var blurHandle = input.after("blur", function() {
                input.detach(blurHandle);
                T.Assert.areEqual("hint text", input.get("value"));
                T.Assert.isTrue(input.hasClass("inputHint"));
            });
            var focusHandle = input.after("focus", function() {
                input.detach(focusHandle);
                input.simulate("blur");
            });
            input.simulate("focus");
        },
        testNoHintText: function() {
            var input = T.one("#input5");
            var textInput = new Y.lane.TextInput(input);
            T.Assert.areEqual(input.get("value"), "");
            T.Assert.isTrue(input.hasClass("inputHint"));
        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");
    
    
    T.Test.Runner.add(textinputsTestCase);
    T.Test.Runner.run();
});