YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var menuTestCase = new Y.Test.Case({

        name: 'Lane Menu Test Case',

        testClickOnH2: function() {
            var h2 = Y.one("h2"),
            menu = Y.one("#menu");
            Y.Assert.isTrue(menu._node.className === 'menu-container mobile');
            h2.simulate("click");
            Y.Assert.isTrue(menu._node.className === 'menu-container mobile active');
        },
      
       testClickOnSamePageLink: function(){
            var anchor = Y.one("#local-link")
            Y.Assert.isFalse(anchor._node.className === 'menuitem-active');
            anchor.simulate("click");
            Y.Assert.isTrue(anchor._node.className === 'menuitem-active');
        }      

      
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(menuTestCase);
    Y.Test.Runner.masterSuite.name = "menu-test.js";
    Y.Test.Runner.run();

});