Y.use(function(){



    Y.lane.Model.set('url-encoded-query','query');
    Y.lane.Model.set('query','query');
    Y.io = function(url, config) {
        Y.io = undefined;
        config.on.success.apply(this, [1,{responseText:Y.JSON.stringify({
            'id':'1477844556',
            'status': 'successful',
            'query': 'query',
            'resources': {
                'pubmed':{
                    'status': 'successful',
                    'url': '/secure/apps/proxy/credential?url=http://pubmed.gov',
                    'hits': '6159'},
                 "mdc_kumar":{
                     "status": "successful",
                     "url": "http://mdc.com/kumar",
                     "hits":"12"
                 },
                 "mdc_brenner":{
                     "status": "successful",
                     "url": "http://mdc.com/brenner",
                     "hits":"24"
                 }
                    }})}]);


        var metasearchTestCase = new Y.Test.Case({
            name: 'Lane Metasearch Test Case',
            testBrennerHits: function() {
                var hitCountSpan = Y.one("#mdc_brenner").get('parentNode').one("span");
                Y.Assert.areEqual("&nbsp;24", hitCountSpan.get("innerHTML"));
            }
        });

        Y.one('body').addClass('yui3-skin-sam');
        new Y.Console({
            newestOnTop: false
        }).render('#log');


        Y.Test.Runner.add(metasearchTestCase);
        Y.Test.Runner.masterSuite.name = 'metasearch-test.js';
        Y.Test.Runner.run();
    };
});



