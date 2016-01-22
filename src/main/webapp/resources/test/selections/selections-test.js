"use strict";

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
        var selections = Y.all('#selections li');
        var select = Y.one('#selections-select');
        select.set("selectedIndex", 1);
        select.simulate("change");
        var selection = select.get("value");
        var othersVisible = false;
        for (var i = 0; i < selections.size(); i++) {
            if (selections.item(i).get('id') != selection) {
                if (selections.item(i).getStyle('display') != 'none') {
                    othersVisible = true;
                }
            }
        }
        Y.Assert.isFalse(othersVisible);
    },
    testAnotherOthersHiddenOnSelect: function() {
        var selections = Y.all('selections');
        var select = Y.one('#selections-select');
        select.set("selectedIndex", 3);
        select.simulate("change");
        var selection = select.get("value");
        var othersVisible = false;
        for (var i = 0; i < selections.size(); i++) {
            if (selections.item(i).get('id') != selection) {
                if (selections.item(i).getStyle('display') != 'none') {
                    othersVisible = true;
                }
            }
        }
        Y.Assert.isFalse(othersVisible);
    },
    testAllVisibleOnSelectAll: function() {
        var select = Y.one('#selections-select');
        select.set("selectedIndex", 0);
        select.simulate("change");
        this.testAllVisible();
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(selectionsTestCase);
Y.Test.Runner.masterSuite.name = "selections-test.js";
Y.Test.Runner.run();
