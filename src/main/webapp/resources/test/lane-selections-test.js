/**
 * @author ceyates
 */
(function() {
    var selections = document.getElementById('selections').getElementsByTagName('li');
    var options = document.getElementsByTagName('option');
    var LaneSelectionsTestCase = new YAHOO.tool.TestCase({
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
    
    
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(LaneSelectionsTestCase);
    YAHOO.tool.TestRunner.run();
})();
