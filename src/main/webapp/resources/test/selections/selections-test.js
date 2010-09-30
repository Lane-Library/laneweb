/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var selectionsTestCase = new Y.Test.Case({
        name: "Lane Selections TestCase",
        testAllVisible: function() {
            var allVisible = true;
            var selections = Y.all('.selections li');
            for (var i = 0; i < selections.size(); i++) {
                if (selections.item(i).getStyle('display') =='none') {
                    allVisible = false;
                }
            }
            Y.Assert.isTrue(allVisible);
        },
        testOthersHiddenOnSelect: function() {
            var selections = Y.all('selections');
            var selection = Y.all('option').item(1).get('value');
            Y.all('option').item(1).simulate('click');
            var othersVisible = false;
            for (var i = 0; i < selections.size(); i++) {
                if (selections.item(i).get('id') != selection) {
                    if (selections.item(i).getStyle('display') != 'none') {
                        othersVisible = true;
                    }
                }
            }
            Y.Assert.isTrue(othersVisible === false);
        },
        testAnotherOthersHiddenOnSelect: function() {
            var selections = Y.all('selections');
            var selection = Y.all('option').item(3).get('value');
            Y.all('option').item(3).simulate('click');
            var othersVisible = false;
            for (var i = 0; i < selections.size(); i++) {
                if (selections.item(i).get('id') != selection) {
                    if (selections.item(i).getStyle('display') != 'none') {
                        othersVisible = true;
                    }
                }
            }
            Y.Assert.isTrue(othersVisible === false);
        },
        testAllVisibleOnSelectAll: function() {
            Y.one('option').simulate('click');
            this.testAllVisible();
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(selectionsTestCase);
    Y.Test.Runner.run();
});
