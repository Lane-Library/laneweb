/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('lane-textinputs', 'node-event-simulate', 'console', 'test', function(Y){

    var textinputsTestCase = new Y.Test.Case({
		name: 'Lane TextInputs Test Case',
        testConstructorHintText : function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input, 'hint text');
            Y.Assert.areEqual(input.get('value'), 'hint text');
            Y.Assert.isTrue(input.hasClass('inputHint'));
            textInput.destroy();
        },
        testSetHintText: function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input, 'hint text');
            Y.Assert.areEqual('hint text', input.get('value'));
            textInput.setHintText('new hint text');
            Y.Assert.areEqual('new hint text', input.get('value'));
            Y.Assert.isTrue(input.hasClass('inputHint'));
            textInput.destroy();
        },
        testFocus: function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input, 'hint text');
            Y.Assert.isTrue(input.hasClass('inputHint'), 'doesn\'t have class inputHint');
            //have to do input.after('focus') safari value is not set on return from simulate.
            var focusHandle = input.after('focus', function() {
                input.detach(focusHandle);
                Y.Assert.areEqual('', input.get('value'), 'value = ' + input.get('value'));
                Y.Assert.isFalse(input.hasClass('inputHint'), 'has class inputHint');
                textInput.destroy();
            });
            input.simulate('focus');
        },
        testBlur: function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input, 'hint text');
            Y.Assert.isTrue(input.hasClass('inputHint'), 'doesn\'t have class inputHint');
            //have to do input.after('focus') safari value is not set on return from simulate.
            var blurHandle = input.after('blur', function() {
                input.detach(blurHandle);
                Y.Assert.areEqual('hint text', input.get('value'));
                Y.Assert.isTrue(input.hasClass('inputHint'));
                textInput.destroy();
            });
            var focusHandle = input.after('focus', function() {
                input.detach(focusHandle);
                input.simulate('blur');
            });
            input.simulate('focus');
        },
        testDestroy: function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input, 'hint text');
            textInput.destroy();
            Y.Assert.areEqual('', input.get('value'), 'value = ' + input.get('value'));
        },
        testNoHintText: function() {
            var input = Y.one('input[type="text"]');
            var textInput = new Y.lane.TextInput(input);
            Y.Assert.areEqual(input.get('value'), '');
            Y.Assert.isTrue(input.hasClass('inputHint'));
            textInput.destroy();
        }
	});
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(textinputsTestCase);
    Y.Test.Runner.run();
});