/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y) {


    var menuItemsTestCase = new Y.Test.Case({

        name: 'Lane MenuAndItems Test Case',

        menuAndItems: null,

        testGetActiveItem : function() {
            Y.Assert.areEqual(this.menuAndItems.get("activeItem"), 0);
        },

        testMenuClick : function() {
            var menuItem = Y.all("#menu li").item(2);
            var item = Y.all("#items li").item(2);
            menuItem.simulate("click");
            Y.Assert.areEqual(this.menuAndItems.get("activeItem"), 2);
            Y.Assert.isTrue(menuItem.hasClass(this.menuAndItems.getClassName("menu", "active")));
            Y.Assert.isTrue(item.hasClass(this.menuAndItems.getClassName("item", "active")));
        }

    });

    (function() {
        var menu = Y.all("#menu li"),
            items = Y.all("#items li"),
            srcNode = Y.one("#menuItems");
        menuItemsTestCase.menuAndItems = new Y.lane.MenuAndItems({srcNode:srcNode,menu:menu,items:items,render:true});
    })();

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(menuItemsTestCase);
    Y.Test.Runner.masterSuite.name = "menu-items-test.js";
    Y.Test.Runner.run();
});
