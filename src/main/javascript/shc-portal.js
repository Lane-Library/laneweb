addEventListener("load", function() {

    "use strict";

    (function() {
        var forms = document.querySelectorAll('form');

        forms.forEach(function(form) {
            form.addEventListener("submit", function(event) {
                var trackingData = {}, re;
                trackingData.title = event.target.name;
                if (!trackingData.title && event.target.className === 'search-form') {
                    trackingData.title = 'SHC-Epic Lane search ' + L.search.getSource();
                }
                if (trackingData.title) {
                    re = event.target.action.match('.*:\/\/([a-zA-Z0-9:\.\-]*)\/(.*)');
                    trackingData.host = re[1] === location.host ? "" : re[1];
                    trackingData.path = "/" + re[2];
                    trackingData.external = trackingData.host.length > 0;
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
        var form = document.querySelector('.verticalPico'),
            inputs = form.querySelectorAll('input[type="text"]'),
            queryInput = form.querySelector("input[name=q]"),
            searchTerms = document.querySelector(".search-form input[name=q]"),
            //build query terms from pico inputs:
            getPicoQuery = function() {
                var qString = '';
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
            var suggest;
            suggest = new L.Suggest(input, "mesh");
            suggest.on("suggest:select", eventHandler);
            input.addEventListener("blur", eventHandler);
            input.addEventListener("keyup", eventHandler);
        });
        form.style.visibility = "visible";
    })();

});
