(function() {

    "use strict";

    var lane = Y.lane,

        limits = {
            p: "mesh-d",
            i: "mesh-i",
            c: "mesh-di"
        },

        fields = [],

        PicoField = function(input, limit) {
            var ynode = new Y.Node(input),
                suggest,
                self = this;
            if (limit) {
                suggest = new lane.Suggest(ynode, limit);
                suggest.on("select", function() {
                    self.fire("input");
                });
            }

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

    Y.augment(PicoField, Y.EventTarget);
    Y.augment(fields, Y.EventTarget);

    [].forEach.call(document.querySelectorAll(".pico-fields input"), function(input) {
        fields.push(new PicoField(input, limits[input.name]));
    });

    lane.on("picoFields:change", function(event) {
        fields.forEach(function(field) {
            field.enable(event.active);
        });
    });

    lane.on("searchReset:reset", function() {
        fields.forEach(function(field) {
            field.reset();
        });
    });

    fields.on("input", function() {
        var query = "";
        fields.forEach(function(field) {
            var value = field.getValue();
            if (value) {
                query += "(" + value + ")";
            }
        });
        query = query.replace(/\)\(/g, ") AND (");
        if (query.indexOf('(') === 0 && query.indexOf(')') === query.length - 1) {
            query = query.replace(/(\(|\))/g, '');
        }
        lane.search.setQuery(query);
    });

})();