if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var PLACEHOLDER = "placeholder",

        lane = Y.lane,

        inputNode = document.querySelector(".search-form input[name=q]"),

        model = function(def, current) {
            return {
                def: def,
                current: current
            };
        }(lane.getData(inputNode, PLACEHOLDER),
                lane.getData(document.querySelector(".search-tab-active"), PLACEHOLDER)),

        view = function() {

            return {
                    setPlaceholder: function(placeholder) {
                        inputNode.placeholder = placeholder;
                    }
                };

        }(),

        controller = function() {

            return {
                activeChange: function(event) {
                    view.setPlaceholder(event.active ? model.current : model.def);
                },

                tabChange: function(event) {
                    var newVal = event.newVal;
                    model.current = newVal[newVal.source].placeholder;
                    view.setPlaceholder(model.current);
                }
            };
        }();

    lane.on("searchTabs:change", controller.tabChange);
    lane.on("search:activeChange", controller.activeChange);


})();

}
