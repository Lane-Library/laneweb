if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var CLICK = "click",
        SOURCE = "source",
        SEARCH_TAB = "search-tab",
        form = document.querySelector(".search-form"),
        tabNodes = form.querySelectorAll("." + SEARCH_TAB),

        model = function(source) {

            var m = {
                source: source
            };

            tabNodes.forEach(function(tab) {
                m[L.getData(tab, SOURCE)] = {
                    placeholder: L.getData(tab, "placeholder"),
                    source: L.getData(tab, SOURCE),
                    help: L.getData(tab, "help"),
                    tip: tab.title
                };
            });

            return m;

        }(L.getData(form.querySelector("." + SEARCH_TAB + "-active"), SOURCE)),

        view = function() {

            var tabs = {},

                v = {
                    change: function(newVal, oldVal) {
                        L.deactivate(tabs[oldVal.source], SEARCH_TAB);
                        L.activate(tabs[newVal.source], SEARCH_TAB);
                    },
                    click: function() {
                        view.fire(CLICK, L.getData(this, SOURCE));
                    }
                };

            Y.augment(v, Y.EventTarget);

            tabNodes.forEach(function(tab){
                tab.addEventListener(CLICK, v.click);
                tabs[L.getData(tab, SOURCE)] = tab;
            });

            return v;

        }(),


        controller = function() {

            return {
                update: function(source) {
                    var prop, newVal = {};
                    for (prop in model) {
                        newVal[prop] = model[prop];
                    }
                    newVal.source = source;
                    view.change(newVal, model);
                    L.fire("tracker:trackableEvent", {
                        category: "lane:searchDropdownSelection",
                        action: source,
                        label: "from " + model.source + " to " + source
                    });
                    this.fire("change", {newVal:newVal, oldVal:model});
                    model = newVal;
                }
            };

        }();

    Y.augment(controller, Y.EventTarget, false, null, {
        prefix: "searchTabs",
        emitFacade: true
    });

    controller.addTarget(L);

    view.on(CLICK, controller.update, controller);

})();

}
