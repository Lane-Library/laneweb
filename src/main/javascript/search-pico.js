(function() {

    "use strict";

    let fields = [],

        PicoField = function(input) {
            let suggest,
                self = this;
            suggest = new L.Suggest(input);
            suggest.on("select", function() {
                self.fire("input");
            });
            input.addEventListener("input", function() {
                self.fire("input");
            });

            this.addTarget(fields);
            return {
                enable: function(enable) {
                    input.disabled = !enable;
                },
                getValue: function() {
                    return input.value;
                },
                reset: function() {
                    input.value = "";
                }
            };
        };

    L.addEventTarget(PicoField);
    L.addEventTarget(fields);

    document.querySelectorAll(".pico-fields input").forEach(function(input) {
        fields.push(new PicoField(input));
    });

    L.on("picoFields:change", function(event) {
        fields.forEach(function(field) {
            field.enable(event.active);
        });
    });

    L.on("searchReset:reset", function() {
        fields.forEach(function(field) {
            field.reset();
        });
    });

    fields.on("input", function() {
        let query = "";
        fields.forEach(function(field) {
            let value = field.getValue();
            if (value) {
                query += "(" + value + ")";
            }
        });
        query = query.replace(/\)\(/g, ") AND (");
        if (query.indexOf('(') === 0 && query.indexOf(')') === query.length - 1) {
            query = query.replace(/(\(|\))/g, '');
        }
        L.search.setQuery(query);
    });

})();
