YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let menuTestCase = new Y.Test.Case({

        name: 'Lane Menu Test Case',

        testClickOnH2: function() {
            let h2 = Y.one("h2"),
            menu = Y.one("#menu");
            Y.Assert.isTrue(menu._node.className === 'menu-container mobile');
            h2.simulate("click");
            Y.Assert.isTrue(menu._node.className === 'menu-container mobile active');
        },
      
       testClickOnSamePageLink: function(){
        let anchor1 = Y.one("#internal-1"), anchor2 = Y.one("#internal-2");
        Y.Assert.isFalse(anchor1._node.className === 'menuitem-active');
        Y.Assert.isFalse(anchor2._node.className === 'menuitem-active');
        anchor1.simulate("click");
        Y.Assert.isTrue(anchor1._node.className === 'menuitem-active');
        Y.Assert.isFalse(anchor2._node.className === 'menuitem-active');
    }      

      
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(menuTestCase);
    Y.Test.Runner.masterSuite.name = "menu-test.js";
    Y.Test.Runner.run();

});