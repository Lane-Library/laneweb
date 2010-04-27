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
            for (var i = 0; i < selections.length; i++) {
                if (selections[i].style.display == 'none') {
                    allVisible = false;
                }
            }
            YAHOO.util.Assert.isTrue(allVisible);
        },
        testOthersHiddenOnSelect: function() {
            var selection = options[1].value;
            YAHOO.util.UserAction.click(options[1], {});
            var othersVisible = false;
            for (var i = 0; i < selections.length; i++) {
                if (selections[i].id != selection) {
                    if (selections[i].style.display != 'none') {
                        othersVisible = true;
                    }
                }
            }
            YAHOO.util.Assert.isTrue(othersVisible === false);
        },
        testAnotherOthersHiddenOnSelect: function() {
            var selection = options[3].value;
            YAHOO.util.UserAction.click(options[3], {});
            var othersVisible = false;
            for (var i = 0; i < selections.length; i++) {
                if (selections[i].id != selection) {
                    if (selections[i].style.display != 'none') {
                        othersVisible = true;
                    }
                }
            }
            YAHOO.util.Assert.isTrue(othersVisible === false);
        },
        testAllVisibleOnSelectAll: function() {
            YAHOO.util.UserAction.click(options[0], {});
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
