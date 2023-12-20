YUI({ fetchCSS: false }).use("test", "test-console", "dom", "node-event-simulate", function(Y) {

    "use strict";

    let zoteroTestCase = new Y.Test.Case({

        name: 'Zotero Test Case',

        testZotero: function() {
            let metadata = document.querySelector('.zotero-metadata');
            document.querySelectorAll("li[data-doi]").forEach(function(node) {
                Y.Assert.isTrue(metadata.textContent.indexOf("doi:" + node.dataset.doi) > -1);
            })
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(zoteroTestCase);
    Y.Test.Runner.masterSuite.name = "zotero-test.js";
    Y.Test.Runner.run();

});
