if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var CLICK = "click",
        SOURCE = "source",
        SEARCH_TAB = "search-tab",
        form = document.querySelector(".search-form"),
        lane = Y.lane,
        tabNodes = form.querySelectorAll("." + SEARCH_TAB),

        model = function(source) {

            var m = {
                source: source
            };

            tabNodes.forEach(function(tab) {
                m[lane.getData(tab, SOURCE)] = {
                    placeholder: lane.getData(tab, "placeholder"),
                    source: lane.getData(tab, SOURCE),
                    help: lane.getData(tab, "help"),
                    tip: tab.title
                };
            });

            return m;

        }(lane.getData(form.querySelector("." + SEARCH_TAB + "-active"), SOURCE)),

        view = function() {

            var tabs = {},

                v = {
                    change: function(newVal, oldVal) {
                        lane.deactivate(tabs[oldVal.source], SEARCH_TAB);
                        lane.activate(tabs[newVal.source], SEARCH_TAB);
                    },
                    click: function() {
                        view.fire(CLICK, lane.getData(this, SOURCE));
                    }
                };

            Y.augment(v, Y.EventTarget);

            tabNodes.forEach(function(tab){
                tab.addEventListener(CLICK, v.click);
                tabs[lane.getData(tab, SOURCE)] = tab;
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
                    lane.fire("tracker:trackableEvent", {
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

    controller.addTarget(lane);

    view.on(CLICK, controller.update, controller);

})();

}
