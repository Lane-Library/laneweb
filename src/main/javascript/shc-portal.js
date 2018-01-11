(function() {

    "use strict";

    (function() {
        var i, forms = document.querySelectorAll('form');

        forms.forEach(function(form) {
            form.addEventListener("submit", function(event) {
                var trackingData = {}, re;
                trackingData.title = event.target.name;
                if (!trackingData.title && event.target.className === 'search-form') {
                    trackingData.title = 'SHC-Epic Lane search ' + L.search.getSource();
                }
                if (trackingData.title) {
                    re = event.target.action.match('.*:\/\/([a-zA-Z\.\-]*)\/(.*)');
                    trackingData.host = re[1];
                    trackingData.path = re[2];
                    trackingData.external = trackingData.host !== location.host;
                    L.fire("tracker:trackablePageview", {
                        host : trackingData.host,
                        path : trackingData.path,
                        title : trackingData.title,
                        external : trackingData.external
                    });
                }
            });
        });
    })();

    (function() {
        var i, form = document.querySelector('.verticalPico'),
            inputs = form.querySelectorAll('input[type="text"]'),
            queryInput = form.querySelector("input[name=q]"),
            searchTerms = document.querySelector(".search-form input[name=q]"),
            //build query terms from pico inputs:
            getPicoQuery = function() {
                var qString = '', j;
                inputs.forEach(function(input) {
                    if (input.value) {
                        qString += "(" + input.value + ")";
                    }
                });
                if (qString.length) {
                    qString = qString.replace(/\)\(/g, ") AND (");
                    if (qString.indexOf('(') === 0 && qString.indexOf(')') === qString.length - 1) {
                        qString = qString.replace(/(\(|\))/g, '');
                    }
                }
                return qString;
            },
            eventHandler = function() {
                var picoQuery = getPicoQuery();
                if (picoQuery) {
                    searchTerms.value = picoQuery;
                    queryInput.value = picoQuery;
                }
            };

        inputs.forEach(function(input) {
            var limit, suggest;
            switch (input.name) {
            case 'p':
                limit = "mesh-d";
                break;
            case 'i':
                limit = "mesh-i";
                break;
            case 'c':
                limit = "mesh-di";
                break;
            }
            if (limit) {
                suggest = new L.Suggest(input, limit);
                suggest.on("suggest:select", eventHandler);
            }
            input.addEventListener("blur", eventHandler);
            input.addEventListener("keyup", eventHandler);
        });
        form.style.visibility = "visible";
    })();

})();
