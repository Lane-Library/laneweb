if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var CLICK = "click",
        SOURCE = "source",
        SEARCH_TAB = "search-tab",
        SEARCH_TAB_ACTIVE = SEARCH_TAB + "-active",
        form = document.querySelector(".search-form"),
        tabNodes = form.querySelectorAll("." + SEARCH_TAB),

        model = function(source) {

            var m = {
                source: source
            };

            tabNodes.forEach(function(tab) {
                m[tab.dataset[SOURCE]] = {
                    placeholder: tab.dataset.placeholder,
                    source: tab.dataset[SOURCE],
                    help: tab.dataset.help,
                    tip: tab.title
                };
            });

            return m;

        }(form.querySelector("." + SEARCH_TAB_ACTIVE).dataset[SOURCE]),

        view = function() {

            var tabs = {},

                v = {
                    change: function(newVal, oldVal) {
                        tabs[oldVal.source].classList.remove(SEARCH_TAB_ACTIVE);
                        tabs[newVal.source].classList.add(SEARCH_TAB_ACTIVE);
                    },
                    click: function() {
                        view.fire(CLICK, this.dataset[SOURCE]);
                    }
                };

            L.addEventTarget(v);

            tabNodes.forEach(function(tab){
                tab.addEventListener(CLICK, v.click);
                tabs[tab.dataset[SOURCE]] = tab;
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

    L.addEventTarget(controller, {
        prefix: "searchTabs",
        emitFacade: true
    });

    controller.addTarget(L);

    view.on(CLICK, controller.update, controller);

})();

}
