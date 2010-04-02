/**
 * @author ceyates
 */
(function() {
    var liaisons = document.getElementById('liaisons').getElementsByTagName('li');
    var options = document.getElementsByTagName('option');
    var LaneLiaisonTestCase = new YAHOO.tool.TestCase({
        name: "Lane Liaisons TestCase",
        testAllVisible: function() {
            var allVisible = true;
            for (var i = 0; i < liaisons.length; i++) {
                if (liaisons[i].style.display == 'none') {
                    allVisible = false;
                }
            }
            YAHOO.util.Assert.isTrue(allVisible);
        },
        testOthersHiddenOnSelect: function() {
            var liaison = options[1].value;
            YAHOO.util.UserAction.mouseup(options[1],{});
            var othersVisible = false;
            for (var i = 0; i < liaisons.length; i++) {
                if (liaisons[i].id != liaison) {
                    if (liaisons[i].style.display != 'none') {
                        othersVisible = true;
                    }
                }
            }
            YAHOO.util.Assert.isTrue(othersVisible === false);
        },
        testAnotherOthersHiddenOnSelect: function() {
            var liaison = options[3].value;
            YAHOO.util.UserAction.mouseup(options[3],{});
            var othersVisible = false;
            for (var i = 0; i < liaisons.length; i++) {
                if (liaisons[i].id != liaison) {
                    if (liaisons[i].style.display != 'none') {
                        othersVisible = true;
                    }
                }
            }
            YAHOO.util.Assert.isTrue(othersVisible === false);
        },
        testAllVisibleOnSelectAll: function() {
            YAHOO.util.UserAction.mouseup(options[0],{});
            this.testAllVisible();
        }
    });
    
    
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(LaneLiaisonTestCase);
    YAHOO.tool.TestRunner.run();
})();
