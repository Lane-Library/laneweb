Y.use(function(){



    Y.lane.Model.set('query','query');
    Y.io = function(url, config) {
        config.on.success.apply(this, [1,{responseText:Y.JSON.stringify({'id':'1477844556','status': 'successful','query': 'query','resources': {'pubmed':{'status': 'successful','url': '/secure/apps/proxy/credential?url=http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=search&term=query&holding=f1000%2CF1000M&otool=Stanford','hits': '6159'}}})}]);


        var metasearchTestCase = new Y.Test.Case({
            name: 'Lane Metasearch Test Case'
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



