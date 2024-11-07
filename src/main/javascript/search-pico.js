(function () {

    "use strict";

    let fields = [],

        PicoField = function (input) {
            let suggest;

            suggest = new L.Suggest(input);
            L.addEventTarget(suggest);
            suggest.on("suggest:select", function () {
                fields.fire("input");
            });
            input.addEventListener("input", function () {
                fields.fire("input");
            });
            return {
                enable: function (enable) {
                    input.disabled = !enable;
                },
                getValue: function () {
                    return input.value;
                },
                reset: function () {
                    input.value = "";
                }
            };
        };

    L.addEventTarget(fields);


    document.querySelectorAll(".pico-fields input").forEach(function (input) {
        fields.push(new PicoField(input));
    });

    L.on("picoFields:change", function (event) {
        fields.forEach(function (field) {
            field.enable(event.active);
        });
    });

    L.on("searchReset:reset", function () {
        fields.forEach(function (field) {
            field.reset();
        });
    });

    fields.on("input", function () {
        let query = "";
        fields.forEach(function (field) {
            let value = field.getValue();
            if (value) {
                query += "(" + value + ")";
            }
        });
        query = query.replace(/\)\(/g, ") AND (");
        if (query.startsWith('(') && query.indexOf(')') === query.length - 1) {
            query = query.replace(/[()]/g, '');
        }
        L.search.setQuery(query);
    });


})();
