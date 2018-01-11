(function() {

    "use strict";

    (function() {
        var i, forms = Y.all('form');

        for (i = 0; i < forms.size(); i++) {
            forms.item(i).on("submit", function(event) {
                var trackingData = {}, re, url;
                trackingData.title = event.target.get('name');
                if (!trackingData.title && event.target.get('className') == 'search-form') {
                    trackingData.title = 'SHC-Epic Lane search ' + L.search.getSource();
                }
                if (trackingData.title) {
                    re = event.target.get('action').match('.*:\/\/([a-zA-Z\.\-]*)\/(.*)');
                    trackingData.host = re[1];
                    trackingData.path = re[2];
                    trackingData.external = trackingData.host !== location.host;//!(event.target.get('action').indexOf('/') === 0);
                    L.fire("tracker:trackablePageview", {
                        host : trackingData.host,
                        path : trackingData.path,
                        title : trackingData.title,
                        external : trackingData.external
                    });
                }
            });
        }
    })();

    (function() {
        var i, suggest, form = Y.one('.verticalPico'),
            inputs = form.all('input[type="text"]'),
            queryInput = form.one("input[name=q]"),
            searchTerms = Y.one(".search-form input[name=q]"),
            getPicoQuery = function() { //build query terms from pico inputs
                var qString = '', i;
                for (i = 0; i < inputs.size(); i++) {
                    if (inputs.item(i).get('value') && inputs.item(i).get('value') != inputs.item(i).get('title')) {
                        qString += '(' + inputs.item(i).get('value') + ')';
                    }
                }
                if (qString.length) {
                    qString = qString.replace(/\)\(/g, ") AND (");
                    if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
                        qString = qString.replace(/(\(|\))/g, '');
                    }
                }
                return qString;
            };

        for (i = 0; i < inputs.size(); i++) {
            switch (inputs.item(i).get('name')) {
            case 'p':
                suggest = new L.Suggest(inputs.item(i), "mesh-d");
                break;
            case 'i':
                suggest = new L.Suggest(inputs.item(i), "mesh-i");
                break;
            case 'c':
                suggest = new L.Suggest(inputs.item(i), "mesh-di");
                break;
            }
            inputs.item(i).on("blur", function() {
                var picoQuery = getPicoQuery();
                if (picoQuery) {
                    searchTerms.set("value", picoQuery);
                    queryInput.set("value", picoQuery);
                }
            });
            inputs.item(i).on("keyup", function() {
                var picoQuery = getPicoQuery();
                if (picoQuery) {
                    searchTerms.set("value", picoQuery);
                    queryInput.set("value", picoQuery);
                }
            });
        }
        Y.Global.on("lane:suggestSelect", function(event) {
            var picoQuery = getPicoQuery();
            if (picoQuery) {
                searchTerms.set("value", picoQuery);
                queryInput.set("value", picoQuery);
            }
        });
        form.setStyle('visibility', 'visible');
    })();

})();
