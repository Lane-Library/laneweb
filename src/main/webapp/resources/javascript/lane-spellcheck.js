YUI().use('node', 'event-custom', 'yui2-connection', 'json-parse', function(Y) {
    Y.Global.on('lane:searchready', function() {
        if (Y.one('#spellCheck') && LANE.search.getEncodedSearchString()) {
            //get the suggestion
            Y.YUI2.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q=' + LANE.search.getEncodedSearchString(), {
                success: function(o) {
                    var sc = Y.JSON.parse(o.responseText), s, a;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup 
                        //and add the suggestion to the href
                        s = document.getElementById('spellCheck');
                        //s.style.display = 'inline';
                        LANE.search.popin.fire(s);
                        a = s.getElementsByTagName('a')[0];
                        a.href = document.location.href.replace('q=' + LANE.search.getEncodedSearchString(), 'q=' + encodeURIComponent(sc.suggestion));
                        a.innerHTML = sc.suggestion;
                    }
                }
            });
        }
    });
});

