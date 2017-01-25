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

        view = function(inputNode) {

            var queryInput = new lane.TextInput(new Y.Node(inputNode), inputNode.getAttribute(PLACEHOLDER));
            return {
                    setPlaceholder: function(placeholder) {
                        queryInput.setHintText(placeholder);
                    }
                };

        }(inputNode),

        controller = function(model, view) {

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
        }(model, view);

    lane.on("searchTabs:change", controller.tabChange);
    lane.on("search:activeChange", controller.activeChange);


})();

}
