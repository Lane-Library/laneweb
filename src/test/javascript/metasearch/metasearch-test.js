YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    L.Model.set('url-encoded-query','query');
    L.Model.set('query','query');
    L.io = function(url, config) {
        L.io = function(url, config) {
            config.on.success.apply(this,[2,{responseText:JSON.stringify({
                'id':'1477844556',
                'status': 'successful',
                "query" : "query",
                'resources': {
                    "cro_fudd":{
                        "status": "canceled",
                        "url": "http://cro.com/fudd"
                    }
                }
            })}]);
        };
        config.on.success.apply(this, [1,{responseText:JSON.stringify({
            'id':'1477844556',
            'status': 'running',
            'query': 'query',
            'resources': {
                'pubmed':{
                    'status': 'successful',
                    'url': '/secure/apps/proxy/credential?url=http://pubmed.gov',
                    'hits': 6159},
                    "ck_kumar":{
                        "status": "successful",
                        "url": "http://mdc.com/kumar",
                        "hits":12
                    },
                    "ck_brenner":{
                        "status": "successful",
                        "url": "http://mdc.com/brenner",
                        "hits":24
                    },
                    "cro_fudd":{
                        "status": "running",
                        "url": "http://cro.com/fudd"
                    }
            }})}]);


        var metasearchTestCase = new Y.Test.Case({

            name: 'Lane Metasearch Test Case',

            testBrennerHits: function() {
                var hitCountSpan = Y.one("#ck_brenner").get('parentNode').one("span");
                Y.Assert.areEqual("&nbsp;24", hitCountSpan.get("innerHTML"));
            }
        });

        new Y.Test.Console().render();

        Y.Test.Runner.add(metasearchTestCase);
        Y.Test.Runner.masterSuite.name = 'metasearch-test.js';
        Y.Test.Runner.run();
    };

});
