YUI({ fetchCSS: false }).use("test", "test-console", "dom", "node-event-simulate", function(Y) {

    "use strict";

    var tableHideColsTestCase = new Y.Test.Case({

        name: 'Table Hide Empty Columns Test Case',

        testHideCols: function() {
            var rawTable = document.querySelector('.raw');
            rawTable.querySelectorAll('.table-head:nth-child(3),table-cell td:nth-child(3)').forEach(function(node) {
                Y.Assert.areNotEqual("none", node.style.display);
            })
            var hideTable = document.querySelector('.hide-empty-columns');
            hideTable.querySelectorAll('.table-head:nth-child(3),table-cell td:nth-child(3)').forEach(function(node) {
              
                Y.Assert.areEqual("none", node.style.display);
            })
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(tableHideColsTestCase);
    Y.Test.Runner.masterSuite.name = "hide-empty-columns-test.js";
    Y.Test.Runner.run();

});
