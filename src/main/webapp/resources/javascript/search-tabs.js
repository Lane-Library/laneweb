if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var CLICK = "click",
        SOURCE = "source",
        SEARCH_TAB = "search-tab",
        form = document.querySelector(".search-form"),
        forEach = [].forEach,
        lane = Y.lane,
        tabNodes = form.querySelectorAll("." + SEARCH_TAB),

        model = function(tabNodes, source) {

            var model = {
                source: source
            };

            forEach.call(tabNodes, function(tab) {
                model[lane.getData(tab, SOURCE)] = {
                    placeholder: lane.getData(tab, "placeholder"),
                    source: lane.getData(tab, SOURCE),
                    help: lane.getData(tab, "help"),
                    tip: tab.title
                };
            });

            return model;

        }(tabNodes, lane.getData(form.querySelector("." + SEARCH_TAB + "-active"), SOURCE)),

        view = function(tabNodes) {

            var tabs = {},

                view = {
                    change: function(newVal, oldVal) {
                        lane.deactivate(tabs[oldVal.source], SEARCH_TAB);
                        lane.activate(tabs[newVal.source], SEARCH_TAB);
                    },
                    click: function() {
                        view.fire(CLICK, lane.getData(this, SOURCE));
                    }
                };

            Y.augment(view, Y.EventTarget);

            forEach.call(tabNodes, function(tab){
                tab.addEventListener(CLICK, view.click);
                tabs[lane.getData(tab, SOURCE)] = tab;
            });

            return view;

        }(tabNodes),


        controller = function(model, view) {

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

        }(model, view);

    Y.augment(controller, Y.EventTarget, false, null, {
        prefix: "searchTabs",
        emitFacade: true
    });

    controller.addTarget(lane);

    view.on(CLICK, controller.update, controller);

})();

}
