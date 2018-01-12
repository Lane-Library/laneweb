if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var PLACEHOLDER = "placeholder",

        inputNode = document.querySelector(".search-form input[name=q]"),

        model = function(def, current) {
            return {
                def: def,
                current: current
            };
          }(inputNode.dataset[PLACEHOLDER],
              document.querySelector(".search-tab-active").dataset[PLACEHOLDER]),

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

    L.on("searchTabs:change", controller.tabChange);
    L.on("search:activeChange", controller.activeChange);


})();

}
